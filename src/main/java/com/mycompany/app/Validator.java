package com.mycompany.app;
import java.util.Scanner;

/**
 * @author Mansi
 *
 * This class handles all the validations to be done on the user inputs.
 */
public class Validator {
	 
	 /**
	 * @param sc input is scanner object
	 * @param prompt input is text to be displayed to user
	 * @return user selected choice
	 */
	public static int getUserChoice(Scanner sc, String prompt) {
		int userChoice = IConstants.INVALID_CHOICE;
		String choice = "";
		int count = 0;
		boolean isValid = false;
		//continue until user inputs a valid value
		while (isValid == false) {
			System.out.print(prompt);		
			choice = sc.nextLine().trim();
			System.out.println(); // Force next input on another line
			if(choice.isEmpty()){
				System.out.println("Error! This entry is required. Try again.\n");
			}
			else{
				try{
					userChoice = Integer.parseInt(choice);
					 //check if user entered choice within range
					if(userChoice >= IConstants.CHOICE_MIN && userChoice <= IConstants.CHOICE_MAX){  
						isValid = true;
					}
					else {
						//reset choice
						userChoice = IConstants.INVALID_CHOICE; 
						count++;
						if(count < IConstants.MAX_COUNT) { 
							System.out.println("Error! Enter valid positive value between 1-5.\n");
						}
						//if user enters wrong choice 10 times, exit the program
						else {
							System.out.println("Exceeded number of attempts. Exiting!!\n");
                            return IConstants.INVALID_CHOICE;
						}				
					}
				}
				catch(NumberFormatException er){
					count++;
					System.out.println("Error! Invalid value. Try again with Integer Value.\n");		
				} 
			}
		}
		return userChoice;
	 }
}
