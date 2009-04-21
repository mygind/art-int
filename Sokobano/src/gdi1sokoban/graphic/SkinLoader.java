package gdi1sokoban.graphic;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.base.ModelDescriptor;
import gdi1sokoban.graphic.base.ModelLoader;
import gdi1sokoban.graphic.base.ModelManager;
import gdi1sokoban.logic.SkinIdentifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class SkinLoader {
	public static Skin load(SkinIdentifier ident) throws FileNotFoundException, IllegalFormatException {
		
		boolean[][] pattern = null;
		int iPattern = 0;
		
		LinkedList<Model> meshes = new LinkedList<Model>();
		
		LinkedList<Tile> walls = new LinkedList<Tile>();
		LinkedList<Tile> floors = new LinkedList<Tile>();
		LinkedList<Tile> targets = new LinkedList<Tile>();
		
		LinkedList<Tile> currentTileList = walls;
		Vector<Animation> crates = null;
		ModelManager.Resource skybox = null;
		
		// Parse file:
		String filename = "res"+File.separator+"skins"+File.separator+ident.getId()+File.separator+"skin.txt";
		File skinFile = new File(filename);
		//System.out.println(skinFile.exists());
		Scanner scanner = new Scanner(skinFile);
		scanner.useDelimiter("\\n");
		
		try {
			while (scanner.hasNext())
			{
				String currentLine = scanner.next();
				String[] s = currentLine.split("\\s+");
				
				if (s.length == 0) {
					continue;
				}
				else if (s[0].equals("wall")) {
					if (pattern != null) {
						currentTileList.addFirst(new Tile(pattern, meshes));
					}
					meshes = new LinkedList<Model>();
					pattern = new boolean[3][];
					iPattern = 0;
					currentTileList = walls;
				}
				else if (s[0].equals("floor")) {
					if (pattern != null) {
						currentTileList.addFirst(new Tile(pattern, meshes));
					}
					meshes = new LinkedList<Model>();
					pattern = new boolean[3][];
					iPattern = 0;
					currentTileList = floors;
				}
				else if (s[0].equals("target")) {
					if (pattern != null) {
						currentTileList.addFirst(new Tile(pattern, meshes));
					}
					meshes = new LinkedList<Model>();
					pattern = new boolean[3][];
					iPattern = 0;
					currentTileList = targets;
				}
				else if (s[0].equals("p")) {
					pattern[iPattern] = new boolean[] {(Integer.parseInt(s[1]) == 1), (Integer.parseInt(s[2]) == 1), (Integer.parseInt(s[3]) == 1)};
					iPattern++;
				}
				else if (s[0].equals("mesh")) {
					Model mesh = ModelLoader.load("res/mesh/" + s[1], GL11.GL_LINEAR, GL11.GL_LINEAR);
					meshes.add(mesh);
				}
				else if (s[0].equals("crates")) {
					crates = new Vector<Animation>(3);
					crates.add(AnimationLoader.load("res/mesh/" + s[1]));
					crates.add(AnimationLoader.load("res/mesh/" + s[2]));
					crates.add(AnimationLoader.load("res/mesh/" + s[3]));
				}
				else if (s[0].equals("skybox")) {
					skybox = ModelManager.getInstance().getInstance(new ModelDescriptor("res/mesh/" + s[1]));
				}
				else if (s[0].equals("worker")) {
					//worker = ModelManager.getInstance().getInstance(new ModelDescriptor(modelFile.getParent() + File.separator + s[1]));
				}
			}
			
			currentTileList.addFirst(new Tile(pattern, meshes));
	
			if ((crates == null) || (skybox == null) || (targets.isEmpty()) || (walls.isEmpty()) || (floors.isEmpty()))
				throw new Exception();
			
			scanner.close();
		}
		catch (Exception exception) {
			throw new IllegalFormatException("Invalid skin file: " + filename);
		}
		
		return new Skin(walls, floors, targets, crates, skybox, ident);
	}
}
