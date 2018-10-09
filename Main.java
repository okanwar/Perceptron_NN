import java.util.*;
import java.io.*;

public class Main{
	public static void main(String [] args){
		
		//Begin
		System.out.println("---- Welcome to the perceptron net ----\n" +
			"(1) Train a new perceptron net\n" +
			"(2) Train a net from file\n" );

		Scanner input = new Scanner(System.in);
		int userIn = input.nextInt();

		switch(userIn){
			case 1:
				//New perceptron net from scratch			
				break;
			case 2:
				//Load perceptron net from file
				break;
			default:
				break;
		}

	}


	private static void trainPerceptronNet(boolean newNet){
		
		//Get data file
		System.out.print("Enter a file name for the training data: ");	

		//Create new net if required
		if(newNet){
		
			//Get new net properties
		}



		//Open data file
		
		//Parse data file and created training set
		
		//Train Perceptron
	}

}
