package gdi1sokoban.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Class provides cryptography operations over the socoban data
 * @author Stalker
 *
 */
public class Security {
	//DES key used to code data
	// KEY IM CODE / BYTECODE... -> WARUM DANN UEBERHAUPT NOCH VERSCHLUESSELN?
	private byte[] key = {-33, 94, 2, -23, 55, -32, 50, 81};
	
	
	private static Security _instance = null;
	
	private Security(){
		
	}
	
	// DEACTIVATET!
	// THIS CLASS CAUSES ERRORS ON CERTAIN SYSTEMS
	/*public static Security getInstance(){
		if(_instance == null)
			_instance = new Security();
		return _instance;
	}*/
	
	@Deprecated
	public byte[] encodeStringtoByte(String raw){
		byte[] enc = null;
		try{
			Cipher chr = Cipher.getInstance("DES");
			SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
			chr.init(Cipher.ENCRYPT_MODE, sKey);
			byte[] utf8 = raw.getBytes("UTF8");
			enc = chr.doFinal(utf8);
			//out =new sun.misc.BASE64Encoder().encode(enc);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(InvalidKeySpecException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(BadPaddingException e){
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			e.printStackTrace();
		}
		return enc;
		
	}
	
	@Deprecated
	public String encodeString(String raw){
		String out =new sun.misc.BASE64Encoder().encode(encodeStringtoByte(raw));
		return out;
	}
	
	@Deprecated
	public byte[] decodeStringToByte(String coded){
		byte[] dec = null;
		try {
			dec = new sun.misc.BASE64Decoder().decodeBuffer(coded);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return decodeByte(dec);
		
	}
	
	@Deprecated
	public String decodeString(String coded){
		String out = null;
		try {
			out = new String(decodeStringToByte(coded), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return out; 
	}
	
	/**
	 * Decodes given bytes array and returns decoded bytes array
	 * @param dec array of bytes to decode
	 * @return decoded array of bytes
	 */
	public byte[] decodeByte(byte[] dec){
		byte[] out =null;
		try{
			Cipher decode = Cipher.getInstance("DES");
			SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
			decode.init(Cipher.DECRYPT_MODE, sKey);
			out = decode.doFinal(dec);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return out;
		
	}
	
	/**
	 * Encodes given array of bytes and returns new encoded array of bytes
	 * @param raw array of bytes to encode
	 * @return encoded array of bytes 
	 */
	public byte[] encodeByte(byte[] raw){
		byte[] enc = null;
		try{
			Cipher chr = Cipher.getInstance("DES");
			SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));
			chr.init(Cipher.ENCRYPT_MODE, sKey);
			enc = chr.doFinal(raw);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(InvalidKeySpecException e){
			e.printStackTrace();
		}catch(BadPaddingException e){
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			e.printStackTrace();
		}
		return enc;
	
	}
	
	/**
	 * Method saves and encodes some object into file with given filename. 
	 * @param object object to save
	 * @param filename file to save
	 * @throws IOException
	 */
	public void saveObject(Serializable object, String filename){
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try{
			ObjectOutputStream objstream = new ObjectOutputStream(out);
			objstream. writeObject(object);
			objstream.close();
				
			FileOutputStream wr = new FileOutputStream(filename);
			//System.out.println(out.toString());
			//viewByte(out.toByteArray());
			//viewByte(sec.encodeStringtoByte(out.toString()));
			//viewByte(sec.encodeByte(out.toByteArray()));
			wr.write(encodeByte(out.toByteArray()));
			wr.close();
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method loads and decodes some object from the file with given file name
	 * @param filename file to load
	 * @return new Object
	 * @throws Exception
	 */
	public Object loadObject(String filename){
		File file = new File(filename);
		byte[] out =null;
		try{
			FileInputStream in = new FileInputStream(file);
			out = new byte[(int)file.length()];
			in.read(out);
			in.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//viewByte(out);
		//viewByte(sec.decodeByte(out));
		ByteArrayInputStream input = new ByteArrayInputStream(decodeByte(out));
		Object object = null;
		try{
			ObjectInputStream objstream = new ObjectInputStream(input);
			object = objstream.readObject();
			objstream.close();
			input.close();
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    return object;
	}

	private byte[] readBytes(String filename){
		File file = new File(filename);
		byte[] out =null;
		try{
			FileInputStream in = new FileInputStream(file);
			out = new byte[(int)file.length()];
			in.read(out);
			in.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Encodes file with the given file name
	 * @param filename file to encode
	 */
	public void encodeFile(String filename){
		byte[] out =readBytes(filename);
		try{
			FileOutputStream wr = new FileOutputStream(filename);
			wr.write(encodeByte(out));
			wr.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Deprecated
	public void decodeFile(String filename){
		
		
	}
	
	/**
	 * Decodes file with the given file name and represents it as DOM Object 
	 * @param filename file to decode
	 * @return decoded file as Document Object
	 */
	public Document decodeFileToDOM(String filename){
		Document doc = null;
		byte[] dec = readBytes(filename);
		ByteArrayInputStream input = new ByteArrayInputStream(decodeByte(dec));
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.parse(input);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		} catch (SAXException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//TEST ON
//		try {
//			FileOutputStream fi = new FileOutputStream(filename);
//			fi.write(decodeByte(dec));
//			fi.close();
//		} catch (FileNotFoundException e) {
//			// k
//			e.printStackTrace();
//		} catch (IOException e) {
//			//
//			e.printStackTrace();
//		}
//		
		//TEST OFF
		return doc;
		
	}
	
//	@Deprecated
//	public static void main(String... args){
//		Security sec = Security.getInstance();
//		//sec.encodeFile("res\\levelSet\\0\\highScores.xml");
//		sec.decodeFileToDOM("res\\levelSet\\0\\highScores.xml");
//		
//	}
	
}

