package everis.bootcamp.creditproductms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "CREDIT_PRODUCT_TRANS_LOG")
@EqualsAndHashCode(callSuper = false)
public class CreditProductTransactionLog {

  @Id
  private String id;
  private String clientNumDoc;
  private String numAccount;
  private double creditLimit;
  private double availableCredit;
  private double transaction;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date date;

  /**
   * CreditProductTransactionLog.
   */
  public CreditProductTransactionLog(String clientNumDoc, String numAccount, double availableCredit,
      double creditLimit,
      double transaction, Date date) {
    this.clientNumDoc = clientNumDoc;
    this.numAccount = numAccount;
    this.availableCredit = availableCredit;
    this.creditLimit = creditLimit;
    this.transaction = transaction;
    this.date = date;
  }

}