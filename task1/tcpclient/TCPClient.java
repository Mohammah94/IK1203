package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {

       
        ByteArrayOutputStream dataFromServer = new ByteArrayOutputStream();
        
        try {
            Socket mySocketTCP = new Socket(hostname, port);
            
            mySocketTCP.getOutputStream().write(toServerBytes,0 , toServerBytes.length);
            byte[] userserver = new byte [1024];
            int data = 0 ;

            while ((data = mySocketTCP.getInputStream().read(userserver)) != -1){
                dataFromServer.write(userserver, 0, data);

            }
        
            mySocketTCP.close();

        } 
        catch (Exception e) {}

        return dataFromServer.toByteArray();
    }
}
