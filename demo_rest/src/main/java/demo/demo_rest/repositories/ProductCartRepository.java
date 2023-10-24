package demo.demo_rest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.demo_rest.entities.Cart;
import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.ProductInCart;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductInCart, Integer>{   
    List<ProductInCart> findByProduct(Product p);
    ProductInCart findByProductAndCart(Product p, Cart c);

}

