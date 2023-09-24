package demo.exceptions;

public class ProductAlreadyExistException extends RuntimeException{
    public ProductAlreadyExistException(){

    }

    public ProductAlreadyExistException(String message){
        super(message);
    }
}
