package demo.demo_rest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.demo_rest.entities.BuyedList;
import demo.demo_rest.entities.Cart;
import demo.demo_rest.entities.BuyedList;
import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.BuyedProduct;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.Users;
import demo.demo_rest.repositories.CartRepository;
import demo.demo_rest.repositories.BuyedListRepository;
import demo.demo_rest.repositories.BuyedProductRepository;
import demo.demo_rest.repositories.ProductCartRepository;
import demo.demo_rest.repositories.ProductRepository;
import demo.demo_rest.repositories.UsersRepository;
import demo.exceptions.BudgetNotEnoughException;
import demo.exceptions.CartIsEmptyException;
import demo.exceptions.ImpossibleToCreateException;
import demo.exceptions.ProductDoesNotExistException;
import demo.exceptions.UserAlreadyExistException;
import demo.exceptions.UserDoesNotExistException;
import jakarta.transaction.Transactional;

@Service
public class CartService {
    @Autowired 
    UsersRepository usersRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCartRepository productCartRepository;

    @Autowired
    BuyedListRepository buyedListRepository;

    @Autowired
    BuyedProductRepository buyedProductRepository;

    @Transactional
    public Cart addProductToCart(String code, String email, int q)throws RuntimeException{
        Users u = usersRepository.findByEmail(email).get();
        Product p = productRepository.findByCode(code);

        if (u==null) {
            throw new UserDoesNotExistException();
        }

        if (p==null) {
            throw new ProductDoesNotExistException();
        }

        ProductInCart ret = findByProductAndCart(code, email);

        if (ret!=null) {
            int qTot = q + ret.getQuantity();

            if (qTot>ret.getProduct().getScorta()) {
                int scarto = qTot - ret.getProduct().getScorta();
                qTot-=scarto;
                ret.setQuantity(qTot);
                productCartRepository.save(ret);                
            }else{
                ret.setQuantity(qTot);
                productCartRepository.save(ret);                
            }

            if (q==0) {
                productCartRepository.delete(ret);
            }else if(q<0){
                throw new RuntimeException();
            }
        }else{
            ProductInCart pInCart = new ProductInCart(null, q, p, u.getCart());
            pInCart = productCartRepository.save(pInCart);
            u.getCart().getProdotti().add(pInCart);
            usersRepository.save(u);              
        }
        return u.getCart();               
    }


    public ProductInCart findByProductAndCart(String code, String email){
        Product p = productRepository.findByCode(code);
        Users u = usersRepository.findByEmail(email).get();
        return productCartRepository.findByProductAndCart(p, u.getCart());
    }

    @Transactional
    public ProductInCart setQuantity(String code, String email, int quantity)throws RuntimeException{
        Product p = productRepository.findByCode(code);
        Users u = usersRepository.findByEmail(email).get();
        ProductInCart pIC = findByProductAndCart(code, email);

        if (u==null) {
            throw new UserDoesNotExistException();
        }

        if (p==null) {
            throw new ProductDoesNotExistException();
        }
        
        if (quantity<=0) {
            removeProductToCart(code, email);
            return null;
        }else{
           pIC.setQuantity(quantity);
           productCartRepository.save(pIC);
           usersRepository.save(u);
        }
        return pIC;
    }

    @Transactional
    public Cart removeProductToCart(String code, String email)throws RuntimeException{
        Users u = usersRepository.findByEmail(email).get();
        Product p = productRepository.findByCode(code);
        ProductInCart pIC = findByProductAndCart(code, email);

        if (u==null) {
            throw new UserDoesNotExistException();
        }

        if (p==null) {
            throw new ProductDoesNotExistException();
        }

        if (pIC==null) {
            throw new ProductDoesNotExistException();
        }
        
        boolean isPresent = false;
        for (ProductInCart pInC : u.getCart().getProdotti()) {
            if (pIC.equals(pInC)) {
                isPresent = true;
            }
        }
        if (isPresent) {
            u.getCart().getProdotti().remove(pIC);
            productCartRepository.delete(pIC);
            usersRepository.save(u);
            cartRepository.save(u.getCart());
        } 

        return u.getCart();
    }

    
    public List<ProductInCart> findProductInCartByProduct(String code){
        Product p = productRepository.findByCode(code);
        return productCartRepository.findByProduct(p);
    }

    @Transactional
    public Users buyByCart(String code, String email)throws RuntimeException{
        Users u = usersRepository.findByEmail(email).get();
        Product p = productRepository.findByCode(code);

        if (u==null) {
            throw new UserDoesNotExistException();
        }

        if (p==null) {
            throw new ProductDoesNotExistException();
        }

        ProductInCart pIC = findByProductAndCart(code, email);
        int prezzoTot = p.getPrice() * pIC.getQuantity();

        if (u.getBudget()>=prezzoTot) {
            int resto = u.getBudget()-prezzoTot;
            u.setBudget(resto);   
        }else{
            throw new BudgetNotEnoughException();
        }

        BuyedProduct bP = new BuyedProduct();

        bP.setName(p.getName());
        bP.setCode(code);
        bP.setPrice(p.getPrice());
        bP.setQuantity(pIC.getQuantity());
        u.getListaProdottiComprati().setPriceTot(prezzoTot);

        u.getListaProdottiComprati().getProdottiComprati().add(bP);
        buyedProductRepository.save(bP);
        buyedListRepository.save(u.getListaProdottiComprati());

        int differenza = p.getScorta() - pIC.getQuantity();
        p.setScorta(differenza);        
        // removeProductToCart(code, email);
        productCartRepository.delete(pIC);

        u = usersRepository.save(u);
        p = productRepository.save(p);
        
        return u;
    }

