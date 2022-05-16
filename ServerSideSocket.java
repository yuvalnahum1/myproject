package networking.copy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSideSocket {
    private ServerSocket serverSocket;
    private Socket clientSocket1;
    private Socket clientSocket2;
    private PrintWriter out1;
    private BufferedReader in1;
    private PrintWriter out2;
    private BufferedReader in2;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket1 = serverSocket.accept();
        clientSocket2 = serverSocket.accept();
        out1 = new PrintWriter(clientSocket1.getOutputStream(), true);
        in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
        out2 = new PrintWriter(clientSocket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
    }

    public void sendData(String d1, String d2) throws IOException {
        out1.println(d2);
        out2.println(d1);

    }




    public void communicate() throws IOException {
        String locations1 = in1.readLine();
        String locations2 = in2.readLine();
        out1.println(locations1);
        out2.println(locations1);
        while(true){
            String status1 = in1.readLine();
            String status2 = in2.readLine();
            sendData(status1, status2);
            if(status1.equals("outOfGame") || status2.equals("outOfGame")){
                return;
            }
            String location1 = in1.readLine();
            String location2 = in2.readLine();
            sendData(location1, location2);
        }

    }

    public void stop() throws IOException {
        in1.close();
        out1.close();
        clientSocket1.close();
        in2.close();
        out2.close();
        clientSocket2.close();
        serverSocket.close();
    }
    public static void main(String[] args) throws IOException {
        ServerSideSocket server=new ServerSideSocket();
        server.start(8888);
        server.communicate();
        server.stop();
    }
}