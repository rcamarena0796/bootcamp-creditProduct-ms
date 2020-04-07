package com.everis.bootcamp.creditproductms.service;

import com.everis.bootcamp.creditproductms.model.CreditProductType;
import reactor.core.publisher.Mono;

public interface CreditProductTypeService {

  public Mono<CreditProductType> findByNumId(int numId);
}
