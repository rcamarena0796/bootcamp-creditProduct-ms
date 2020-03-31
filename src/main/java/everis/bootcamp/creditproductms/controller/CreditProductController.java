package everis.bootcamp.creditproductms.controller;

import everis.bootcamp.creditproductms.model.CreditProduct;
import everis.bootcamp.creditproductms.model.CreditProductTransactionLog;
import everis.bootcamp.creditproductms.service.CreditProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;

@Api(tags = "Credit Product API", value = "Operations for credit products")
@RestController
@RequestMapping("/creditprod")
public class CreditProductController {

    @Autowired
    private CreditProductService service;


    @GetMapping("/test")
    public Mono<CreditProduct> saludo() {
        CreditProduct hola = new CreditProduct();
        hola.setBankName("BCP");
        return Mono.justOrEmpty(hola);
    }

    @ApiOperation(value = "Service used to find all credit products")
    @GetMapping("/findAll")
    public Flux<CreditProduct> findAll() {
        return service.findAll();
    }

    @ApiOperation(value = "Service used to find a credit product by id")
    @GetMapping("/findById/{id}")
    public Mono<CreditProduct> findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @ApiOperation(value = "Service used to find a credit product by clientNumDoc")
    @GetMapping("/find/{clientNumDoc}")
    public Flux<CreditProduct> findByClientNumDoc(@PathVariable("clientNumDoc") String clientNumDoc) {
        return service.findByClientNumDoc(clientNumDoc);
    }

    @ApiOperation(value = "Service used to return transaction log of a credit product")
    @GetMapping("/log/{clientNumDoc}")
    public Flux<CreditProductTransactionLog> findLogByClientNumDoc(@PathVariable("clientNumDoc") String clientNumDoc) {
        return service.findLogByClientNumDoc(clientNumDoc);
    }

    //GUARDAR
    @ApiOperation(value = "Service used to save a credit product")
    @PostMapping("/save")
    public Mono<ResponseEntity<CreditProduct>> create(@Valid @RequestBody CreditProduct bp) {
        return service.save(bp).map(b -> ResponseEntity.created(URI.create("/api/bankproduct".concat(b.getId())))
                .contentType(MediaType.APPLICATION_JSON).body(b));
    }


    //ACTUALIZAR
    @ApiOperation(value = "Service used to update a credit product")
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<CreditProduct>> update(@PathVariable("id") String id, @RequestBody CreditProduct bp) {
        return service.update(bp, id)
                .map(b -> ResponseEntity.created(URI.create("/api/bankproduct".concat(b.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(b))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    //ELIMINAR
    @ApiOperation(value = "Service used to delete a credit product")
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    //TRANSACCION
    @ApiOperation(value = "Service used to manage money transactions of a credit product")
    @PostMapping("/transaction/{numAccount}")
    public Mono<ResponseEntity<CreditProduct>> transaction(@PathVariable("numAccount") String numAccount, @RequestBody double money) {
        return service.moneyTransaction(numAccount, money)
                .map(b -> ResponseEntity.created(URI.create("/api/bankproduct".concat(b.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(b))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //PAGAR DEUDA DESDE CUENTA BANCARIA
    @ApiOperation(value = "Service used to pay a credit product form a bank account")
    @PostMapping("/payDebt/{numAccount}")
    public Mono<String> payDebtFromBankAcc(@PathVariable("numAccount") String numAccount) {
        return service.payDebtFromBankAcc(numAccount);
    }

    //enviar deuda a pagar
    @ApiOperation(value = "Service used to get the account debt")
    @GetMapping("/getDebt/{numAccount}")
    public Mono<Double> getDebt(@PathVariable("numAccount") String numAccount) {
        return service.getDebt(numAccount);
    }

}