package everis.bootcamp.creditproductms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "CREDIT_PRODUCT")
@EqualsAndHashCode(callSuper = false)
public class CreditProduct {

  @Id
  private String id;
  @NotBlank(message = "'numAccount' is required")
  private String numAccount;
  @NotBlank(message = "'bankId' is required")
  private String bankId;
  @NotBlank(message = "'idProdType' is required")
  private String idProdType;
  private double creditLimit;
  private double creditAvailable;
  @NotBlank(message = "'clientNumDoc' is required")
  private String clientNumDoc;
  private Boolean debtExpired;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date modifyDate;

}