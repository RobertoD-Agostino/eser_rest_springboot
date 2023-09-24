package demo.demo_rest.repositories;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import demo.demo_rest.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer>{
    Users findByEmail(String email);
    boolean existsByEmail(String email);
    
}
