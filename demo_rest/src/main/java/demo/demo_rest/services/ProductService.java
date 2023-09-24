package demo.demo_rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.demo_rest.entities.Product;
import demo.demo_rest.repositories.ProductRepository;
import demo.exceptions.DbIsEmptyException;
import demo.exceptions.ProductAlreadyExistException;
import demo.exceptions.ProductDoesNotExistException;

@Service
public class ProductService {
    
    @Autowired
    ProductRepository productsRepository;

    public Product addProducts(Product p)throws RuntimeException{
        if (productsRepository.existsByCode(p.getCode())) {
            throw new ProductAlreadyExistException();
        }else{
            return productsRepository.save(p);
        }
    } 

    public Product findByCode(String code)throws RuntimeException{
        if (productsRepository.existsByCode(code)) {
            return productsRepository.findByCode(code);
        }else{
            throw new ProductDoesNotExistException();
        }       
    }

    public List<Product> getAllProducts()throws RuntimeException{
        List<Product> lista = productsRepository.findAll();
        if (lista.size() == 0) {
            throw new DbIsEmptyException();
        }else{
            return lista;
        }
    }

    public void deleteProduct(String code)throws RuntimeException{
        Product p = findByCode(code);
        
        if (p!=null) {
            productsRepository.delete(p);
        }else{
             throw new ProductDoesNotExistException();
        }
    }

    public Product modifyProduct(String code, String newName, int newPrice, String newCode)throws RuntimeException{
        Product p = findByCode(code);

        if (productsRepository.existsByCode(code)){
            p.setName(newName);
            p.setPrice(newPrice);
            p.setCode(newCode);
            return productsRepository.save(p);
        }else{
            throw new ProductDoesNotExistException();
        }       
    }
}
