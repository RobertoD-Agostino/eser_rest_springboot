package demo.exceptions;

public class ProductDoesNotExistException extends RuntimeException{
    
    public ProductDoesNotExistException(){

    }

    public ProductDoesNotExistException(String message){
        super(message);
    }
}
