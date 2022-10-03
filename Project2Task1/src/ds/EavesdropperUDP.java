package ds;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

//Project2 Task1 - Dan Molenhouse dmolenho

public class EavesdropperUDP {
    public static void main(String args[]) {
        //Confirm server is operational
        System.out.println("The eavesdropper is running.");

        //Initiailizations
        DatagramSocket aSocket = null;
        DatagramSocket bSocket = null;
        byte[] buffer = new byte[1000];

        try{
            //User input for socket to listen to
            System.out.println("Insert listening port number: ");
            Scanner s= new Scanner(System.in); //6789
            int aPort = s.nextInt();

            //User input for socket to pretend to be
            System.out.println("Insert spoof port number: ");
            int bPort = s.nextInt();
            bSocket = new DatagramSocket(bPort); //6798

            //buffer
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            while(true){

                bSocket.receive(request);//Recieve request from client via spoofed port

                //Create reply packet that will be sent to the port the user thought they were typing in (6789)
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), aPort);

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

                reply.setData(newArray); //Setdata to parsed string
                String requestString = new String(reply.getData());
                System.out.println("Intercepted: " + requestString); //Print intercepted string

                bSocket.send(reply); //Send reply to server on port 6789

                byte[] buffer2 = new byte[1000]; //Buffer for inputs
                DatagramPacket request2 = new DatagramPacket(buffer2, buffer2.length);

                //Recieve reply from server
                bSocket.receive(request2);

                //Create reply to be sent back through spoofed port
                DatagramPacket reply2 = new DatagramPacket(request2.getData(),
                        request2.getLength(), request2.getAddress(), request.getPort());

                //Send reply to client
                bSocket.send(reply2);
                Arrays.fill(buffer, (byte)0);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(bSocket != null) bSocket.close();}

    }
}