    @Transactional
    public Users buyAllProductsByCart(String email)throws RuntimeException{
        Users u = usersRepository.findByEmail(email).get();
        
        if (u==null) {
            throw new UserDoesNotExistException();
        }
     
        if (u.getCart().getProdotti().size()==0) {
            throw new CartIsEmptyException();
        }

            int prezzoTot = 0;       

        for (ProductInCart  pIC : u.getCart().getProdotti()) {
            int prezzoSingoloPic = pIC.getProduct().getPrice() * pIC.getQuantity();
            prezzoTot+=prezzoSingoloPic;         
            Product p = pIC.getProduct();

            if (u.getBudget()>=prezzoTot) {
                int resto = u.getBudget()-prezzoTot;
                u.setBudget(resto);
                usersRepository.save(u);   
            }else{
                throw new BudgetNotEnoughException();
            } 

            BuyedProduct bP = new BuyedProduct();
            bP.setName(p.getName());
            bP.setCode(p.getCode());
            bP.setPrice(prezzoSingoloPic);
            bP.setQuantity(pIC.getQuantity());

            int differenza = p.getScorta() - pIC.getQuantity();
            p.setScorta(differenza);
            u.getListaProdottiComprati().setPriceTot(prezzoTot);

            u.getListaProdottiComprati().getProdottiComprati().add(bP);

            bP = buyedProductRepository.save(bP);
            buyedListRepository.save(u.getListaProdottiComprati()); 
            p = productRepository.save(p);
            productCartRepository.delete(pIC);
        }
            u.getCart().getProdotti().clear();
            u = usersRepository.save(u);       
        return u;
    }

}

        // boolean isPresent = false;
        // for (ProductInCart pIC : u.getCart().getProdotti()) {
        //     if ( pIC.getProduct().equals(p)) {
        //         isPresent = true;
        //         int newQ = pIC.getQuantity() + q;
        //         pIC.setQuantity(newQ);
        //         productCartRepository.save(pIC);  
        //     }
        // }

        // if (!isPresent) {
        //     ProductInCart pInCart = new ProductInCart(null, q, p, u.getCart());
        //     pInCart = productCartRepository.save(pInCart);
        //     u.getCart().getProdotti().add(pInCart);
        //     usersRepository.save(u);   
        // } 





            // @Transactional
    // public Users buyByCart(String code, String email)throws RuntimeException{
    //     Users u = usersRepository.findByEmail(email);
    //     Product p = productRepository.findByCode(code);

    //     if (u==null) {
    //         throw new UserDoesNotExistException();
    //     }

    //     if (p==null) {
    //         throw new ProductDoesNotExistException();
    //     }

    //     ProductInCart pIC = findByProductAndCart(code, email);
    //     int prezzoTot = p.getPrice() * pIC.getQuantity();

    //     if (u.getBudget()>=prezzoTot) {
    //         int resto = u.getBudget()-prezzoTot;
    //         u.setBudget(resto);   
    //     }else{
    //         throw new BudgetNotEnoughException();
    //     }

    //     boolean isPresent = false;
    //     for (BuyedProduct bPLista : u.getListaProdottiComprati().getProdottiComprati()) {
    //         if (bPLista.getCode().equals(code)) {
    //             isPresent = true;
    //             int qTot = bPLista.getQuantity() + pIC.getQuantity();
    //             bPLista.setQuantity(qTot);
    //             buyedProductRepository.save(bPLista);
    //             buyedListRepository.save(u.getListaProdottiComprati());
    //         }
    //     }

    //     if (!isPresent) {
    //         BuyedProduct bP = new BuyedProduct();
    //         bP.setName(p.getName());
    //         bP.setCode(code);
    //         bP.setPrice(p.getPrice());
    //         bP.setQuantity(pIC.getQuantity());
    //         u.getListaProdottiComprati().setPriceTot(prezzoTot);

    //         u.getListaProdottiComprati().getProdottiComprati().add(bP);
    //         buyedProductRepository.save(bP);
    //         buyedListRepository.save(u.getListaProdottiComprati());
    //         u = usersRepository.save(u); 
    //     }


    //     int differenza = p.getScorta() - pIC.getQuantity();
    //     p.setScorta(differenza);        
    //     removeProductToCart(code, email);

    //     u = usersRepository.save(u);
    //     p = productRepository.save(p);
        
    //     return u;
    // }