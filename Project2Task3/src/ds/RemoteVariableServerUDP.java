package ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

//Project2 Task3 - Dan Molenhouse dmolenho

public class RemoteVariableServerUDP{
    static TreeMap<Integer, Integer> users = new TreeMap<Integer, Integer>();

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

                //Split request string based on the : delimitr
                String[] inputs = requestString.split(":");
                int requestInt = parseInt(inputs[0]); //First number is operand
                int ID = parseInt(inputs[1]); //second number is ID
                int operation = parseInt(inputs[2]); //Third number is operation

                //If the ID does not exist in the users TreeMap database, this adds it with a starting
                //total of 0
                if(!users.containsKey(ID)){
                    users.put(ID, 0);
                }

                //Calls serverOperate method which handles the operation based on request
                //and returns a new total based on ID
                int result = serverOperate(requestInt, ID, operation);
                System.out.println("Returning " + result + " to client " + ID);

                //creates new Datagram packet with new total as data
                DatagramPacket reply2 = new DatagramPacket(valueOf(result).getBytes(),
                        valueOf(result).getBytes().length, reply.getAddress(), reply.getPort());

                //Send reply to client
                aSocket.send(reply2);

                //wipe buffer
                Arrays.fill(buffer, (byte)0);

            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}

    }

    //Operation method that simple computes requested operation
    public static int serverOperate(int requestInt, int ID, int operation){
        int newTotal = 0; //placeholder total integer

        //Switch case for operation
        // 1 -> add
        // 2 -> subtract
        // 3 -> get
        switch (operation) {
            //Addition
            case 1 -> {
                int currentTotal = users.get(ID); //get current total
                newTotal = currentTotal + requestInt; //compute addition
                users.replace(ID, newTotal); //update total
            }
            //Subtraction
            case 2 -> {
                int currentTotal = users.get(ID); //grt current total
                newTotal = currentTotal - requestInt; //Compute subtraction
                users.replace(ID, newTotal); //update total
            }
            //Get Request
            case 3 -> {
                newTotal = users.get(ID); //just return current value for given ID
            }
            default -> System.out.print("Input issue regarding operation, check client side input");
        }
        return newTotal;
    }
}
