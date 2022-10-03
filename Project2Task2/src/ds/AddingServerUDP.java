package ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;


public class AddingServerUDP{
    public static int total = 0;

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
                int requestInt = parseInt(requestString);
                System.out.println("Adding: " + requestInt + " to " + total);

                //Calls add method for simple addition
                serverAdd(requestInt);
                System.out.println("Returning sum of " + total + " to client");

                //creates new Datagram packet with new total as data
                DatagramPacket reply2 = new DatagramPacket(valueOf(total).getBytes(),
                        valueOf(total).getBytes().length, reply.getAddress(), reply.getPort());


                aSocket.send(reply2);
                Arrays.fill(buffer, (byte)0);

            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}

    }

    //Simple addition method adds user input to static total
    public static void serverAdd(int j){
        total = total + j;
    }

}
