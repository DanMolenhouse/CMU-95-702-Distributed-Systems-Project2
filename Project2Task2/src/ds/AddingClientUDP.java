package ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

//Project2 Task2 - Dan Molenhouse dmolenho

public class AddingClientUDP{
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
            while ((nextLine = typed.readLine()) != null) {

                //If statement tests for Halt!
                if(nextLine.contains("Halt")){
                    System.out.println("Client side quitting");
                    aSocket.close();
                    break;
                }else{
                    //Calls add method which handles all connections
                    int answ = add(parseInt(nextLine));
                    System.out.println("The serve returned " + answ);
                }

            }

        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}

    }

    //add method handles all client - server communications and requests int to be added to total stored on server
    public static int add(int j) throws IOException {
        byte [] m = valueOf(j).getBytes();    //Puts user input into byte array
        InetAddress aHost = InetAddress.getByName("localhost"); //Get IP address
        DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort); //creates datagram packet
        aSocket.send(request); //Sends request to socket
        byte[] buffer = new byte[1000]; //Buffer for inputs
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

        return parseInt(replyString);
    }

}