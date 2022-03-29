import java.net.*;
import java.io.*;
import tcpclient.TCPClient;




public class ConcHTTPAsk {
    Integer limit = null;
    Integer timeout = null;
    boolean shutdown = false;
    String host = null;
    String string = null; 
    Integer port= null;
    public static void main( String[] args) throws Exception {
        int portnumber = Integer.parseInt(args[0]);
        System.out.println(" Thread"+ Thread.currentThread().getName());

                ServerSocket mySocket = new ServerSocket (portnumber);
        try {
            while(true)
            {
                
                Socket clientSocket = mySocket.accept();
                Runnable runnable = new MyRunnable(clientSocket);
                Thread t = new Thread(runnable);
                t.start();
                //mySocket.close();
            }
            
        } 
        catch (Exception e) {
            System.out.println ("catch is ok");
            //TODO: handle exception
        }

        }
    }