package demo.demo_rest.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import demo.demo_rest.entities.BuyedList;
import demo.demo_rest.entities.BuyedProduct;
import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.Users;
import demo.demo_rest.repositories.BuyedListRepository;
import demo.demo_rest.repositories.BuyedProductRepository;
import demo.demo_rest.repositories.CartRepository;
import demo.demo_rest.repositories.ProductCartRepository;
import demo.demo_rest.repositories.ProductRepository;
import demo.demo_rest.repositories.UsersRepository;
import demo.exceptions.BudgetNotEnoughException;
import demo.exceptions.DbIsEmptyException;
import demo.exceptions.NotEnoughStockException;
import demo.exceptions.ProductAlreadyExistException;
import demo.exceptions.ProductDoesNotExistException;
import demo.exceptions.UserDoesNotExistException;
import jakarta.transaction.Transactional;

@Service
public class ProductService {
    
    @Autowired
    ProductRepository productsRepository;

    @Autowired
    ProductCartRepository productCartRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BuyedProductRepository buyedProductRepository;

    @Autowired
    BuyedListRepository buyedListRepository;

    @Transactional
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

    @Transactional
    public void deleteProduct(String code)throws RuntimeException{
        Product p = findByCode(code);      
        productsRepository.delete(p);      
    }

    @Transactional
    public Product modifyProduct(String code, String newName, int newPrice, String newCode)throws RuntimeException{
        Product p = findByCode(code);
        p.setName(newName);
        p.setPrice(newPrice);
        p.setCode(newCode);
        return productsRepository.save(p);  
    }

    @Transactional
    public Users buyNow(String email,String code,  int quantity){
        Users u = usersRepository.findByEmail(email).get();
        Product p = findByCode(code);

        if (u==null) {
            throw new UserDoesNotExistException();
        }

        if (p==null) {
            throw new ProductDoesNotExistException();
        }
        
        BuyedProduct bP = new BuyedProduct();
        bP.setName(p.getName());
        bP.setCode(code);
        bP.setPrice(p.getPrice());

        if (quantity>p.getScorta()) {
            throw new NotEnoughStockException();
        }else{
            bP.setQuantity(quantity);
        } 
        int priceTot = p.getPrice() * quantity;
        
        if (u.getBudget()>=priceTot) {
            int resto = u.getBudget()-priceTot;
            u.setBudget(resto);   
        }else{
            throw new BudgetNotEnoughException();
        }

        u.getListaProdottiComprati().setPriceTot(priceTot);                 

        u.getListaProdottiComprati().getProdottiComprati().add(bP);
        buyedProductRepository.save(bP);
        buyedListRepository.save(u.getListaProdottiComprati());

        int differenza = p.getScorta() - bP.getQuantity();
        p.setScorta(differenza);  

        u = usersRepository.save(u);
        p = productsRepository.save(p);

        return u;
    }


    public Page<Product> findByType(String type, int numberPage, int sizePage){
        PageRequest pageRequest = PageRequest.of(numberPage, sizePage);
        return productsRepository.findByType(type, pageRequest);
    }

    public Comparator<Product> getComparatorForSortingField(String sort)throws RuntimeException{
        switch(sort){
            case "name":
            return Comparator.comparing(Product:: getName);
            case "price":
            return Comparator.comparing(Product:: getPrice);
            case "type":
            return Comparator.comparing(Product:: getType);
            default:
            return Comparator.comparing(Product:: getCode);
        }
    }


    public List<Product> getProductsSorted(String sort, String order){
        List<Product> products = productsRepository.findAll();

        if (sort!=null) {
            Comparator<Product> comparator = getComparatorForSortingField(sort);
            if ("desc".equals(order)){
                products.sort(comparator.reversed());
            }else{
                products.sort(comparator);
            }
        }
        return products;
    }




}
