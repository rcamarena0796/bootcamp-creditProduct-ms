package com.everis.bootcamp.creditproductms.dao;


import com.everis.bootcamp.creditproductms.model.CreditProductTransactionLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditProductTransactionLogRepository extends
    ReactiveMongoRepository<CreditProductTransactionLog, String> {

  public Flux<CreditProductTransactionLog> findAllByClientNumDoc(String clientNumDoc);
}