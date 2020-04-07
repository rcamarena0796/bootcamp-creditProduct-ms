package everis.bootcamp.creditproductms.service.impl;

import everis.bootcamp.creditproductms.dao.CreditProductTypeRepository;
import everis.bootcamp.creditproductms.model.CreditProductType;
import everis.bootcamp.creditproductms.service.CreditProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class CreditProductTypeServiceImpl implements CreditProductTypeService {

  private static final Logger log = LoggerFactory.getLogger(CreditProductTypeServiceImpl.class);

  @Autowired
  private CreditProductTypeRepository repo;


  @Override
  public Mono<CreditProductType> findByNumId(int numId) {
    return repo.findByNumId(numId);
  }
}