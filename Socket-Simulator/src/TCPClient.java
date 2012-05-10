import java.io.*;
import java.net.*;
 
public class TCPClient {
    
    public static void main(String[] args)
    {
        try {
            Socket socket = new Socket("localhost", 9999); //socket
            
            System.out.println("Connection established");
            DataInputStream in =
                    new DataInputStream(socket.getInputStream());
            System.out.println("from server: " + in.readLine());
            DataOutputStream out = 
                    new DataOutputStream(socket.getOutputStream());//out
            
            //BufferedReader in = 
                    //new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            in.close();
            //socket.close();
        }
        catch(Exception e)
        {
            System.out.println("We gots an error "+ e.toString());
        }
    }
}
