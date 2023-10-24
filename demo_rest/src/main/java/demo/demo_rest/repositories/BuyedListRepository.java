package demo.demo_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.demo_rest.entities.BuyedList;

@Repository
public interface BuyedListRepository extends JpaRepository<BuyedList, Integer>{
    
}
