import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        start(5000);
    }
    public static void start(int port){
        try(Socket clientSocket = new Socket("localhost",port)){
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(in.readLine());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
