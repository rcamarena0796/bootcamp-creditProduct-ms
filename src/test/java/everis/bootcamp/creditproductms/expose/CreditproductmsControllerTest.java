package everis.bootcamp.creditproductms.expose;

import everis.bootcamp.creditproductms.model.CreditProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class CreditproductmsControllerTest {

    @Autowired
    private WebTestClient webClient;

    private static CreditProduct cpTest;

    @BeforeAll
    public static void setup() {
        cpTest = new CreditProduct();
        cpTest.setBankName("BCP");
    }

    @Test
    public void test_controller_hola_mundo() {
        webClient.get()
                .uri("/api/creditProduct/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreditProduct.class)
                .isEqualTo(cpTest);
    }
}
