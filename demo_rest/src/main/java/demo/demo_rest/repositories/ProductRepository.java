package demo.demo_rest.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.demo_rest.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
    Product findByCode(String code);
    boolean existsByCode(String code);
    Page<Product> findByType(String type, PageRequest pageRequest);
}
