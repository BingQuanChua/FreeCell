import java.util.ArrayList;
/**
 * This class is use to represent the pile in the game
 * The OrderedStack is written only to store Cards objects
 * @author Chua_Bing_Quan TT04 1181303556
 * @param <E> Class object
 */
public class OrderedStack<E> extends ArrayList<E>{
	private java.util.ArrayList<Cards> list = new java.util.ArrayList<>();
	private static int counter = 0; // counter for how many 
	private int suitCode = 0;  // represent a numeric code for the pile

	/**
	 * Constructer, assign a unique number (suitCode) for the pile stack 
	 */
	public OrderedStack(){
		suitCode = counter++;
	}

	/**
	 * This function returns the size of the OrderedStack
	 * @return (int) Size of OrderedStack
	 */
	public int getSize(){
		return list.size();
	}

	/**
	 * This function checks if the new card follows the rule before pushing it into the stack 
	 * @param c (Cards) New card to be pushed into the stack after going through some checkings
	 * @throws CardsNotInOrderException If the current Cards object are not in order with the previous Cards object
	 */
	public void push(Cards c) throws CardsNotInOrderException{
		if(suitCode == c.getSuitCode()){
			if(list.isEmpty()){
				if(c.getRankCode() == 0){
					list.add(c);
				}
				else{
					 // must be A
					throw new CardsNotInOrderException("The first card in the pile must be A");
				}
			}
			else{
				if(c.getRankCode()-1 == list.get(list.size()-1).getRankCode()){
					list.add(c);
				}
				else{
					// does not follow priority
					throw new CardsNotInOrderException("The card does not follow order of previous card");
				}
			}
		}
		else{
			// throw wrong pattern
			throw new CardsNotInOrderException("The card does not belong to this pile");
		}
	}

	/**
	 * This function removes the last Cards object in the OrderedStack
	 * @return (Cards) The removed Cards object
	 */
	public Cards pop(){
		Cards c = list.get(getSize()-1);
		list.remove(getSize()-1);
		return c;
	}

	/**
	 * This function returns the last Cards object in the OrderedStack
	 * @return (Cards) The Cards object on the top of the stack
	 */
	public Cards peek(){
		return list.get(getSize()-1);
	}
	/**
	 * This function clears the stack
	 */
	public void clear(){
		list.clear();
	}
	
	/** 
	 * This function checks if the stack is empty or not
	 * @return (boolean) Whether the function is empty or not
	 */
	public boolean isEmpty() { 
	    return list.isEmpty();
	}
  
	/**
	 * This function overrides the toStirng function
	 * @return (String) Return string for ouput
	 */
  	@Override
  	public String toString(){
   		return list.toString();
  	}
}