/**
 * This class implements a poker card
 * This class is specially design for the FreeCell game in the same folder
 * @author Chua_Bing_Quan TT04 1181303556
 * @version 1.0
 */
public class Cards{
	private int suitCode; // Numeric code for deciding the suit of the card 
	private int rankCode; // Numeric code for deciding the rank of the card 
	private char suits;   // Character for displaying suit (c,d,h,s)
	private char ranks;   // Character for displaying rank (A,2~9,X,J,Q,K)
	
	/**
	 * Constructor, takes in unique combination of suitCode and rankCode
	 * @param suitCode (int) Code for deciding the suit of the card 
	 * @param rankCode (int) Code for deciding the rank of the card 
	 */
	public Cards(int suitCode, int rankCode){
		this.suitCode = suitCode;
		this.rankCode = rankCode;
		
		// translate suitCode into suits for displaying
		switch(suitCode){
			case 0: suits = 'c'; break; 
			case 1: suits = 'd'; break;
			case 2: suits = 'h'; break;
			case 3: suits = 's'; break;
		}
		// translate rankCode into ranks for displaying
		switch(rankCode){
			case 0: ranks = 'A'; break; 
			case 1: ranks = '2'; break;
			case 2: ranks = '3'; break;
			case 3: ranks = '4'; break;
			case 4: ranks = '5'; break;
			case 5: ranks = '6'; break;
			case 6: ranks = '7'; break;
			case 7: ranks = '8'; break;
			case 8: ranks = '9'; break;
			case 9: ranks = 'X'; break;
			case 10: ranks = 'J'; break;
			case 11: ranks = 'Q'; break;
			case 12: ranks = 'K'; break;
		}
	}
	
	/**
	 * This function returns the suitCode of the object
	 * @return (int) Code for suit
	 */
	public int getSuitCode(){
		return suitCode;
	}

	/**
	 * This function returns the rankCode of the object
	 * @return (int) Code for rank
	 */
	public int getRankCode(){
		return rankCode;
	}

	/**
	 * This funcition returns the suit of the object
	 * @return (char) Suit
	 */
	public char getSuits(){
		return suits;
	}

	/**
	 * This function returns the rank of the object
	 * @return (char) Rank
	 */
	public char getRanks(){
		return ranks;
	}

	/**
	 * This function returns the face and point of the Card object
	 * @return (String) name of the card for displaying
	 */
	@Override
	public String toString(){
		return Character.toString(suits) + Character.toString(ranks);
	}
}