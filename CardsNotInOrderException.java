/**
 * This is a custom exception written for the class OrderedStack 
 * @author Chua_Bing_Quan TT04 1181303556
 */
public class CardsNotInOrderException extends Exception{
	public CardsNotInOrderException(String message){
		super("<ERROR> " + message);
	}
}