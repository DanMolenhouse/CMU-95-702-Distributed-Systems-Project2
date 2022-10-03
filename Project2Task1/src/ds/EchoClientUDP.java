package ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;


public class EchoClientUDP{
    public static void main(String args[]){
        // args give message contents and server hostname
        DatagramSocket aSocket = null;

        try {

            // Convert local host to IP Address, initialize server port to 6789, initialize socket
            // Create buffered reader and a placeholder string to read user inputs
            System.out.println("The client is running."); //Confirm client is active
            InetAddress aHost = InetAddress.getByName("localhost"); //Get IP address

            System.out.println("Insert server side port number: ");
            Scanner s= new Scanner(System.in);
            int serverPort = s.nextInt(); //Prompt user for port to scan

            //Initializations
            aSocket = new DatagramSocket();
            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            while ((nextLine = typed.readLine()) != null) {
                byte [] m = nextLine.getBytes();    //Puts user input into byte array
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

                //Test if reply is "Halt!" and if it is, close socket / end connection
                if(replyString.contains("Halt!")){
                    System.out.println("Reply: " + replyString);
                    System.out.println("Client Side Quitting");
                    aSocket.close();
                    break;
                }else {
                    //If not, continue as normal
                    System.out.println("Reply: " + replyString);
                }
            }

        }catch (SocketException e) {System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
}