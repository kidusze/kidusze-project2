import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;


public class Server {
   private ServerSocket serverSocket;
   private ArrayList<LocalDateTime> connectedTimes;


   public Server(int port) throws IOException {
       serverSocket = new ServerSocket(port);
       connectedTimes = new ArrayList<>();
   }


   public void serve(int numClients) {
       try {
           for (int i = 0; i < numClients; i++) {
               Socket clientSocket = serverSocket.accept();
               ClientHandler clientHandler = new ClientHandler(clientSocket);
               clientHandler.start(); // Start the thread directly
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   public ArrayList<LocalDateTime> getConnectedTimes() {
       return connectedTimes;
   }


   // Change ClientHandler to extend Thread
   private class ClientHandler extends Thread {
       private Socket clientSocket;
       private PrintWriter out;
       private BufferedReader in;


       public ClientHandler(Socket socket) {
           this.clientSocket = socket;
       }


       @Override
       public void run() {
           try {
               in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
               out = new PrintWriter(clientSocket.getOutputStream(), true);


               // Handshake
               String clientPasscode = in.readLine();
               if (clientPasscode.equals("12345")) {
                   connectedTimes.add(LocalDateTime.now());
                   //connectedTimes.add(System.currentTimeMillis());


                   // Factorization request
                   String number = in.readLine();
                   try {
                       int num = Integer.parseInt(number);
                       String result = factorize(num);
                       out.println(result);  // Send the factorization result
                   } catch (NumberFormatException e) {
                       out.println("There was an exception on the server");
                   }
               } else {
                   out.println("couldn't handshake");  // Invalid passcode response
               }


               clientSocket.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }


       private String factorize(int num) {
           if (num > Integer.MAX_VALUE) {  // Use Integer.MAX_VALUE for larger numbers
               return "There was an exception on the server";  // Handling extremely large numbers
           }


           int count = 0;
           StringBuilder factors = new StringBuilder("The number " + num + " has ");


           for (int i = 1; i <= num; i++) {
               if (num % i == 0) {
                   count++;
               }
           }
           return factors.append(count).append(" factors").toString();
       }
   }


   public void disconnect() {
       try {
           serverSocket.close(); // Close the server socket to stop accepting new connections
       } catch (IOException e) {
           System.err.println("Error during disconnection: " + e.getMessage());
           System.exit(1);  // Optional, consider logging instead
       }
   }
}


