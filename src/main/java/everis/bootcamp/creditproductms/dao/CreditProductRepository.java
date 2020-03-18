package everis.bootcamp.creditproductms.dao;


import everis.bootcamp.creditproductms.model.CreditProduct;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import reactor.core.publisher.Mono;

public interface CreditProductRepository extends ReactiveMongoRepository<CreditProduct, String> {
    public Mono<CreditProduct> findByClientNumDoc(String clientNumDoc);

    public Mono<CreditProduct> findByNumAccount(String numAccount);
}
