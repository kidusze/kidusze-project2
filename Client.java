import java.io.*;
import java.net.*;


public class Client {
   private Socket socket;
   private PrintWriter out;
   private BufferedReader in;


   public Client(String host, int port) throws IOException {
       socket = new Socket(host, port);
       out = new PrintWriter(socket.getOutputStream(), true);
       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   }


   public Socket getSocket() {
       return socket;
   }


   public void handshake()  {
       out.println("12345");
       out.flush();
   }


   public String request(String number) throws IOException {
       out.println(number);
       return in.readLine();
   }


   public void disconnect() {
       try {
           in.close();
           out.close();
           socket.close();
       } catch (IOException e) {
           System.err.println("Error during disconnection: " + e.getMessage());
           System.exit(1);  // Ideally, you might not want to call System.exit() here
       }
   }
  
}
