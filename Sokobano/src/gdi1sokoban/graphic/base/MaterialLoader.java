package gdi1sokoban.graphic.base;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;

public class MaterialLoader {
	
	public static LinkedList<Material> load(String fileName, int minFilter, int magFilter) throws Exception {
		
		String name = "";
		TextureManager.Resource texture = null;
		float[] ambient = null;
		float[] diffuse = null;
		
		boolean newMaterial = false;
		LinkedList<Material> materials = new LinkedList<Material>();
		
		// Parse file:
		File materialFile = new File(fileName);
		Scanner scanner = new Scanner(materialFile);
		scanner.useDelimiter("\\n");
		
		while (scanner.hasNext())
		{
			String currentLine = scanner.next();
			String[] s = currentLine.split("\\s+");

			if (s.length == 0) {
				continue;
			}
			else if (s[0].equals("newmtl")) {
				if (newMaterial) {
					Material material = new Material(name, ambient, diffuse, texture);
					materials.add(material);
				}
					
				name = s[1];
				ambient = null;
				diffuse = null;
				texture = null;
				newMaterial = true;
			}
			else if (s[0].equals("Ka")) {
				ambient = new float[] {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3]), 1.0f};
			}
			else if (s[0].equals("Kd")) {
				diffuse = new float[] {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3]), 1.0f};
			}
			else if (s[0].equals("map_Kd")) {
				String textureFileName = s[s.length - 1];
				TextureDescriptor textureDescriptor = new TextureDescriptor(materialFile.getParent() + File.separator + textureFileName, minFilter, magFilter);
				texture =  TextureManager.getInstance().getInstance(textureDescriptor);
			}
		}
		scanner.close();
		
		if (newMaterial) {
			Material material = new Material(name, ambient, diffuse, texture);
			materials.add(material);
		}
		
		return materials;	
		
		// Complete format description:
		/*
		-map_Ka  name // Material texture name
		-Kd // material diffuse is multiplied by the texture value
		-Ka // material ambient is multiplied by the texture value
		-blendu on | off // Horizontal texture blending, default on
		-blendv on | off // Vertical texture blending, default on
		-cc on | off // Color correction
		-clamp on | off // Texture clamping, default off
		-mm base gain // unused, default 0
		-o u v w // Texture position offset on surface, default 0 0 0
		-s u v w // Texture position on surface, default 1 1 1
		-t u v w //  unused, default 0 0 0
		-texres value // unused
		Bsp.: map_Ka -s 1 1 1 -o 0 0 0 -mm 0 1 chrome.mpc 
		*/
	}
}
