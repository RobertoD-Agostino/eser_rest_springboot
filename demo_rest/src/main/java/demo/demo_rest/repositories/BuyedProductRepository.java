package demo.demo_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.demo_rest.entities.BuyedProduct;



public interface BuyedProductRepository extends JpaRepository<BuyedProduct, Integer>{
    BuyedProduct findByCode(String code);
    boolean existsByCode(String code);
}
