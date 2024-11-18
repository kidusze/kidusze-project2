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



       
}


