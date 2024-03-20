import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<BankClient> clients = new ArrayList<>();
    public static void main(String[] args) {
        start(5010);
    }
    public static void start(int port){
        try{
            try (ServerSocket socket = new ServerSocket(port)) {
                socket.setReuseAddress(true);
                for (;;) {
                    Socket client = socket.accept();
                    System.out.println("Client connected " + client.getInetAddress().getHostAddress());
                    ClientHandler clientSocket = new ClientHandler(client);
                    new Thread(clientSocket).start();
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private static class ClientHandler implements Runnable{
        private final Socket clientSocket;
        private final PrintWriter out;
        private final BufferedReader in;
        private BankClient currentCLient;
        public ClientHandler(Socket clientSocket) throws IOException{
            this.clientSocket = clientSocket;
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        @Override
        public void run() {
            try{
                out.println("Welcome to the bank, please enter 1 to login or 2 to register: ");
                String choice = in.readLine();
                switch (choice){
                    case "1" -> handleLogin();
                    case "2" -> handleRegistration();
                }
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        private void handleLogin() throws IOException{
            out.println("Enter your UID:");
            String uid = in.readLine();
            out.println("Enter your PIN");
            String pin = in.readLine();
            currentCLient = authenticateClient(uid,pin);
            if(currentCLient != null){
                out.println("Login successful");
                handleLoggedInClient();
            } else {
                out.println("Invalid credentials");
            }
        }
        private BankClient authenticateClient(String UID, String PIN){
            for (BankClient client : clients){
                if(client.getUID().equals(UID) && client.getPIN().equals(PIN)){
                    return client;
                }
            }
            return null;
        }
        private void handleRegistration() throws IOException {
            out.println("Enter your desired UID:");
            String uid = in.readLine();

            if (findClientByUID(uid) != null) {
                out.println("UID already exists. Please choose another.");
                return;
            }
            out.println("Enter a PIN for your account:");
            String pin = in.readLine();
            BankClient newClient = new BankClient(uid,pin);
            clients.add(newClient);
            currentCLient = newClient;
            out.println("Registration successful!");
            handleLoggedInClient();
        }

        private BankClient findClientByUID(String uid) {
            for(var client : clients){
                if(client.getUID().equals(uid)) return client;
            }
            return null;
        }

        private void handleLoggedInClient() throws IOException {
            while (true) {
                out.println("\nAvailable options:");
                out.println("1. Create Account");
                out.println("2. View Accounts");
                out.println("3. Change PIN");
                out.println("4. Deposit");
                out.println("5. Withdraw");
                out.println("6. Logout");
                String choice = in.readLine();
                switch (choice) {
                    case "1" -> handleCreateAccount();
                    case "2" -> handleViewAccounts();
                    case "3" -> handleChangePIN();
                    case "4" -> handleDeposit();
                    case "5" -> handleWithdraw();
                    case "6" -> out.print("Goodbye!");
                }
            }
        }
        private void handleCreateAccount() {
            try{
                out.println("Please insert credentials for your new account");
                out.println("Insert currency you want (PLN USD CAD): ");
                String currency = in.readLine();
                AccountCredentials accountCredentials = new AccountCredentials(currency,0);
                currentCLient.addAccount(accountCredentials);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        private void handleViewAccounts() {
            List<AccountCredentials> accs = currentCLient.getClientAccounts();
            if(accs == null || accs.isEmpty() ){
                out.println("No account were found");
            } else{
                for (var val : accs){
                    out.println("Currency: "+val.getCurrency()+"\n" +
                            "Balance: "+val.getBalance()+"\n"+
                            val.getAuthorizedUsers());
                }
            }
        }
        private void handleChangePIN() {
            try{
                out.println("Insert your old PIN: ");
                String pin = in.readLine();
                if(pin.equals(currentCLient.getPIN())){
                    out.println("Insert your new pin: ");
                    String temp = in.readLine();
                    currentCLient.setPIN(temp);
                    out.println("Your PIN has been changed");
                } else{
                    out.println("Invalid pin");
                }
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        private void handleDeposit() {
            try{
                out.print("What is the currency you want to deposit");
                String currency = in.readLine();
                AccountCredentials temp = currentCLient.getAccountByCurrency(currency);
                if(temp!=null){
                    out.println("Insert how much money you want to deposit: ");
                    double value = Double.parseDouble(in.readLine());
                    if(value > 0){
                        temp.setBalance(temp.getBalance()+value);
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        private void handleWithdraw() {
            try{
                out.println("What is the currency you want to withdraw");
                String currency = in.readLine();
                AccountCredentials temp = currentCLient.getAccountByCurrency(currency);
                if(temp!=null){
                    out.println("Insert how much money you want to withdraw: ");
                    double value = Double.parseDouble(in.readLine());
                    if(value > 0 && value <= temp.getBalance()){
                        temp.setBalance(temp.getBalance()-value);
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
