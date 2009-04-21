package gdi1sokoban.graphic;

import gdi1sokoban.graphic.base.Material;
import gdi1sokoban.graphic.base.Mesh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class Geometry extends DisplayList {
	
	HashMap<Material, HashMap<Transformation, LinkedList<Mesh>>> _geometry = new HashMap<Material, HashMap<Transformation, LinkedList<Mesh>>>();
	
	public Geometry() {
		super(0);
	}
	
	public void add(Model model, Transformation transformation) {
		for (Material material : model.getMaterials()) {
			
			// Get meshs with same material:
			HashMap<Transformation, LinkedList<Mesh>> transformations = _geometry.get(material);
			if (transformations == null) {
				transformations = new HashMap<Transformation, LinkedList<Mesh>>();
				_geometry.put(material, transformations);
			}
			
			// Get meshs with same material & transformation:
			LinkedList<Mesh> meshs = transformations.get(transformation);
			if (meshs == null) {
				meshs = new LinkedList<Mesh>();
				transformations.put(transformation, meshs);
			}
			
			// Add mesh to its dear friends:
			meshs.add(model.getMesh(material));
		}
	}
	
	public void build() {
		if (_displayList != 0) GL11.glDeleteLists(_displayList, 1);
		
		_displayList = GL11.glGenLists(1);
		GL11.glNewList(_displayList, GL11.GL_COMPILE);
		
		// Render similar materials:
		for (Map.Entry<Material, HashMap<Transformation, LinkedList<Mesh>>> iMaterial : _geometry.entrySet()) {
			if (iMaterial.getKey() != null) // Material could be null, meaning there is no material
				iMaterial.getKey().render();
			
			// Render similar transformations:
			for (Map.Entry<Transformation, LinkedList<Mesh>> iTransformation : iMaterial.getValue().entrySet()) {
				GL11.glPushMatrix();
				iTransformation.getKey().render();
				
				// Render the meshes:
				for (Mesh mesh : iTransformation.getValue()) {
					mesh.render();
				}
				
				GL11.glPopMatrix();
			}	
		}
		
		GL11.glEndList();
	}
	
	public void render() {
		assert(_displayList != 0);
		GL11.glPushMatrix();
		super.render();
		GL11.glPopMatrix();
	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}
}
