package ds;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
//Project2Task5 Dan Molenhouse dmolenho

public class SigningClientTCP {

    public static Socket clientSocket = null;
    public static int serverPort;
    public static BigInteger publicKey; //e
    private static BigInteger privateKey; //d
    public static BigInteger modulus;   //n
    public static String ID;
    public static String publicKeyString;

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

            buildKeys();
            generateID();
            System.out.println("User ID: " + ID);

            clientSocket = new Socket("localhost", serverPort);

            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String m;

            while (!clientSocket.isClosed()) {

                //user interface
                System.out.println("1. Add a value to your sum.");
                System.out.println("2. Subtract a value from your sum.");
                System.out.println("3. Get your sum.");
                System.out.println("4. Exit Client.");

                //switch case for options
                switch(s.nextInt()){
                    case 1:{
                        //Enter value to add
                        System.out.println("Enter value to add:");
                        int addNum = parseInt(typed.readLine());

                        //operate
                        operate(addNum, 1);
                    }; break;
                    case 2:{

                        //user input for int
                        System.out.println("Enter value to subtract:");
                        int addNum = parseInt(typed.readLine());

                        //operate subtract
                        operate(addNum, 2);
                    }; break;
                    case 3:{

                        //get request, no user input
                        int addNum = 0;
                        operate(addNum, 3);
                    }; break;
                    case 4: System.out.println("The client side is closed. Server remains active.\n"); clientSocket.close(); break;
                    default: System.out.println("Incorrect submission. Client closing."); break;
                }

            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
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

    // Same as previous but no longer requires ID input
    public static void operate(int j, int operation) throws IOException, NoSuchAlgorithmException {

        //TCP connections
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

        //create message
        String message = ID + ":" + publicKeyString + ":" + operation + ":" + j;

        //sign message
        String signature = sign(message);

        //create signed message
        String sendRequest = message + ":" + signature;

        //send signed message
        out.println(sendRequest);
        out.flush();

        String data = in.readLine(); // read a line of data from the stream
        System.out.println("The server returned " + data + "\n");

    }

    // bytesToHex function code sourced from http://www.java2s.com/example/java-utility-method/byte-array-to-hex/bytestohex-byte-bytes-6f3c0.html
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : bytes) {
            int intVal = 0xff & hashByte;
            if (intVal < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(intVal));
        }
        return sb.toString();
    }

    //Generate ID for current client
    public static void generateID() throws NoSuchAlgorithmException {
        //concacte publickey and modulus
        String e = valueOf(publicKey);
        String n = valueOf(modulus);
        publicKeyString = e + n;

        //hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(publicKeyString.getBytes());
        String IDstring2 = bytesToHex(md.digest());

        //last 20 is ID
        ID = IDstring2.substring(IDstring2.length()-20,IDstring2.length());
    }

    //BuildKeys method uses code borrowed from RSA Example provided to us
    //in the problem statement
    public static void buildKeys(){

        // Each public and private key consists of an exponent and a modulus
        BigInteger n; // n is the modulus for both the private and public keys
        BigInteger e; // e is the exponent of the public key
        BigInteger d; // d is the exponent of the private key

        Random rnd = new Random();

        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);

        System.out.println("Public Key = " + e + n);  // Step 6: (e,n) is the RSA public key
        publicKey = e;
        System.out.println("Private Key  = " + d + n);  // Step 7: (d,n) is the RSA private key
        privateKey = d;
        //System.out.println("Modulus for both = " + n);  // Modulus for both keys
        modulus = n;
    }

    //Sign method loosely based on ShortMessageSign provided to us in course materials
    public static String sign(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);
        //set first to 0
        bigDigest[0] = 0;


        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(bigDigest);

        // encrypt the digest with the private key
        BigInteger c = m.modPow(privateKey, modulus);

        // return this as a big integer string
        return c.toString();

    }


}