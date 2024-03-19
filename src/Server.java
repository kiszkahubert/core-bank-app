import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        start(5000);
    }
    public static void start(int port){
        try{
            ServerSocket socket = new ServerSocket(port);
            socket.setReuseAddress(true);
            for (;;){
                Socket client = socket.accept();
                System.out.println("Client connected "+ client.getInetAddress().getHostAddress());
                ClientHandler clientSocket = new ClientHandler(client);
                new Thread(clientSocket).start();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @AllArgsConstructor
    private static class ClientHandler implements Runnable{
        private final Socket clientSocket;

        @Override
        public void run() {
            try{
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("Welcome in the bank, please insert your UID and password");
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
