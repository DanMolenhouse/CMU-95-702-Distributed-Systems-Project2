package ds;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;
import static java.lang.Integer.parseInt;

//Project2Task4 Dan Molenhouse dmolenho

public class EchoServerTCP {
    static TreeMap<Integer, Integer> users = new TreeMap<Integer, Integer>();

    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            //Confirm server is operational
            System.out.println("The server is running.");

            //User input for socket to listen to
            System.out.println("Insert listening port number: ");
            Scanner s= new Scanner(System.in);
            int serverPort = s.nextInt(); // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);

            /*
             * Block waiting for a new connection request from a client.
             * When the request is received, "accept" it, and the rest
             * the tcp protocol handshake will then take place, making
             * the socket ready for reading and writing.
             */
            clientSocket = listenSocket.accept();
            // If we get here, then we are now connected to a client.

            // Set up "in" to read from the client socket
            Scanner in;
            in = new Scanner(clientSocket.getInputStream());

            // Set up "out" to write to the client socket
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            /*
             * Forever,
             *   read a line from the socket
             *   print it to the console
             *   echo it (i.e. write it) back to the client
             */
            while (true) {
                String data = in.nextLine();

                //Split data from stream into invidual parts based on regex :
                String[] inputs = data.split(":");
                int requestInt = parseInt(inputs[0]);
                int ID = parseInt(inputs[1]);
                int operation = parseInt(inputs[2]);

                //Add new user to user treemap
                if(!users.containsKey(ID)){
                    users.put(ID, 0);
                }

                //operate based on input
                int result = serverOperate(requestInt, ID, operation);

                //send result to client
                System.out.println("Returning " + result + " to client " + ID);
                out.println(result);
                out.flush();
            }

            // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    //Operation method to compute operation
    public static int serverOperate(int requestInt, int ID, int operation){
        int newTotal = 0; //placholder total
        switch (operation) {
            case 1 -> {
                //Addition method
                int currentTotal = users.get(ID);
                newTotal = currentTotal + requestInt; //compute new total
                users.replace(ID, newTotal);
            }
            case 2 -> {
                //subraction
                int currentTotal = users.get(ID);
                newTotal = currentTotal - requestInt; //compute new total
                users.replace(ID, newTotal);
            }
            case 3 -> {
                newTotal = users.get(ID); //get request
            }
            default -> System.out.print("Input issue regarding operation, check client side input");
        }
        return newTotal;
    }
}