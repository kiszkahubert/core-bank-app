import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
public class Transaction {
    String type;
    double amount;
    Date timestamp;
}
