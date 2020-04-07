package everis.bootcamp.creditproductms.expose;

import everis.bootcamp.creditproductms.controller.CreditProductController;
import everis.bootcamp.creditproductms.dao.CreditProductRepository;
import everis.bootcamp.creditproductms.model.CreditProduct;
import everis.bootcamp.creditproductms.service.CreditProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CreditProductController.class)
@Import(CreditProductService.class)
public class CreditproductmsControllerTest {

  @MockBean
  protected CreditProductService clientService;

  @MockBean
  CreditProductRepository repository;

  @Autowired
  private WebTestClient webClient;

  private static CreditProduct cpTest;

  @BeforeAll
  public static void setup() {
    cpTest = new CreditProduct();
    cpTest.setBankId("1");
  }

  @Test
  public void test_controller_hola_mundo() {
    webClient.get()
        .uri("/creditprod/test")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CreditProduct.class)
        .isEqualTo(cpTest);
  }
}
