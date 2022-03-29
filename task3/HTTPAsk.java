import java.net.*;
import java.io.*;
import tcpclient.TCPClient;


public class HTTPAsk 
{
    public static void main( String[] args) throws IOException
     {
        boolean shutdown = false;
        Integer timeout = null;
        Integer limit = null;
        Integer port = null;
        String string = null;
        byte [] stringBytes = null;
        String host = null;
         
        int portNumber = Integer.parseInt(args[0]);
        String client;

        ServerSocket mysocket = new ServerSocket (portNumber);

        while(true)
        {  
            try(Socket clientSocket = mysocket.accept();)
            {
            
                InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

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
                                port = Integer.parseInt(b[i+1]);
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
                finally
                {
              mysocket.close();
             }
        }
    }
}