package org.pkfrc.vending.ws.product;

import java.util.List;

import org.pkfrc.core.dto.base.BaseRecordDTO;
import org.pkfrc.core.dto.base.BaseRecordsDTO;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.core.ws.base.BaseWS;
import org.pkfrc.projurise.repo.product.ProductRepository;
import org.pkfrc.vending.dto.product.ProductDTO;
import org.pkfrc.vending.entities.product.Product;
import org.pkfrc.vending.services.product.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/product")
public class ProductWS extends BaseWS<Product, Long> {

    Logger logger = LoggerFactory.getLogger(ProductWS.class);

    @Autowired
    IProductService service;
    
    @Autowired
	ProductRepository repository;

    @Override
    public Logger getLOGGER() {
        return logger;
    }

    @Override
    public IBaseService<Product, Long> getService() {
        return service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BaseRecordDTO<ProductDTO>> findEntity(@PathVariable Long id, @RequestParam(value = "lang", required = true, defaultValue = defaultLang) String lang) {
        return super.findEntity(id, ProductDTO.class);
    }

    @GetMapping(value = "findByCode/{code}")
    public ResponseEntity<BaseRecordDTO<ProductDTO>> findByCode(@PathVariable String code, @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang) {
        try {
            ServiceData<Product> sData = service.findByCode(code);
            BaseRecordDTO<ProductDTO> result = evaluateServiceData(sData, ProductDTO.class);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }
    @PostMapping(value = "/add")
    public ResponseEntity<BaseRecordDTO<ProductDTO>> add(
            @RequestBody ProductDTO dto,
            @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang ) {
        return super.create(dto, new Product(), "admin", ProductDTO.class);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<BaseRecordDTO<ProductDTO>> edit(
            @RequestBody ProductDTO dto,
            @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang) {
        return super.update(dto, new Product(), "admin");
    }

    @GetMapping(value = "/all")
    public ResponseEntity<BaseRecordsDTO<ProductDTO>> findAll(
            @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang) {
        return super.findAll(ProductDTO.class);
    }

    @PostMapping(value = "/all")
    public ResponseEntity<BaseRecordsDTO<ProductDTO>> findAll(
            @RequestBody List<SearchCriteria> criterias,
            @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang) {
        return super.findAll(criterias, ProductDTO.class);
    }

    @PostMapping(value = "/page")
    public ResponseEntity<BaseRecordsDTO<ProductDTO>> findPage(
            @RequestBody List<SearchCriteria> criterias,
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size) {
        return super.findPage(criterias, page, size, ProductDTO.class);
    }
    
    @GetMapping(value = "/test")
    public String test() {
        return "Hello, World";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseRecordDTO<ProductDTO>> deleteEntity(@PathVariable Long id, @RequestParam(value = "lang", required = false, defaultValue = defaultLang) String lang) {
        return super.delete(id, ProductDTO.class, "admin");
    }   
    
}