import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    public static void main(String[] args) {
        start(5010);
    }
    public static void start(int port){
        try(Socket clientSocket = new Socket("localhost",port)){
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
            new Thread(()->{
                try{
                    String line;
                    while((line = in.readLine())!=null){
                        messageQueue.put(line);
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }).start();
            Scanner scanner = new Scanner(System.in);
            String line = null;
            while(!"exit".equalsIgnoreCase(line)){
                while(!messageQueue.isEmpty()){
                    System.out.println(messageQueue.poll());
                }
                line = scanner.nextLine();
                out.println(line);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
