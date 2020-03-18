package everis.bootcamp.creditproductms.service;

import everis.bootcamp.creditproductms.model.CreditProductType;
import reactor.core.publisher.Mono;

public interface CreditProductTypeService {
    public Mono<CreditProductType> findByNumId(int numId);
}
