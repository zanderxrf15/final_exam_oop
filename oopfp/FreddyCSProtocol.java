package oopfp;

import java.util.Random;

public class FreddyCSProtocol {
	// Status in the protocol's logic
    private static final int WAITING = 0;
    private static final int ASK1 = 1;
    private static final int ORDER = 2;
    private static final int TYPE = 3;
    private static final int DELIVERY = 4;
    private static final int END = 5;
    private static final int CHAT = 6;
    // The string to be concatenated with the orders later down the line
    String orderList = "Great, here's your order:\n";
    
    // first status is waiting for the client's first chat
    private int state = WAITING;
    public boolean chatLiveServer = false;
    
    public String processInput(String input){
        String output = "";

        //welcome the client and ask what they want to do in the application
        if(state == WAITING){
            output = "Welcome to Freddy Fazbear's Pizzeria Online Order Service.\nHow may We help you today?\n1. Order a pizza\n2. Talk to a Customer Service Agent\n\n Type 'Exit' at any moment to end the session";
            state = ASK1;
        }
        else if(state == ASK1){
            //if they are ordering, list the items available and ask what they want to order. Then send them to the ORDER state
            //if they want to talk to customer service, send them to the CHAT state
            //if their answer is none of both, ask them again between those two
            if(input.equals("1") || input.equalsIgnoreCase("Order a pizza")){
                output = "Hello, Customer. What would you like to order?\n1. Chicken Pepperoni\n2. Beef Pepperoni\n3. Extremely Cheezy\n4. Meat Lovers\n5. Potato Wedges\n6. Cheesy Bread\n7. Coca Cola\n8. Sprite\n9. Fanta";
                state = ORDER;
            }
            else if(input.equals("2") || input.equalsIgnoreCase("talk to a customer service agent")){
                output = "Please hold. We are finding an available agent for you";
                state = CHAT;
            }
            else{
                output = "Please type only the number or only the sentence. ('1' or 'order a pizza')";
            }
        }
        else if(state == ORDER){
            //gets the order and concatenate it into the orderList string to be printed out later
            //if they type no, they will be sent to the TYPE state to chose between a takeaway order or a delivery order
            if(input.equalsIgnoreCase("Chicken Pepperoni") || input.equals("1")){
            	orderList = orderList.concat("Chicken Pepperoni\n");
            	output = "Chicken Pepperoni. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Beef Pepperoni") || input.equals("2")){
            	orderList = orderList.concat("Beef Pepperoni\n");
            	output = "Beef Pepperoni. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Extremely Cheezy") || input.equals("3")) {
            	orderList = orderList.concat("Extremely Cheezy\n");
            	output = "Extremely Cheezy. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Meat Lovers") || input.equals("4")) {
            	orderList = orderList.concat("Meat Lovers\n");
            	output = "Meat Lovers. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Potato Wedges") || input.equals("5")) {
            	orderList = orderList.concat("Potato Wedges\n");
            	output = "Potato Wedges. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Cheesy Bread") || input.equals("6")) {
            	orderList = orderList.concat("Cheesy Bread\n");
            	output = "Cheesy Bread. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Coca Cola") || input.equals("7")) {
            	orderList = orderList.concat("Coca Cola\n");
            	output = "Coca Cola. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Sprite") || input.equals("8")) {
            	orderList = orderList.concat("Sprite\n");
            	output = "Sprite. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("Fanta") || input.equals("9")) {
            	orderList = orderList.concat("Fanta\n");
            	output = "Fanta. Anything else? (type 'no' if that's all)";
            }
            else if(input.equalsIgnoreCase("no")) {
            	orderList = orderList.concat("\nWould you like to Takeaway or Delivery?\nType 'Takeaway' or 'Delivery'");
            	output = orderList;
            	state = TYPE;
            }
            else{
                output = "Please type only the number or only the sentence. ('1' or 'chicken pepperoni')";
            }
        }
        else if(state == TYPE) {
            //if they do takeaway, generate an order code for them. And send them to the END state
            //if they do delivery, send them to the DELIVERY state and ask for their address
        	if(input.equalsIgnoreCase("Takeaway")) {
        		Random r = new Random();
        		int fourDigit = 1000 + r.nextInt(10000);
        		String takeCode = "Great, here's your Order Code\n";
        		takeCode = takeCode + fourDigit;
        		takeCode = takeCode.concat("\nShow the code to the cashier to receive your order\nDo you need anything else?\nTyping 'Yes' will take you back to the start\\n'Exit' will end the session");
        		output = takeCode;
        		state = END;
        	}
        	else if(input.equalsIgnoreCase("Delivery")) {
        		output = "Great, please input your addreess";
        		state = DELIVERY;
        	}
        	else {
        		output = "Type 'Takeaway' or 'Delivery'";
        	}
        }
        else if(state == DELIVERY) {
            //asks for the adress and confirm it. if it's correct, send them to the END state
        	String address;
        	address = input;
        	if(input.equalsIgnoreCase("no")) {
        		output = "Please input your address";
        	}
        	else if(input.equalsIgnoreCase("yes")) {
        		output = "Great, we'll send your order to your location as soon as possible.\nPlease wait, and Have a great day\nDo you need anything else?\n'Yes' will take you back to the start\\n'Exit' will end the session";
        		state = END;
        	}
        	else {
        		address = address.concat("\nIs this Correct? ('yes'/'no')");
        		output = address;
        	}
        }
        else if(state == END) {
            //both states that ends up here sends a message asking if they need anything else or not. typing yes will take them back to the WAITING state
            //the other option is to exit
        	if(input.equalsIgnoreCase("yes")) {
        		state = WAITING;
        	}
        	else {
        		output = "Type 'yes' to restart the session or 'exit' to end session";
        	}
        }
        //chatLiveServer toggle on
        if(state == CHAT){
            chatLiveServer = true;
        }
        return output;
    }
}
