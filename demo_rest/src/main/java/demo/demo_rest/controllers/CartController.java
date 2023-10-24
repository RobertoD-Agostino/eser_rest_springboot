package demo.demo_rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.demo_rest.entities.Cart;
import demo.demo_rest.entities.ProductInCart;
import demo.demo_rest.entities.Users;
import demo.demo_rest.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;


    @PostMapping("/addProductToCart")
    public ResponseEntity addProductToCart(@RequestParam("code") String code, @RequestParam("email") String email, @RequestParam("quantity") int quantity){
        try {
            Cart ret = cartService.addProductToCart(code, email,quantity);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: "+ e.getClass().getSimpleName() +" impossibile aggiungere il prodotto";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/removeProductToCart")
    public ResponseEntity removeProductToCart(@RequestParam("code") String code, @RequestParam("email") String email){
        try {
            Cart ret = cartService.removeProductToCart(code, email);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: "+ e.getClass().getSimpleName() +" impossibile rimuovere il prodotto";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findProductInCartByProductAndCart")
    public ResponseEntity findByProductAndCart(@RequestParam("code") String code, @RequestParam("email") String email){
        try {
            ProductInCart ret = cartService.findByProductAndCart(code, email);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: "+ e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/findProductInCartByProduct")
    public ResponseEntity findProductInCartByProduct(@RequestParam("code") String code){
        try {
            List<ProductInCart> ret = cartService.findProductInCartByProduct(code);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/setQuantity")
    public ResponseEntity setQuantity(@RequestParam("code") String code,@RequestParam("email") String email,@RequestParam("quantity") int quantity){
        try {
            ProductInCart ret = cartService.setQuantity(code, email, quantity);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName() + "Il prodotto è stato eliminato, causa quantità nulla";
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/buyByCart")
    public ResponseEntity buyByCart(@RequestParam("email") String email, @RequestParam("code") String code){
        try {
            Users ret = cartService.buyByCart(code, email);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/buyAllCart")
    public ResponseEntity buyAllCart(@RequestParam("email") String email){
        try {
            Users ret = cartService.buyAllProductsByCart(email);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (Exception e) {
            String errore = "Errore: " + e.getClass().getSimpleName();
            return new ResponseEntity(errore, HttpStatus.BAD_REQUEST); 
        }
    }


}
