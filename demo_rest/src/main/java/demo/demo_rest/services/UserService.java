package demo.demo_rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.demo_rest.entities.BuyedList;
import demo.demo_rest.entities.Cart;
import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.UserDto;
import demo.demo_rest.entities.Users;
import demo.demo_rest.repositories.BuyedListRepository;
import demo.demo_rest.repositories.CartRepository;
import demo.demo_rest.repositories.ProductCartRepository;
import demo.demo_rest.repositories.ProductRepository;
import demo.demo_rest.repositories.UsersRepository;
import demo.exceptions.DbIsEmptyException;
import demo.exceptions.ProductAlreadyExistException;
import demo.exceptions.ProductDoesNotExistException;
import demo.exceptions.UserAlreadyExistException;
import demo.exceptions.UserDoesNotExistException;
import demo.exceptions.UserIsInvalidException;

@Service
public class UserService {
    
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

    // public Users addUser(Users u)throws RuntimeException{ 
    //     String regexNome = "^[a-zA-Z]{4,}";
    //     Cart carrello = new Cart();
    //     BuyedList bL = new BuyedList();
        
    //     if (existByEmail(u.getEmail()) || !u.getName().matches(regexNome)) {
    //         throw new UserAlreadyExistException();
    //     }else{
    //         cartRepository.save(carrello);
    //         u.setCart(carrello);
    //         buyedListRepository.save(bL);
    //         u.setListaProdottiComprati(bL);
    //         u = usersRepository.save(u);
    //     }       

    //     return u; 
    // }

    public UserDto addUser(Users u)throws RuntimeException{ 
        String regexNome = "^[a-zA-Z]{4,}";
        Cart carrello = new Cart();
        BuyedList bL = new BuyedList();
        
        if (existByEmail(u.getEmail()) || !u.getName().matches(regexNome)) {
            throw new UserAlreadyExistException();
        }else{
            cartRepository.save(carrello);
            u.setCart(carrello);
            buyedListRepository.save(bL);
            u.setListaProdottiComprati(bL);
            u = usersRepository.save(u);
        }       
        
        UserDto uDto = new UserDto(u.getId(), u.getEmail());

        return uDto; 
    }


    public boolean existByEmail(String email){
        return usersRepository.existsByEmail(email);
    }

    public List<Users> getAllUsers()throws RuntimeException{
        List<Users> lista = usersRepository.findAll();
        if (lista.size()==0) {
            throw new DbIsEmptyException();
        }else{
            return lista;
        }
    }


    public void removeUser(String email)throws RuntimeException{
        Users u = findByEmail(email);
        
        if (u!=null) {
            usersRepository.delete(u);
        }else{
            throw new UserDoesNotExistException("L'utente " + email + " non esiste");
        }
    }

    public Users findByEmail(String email)throws RuntimeException{
        if (usersRepository.existsByEmail(email)) {
            return usersRepository.findByEmail(email).get();
        }else{
            throw new UserDoesNotExistException();
        }
        
    }

    public boolean UserIsValid(Users u){
        String regexNome = "^[a-zA-Z]{4,}";
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z.-]+$";
        if (!u.getName().matches(regexNome) && u.getSurname().matches(regexNome) && u.getEmail().matches(regexEmail)){
            return false;          
        }
        return true;         
    }

    public Users modifyUser(String email, String newName, String newSurname, String newEmail)throws RuntimeException{
        Users u = findByEmail(email);

        if (u!=null || UserIsValid(u)) {       
            u.setName(newName);
            u.setSurname(newSurname);
            u.setEmail(newEmail);
            return usersRepository.save(u);
        }else{
            throw new UserIsInvalidException();
        }      
    }

    // public Users modifyPassword(){
        
    // }
}
