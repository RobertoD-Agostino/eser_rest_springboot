package demo.demo_rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import demo.demo_rest.entities.Users;
import demo.demo_rest.repositories.UsersRepository;
import demo.exceptions.DbIsEmptyException;
import demo.exceptions.UserAlreadyExistException;
import demo.exceptions.UserDoesNotExistException;
import demo.exceptions.UserIsInvalidException;

@Service
public class UserService {


    @Autowired 
    UsersRepository usersRepository;

    public Users addUser(Users u)throws RuntimeException{ 
        String regexNome = "^[a-zA-Z]{4,}";
        if (existByEmail(u.getEmail()) || !u.getName().matches(regexNome)) {
            throw new UserAlreadyExistException();
        }else{
            usersRepository.save(u);
        }       
        return u; 
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
            return usersRepository.findByEmail(email);
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
}
