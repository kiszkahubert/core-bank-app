import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountCredentials{
    private String currency;
    private double balance;
    private final List<BankClient> authorizedUsers = new ArrayList<>();

}
