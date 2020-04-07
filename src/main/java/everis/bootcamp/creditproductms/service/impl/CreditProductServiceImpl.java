package everis.bootcamp.creditproductms.service.impl;

import everis.bootcamp.creditproductms.dao.CreditProductRepository;
import everis.bootcamp.creditproductms.dao.CreditProductTransactionLogRepository;
import everis.bootcamp.creditproductms.dto.DatesDto;
import everis.bootcamp.creditproductms.model.CreditProduct;
import everis.bootcamp.creditproductms.model.CreditProductTransactionLog;
import everis.bootcamp.creditproductms.service.CreditProductService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditProductServiceImpl implements CreditProductService {

  private static final Logger logger = LoggerFactory.getLogger(CreditProductServiceImpl.class);

  @Autowired
  private CreditProductRepository creditRepo;
  @Autowired
  private CreditProductTransactionLogRepository logRepo;


  @Override
  public Mono<CreditProduct> findByNumAccount(String name) {
    return creditRepo.findByNumAccount(name);
  }

  @Override
  public Mono<CreditProduct> findById(String id) {
    return creditRepo.findById(id);
  }

  @Override
  public Flux<CreditProduct> findByClientNumDoc(String numDoc) {
    return creditRepo.findAllByClientNumDoc(numDoc);
  }

  @Override
  public Flux<CreditProduct> findAll() {
    return creditRepo.findAll();
  }

  @Override
  public Mono<CreditProduct> update(CreditProduct cp, String id) {
    try {
      return creditRepo.findById(id)
          .flatMap(dbCreditProd -> {

            //CreateDate
            if (cp.getCreateDate() != null) {
              dbCreditProd.setCreateDate(cp.getCreateDate());
            }

            //ModifyDate
            dbCreditProd.setModifyDate(new Date());

            //idProdType
            if (cp.getIdProdType() != null) {
              dbCreditProd.setIdProdType(cp.getIdProdType());
            }

            //CreditLimit
            if (cp.getCreditLimit() != 0) {
              dbCreditProd.setCreditLimit(cp.getCreditLimit());
            }

            //CreditAvailable
            if (cp.getCreditAvailable() != 0) {
              dbCreditProd.setCreditAvailable(cp.getCreditAvailable());
            }

            //clientNumDoc
            if (cp.getClientNumDoc() != null) {
              dbCreditProd.setClientNumDoc(cp.getClientNumDoc());
            }

            //numAccount
            if (cp.getNumAccount() != null) {
              dbCreditProd.setNumAccount(cp.getNumAccount());
            }

            //bankName
            if (cp.getBankId() != null) {
              dbCreditProd.setBankId(cp.getBankId());
            }

            //debtExpired
            if (cp.getDebtExpired() != null) {
              dbCreditProd.setDebtExpired(cp.getDebtExpired());
            }

            return creditRepo.save(dbCreditProd);

          }).switchIfEmpty(Mono.empty());
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<Void> delete(String id) {
    try {
      return creditRepo.findById(id).flatMap(cp -> {
        return creditRepo.delete(cp);
      });
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  private Mono<String> getClientTypeFromApi(String numDoc) {
    String url = "http://localhost:8001/client/getClientType/" + numDoc;
    return WebClient.create()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class);
  }


  private Mono<Boolean> getExistBank(String numId) {
    String url = "http://localhost:8002/bank/exist/" + numId;
    return WebClient.create()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(Boolean.class);
  }

  @Override
  public Mono<CreditProduct> save(CreditProduct cp) {
    try {

      Mono<Boolean> existeBanco = getExistBank(cp.getBankId());

      Mono<Boolean> debts = validateClientDebts(cp.getClientNumDoc());

      return existeBanco.flatMap(existe -> {
        return debts.flatMap(validDebts -> {
          if (existe && validDebts) {
            if (cp.getCreateDate() == null) {
              cp.setCreateDate(new Date());
            } else {
              cp.setCreateDate(cp.getCreateDate());
            }
            if (cp.getCreditLimit() < 0) {
              return Mono.error(new Exception("Ingresar un limite de credito valido"));
            }
            cp.setCreditAvailable(cp.getCreditLimit());
            cp.setDebtExpired(false);

            Mono<String> clientType = getClientTypeFromApi(cp.getClientNumDoc());
            return clientType.flatMap(ct -> {
              if (!ct.equals("-1")) {
                return creditRepo.save(cp);

              } else {
                return Mono.error(new Exception("Cliente no registrado"));
              }
            });
          } else {
            return Mono.error(new Exception("El banco del producto no existe"));
          }
        });
      });

    } catch (Exception e) {
      return Mono.error(e);
    }
  }


  @Override
  public Mono<CreditProduct> moneyTransaction(String numAccount, double money) {
    try {
      return creditRepo.findByNumAccount(numAccount)
          .flatMap(dbCreditProd -> {
            Mono<Boolean> existeBanco = getExistBank(dbCreditProd.getBankId());

            return existeBanco.flatMap(existe -> {

              if (existe) {
                double currentMoney = dbCreditProd.getCreditAvailable();
                if (currentMoney + money >= 0 && currentMoney + money <= dbCreditProd
                    .getCreditLimit()) {
                  dbCreditProd.setCreditAvailable(currentMoney + money);
                } else if (currentMoney + money > dbCreditProd.getCreditLimit()) {
                  dbCreditProd.setCreditAvailable(dbCreditProd.getCreditLimit());
                } else {
                  return Mono.error(new Exception("Monto de carga supera el limite de la cuenta"));
                }

                //guardar log
                CreditProductTransactionLog transactionLog = new CreditProductTransactionLog(
                    dbCreditProd.getClientNumDoc(),
                    dbCreditProd.getNumAccount(), dbCreditProd.getCreditAvailable() - money,
                    dbCreditProd.getCreditLimit(),
                    money, new Date());
                logRepo.save(transactionLog).subscribe();

                return creditRepo.save(dbCreditProd);
              } else {
                return Mono.error(new Exception("Banco no existe"));
              }
            });

          }).switchIfEmpty(Mono.error(new Exception("cuenta no encontrada")));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<Double> getDebt(String numAccount) {
    return creditRepo.findByNumAccount(numAccount).flatMap(cp -> {
      Double ret = new Double(cp.getCreditLimit() - cp.getCreditAvailable());
      return Mono.justOrEmpty(ret);
    });
  }

  @Override
  public Flux<CreditProductTransactionLog> findLogByClientNumDoc(String numDoc) {

    return logRepo.findAllByClientNumDoc(numDoc);
  }


  @Override
  public Mono<String> payDebtFromBankAcc(String numAccount) {
    try {
      return creditRepo.findByNumAccount(numAccount).flatMap(cp -> {
        Mono<Boolean> bankExist = getExistBank(cp.getBankId());
        return bankExist.map(exist -> {
          if (exist) {
            cp.setCreditAvailable(cp.getCreditLimit());
            creditRepo.save(cp).subscribe();
            return "1";
          } else {
            return "-1";
          }
        });
      }).switchIfEmpty(Mono.just("-1"));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Flux<CreditProduct> findByNumAccountAndBankId(String numAccount, String bankId) {
    return creditRepo.findByClientNumDocAndBankId(numAccount, bankId);
  }


  @Override
  public Mono<Boolean> validateClientDebts(String clientNumDoc) {
    //busca si tiene alguna deuda vencida y si existe, devuelve false
    return creditRepo.findAllByClientNumDocAndDebtExpired(clientNumDoc, true).collectList()
        .map(lista -> {
          if (lista.isEmpty()) {
            return true;
          }
          return false;
        }).switchIfEmpty(Mono.error(new Exception("no se encontraron productos del cliente")));
  }

  @Override
  public Flux<CreditProduct> productReport(DatesDto dates) {
    return creditRepo.findAllByModifyDateBetween(dates.getStartDate(), dates.getEndDate());
  }
}