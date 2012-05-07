import java.io.*;
import java.net.*;
import java.util.Timer;


public class UDPClient {
   
    private static String message = "WADDUP, THIS IS CLIENT";
    private static final int PORT_NUM = 9999;
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static byte[] bytesSend;
    private static byte[] bytesReceived;
    private static final int MAX = 1000;
    private static final int DATA_SIZE = 1;
    private static final int ONE_KB = 1024;
    private static final int FOUR_KB = 4096;
    private static final int EIGHT_KB = 8192;
    private static final int SIXTEEN_KB = 16384;
    private static final int THIRTY_TWO_KB = 32768;
    private static final int SIXTY_FOUR_KB = 65536;
    
    private static InetAddress address;
    private String receivedMessage;
    
    UDPClient(int sizeOfData) throws SocketException, UnknownHostException {
        
        socket = new DatagramSocket();
        bytesSend = new byte[sizeOfData];
        bytesReceived = new byte[sizeOfData];
        //address = InetAddress.getByName("localhost");
        //packet = new DatagramPacket(bytesSend, bytesSend.length, address, PORT_NUM);
    }
    
    
    public void send(DatagramPacket aPacket) throws IOException
    {
        socket.send(aPacket);
    }
    
    public void receive(DatagramPacket aPacket) throws IOException
    {
        socket.receive(aPacket);
    }
    
    public void setAddress(String name) throws UnknownHostException
    {
        address = InetAddress.getByName(name);
    }
    
    public void createPacket(byte[] bytes, int len, InetAddress ipAddress)
    {
        packet = new DatagramPacket(bytes, len, ipAddress, PORT_NUM);
    }
    
    public void setMessage(String message)
    {
        bytesSend = message.getBytes();
    }
    
    public String receiveMessage(byte[] aByte)
    {
        receivedMessage = new String(aByte);
        return receivedMessage;
    }
    
    public void close()
    {
        socket.close();
    }
    
    public static void main(String[] args) throws IOException {
       
        // Checks if there is an argument, else, show usage
        if(args.length<=0)
        {
            System.out.println("Usage: UDPClient destination");
            System.exit(0);
        }
       
        //t = new Timer();
        //Need to test:  1Kbyte, 4KB, 8 KB, 16KB, 32 KB, and 64KB
       // long start = System.nanoTime();
//        for(int i = 0; i < MAX; i++) {  <--- this whole loop is for the for b(ii) only
//            UDPClient client = new UDPClient(DATA_SIZE);
//            client.setMessage("hello, this is the client");
//            client.setAddress("localhost");
//            client.createPacket(bytesSend, bytesSend.length, address);
//            client.send(packet);
//            client.receiveMessage(packet.getData());  
//        }
        
        UDPClient client;
        
        /*
         * This is what I initially came up with for part b(iii)
         * I sort of feel I am calculating delay improperly but I have no reference!
         * Basically what I attempting with this loop is to start out with 1KB 
         * and increase the size of the message by doubling it, then doing the process 
         * 100 times like the directions ask. I included 2KB even thoguh it wasn't asked of us.
         */
        int packetSize = ONE_KB;
        while (packetSize <= SIXTY_FOUR_KB) { //Jump out once we hit 64KB!
            long start = System.nanoTime();
            for (int i = 0; i < 100; i++) {
                
                client = new UDPClient(packetSize);
                client.setAddress(args[0]); // Connects to the specified address
                socket.connect(address, PORT_NUM);
                client.setMessage(message); // Sends the specified message
                client.createPacket(bytesSend, bytesSend.length, address);
                client.send(packet);
                //System.out.println("Socket is  " + socket.getPort());
                //System.out.println("# " + i + " Client is waiting on response from : " + address);
                
                client.createPacket(bytesReceived, bytesReceived.length, address);
                client.receive(packet);
                String aSentence = new String(packet.getData(), 0, packet.getLength());
                //System.out.println(aSentence);
            }
            
            socket.close();
            
            long difference = System.nanoTime() - start;
            double timeInSeconds = (double) difference / 1000000000.0;
            double throughput = packet.getData().length / timeInSeconds;
            //System.out.println("The elapse time is " + timeInSeconds);
            System.out.println("For packetSize: " + packetSize + " the avg RTT is  " + timeInSeconds / 100);
            //System.out.println("The throughput is  " + throughput);
            
            packetSize *= 2;
        }
    }
    
}
