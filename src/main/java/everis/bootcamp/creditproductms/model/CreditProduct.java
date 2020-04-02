package everis.bootcamp.creditproductms.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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