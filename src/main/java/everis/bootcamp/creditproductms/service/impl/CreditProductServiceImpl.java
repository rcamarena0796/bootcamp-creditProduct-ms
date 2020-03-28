package everis.bootcamp.creditproductms.service.impl;

import java.util.Date;

import everis.bootcamp.creditproductms.dao.CreditProductRepository;
import everis.bootcamp.creditproductms.dao.CreditProductTransactionLogRepository;
import everis.bootcamp.creditproductms.model.CreditProduct;
import everis.bootcamp.creditproductms.model.CreditProductTransactionLog;
import everis.bootcamp.creditproductms.service.CreditProductService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import reactor.core.publisher.Flux;

@Service
public class CreditProductServiceImpl implements CreditProductService {

    private static final Logger log = LoggerFactory.getLogger(CreditProductServiceImpl.class);

    @Autowired
    private CreditProductRepository bankRepo;
    @Autowired
    private CreditProductTransactionLogRepository logRepo;


    @Override
    public Mono<CreditProduct> findByNumAccount(String name) {
        return bankRepo.findByNumAccount(name);
    }

    @Override
    public Mono<CreditProduct> findById(String id) {
        return bankRepo.findById(id);
    }

    @Override
    public Mono<CreditProduct> findByClientNumDoc(String numDoc) {
        return bankRepo.findByClientNumDoc(numDoc);
    }

    @Override
    public Flux<CreditProduct> findAll() {
        return bankRepo.findAll();
    }

    @Override
    public Mono<CreditProduct> update(CreditProduct cp, String id) {
        try {
            return bankRepo.findById(id)
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
                        if (cp.getBankName() != null) {
                            dbCreditProd.setBankName(cp.getBankName());
                        }


                        return bankRepo.save(dbCreditProd);

                    }).switchIfEmpty(Mono.empty());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<Void> delete(String id) {
        try {
            return bankRepo.findById(id).flatMap(cp -> {
                return bankRepo.delete(cp);
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


    @Override
    public Mono<CreditProduct> save(CreditProduct cp) {
        try {
            if (cp.getCreateDate() == null) {
                cp.setCreateDate(new Date());
            } else {
                cp.setCreateDate(cp.getCreateDate());
            }
            if (cp.getCreditLimit() < 0) {
                return Mono.error(new Exception("Ingresar un limite de credito valido"));
            }
            cp.setCreditAvailable(cp.getCreditLimit());

            Mono<String> clientType = getClientTypeFromApi(cp.getClientNumDoc());
            return clientType.flatMap(ct -> {
                if (!ct.equals("-1")) {
                    return bankRepo.save(cp);

                } else {
                    return Mono.error(new Exception("Cliente no registrado"));
                }
            });

        } catch (Exception e) {
            return Mono.error(e);
        }
    }


    @Override
    public Mono<CreditProduct> moneyTransaction(String id, double money) {
        try {
            return bankRepo.findById(id)
                    .flatMap(dbCreditProd -> {
                        double currentMoney = dbCreditProd.getCreditAvailable();
                        if (currentMoney + money >= 0 && currentMoney + money <= dbCreditProd.getCreditLimit()) {
                            dbCreditProd.setCreditAvailable(currentMoney + money);
                        } else if (currentMoney + money > dbCreditProd.getCreditLimit()) {
                            dbCreditProd.setCreditAvailable(dbCreditProd.getCreditLimit());
                        } else {
                            return Mono.error(new Exception("Monto de carga supera el limite de la cuenta"));
                        }

                        //guardar log
                        CreditProductTransactionLog transactionLog = new CreditProductTransactionLog(dbCreditProd.getClientNumDoc(),
                                dbCreditProd.getNumAccount(), dbCreditProd.getCreditAvailable() - money,dbCreditProd.getCreditLimit()
                                , money, new Date());
                        logRepo.save(transactionLog).subscribe();

                        return bankRepo.save(dbCreditProd);

                    }).switchIfEmpty(Mono.error(new Exception("cuenta no encontrada")));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    @Override
    public Flux<CreditProductTransactionLog> findLogByClientNumDoc(String numDoc) {

        return logRepo.findAllByClientNumDoc(numDoc);
    }
}