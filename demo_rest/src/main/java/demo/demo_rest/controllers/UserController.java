package demo.demo_rest.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.demo_rest.entities.Cart;
import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.Role;
import demo.demo_rest.entities.UserDto;
import demo.demo_rest.entities.Users;
import demo.demo_rest.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService us;

    // @PostMapping("/add")
    // public ResponseEntity addUser(@RequestBody Users u){
    //     try {
    //         Users j = us.addUser(u);
    //         return new ResponseEntity(j, HttpStatus.OK);
    //     } 
    //     catch (Exception e) {
    //         String errore = "Errore: " + e.getClass().getSimpleName() + " l'utente " + u.getEmail() + " esiste già";
    //         return new ResponseEntity(errore, HttpStatus.BAD_REQUEST ); 
    //     }            
    // }
    
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody Users u){
        try {
            UserDto ret = us.addUser(u);
            return new ResponseEntity(ret, HttpStatus.OK);
        } 
        catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + " l'utente " + u.getEmail() + " esiste già";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST ); 
        }            
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity getAllUsers(){
        List<Users> ret = us.getAllUsers();
        try {
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + " il database è vuoto";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
         
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestParam("email") String email){
        try {
            us.removeUser(email);
            return new ResponseEntity("Utente eliminato",HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + " l'utente " + email + " non esiste";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST ); 
        }

    }

    @GetMapping("/findByEmail")
    public ResponseEntity findByEmail(@RequestParam("email") String email){
        try {
            Users ret = us.findByEmail(email);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + " l'utente " + email + " non esiste";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST ); 
        }
           
    }

    @PutMapping("/modifyUser")
    public ResponseEntity modifyUser(@RequestParam("email") String email,@RequestParam("newName") String newName,@RequestParam("newSurname") String newSurname,@RequestParam("newEmail") String newEmail){
        try {
           Users ret =  us.modifyUser(email, newName, newSurname, newEmail);
            return new ResponseEntity(ret, HttpStatus.OK);
         } catch (Exception e) {
            String errore = "Errore: "+ e.getClass().getSimpleName() +" impossibile modificare l'utente " + email;
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
         }

    }


    @GetMapping("/test")
    public Users test(@RequestParam("name") String name, @RequestParam("email") String email){
        Users u = new Users(null, name, null, email,0,null,null, null, Role.ADMIN);
        return u;
    }


}
