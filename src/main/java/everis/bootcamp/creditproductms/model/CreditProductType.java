package everis.bootcamp.creditproductms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "CREDIT_PRODUCT_TYPE")
@EqualsAndHashCode(callSuper = false)
public class CreditProductType {
    private String numId;
    private String name;
}