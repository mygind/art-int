package gdi1sokoban.graphic;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.base.ModelDescriptor;
import gdi1sokoban.graphic.base.ModelManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class AnimationLoader {
	public static Animation load(String filename) throws FileNotFoundException, IllegalFormatException {

		HashMap<String, ModelManager.Resource> models = new HashMap<String, ModelManager.Resource>();
		HashMap<String, HashMap<String, Sequence>> animations = new HashMap<String, HashMap<String, Sequence>>();
		
		HashMap<String, Sequence> sequences = new HashMap<String, Sequence>();
		
		Vector<Transformation> sequenceTransformations = new Vector<Transformation>();
		Vector<Long> sequenceTimes = new Vector<Long>();
		
		String modelName = null;
		String animationName = null;
		String sequenceName = null;
		
		float[] position = null;
		float[] rotation = null;
		float[] scale = null;
		
		boolean hasTransformation = false;
		
		// Parse file:
		File modelFile = null;
		modelFile = new File(filename);
		Scanner scanner = new Scanner(modelFile);
		scanner.useDelimiter("\\n");
		
		try {
			while (scanner.hasNext())
			{
				String currentLine = scanner.next();
				String[] s = currentLine.split("\\s+");
				
				if (s.length == 0) {
					continue;
				}
				else if (s[0].equals("model")) {
					modelName = s[1];
				}
				else if (s[0].equals("mesh")) {
					ModelManager.Resource mesh = ModelManager.getInstance().getInstance(new ModelDescriptor(modelFile.getParent() + File.separator + s[2]));
					models.put(s[1], mesh);
				}
				else if (s[0].equals("anim")) {
					
					if (animationName != null) {
						
						// bisherige Transformation eintragen:
						if (hasTransformation)
							sequenceTransformations.add(new Transformation(position, rotation, scale));
					
						// Bisherige Sequence eintragen:
						Sequence sequence = new Sequence(sequenceTransformations, sequenceTimes);
						sequences.put(sequenceName, sequence);
	
						// Bisherige Animation eintragen:
						animations.put(animationName, sequences);
					}
					
					// Neue Aufzeichnung starten:
					animationName = s[1];
					
					sequences = new HashMap<String, Sequence>();
				}
				else if (s[0].equals("sequence")) {
					
					if (sequenceName != null) {
						
						// bisherige Transformation eintragen:
						if (hasTransformation)
							sequenceTransformations.add(new Transformation(position, rotation, scale));
	
						// Bisherige Sequence eintragen:
						Sequence sequence = new Sequence(sequenceTransformations, sequenceTimes);
						sequences.put(sequenceName, sequence);
					}
					
					sequenceTimes = new Vector<Long>();
					sequenceTransformations = new Vector<Transformation>();
					sequenceName = s[1];
					
					hasTransformation = false;
					position = null;
					rotation = null;
					scale = null;
				}
				else if (s[0].equals("time")) {
					
					// bisherige Transformation eintragen:
					if (hasTransformation) {
						sequenceTransformations.add(new Transformation(position, rotation, scale));
					}
					
					sequenceTimes.add(Long.valueOf(s[1]));
					hasTransformation = false;
				}
				else if (s[0].equals("p")) {
					position = new float[] {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3])};
					hasTransformation = true;
				}
				else if (s[0].equals("r")) {
					rotation = new float[] {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3])};
					hasTransformation = true;
				}
				else if (s[0].equals("s")) {
					scale = new float[] {Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3])};
					hasTransformation = true;
				}
			}
			
			if (animationName != null) {
				
				// bisherige Transformation eintragen:
				if (hasTransformation)
					sequenceTransformations.add(new Transformation(position, rotation, scale));
			
				// Bisherige Sequence eintragen:
				Sequence sequence = new Sequence(sequenceTransformations, sequenceTimes);
				sequences.put(sequenceName, sequence);
	
				// Bisherige Animation eintragen:
				animations.put(animationName, sequences);
			}
			
			scanner.close();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new IllegalFormatException("Invalid model file: " + filename);
		}
		
		return new Animation(modelName, models, animations);
	}
}
