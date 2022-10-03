package ds;

import java.net.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
//Project2Task4 Dan Molenhouse dmolenho
public class EchoClientTCP {

    public static int serverPort;
    public static Socket clientSocket = null;

    public static void main(String args[]) {

        // arguments supply hostname
        try {
            // Convert local host to IP Address, initialize server port to 6789, initialize socket
            // Create buffered reader and a placeholder string to read user inputs
            System.out.println("The client is running."); //Confirm client is active

            //Request port from user
            System.out.println("Insert server side port number: ");
            Scanner s= new Scanner(System.in);
            serverPort = s.nextInt(); //Prompt user for port to scan

            clientSocket = new Socket("localhost", serverPort);

            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String m;

            while (!clientSocket.isClosed()) {
                //Assemble user interface
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
                        //value input
                        System.out.println("Enter value to add:");
                        int addNum = parseInt(typed.readLine());

                        //user id
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //operate on given inputs
                        operate(addNum, ID, 1);
                    }; break;
                    case 2:{
                        //value input
                        System.out.println("Enter value to subtract:");
                        int addNum = parseInt(typed.readLine());

                        //user id
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //operate on given inputs
                        operate(addNum, ID, 2);
                    }; break;
                    case 3:{
                        //placeholder int
                        int addNum = 0;

                        //user id
                        System.out.println("Enter ID:");
                        int ID = parseInt(typed.readLine());

                        //operate on given inputs
                        operate(addNum, ID, 3);
                    }; break;
                    case 4: System.out.println("The client side is closed. Server remains active.\n"); clientSocket.close(); break;
                    default: System.out.println("Incorrect submission. Client closing."); break;
                }

            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
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

    //Operate simply sends and receives message to server where operation itself is handled
    public static void operate(int j, int ID, int operation) throws IOException {

        //initializations for TCP stream
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

        //String to be sent, : delimited
        String sendRequest = j + ":" + ID + ":" + operation;

        //Send rew
        out.println(sendRequest);
        out.flush();

        String data = in.readLine(); // read a line of data from the stream
        System.out.println("The server returned " + data + "\n");

    }

}