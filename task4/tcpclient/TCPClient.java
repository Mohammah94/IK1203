package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    private boolean shutdown;
    private Integer timeout;
    private Integer limit;

    

    public TCPClient(boolean shutdown, Integer timeout, Integer limit){

        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;  
        }
        
    
    public TCPClient(Integer limit2, int portNumber, boolean shutdown2) {
    }


    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        
        Socket mySocketTCP = new Socket(hostname, port);

        ByteArrayOutputStream dataFromServer = new ByteArrayOutputStream();
        try {
        
            if(this.timeout != null){

            mySocketTCP.setSoTimeout(this.timeout);
        }
        
        mySocketTCP.getOutputStream().write(toServerBytes, 0 , toServerBytes.length);

        if (this.shutdown == true) {
                
            mySocketTCP.shutdownOutput();
         }

            byte[] userserver = new byte[1024];
            int data = 0 ;

    
        while((data = mySocketTCP.getInputStream().read(userserver))!= -1) {

            if(limit == null){
                
                dataFromServer.write(userserver ,0, data);
            
            }

            else if ((limit !=null)&&((limit-dataFromServer.size())>=data )) {

            dataFromServer.write(userserver,0,data);

            }

            else {

                dataFromServer.write(userserver,0,(limit-dataFromServer.size()));
                
                break;
            }  
        }
    }
        
        catch (SocketTimeoutException s) {
            assert dataFromServer != null;
            return dataFromServer.toByteArray();
}

        finally {

                mySocketTCP.close();     
        }

        return dataFromServer.toByteArray();
    }
}