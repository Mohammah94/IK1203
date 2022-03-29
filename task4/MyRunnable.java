import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import tcpclient.TCPClient;

public class MyRunnable implements Runnable {
    Integer limit = null;
    Integer timeout = null;
    boolean shutdown = false;
    Integer port= null;
    String host = null;
    String string = null; 
    byte [] stringBytes = null;
    String client;

     Socket clientSocket;

    public MyRunnable(Socket clientSocket){
        
        this.clientSocket = clientSocket;

    }
    public void run ( ){
        
        /*Integer limit = null;
        Integer timeout = null;
        boolean shutdown = false;
        Integer port= null;
        String host = null;
        String string = null; 
        byte [] stringBytes = null;
        String client;*/ 

        try{

            InputStreamReader input = new InputStreamReader(this.clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(this.clientSocket.getOutputStream());

            StringBuilder bulider = new StringBuilder();
            int x;
                while ( (x = input.read())!= '\n')
                {
                    bulider.append((char)x);
                }

                client = bulider.toString();

                if (client.contains("GET") && client.contains("/ask"))
                {  

                String [] b =client.split("[?\\&\\=\\ ]");

                for (int j = 0; j < b.length; j++)
                {
                    System.out.println(b[j]);
                }
            
                    for(int i = 0; i< b.length; i++)
                    {
                        switch (b[i])
                        {

                            case "hostname":
                            host = b[i+1];
                                break;

                            case "port":
                            port = Integer.parseInt(b[i +1]);
                                break; 

                            case "string":
                            string = b[i+1];
                                break;
                           
                            case "shutdown":
                            shutdown = true;
                                break;

                            case "timeout":
                            timeout = Integer.parseInt(b[i+1]);
                                break;

                            case "limit":
                            limit = Integer.parseInt(b[i+1]);
                                break;
                             
                        }
                    }
                }

            else

            {   
                output.writeBytes("HTTP/1.1 400 bad Request\r\n\r\n" +"BAD REQUEST" );
            }

            if (port !=0 && host != null){

            try
            {
                if (string != null)
                stringBytes = string.getBytes();
                else
                stringBytes = new byte[0];
                TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                byte [] fromClient = tcpClient.askServer(host, port, stringBytes);
                String svar = new String(fromClient);
                output.writeBytes("HTTP/1.1 200 ok\r\n\r\n" + svar);
            }

            catch(Exception e)
            {
                output.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n"+ "NoT FOUND");
            }

            finally
            {
                clientSocket.close();
            }
        }

            else {
            output.writeBytes("HTTP/1.1 400 bad Request\r\n\r\n" +"BAD REQUEST" );
                 }
        
        }
             catch (Exception e)
             {
          System.out.println(e);
          System.exit(1);
            }
    }
}
