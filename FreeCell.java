import java.util.*;
import java.io.*;
/**
 * This program is a simplified version of the FreeCell game
 * @author Chua_Bing_Quan TT04 1181303556
 * @version 1.0
 */
public class FreeCell{
	public static boolean quit = false;     // flag for checking quit game condition
	public static boolean enableCLS = true; // flag for checking clear screen options

	public static void main(String[] args) throws IOException, InterruptedException{
		Scanner input = new Scanner(System.in);
		// Setting up a vector of ordered cards
		ArrayList<Cards> deck = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 13; j++){
				deck.add(new Cards(i, j));
			}
		}
		Collections.shuffle(deck); //shuffling the deck 
		
		// Creating ArrayList of OrderedStack (the pileBoard)  
		List<OrderedStack<Cards>> pileBoard = new ArrayList<OrderedStack<Cards>>();
		for(int i = 0; i < 4; i++){
			pileBoard.add(new OrderedStack<>());
		}
		// Creating ArrayList of Stack (the columnBoard)  
		List<Stack<Cards>> columnBoard = new ArrayList<Stack<Cards>>();
		for(int i = 0; i < 9; i++){
			columnBoard.add(new Stack<>());
		}
		
		String source;      // source (from which column) / command (input X, R) / cheat codes
		String card;        // the card
		String destination; // destination of the card (where it goes)
		boolean specialCmd = false; 	   	// flag for special command cases (restart, quit, cheat) during each move
		boolean inputError = false;			// flag for input validation errors
		boolean sourceError = false;		// flag for invalid source and card 
		boolean destinationError = false;	// flag for invalid move to destination
		boolean win = false;                // flag for endgame conditions
		int step = 0;     // number of moves made by the user
		int col = 0;      // nth column of which to be rotated
		int numberOfMovingCards = 0;  // number of moving cards in one command		
		Cards tempCard;     // to hold a temparary card while moving two cards 
		String preMove = "-";  // holds previous move made by user

		int sourceCode = 0;      // holds code for the cards destination
		int suitCode = 0;        // holds code for the suit of the card
		int rankCode = 0;        // holds code for the rank of the card
		int destinationCode = 0; // holds code for the card's destination
		
		// starting the game
		distributeCards(columnBoard, deck);
		clearScreen();
		mainMenu();
		clearScreen();
		
		if(!quit){
			do{
				System.out.println("\n   Number of Moves > " + step++);
				if(preMove == "-")
					System.out.println();
				else
					System.out.println("   Previous   Move > " + preMove);
				System.out.println();
				displayBoard(pileBoard, columnBoard);
				do{
					System.out.print("\n   Command > ");
					specialCmd = false; // reseting all the flags
					inputError = false;
					sourceError = false;
					destinationError = false;
					numberOfMovingCards = 0;

					source = input.next();
					// checking for special command
					if(source.length() == 1){
						// command for immediate exit the game
						if(Character.toUpperCase(source.charAt(0)) == 'X'){
							specialCmd = true;
							quit = true;
						}
						// command for resetting current game
						if(Character.toUpperCase(source.charAt(0)) == 'R'){
							for(int i = 0; i < 4; i++)
								pileBoard.get(i).clear(); // clearing previous game
							for(int i = 0; i < 9; i++)
								columnBoard.get(i).clear(); // clearing previous game
							Collections.shuffle(deck); // shuffles the deck
							distributeCards(columnBoard, deck); // redistribute the cards
							preMove = "Resetting the game";
							step = 0;
							specialCmd = true;
						}
						// command for autosolving the game
						if(Character.toUpperCase(source.charAt(0)) == 'S'){
							autoSolve(pileBoard, columnBoard, step);
							specialCmd = true;
						}
					}
					if(source.length() == 2){
						// command for rotating specific column
						if(Character.toUpperCase(source.charAt(0)) == 'R'){
							// for rotating the column (enable cheat)
							switch(source.charAt(1)){
								case 49: col = 0; break;
								case 50: col = 1; break;
								case 51: col = 2; break;
						 		case 52: col = 3; break;
						 		case 53: col = 4; break;
						 		case 54: col = 5; break;
						 		case 55: col = 6; break;
						 		case 56: col = 7; break;
								case 57: col = 8; break;
							}
							if((columnBoard.get(col)).size() > 1){
								columnRotation(columnBoard, col);
								preMove = "Rotating Column " + (col+1);
								specialCmd = true;
							}
						}		
					}
					
					// proceed if no special command
					if(!specialCmd){
						card = input.next();
						destination = input.next();
						input.nextLine(); // discard extra input
						
						// check for input error
						// checking source input
						if(source.length()==1){
							switch(source.charAt(0)){
								// checking for right source column 1~9
								case 49: sourceCode = 0; break;
								case 50: sourceCode = 1; break;
								case 51: sourceCode = 2; break;
								case 52: sourceCode = 3; break;
								case 53: sourceCode = 4; break;
								case 54: sourceCode = 5; break;
								case 55: sourceCode = 6; break;
								case 56: sourceCode = 7; break;
								case 57: sourceCode = 8; break;
								case 67:
								case 68:
								case 72:
								case 83: System.out.println("  <ERROR> You can't move from the deck"); inputError = true; break;
								default: System.out.println("  <ERROR> Source column does not exist"); inputError = true; break;
							}
						}
						else{
							System.out.println("  <ERROR> No such source command");
							inputError = true;
						}

						//checking card input
						if(card.length() == 2){
							// check suit c,d,h,s
							switch(Character.toUpperCase(card.charAt(0))){
								case 67: suitCode = 0; break;
								case 68: suitCode = 1; break;
								case 72: suitCode = 2; break;
								case 83: suitCode = 3; break;
								default: System.out.println("  <ERROR> No existing suit"); inputError = true; break;
							}
							// check rank A,2,3,4,5,6,7,8,9,X,J,Q,K
							switch(Character.toUpperCase(card.charAt(1))){
								case 65: rankCode = 0;  break;
								case 50: rankCode = 1;  break;
								case 51: rankCode = 2;  break;
								case 52: rankCode = 3;  break;
								case 53: rankCode = 4;  break;
								case 54: rankCode = 5;  break;
								case 55: rankCode = 6;  break;
								case 56: rankCode = 7;  break;
								case 57: rankCode = 8;  break;
								case 88: rankCode = 9;  break;
								case 74: rankCode = 10; break;
								case 81: rankCode = 11; break;
								case 75: rankCode = 12; break;
								default: System.out.println("  <ERROR> No exisiting face"); inputError = true; break;
							}
						}
						else{
							System.out.println("  <ERROR> No existing card");
						 	inputError = true;
						}

						// checking destination input
						if(destination.length()==1){
							switch(Character.toUpperCase(destination.charAt(0))){
								// checking for the right destination c,d,h,s,1~9
								case 67: destinationCode = 0;  break;
								case 68: destinationCode = 1;  break;
								case 72: destinationCode = 2;  break;
								case 83: destinationCode = 3;  break;
								case 49: destinationCode = 4;  break;
								case 50: destinationCode = 5;  break;
								case 51: destinationCode = 6;  break;
								case 52: destinationCode = 7;  break;
								case 53: destinationCode = 8;  break;
								case 54: destinationCode = 9;  break;
								case 55: destinationCode = 10; break;
								case 56: destinationCode = 11; break;
								case 57: destinationCode = 12; break;
								default: System.out.println("  <ERROR> Destination not found"); inputError = true; break;
							}
						}
						else{
							System.out.println("  <ERROR> No such destination command");
							inputError = true;
						}

						//proceed if no input error
						if(!inputError){
							// check for source error
							if(columnBoard.get(sourceCode).isEmpty()){
								System.out.println("  <ERROR>  The source column is empty");
								sourceError = true;
							}
							else{
								// check if the card is inside the column
								if(cardSearch(columnBoard, sourceCode, suitCode, rankCode)){
									// check if the card is at the top stack (last card in stack)
									if( ((columnBoard.get(sourceCode)).peek()).getSuitCode() == suitCode && ((columnBoard.get(sourceCode)).peek()).getRankCode() == rankCode ){
										numberOfMovingCards = 1;
									}
									else{
										// check if the card is at the second top
										if( ((columnBoard.get(sourceCode)).get( ((columnBoard.get(sourceCode)).size()-2) ).getSuitCode() == suitCode && ((columnBoard.get(sourceCode)).get( ((columnBoard.get(sourceCode)).size()-2) )).getRankCode() == rankCode)){
											// check if both cards are in order
											if( ((columnBoard.get(sourceCode)).peek()).getRankCode()+1 == ((columnBoard.get(sourceCode)).get( ((columnBoard.get(sourceCode)).size()-2) )).getRankCode()){
												numberOfMovingCards = 2;
											}
											else{
												System.out.println("  <ERROR> The last two cards in the source column are not in order.");
												sourceError = true;
											}
										}
										else{
											System.out.println("  <ERROR> You can't move the card at the moment");
											sourceError = true;
										}
									}
								}
								else{
									System.out.println("  <ERROR> Card is not inside the source column");
									sourceError = true;
								}
							}

							// proceed if no source error
							if(!sourceError){
								// no need further checking for card to pile as it would be done in the OrderedStack class
								if(destinationCode < 4){ 
									// moving two cards to the pile
									if(numberOfMovingCards == 2){
										try{
											// try pushing the first card
											(pileBoard.get(destinationCode)).push((columnBoard.get(sourceCode)).peek());
											// no problem, deletes card
											columnBoard.get(sourceCode).pop();
											numberOfMovingCards--;
											// try pushing the second card
											(pileBoard.get(destinationCode)).push((columnBoard.get(sourceCode)).peek());
											// no problem, deletes card
											columnBoard.get(sourceCode).pop();
											numberOfMovingCards--;
										}
										catch(CardsNotInOrderException ex){
											System.out.println(ex);
											destinationError = true;
											if(numberOfMovingCards == 1)
												System.out.println("  Hint: The last card is moved to the Pile");
											else
												System.out.println("  Hint: The last card does not belong to the Pile");
										}
									}
									// moving one card to the pile
									else{
										try{
											// try pushing the card into the ordered stack
											(pileBoard.get(destinationCode)).push((columnBoard.get(sourceCode)).peek());
											// if no error
											columnBoard.get(sourceCode).pop();
										}
										catch(CardsNotInOrderException ex){
											System.out.println(ex);
											destinationError = true;
										}
									}
								}
								// moving card to column
								// checking possible error when moving cards to column
								else{ 
									if(!(columnBoard.get(destinationCode-4).isEmpty())){
										if(numberOfMovingCards == 2){
											if( ((columnBoard.get(destinationCode-4)).peek()).getRankCode() != rankCode+1 ){
												System.out.println("  <ERROR> Unable to Move: Order with destination is wrong");
												destinationError = true;
											}
											else{
												tempCard = (columnBoard.get(sourceCode)).pop();
												(columnBoard.get(destinationCode-4)).push((columnBoard.get(sourceCode)).pop());
												columnBoard.get(destinationCode-4).push(tempCard);
											}
										}
										// moving one card
										else{
											if( ((columnBoard.get(destinationCode-4)).peek()).getRankCode() != rankCode+1 ){
												System.out.println("  <ERROR> Unable to Move: Order with destination is wrong");
												destinationError = true;
											}
											else{
												(columnBoard.get(destinationCode-4)).push((columnBoard.get(sourceCode)).pop());
											}
										}
									}
									// no need further checking if the destination column is empty
									else{
										(columnBoard.get(destinationCode-4)).push((columnBoard.get(sourceCode)).pop());
									}
								}
							}
							if(destinationCode < 4)
								preMove = "Moving " + Character.toLowerCase(card.charAt(0)) + Character.toUpperCase(card.charAt(1)) + " from Column " + source + " to Pile " + destination;
							else
								preMove = "Moving " + Character.toLowerCase(card.charAt(0)) + Character.toUpperCase(card.charAt(1)) + " from Column " + source + " to Column " + destination;
							if(numberOfMovingCards == 2)
								preMove = preMove + " with ";

						}					
					}
				}while(inputError || sourceError || destinationError);
				// repeat the loop while there is still error
				if(quit)
					break;

				if(enableCLS)
					clearScreen();
				else
					System.out.println("\n\n\n");
				win = endGameCheck(columnBoard);
			}while(!win);
		}	
			
		if(enableCLS)
			clearScreen();	
		if(win){
			//System.out.println("\n   Number of Moves > " + step);
			//System.out.println("   Previous   Move > " + preMove);
			System.out.println();
			displayBoard(pileBoard, columnBoard);
			System.out.println("  > You win!");
		}
		input.close(); // close Scanner 
	}
	
	/**
	 * This function displays the stack of cards (displays pileBoard and columnBoard)
	 * @param pileBoard The array list of piles (c,d,h,s) for the game
	 * @param columnBoard The array list of columns (1~9) for the game
	 */
	public static void displayBoard(List<OrderedStack<Cards>> pileBoard, List<Stack<Cards>> columnBoard){
		System.out.println("    Pile   c: " + pileBoard.get(0));
		System.out.println("    Pile   d: " + pileBoard.get(1));
		System.out.println("    Pile   h: " + pileBoard.get(2));
		System.out.println("    Pile   s: " + pileBoard.get(3));
		for(int i = 0; i < 70; i++)
			System.out.print("=");
		System.out.println();
		for(int i = 0; i < 9; i++){
			System.out.println("    Column " + (i+1) + ": " + columnBoard.get(i));
		}
		System.out.println();
	}
	
	/**
	 * This function distributes the cards into the columnBoard (initial state of a game)
	 * @param columnBoard The array list of columns (1~9) for the game
	 * @param deck Array list of 52 Cards 
	 */
	public static void distributeCards(List<Stack<Cards>> columnBoard, ArrayList<Cards> deck){
		// randomizing number of cards for each column
		ArrayList<Integer> numberOfCards = new ArrayList<>(Arrays.asList(7, 7, 7, 7, 6, 6, 6, 6));
		// Collections.shuffle(numberOfCards); // for randomizing the order of the columns
		// generating cummulative
		for(int i = 1; i < 8; i++){
			numberOfCards.set(i, numberOfCards.get(i) + numberOfCards.get(i-1)); 
		}
		// adding 0 to the front of the array list
		numberOfCards.add(0,0);

		for(int i = 1; i < 9; i++){
			for(int j = numberOfCards.get(i-1); j < numberOfCards.get(i); j++){
				(columnBoard.get(i-1)).push(deck.get(j));
			}
		}
	}

	/**
	 * This function search for the card with a given pattern code and rank code from a column
	 * @param columnBoard The array list of columns (1~9) for the game
	 * @param s (int) Code for the source column (Column to search)
	 * @param p (int) Code for the pattern (suit) of the card
	 * @param r (int) Code for the rank of the card
	 * @return (boolean) Whether the card is found within the column
	 */
	public static boolean cardSearch(List<Stack<Cards>> columnBoard, int s, int p, int r){
		for(int i = 0; i < columnBoard.get(s).size(); i++){
			if((columnBoard.get(s)).get(i).getSuitCode() == p && (columnBoard.get(s)).get(i).getRankCode() == r)
				return true;
		}
		return false;
	}

	/**
	 * This function checks if the game ended or not
	 * @param columnBoard The array list of columns (1~9) for the game
	 * @return (boolean) Whether the game ended or not
	 */
	public static boolean endGameCheck(List<Stack<Cards>> columnBoard){
		for(int i = 0; i < 9; i++){
			if(!(columnBoard.get(i)).isEmpty())
				return false;
		}
		return true;
	}

	/**
	 * (Cheat) This function brings the last element in the column to the front
	 * @param columnBoard The array list of columns (1~9) for the game
	 * @param c (int) Code for which column to perform rotation (starts from 0)
	 */
	public static void columnRotation(List<Stack<Cards>> columnBoard, int c){
		(columnBoard.get(c)).add(0, (columnBoard.get(c)).remove((columnBoard.get(c)).size()-1));
	}

	/**
	 * This function auto solves the game 
	 * @param pileBoard The array list of piles (c,d,h,s) for the game
	 * @param columnBoard The array list of columns (1~9) for the game
	 * @param step (int) Starting from the nth step the user uses auto solve 
	 */
	public static void autoSolve(List<OrderedStack<Cards>> pileBoard, List<Stack<Cards>> columnBoard, int step){
		Cards temp;
		enableCLS = false;
		for(int r = 0; r < 13; r++){
			for(int s = 0; s < 4; s++){
				for(int c = 0; c < 9; c++){
					// if the card is in the top stack
					if(columnBoard.get(c).size() > 0){
						for(int i = 0; i < columnBoard.get(c).size(); i++){
							// check if the card is in the column
							if((columnBoard.get(c)).get(i).getSuitCode() == s && (columnBoard.get(c)).get(i).getRankCode() == r){
								for(int t = 0; t < ((columnBoard.get(c).size()-1)-i); t++){
									// rotate column
									//(columnBoard.get(c)).add(0, (columnBoard.get(c)).remove((columnBoard.get(c)).size()-1));
									columnRotation(columnBoard, c);
									System.out.println("  Step " + (step++) + " Rotate Column " + (c+1));
								}
								// move the card to pile
								temp = columnBoard.get(c).pop();
								try{
									pileBoard.get(s).push(temp);
									System.out.println("  Step " + (step++) + " Move " + temp + " from Column " + (c+1) + " to Pile " + temp.getSuits());
								}
								catch(CardsNotInOrderException ex){
									System.out.println("  <ERROR>");
								}	
							}
						}
					}		
				}
			}
		}
	}

	/**
	 * This function clears the console
	 * @throws IOException Handle IOException 
	 * @throws InterruptedException Handle InterruptedException
	 */
	public static void clearScreen() throws IOException, InterruptedException{
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}

	/**
	 * This function handles the main menu of the game
	 * @throws IOException Handle IOException from the clearScreen() function
	 * @throws InterruptedException Handle InterruptedException from the clearScreen() function
	 */
	public static void mainMenu() throws IOException, InterruptedException{
		Scanner input = new Scanner(System.in);
		String cmd;
		for(int i = 0; i < 5; i++)
			System.out.println();
		System.out.println("		             ___________                       _________         __   __      ");
		System.out.println("		 	     \\_   _____/______   ____   ____   \\_   ___ \\  ____ |  | |  |      ");
		System.out.println("			      |    __) \\_  __ \\_/ __ \\_/ __ \\  /    \\  \\/_/ __ \\|  | |  |  ");
		System.out.println("			      |     \\   |  | \\/\\  ___/\\  ___/  \\     \\___\\  ___/|  |_|  |__");
		System.out.println("			      \\___  /   |__|    \\___  >\\___  >  \\______  /\\___  >____/____/  ");
		System.out.println("			          \\/                \\/     \\/          \\/     \\/             ");
		System.out.println("\n\n");
		System.out.println("						      1 -   PLAY  ");
		System.out.println("						      2 -   HELP  ");
		System.out.println("						      3 - SETTINGS");
		System.out.println("						      4 -   QUIT  ");
		System.out.println();
		System.out.print("						     Command > ");
		cmd = input.nextLine();
		if(cmd.length() == 1){
			switch(cmd.charAt(0)){
				case 49: break;
				case 50: clearScreen();
						 System.out.println("    <HELP>");
						 System.out.println("   <-- BASIC RULES -->");
						 System.out.println("             suits of the cards:  c (clubs), d (diamonds), h (hearts), s (spades)       ");
						 System.out.println("             faces of the cards:  A, 2, 3, 4, 5, 6, 7, 8, 9, X(10), J, Q, K           \n");
						 System.out.println("   1. Card(s) shall be moved from the end (right-most) of a Column to the end of another Column or the\n      end of another Pile.");
						 System.out.println("   2. No card shall be moved from the Piles.");
						 System.out.println("   3. When moving a card to a Pile, the card must be placed at the end of the Pile of the matching suit\n      and the moved card must be one point bigger than the last (right-most) card at the Pile.");
						 System.out.println("   4. When moving a card to another Column, the suit and color are always ignored. The moved card must\n      be placed to a Column where its last card is one point bigger than the moved card.");
						 System.out.println("   5. If you have a group of consecutive right-most cards whereby the faces are in order, you may move\n      all of them to another Column in just one command.");
						 System.out.println("   6. If you have a group of consecutive right-most cards whereby the suits and faces are in order, you\n      may move all of them to the Pile in just one command.");
						 System.out.println("\n\n");
						 System.out.println("   <-- COMMAND GUIDE -->");
						 System.out.println("   <source> <card> <destination> : To move card(s) from source Column to destination Column or Pile");
						 System.out.println("                             r   : To restart the game to new game.");
						 System.out.println("                             x   : To quit the game.");
						 System.out.println("\n\n\n");
						 System.out.print("   (Enter to return to main menu)");
						 cmd = input.nextLine();
						 clearScreen();
						 mainMenu();
						 break;
				case 51: clearScreen();
						 System.out.println("    <SETTINGS>");
						 System.out.println("   <-- Clear Screen -->");
						 System.out.print("    Clear screen is currently");
						 if(enableCLS){
						 	System.out.println(" enabled");
						 	System.out.print("    Disable clear screen after each move? <Y/N>: ");
						 }
						 else{
						 	System.out.println(" disabled");
						 	System.out.print("    Enable clear screen after each move? <Y/N>: ");
						 }
						 cmd = input.nextLine();
						 if(cmd.length() == 1){
						 	if(Character.toUpperCase(cmd.charAt(0)) == 'Y'){
						 		if(enableCLS){
						 			enableCLS = false;
						 			System.out.println("    > Clear screen disabled");
						 		}
						 		else{
						 			enableCLS = true;
						 			System.out.println("    > Clear screen enabled");
						 		}
						 		System.out.print("\n\n\n   (Enter to return to main menu)");
								cmd = input.nextLine();
						 	}
						 	else 
						 		if(Character.toUpperCase(cmd.charAt(0)) == 'N'){
						 			System.out.println("    > No changes were made");
						 			System.out.println("\n\n\n");
						 			System.out.print("   (Enter to return to main menu)");
									cmd = input.nextLine();
						 		}
							clearScreen();
							mainMenu();
							break;
						 }
						 else{
						 	clearScreen();
						 	mainMenu();
							break;
						 }
				case 52: quit = true;
						 break;
				default: clearScreen();
						 mainMenu();
						 break;
			}
		}
		else{
			clearScreen();
			mainMenu();
		}
	}
}