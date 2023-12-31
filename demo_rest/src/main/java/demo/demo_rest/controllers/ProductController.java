package demo.demo_rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.demo_rest.entities.Product;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.Users;
import demo.demo_rest.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired 
    private ProductService productService;

    @PostMapping("/addProduct")
    @PreAuthorize("hasAuthority ('ADMIN')")
    public ResponseEntity addProduct(@RequestBody Product p){
        try {
           Product ret = productService.addProducts(p);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Errore: " + e.getClass().getSimpleName() + " il prodotto esiste già", HttpStatus.BAD_REQUEST);
        }
        
    }

    @GetMapping("/findByCode")
    @PreAuthorize("hasAuthority ('USER')")
    public ResponseEntity findByCode(@RequestParam("code") String code){
        try {
            Product ret = productService.findByCode(code);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Errore: " + e.getClass().getSimpleName() + " prodotto non esistente", HttpStatus.BAD_REQUEST);
        }
        
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity deleteProduct(@RequestParam("code") String code){
        try {
            productService.deleteProduct(code);
            return new ResponseEntity(code + " eliminato",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Errore: " + e.getClass().getSimpleName() + " prodotto non esistente", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllProducts")
    @PreAuthorize("hasAnyAuthority ('USER','ADMIN')")
    public ResponseEntity getAllProducts(){
        try {
            List<Product> lista = productService.getAllProducts();
            return new ResponseEntity(lista, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + " il database è vuoto";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
         
    }

    @PutMapping("/modifyProduct")
    public ResponseEntity modifyProduct(@RequestParam("code") String code,@RequestParam("newName") String newName,@RequestParam("newPrice") int newPrice,@RequestParam("newCode") String newCode){
        try {
            Product ret = productService.modifyProduct(code, newName, newPrice, newCode);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buyNow")
    public ResponseEntity buyNow(@RequestParam("email") String email, @RequestParam("code") String code, @RequestParam("quantity") int quantity){
        try {
            Users ret = productService.buyNow(email, code, quantity);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByType")
    public ResponseEntity findByType(@RequestParam("type") String type, @RequestParam("numberPage") int numberPage, @RequestParam("sizePage") int sizePage){
        try {
            Page<Product> ret = productService.findByType(type, numberPage, sizePage);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getProductsSorted")
    public ResponseEntity getProductsSorted(@RequestParam("sort") String sort, @RequestParam("order") String order){
        try {
            List<Product> ret = productService.getProductsSorted(sort, order);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }
}