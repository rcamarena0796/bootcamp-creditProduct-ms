package everis.bootcamp.creditproductms.dao;


import everis.bootcamp.creditproductms.model.CreditProduct;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditProductRepository extends ReactiveMongoRepository<CreditProduct, String> {

    public Flux<CreditProduct> findAllByClientNumDoc(String clientNumDoc);

    public Mono<CreditProduct> findByNumAccount(String numAccount);
}
