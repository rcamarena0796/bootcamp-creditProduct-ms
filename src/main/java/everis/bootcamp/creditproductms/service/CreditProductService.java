package everis.bootcamp.creditproductms.service;

import everis.bootcamp.creditproductms.model.CreditProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditProductService {
    public Mono<CreditProduct> findByNumAccount(String name);

    public Mono<CreditProduct> findById(String id);

    public Mono<CreditProduct> findByClientNumDoc(String numDoc);

    public Flux<CreditProduct> findAll();

    public Mono<CreditProduct> update(CreditProduct bp, String id);

    public Mono<Void> delete(String bp);

    public Mono<CreditProduct> save(CreditProduct bp);
}
