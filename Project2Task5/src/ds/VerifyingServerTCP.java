package ds;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.TreeMap;
import static ds.SigningClientTCP.bytesToHex;
import static java.lang.Integer.parseInt;
//Project2Task5 Dan Molenhouse dmolenho

public class VerifyingServerTCP {
    static TreeMap<String, Integer> users = new TreeMap<String, Integer>(); //users database

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
                String data = in.nextLine(); //get data
                int verify = verify(data); //verify signature and ID

                //if verfiied
                if(verify==1){
                    //Continue as normal
                    System.out.println("Signature Verified");

                    //Split data
                    String[] inputs = data.split(":");
                    String ID = inputs[0];

                    //add new user to database
                    if(!users.containsKey(ID)){
                        users.put(ID, 0);
                    }
                    int operation = parseInt(inputs[2]);
                    int operand = parseInt(inputs[3]);
                    int result = serverOperate(operand,ID,operation);

                    //Return result
                    System.out.println("Returning " + result + " to client " + ID);
                    out.println(result);
                    out.flush();
                }else{ //if not verified
                    out.println("Error in request");
                    out.flush();
                }

            }

            // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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

    //Operation method same as previous 2 tasks
    public static int serverOperate(int requestInt, String ID, int operation){
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

    //Verify method
    public static int verify(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int verified = 1; //0 if fail, 1 if pass

        //Split data
        String[] inputs = data.split(":");
        String ID = inputs[0];
        System.out.println("ID: " + ID);
        String publicKey = inputs[1];
        int operation = parseInt(inputs[2]);
        int operand = parseInt(inputs[3]);
        String signature = inputs[4];

        //hash SHA256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(publicKey.getBytes());
        String IDstring2 = bytesToHex(md.digest());

        //Parse ID from data
        String IDcheck = IDstring2.substring(IDstring2.length()-20,IDstring2.length());

        //Check ID match
        if(IDcheck.compareTo(ID)!=0){
            System.out.println("ID rejected.");
            verified = 0; //no longer passes check
        }

        //recreate message
        String message = ID + ":" + publicKey + ":" + operation + ":" + operand;

        //Parse public key and modolus from data input
        BigInteger publicKeyE = new BigInteger(publicKey.substring(0,5));
        BigInteger publicKeyModulus = new BigInteger(publicKey.substring(5));

        //code below borrowed from ShortMessageVerify
        // Take the encrypted string and make it a big integer
        BigInteger encryptedHash = new BigInteger(signature);

        // Decrypt it
        BigInteger decryptedHash = encryptedHash.modPow(publicKeyE, publicKeyModulus);

        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = message.getBytes("UTF-8");

        // compute the digest of the message with SHA-256
        MessageDigest md2 = MessageDigest.getInstance("SHA-256");
        byte[] messageToCheckDigest = md2.digest(bytesOfMessageToCheck);
        messageToCheckDigest[0] = 0;
        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(messageToCheckDigest);

        //Check if signature match
        if(decryptedHash.compareTo(bigIntegerToCheck)!=0){
            System.out.println("Signature rejected.");
            verified = 0; //no longer passes check
        }
        return verified;
    }
}