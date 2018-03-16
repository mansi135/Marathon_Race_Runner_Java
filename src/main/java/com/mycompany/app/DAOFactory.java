package com.mycompany.app;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Mansi
 *
 * This class provides user an option to selects the input source from which the data has to be read.
 */
public class DAOFactory {
	/**
	 * @param action user input which decides the data source from where runners will be selected
	 * @return MarathonDAO object
	 */
	public static RaceConfigDAO getDAO(int action) throws IOException {
		switch(action){
			case 1: 
				return new RunnerDB();
			case 2: {
				return new RunnerXMLFile(new Scanner(System.in));
            }
			case 3: {
				return new RunnerTextFile(new Scanner(System.in));
            }
			case 4: 
				return new RunnerDefault(); 
			case 5: 
                // Exit
				return null;	
			default:
				System.out.println("Invalid Input");
				return null;
		}
	}
}
