package com.mycompany.app;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Mansi
 *
 * This class reads the xml file entered by the user, if user does not enter filename, default will be used.
 */
public class RunnerXMLFile implements RaceConfigDAO{
	
	private Path xmlPath = null;
	
	/**
	 * @param sc input is scanner object
	 * Constructs the Runners from given XML file definition
	 */
	public RunnerXMLFile(Scanner sc) throws IOException {
		System.out.println("Enter XML file name or press enter to use default file: "); 
		String fileName = sc.nextLine();
		if(fileName != null && fileName.trim().length() != 0){
			xmlPath = Paths.get(fileName);
			if(!Files.exists(xmlPath)){				
				throw new IOException("The " + fileName + " file was not found. Exiting!");
			} 
		}
		else{
			xmlPath = Paths.get("FinalXMLdata.xml");
		}
	}

	/**
	 * Returns the list of runners from XML file
     */
    @Override
	public RaceConfig getRaceConfig() {
        RaceConfig raceConfig = new RaceConfig();
	    
		// prevent the FileNotFoundException
		if(Files.exists(xmlPath)){	    
			
			//create the XMLInputFactory
			XMLInputFactory inputFactory =  XMLInputFactory.newFactory();
			try{
				
				// create a XMLStreamReader object
				FileReader fileReader = new FileReader(xmlPath.toFile());
				XMLStreamReader reader = inputFactory.createXMLStreamReader(fileReader);

                String name = null;
                int speed = -1;
                int restPercent = -1;
				
				// read the products from the file
				while(reader.hasNext()){
					int eventType = reader.getEventType();
					switch(eventType){
						case XMLStreamConstants.START_ELEMENT:
							String elementName = reader.getLocalName();
							if(elementName.equals("Runner")){
								name = reader.getAttributeValue(0);
							} else if(elementName.equals("RunnersMoveIncrement")){
								String speedText = reader.getElementText();
								speed = Integer.parseInt(speedText);
							} else if(elementName.equals("RestPercentage")){
								String restText = reader.getElementText();
								restPercent = Integer.parseInt(restText);
							}
							break;
						case XMLStreamConstants.END_ELEMENT:
							elementName = reader.getLocalName();
							if(elementName.equals("Runner")){
                                if (name != null && speed >= 0 && restPercent >= 0) {
                                    raceConfig.add(name, speed, restPercent);
                                } else {
                                    System.out.println(String.format("Ignoring invalid runner with partially/incorrectly specified fields %s, %d, %d", name, speed, restPercent));
                                }
							}
							break;
						default:
							break;
					}
					reader.next();
				}
			} 
			catch(IOException | XMLStreamException e){
                throw new RuntimeException("The XML file " + xmlPath + " was not correct", e);
			}	
		}		
		return raceConfig;
	}
}
