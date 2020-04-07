package com.everis.bootcamp.creditproductms.dao;


import com.everis.bootcamp.creditproductms.model.CreditProduct;
import java.util.Date;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditProductRepository extends ReactiveMongoRepository<CreditProduct, String> {

  public Flux<CreditProduct> findAllByClientNumDoc(String clientNumDoc);

  public Flux<CreditProduct> findAllByClientNumDocAndDebtExpired(String clientNumDoc,
      Boolean debtExpired);

  public Mono<CreditProduct> findByNumAccount(String numAccount);

  public Flux<CreditProduct> findByClientNumDocAndBankId(String numDoc, String bankId);

  public Flux<CreditProduct> findAllByModifyDateBetween(Date startDate, Date endDate);
}
