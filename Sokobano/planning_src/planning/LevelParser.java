package planning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LevelParser {
	
	public Level parse(String filename) throws FileNotFoundException, ParseException{
		Level level = new Level();
		
		List<String> lines = readAll(filename);
		
		level.intialize(lines);
		
		return level;
	}
	
	private List<String> readAll(String filename) throws FileNotFoundException{
		BufferedReader input = new BufferedReader(new FileReader(filename));
		
		LinkedList<String> lines = new LinkedList<String>();
		String line;
		try{
			while((line = input.readLine()) != null){
				lines.add(line);
			}
		} catch (IOException e){
			System.out.println("FILE ERROR!");
			e.printStackTrace();
		}
		
		return lines;
	}
}
