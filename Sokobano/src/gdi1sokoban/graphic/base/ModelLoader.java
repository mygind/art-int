package gdi1sokoban.graphic.base;

import gdi1sokoban.graphic.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * This MeshLoader is designed for loading Wavefront *.obj-files with related
 * *.mtl material libraries. Only vertices, texture coordinates, texture 
 * normals and faces are read, see MaterialLoader for the restrictions to
 * material libraries.
 */
public class ModelLoader {
	
	/**
	 * Creates a Mesh from a Wavefront *.obj-file.
	 * 
	 * @param filename filename of the *.obj-file
	 * @return a mesh
	 * @throws Exception 
	 * @throws InvalidObjectException
	 */
	static public Model load(String filename, int minFilter, int magFilter) throws Exception {
		String name = "";

		//Result:
		HashMap<Material, Mesh> meshes = new HashMap<Material, Mesh>();
		
		Material currentMaterial = null;
		HashMap<String, Material> materials = new HashMap<String, Material>();
		Vector<float[]> coords = new Vector<float[]>(100);
		Vector<float[]> normals = new Vector<float[]>(100);
		Vector<float[]> textureCoords = new Vector<float[]>(100);
		HashMap<Material, HashMap<Integer, LinkedList<Integer>>> indicesByMaterial = new HashMap<Material, HashMap<Integer, LinkedList<Integer>>>();
		
		HashMap<String, Integer> verticesByIndex = new HashMap<String, Integer>();
		LinkedList<String> vertices = new LinkedList<String>();
		
		// Parse file:
		File modelFile = new File(filename);
		FileInputStream fos = new FileInputStream(modelFile);
		Scanner scanner = new Scanner(fos);
		scanner.useDelimiter("\\n");
		
		while (scanner.hasNext())
		{
			String currentLine = scanner.next();
			String[] s = currentLine.split("\\s+");
			
			
			if (s.length == 0) { // UNNESSESARY || (s[0].startsWith("#")) || (s[0].equals("g"))) {
				continue;
			}
			else if ((s[0].equals("g")) && (name != null)) {
				name = s[1];
			}
			else if (s[0].equals("v")) {
				float[] coord = {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3])};
				coords.add(coord);
			}
			else if (s[0].equals("vn")) {
				float[] normal = {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3])};
				normals.add(normal);
			}
			else if (s[0].equals("vt")) {
				float[] textureCoord = {Float.valueOf(s[1]), 1.0f - Float.valueOf(s[2])};
				textureCoords.add(textureCoord);
			}
			else if (s[0].equals("mtlib")) {
				LinkedList<Material> loadedMaterials = MaterialLoader.load(modelFile.getParent() + File.separator + s[1], minFilter, magFilter);
				for (Material material : loadedMaterials)
					materials.put(material.getName(), material);
			}
			else if (s[0].equals("usemtl")) {
				currentMaterial = materials.get(s[1]);
			}
			else if (s[0].equals("f")) {
				int indexSize = s.length - 1;
				
				// Access indices by material:
				HashMap<Integer, LinkedList<Integer>> indicesByPrimitive = indicesByMaterial.get(currentMaterial);
				if (indicesByPrimitive == null) {
					indicesByPrimitive = new HashMap<Integer, LinkedList<Integer>>();
					indicesByMaterial.put(currentMaterial, indicesByPrimitive);
				}
				
				// Access indices by primitive-type:
				LinkedList<Integer> indices = indicesByPrimitive.get(indexSize);
				if (indices == null) {
					indices = new LinkedList<Integer>();
					indicesByPrimitive.put(indexSize, indices);
				}

				// Iterate over indices:
				for (int i = 0; i < indexSize; i++) {
					
					// 1. Index des Vertex ermitteln:
					Integer index = verticesByIndex.get(s[i + 1]);
					if (index == null) {			// Dieser Vertex existiert noch nicht im VertexBuffer
						index = vertices.size();	// Nächsten freien Index ermitteln
						verticesByIndex.put(s[i + 1], index);	// In Zuordnungstabelle Vertex-Index speichern
						vertices.add(s[i + 1]); 	// In Vertexbuffer eintragen
					}
					
					// 2. Den Index des neuen / bekannten Vertex in der Indexliste dieses Material & Primitiventypes speichern:
					indices.add(index);
				}
			}
		}
		scanner.close();
		
		// Vertices, Normalen und Texturcoords zusammenführen:
		FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(vertices.size() * 8);
		
		for (String vertex : vertices) {
			
			// Transform vertex indices from string to integer values:
			String[] s = vertex.split("/");
			int[] indices = new int[s.length];
			for (int i = 0; i < s.length; i++)
				indices[i] = Integer.parseInt(s[i]) - 1;
			
			// Add corresponding vertex data into the interleaved buffer:
			interleavedBuffer.put(textureCoords.get(indices[1])[0]);
			interleavedBuffer.put(textureCoords.get(indices[1])[1]);
			
			interleavedBuffer.put(normals.get(indices[2])[0]);
			interleavedBuffer.put(normals.get(indices[2])[1]);
			interleavedBuffer.put(normals.get(indices[2])[2]);
			
			interleavedBuffer.put(coords.get(indices[0])[0]);
			interleavedBuffer.put(coords.get(indices[0])[1]);
			interleavedBuffer.put(coords.get(indices[0])[2]);
		}
		interleavedBuffer.flip();
		
		try
		{
			// Iterate over faces of different material:
			for (Map.Entry<Material, HashMap<Integer, LinkedList<Integer>>> iIndicesByPrimitive : indicesByMaterial.entrySet()) {	
				
				// Apply material:
				//if (iIndicesByPrimitive.getKey() != null) iIndicesByPrimitive.getKey().render();
				
				// Create displaylist:
				int displayList = GL11.glGenLists(1);
				
				// Start outputting displaylist:
				GL11.glNewList(displayList, GL11.GL_COMPILE);
				
				GL11.glInterleavedArrays(GL11.GL_T2F_N3F_V3F, 0, interleavedBuffer);
				
				for (Map.Entry<Integer, LinkedList<Integer>> iIndices : iIndicesByPrimitive.getValue().entrySet()) {
					
					// Convert index-list to buffer:
					IntBuffer indexBuffer = BufferUtils.createIntBuffer(iIndices.getValue().size());
					for (Integer index : iIndices.getValue())
						indexBuffer.put(index);
					indexBuffer.flip();
					
					// Determine type of current face:
					int primitiveType;
				
					switch (iIndices.getKey()) {
						case 3: primitiveType = GL11.GL_TRIANGLES; break;
						case 4:	primitiveType = GL11.GL_QUADS; break;
						default: primitiveType = GL11.GL_POLYGON;
					}
					
					// Render primitives:
					GL11.glDrawElements(primitiveType, indexBuffer);
					
					// On some devices, immediate mode is faster than VBs:
					/*GL11.glBegin(primitiveType);
					for (int i = 0; i < iIndices.getValue().size(); i++) {
						GL11.glTexCoord2f(interleavedBuffer.get(indexBuffer.get(i)*8 + 0), interleavedBuffer.get(indexBuffer.get(i)*8 + 1));
						GL11.glNormal3f(interleavedBuffer.get(indexBuffer.get(i)*8 + 2), interleavedBuffer.get(indexBuffer.get(i)*8 + 3), interleavedBuffer.get(indexBuffer.get(i)*8 + 4));
						GL11.glVertex3f(interleavedBuffer.get(indexBuffer.get(i)*8 + 5), interleavedBuffer.get(indexBuffer.get(i)*8 + 6), interleavedBuffer.get(indexBuffer.get(i)*8 + 7));
					}
					GL11.glEnd();*/
				}
				
				GL11.glEndList();
				
				// Insert the mesh related to its material:
				meshes.put(iIndicesByPrimitive.getKey(), new Mesh(displayList));
			}
		}
		catch (Exception exception) {
			
			// Vertex-data is invalid, throw exception:
			exception.printStackTrace();
			throw new IOException("incompatible vertex-data");
		}
		finally {
		}

		return new Model(meshes);
	}
}
