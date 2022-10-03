package ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

//Project2 Task3 - Dan Molenhouse dmolenho

public class RemoteVariableClientUDP{
    public static DatagramSocket aSocket = null;
    public static int serverPort;

    public static void main(String args[]){
        try {

            // Convert local host to IP Address, initialize server port to 6789, initialize socket
            // Create buffered reader and a placeholder string to read user inputs
            System.out.println("The client is running."); //Confirm client is active

            //Request port from user
            System.out.println("Insert server side port number: ");
            Scanner s= new Scanner(System.in);
            serverPort = s.nextInt(); //Prompt user for port to scan

            //Initializations
            aSocket = new DatagramSocket();
            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            //While loop keeps connection open until Halt! is entered, then client side closes
            while (!aSocket.isClosed()) {
                //Assembles menu for user to choose an input
                System.out.println("1. Add a value to your sum.");
                System.out.println("2. Subtract a value from your sum.");
                System.out.println("3. Get your sum.");
                System.out.println("4. Exit Client.");

                //Switch case for each integer
                // 1 -> Add
                // 2 -> Subtract
                // 3 -> Get Request
                // 4 -> End connection
                // Anything else -> Error

                switch(s.nextInt()){
                    case 1:{
                        //Prompt user for value to add
                        System.out.println("Enter value to add:");
                        int addNum = parseInt(typed.readLine());

                        //Prompt user for ID
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //Calls operate method using 1 as operator (addition)
                        //operate handles connection to server
                        int result = operate(addNum, ID, 1);

                        //Return result from server
                        System.out.println("The server returned " + result + "\n");
                            }; break;
                    case 2:{
                        //Prompt user for value to subtract
                        System.out.println("Enter value to subtract:");
                        int addNum = parseInt(typed.readLine());

                        //Prompt user for ID
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //Calls operate method using 2 as operator (subtract)
                        //operate handles connection to server
                        int result = operate(addNum, ID, 2);
                        System.out.println("The server returned " + result + "\n");
                            }; break;
                    case 3:{
                        //addnum = 0 is just a simple placholder value, server wont actually do anything with it for a get request
                        int addNum = 0;

                        //prompt user for ID
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //Calls operate method using 3 as operator (get)
                        //operate handles connection to server
                        int result = operate(addNum, ID, 3);
                        System.out.println("The server returned " + result + "\n");
                            }; break;
                    case 4: System.out.println("The client side is closed. Server remains active.\n"); close(); break;
                    default: System.out.println("Incorrect submission. Client closing."); break;
                }
            }

        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}

    }

    //Simple method to close aSocket connection
    public static void close(){
        System.out.println("Client side quitting");
        aSocket.close();
    }

    //Operate method created to handle connection to server, operation is passed along to server where it is
    // computed on the server side and then returned. Client does not handle any of these operations
    public static int operate(int j, int ID, int operation) throws IOException {
        //buffer for request
        byte[] buffer = new byte[1000];

        // Colon delimited string containing integer, ID and operation
        String sendRequest = j + ":" + ID + ":" + operation;
        byte [] m = sendRequest.getBytes();    //Puts user input into byte array
        InetAddress aHost = InetAddress.getByName("localhost"); //Get IP address

        DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort); //creates datagram packet
        aSocket.send(request); //Sends request to socket

        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        aSocket.receive(reply); //Receive reply from server

        //The following is code to parse out the standalone message from buffered reply text
        byte[] initArray = reply.getData();
        int i = 0;

        //Count number of characters
        while(initArray[i]!=0){
            i++;
        }
        byte[] newArray = new byte[i];
        i = 0;

        //Create new byte array with just the message being echoed
        while(initArray[i]!=0){
            newArray[i] = initArray[i];
            i++;
        }

        //Set data in reply to new array
        reply.setData(newArray);
        String replyString = new String(reply.getData());

        //return integer from server
        return parseInt(replyString);
    }

}