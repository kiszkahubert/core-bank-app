import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class BankClient{
    String UID;
    String PIN;
    List<AccountCredentials> clientAccounts;

    public BankClient(String UID, String PIN) {
        this.UID = UID;
        this.PIN = PIN;
    }
    public void addClient(AccountCredentials credentials){
        clientAccounts.add(credentials);
    }
    public List<AccountCredentials> getAllAccounts(){
        return clientAccounts;
    }
    public AccountCredentials getAccountByCurrency(String currency){
        for (var val : clientAccounts){
            if(val.getCurrency().equals(currency)){
                return val;
            }
        }
        return null;
    }
}
