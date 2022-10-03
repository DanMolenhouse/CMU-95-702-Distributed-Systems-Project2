package ds;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

//Project2Task0 - Dan Molenhouse dmolenho

public class EchoServerUDP{
    public static void main(String args[]){
        //Confirm server is operational
        System.out.println("The server is running.");

        //Initiailizations
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];

        try{
            //User input for socket to listen to
            System.out.println("Insert listening port number: ");
            Scanner s= new Scanner(System.in);
            aSocket = new DatagramSocket(s.nextInt());
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while(true){
                aSocket.receive(request); //Recieve request from client

                //Create reply packet
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());

                //Following block parses out message from the buffered request
                byte[] initArray = reply.getData();
                int i = 0;

                //Counts number of characters present
                while(initArray[i]!=0){
                    i++;
                }
                byte[] newArray = new byte[i];
                i = 0;

                //Fills new byte array with parsed message
                while(initArray[i]!=0){
                    newArray[i] = initArray[i];
                    i++;
                }

                //Sets new data to reply
                reply.setData(newArray);
                String requestString = new String(reply.getData());

                //Tests if message is "Halt!" and if so, closes socket/ends connection
                if(requestString.contains("Halt!")){
                    System.out.println("Echoing: " + requestString);
                    System.out.println("Server Side Quitting");
                    aSocket.send(reply);
                    aSocket.close();
                    break;
                }else {
                    //Otherwise, continues as normal
                    System.out.println("Echoing: " + requestString);
                    aSocket.send(reply);
                    Arrays.fill(buffer, (byte)0);
                }
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
}
