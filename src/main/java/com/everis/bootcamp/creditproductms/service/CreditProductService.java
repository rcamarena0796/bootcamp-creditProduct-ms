package com.everis.bootcamp.creditproductms.service;

import com.everis.bootcamp.creditproductms.dto.DatesDto;
import com.everis.bootcamp.creditproductms.model.CreditProduct;
import com.everis.bootcamp.creditproductms.model.CreditProductTransactionLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditProductService {

  public Mono<CreditProduct> findByNumAccount(String name);

  public Mono<CreditProduct> findById(String id);

  public Flux<CreditProduct> findByClientNumDoc(String numDoc);

  public Flux<CreditProduct> findAll();

  public Mono<CreditProduct> update(CreditProduct bp, String id);

  public Mono<String> delete(String bp);

  public Mono<CreditProduct> save(CreditProduct bp);

  public Mono<CreditProduct> moneyTransaction(String id, double money);

  public Flux<CreditProductTransactionLog> findLogByClientNumDoc(String numDoc);

  public Mono<Double> getDebt(String id);

  public Mono<String> payDebtFromBankAcc(String numAccount);

  public Flux<CreditProduct> findByNumAccountAndBankId(String numAccount, String bankId);

  public Mono<Boolean> validateClientDebts(String clientNumDoc);

  public Flux<CreditProduct> productReport(DatesDto dates);

}
