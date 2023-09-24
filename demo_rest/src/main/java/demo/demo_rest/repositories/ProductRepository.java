package demo.demo_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.demo_rest.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
    Product findByCode(String code);
    boolean existsByCode(String code);
}
