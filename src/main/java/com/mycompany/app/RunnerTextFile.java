package com.mycompany.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mansi
 *
 * This class reads the text file entered by the user, if user does not enter filename, default will be used.
 */
public class RunnerTextFile implements RaceConfigDAO {
	private Path textPath = null;
	private File textFile = null;
	private final String FIELD_SEP = "\t";
	
	/**
	 * @param sc input is scanner object
	 */
	public RunnerTextFile(Scanner sc) throws IOException {
		System.out.println("Enter text file name or press enter to use default file: "); 
		String fileName = sc.nextLine();
		if(fileName != null && fileName.trim().length() != 0){
			textPath = Paths.get(fileName);
			textFile = textPath.toFile();
			if(!Files.exists(textPath)){				
				throw new IOException("The " + fileName + " file was not found. Exiting!");
			} 
		}
		else{
			textPath = Paths.get("FinalTextData.txt");
			textFile = textPath.toFile();
		}
	}
	
    @Override
	public RaceConfig getRaceConfig() throws IOException {
        RaceConfig raceConfig = new RaceConfig();
        
        // prevent the FileNotFoundException
        	try(BufferedReader in = new BufferedReader(new FileReader (textFile))) {
        		String line = in.readLine();
        		while(line != null){
        			String[] columns = line.split(FIELD_SEP);
        			String name = columns[0];
        			String speed = columns[1];
        			String restPercent = columns[2];
        			raceConfig.add(name, Integer.parseInt(speed), Integer.parseInt(restPercent));
        			line = in.readLine();
        		}
        	}
        	catch(IOException e){
                throw new IOException("Error when reading the file " + textFile, e);
        	}
		return raceConfig;
	}
}
