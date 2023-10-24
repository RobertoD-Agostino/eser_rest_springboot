package demo.demo_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.demo_rest.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    
}
