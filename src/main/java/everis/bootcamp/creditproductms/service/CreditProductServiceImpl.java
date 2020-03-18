package everis.bootcamp.creditproductms.service;

import java.net.URI;
import java.util.Date;

import everis.bootcamp.creditproductms.dao.CreditProductRepository;
import everis.bootcamp.creditproductms.model.CreditProduct;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import reactor.core.publisher.Flux;

@Service
public class CreditProductServiceImpl implements CreditProductService {

    private static final Logger log = LoggerFactory.getLogger(CreditProductServiceImpl.class);

    @Autowired
    private CreditProductRepository bankRepo;


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
    }

    @Override
    public Mono<Void> delete(String id) {
        return bankRepo.findById(id).flatMap(cp -> {
            return bankRepo.delete(cp);
        });
    }

    @Override
    public Mono<CreditProduct> save(CreditProduct cp) {
        if (cp.getCreateDate() == null) {
            cp.setCreateDate(new Date());
        } else {
            cp.setCreateDate(cp.getCreateDate());
        }

        return bankRepo.save(cp);
    }
}