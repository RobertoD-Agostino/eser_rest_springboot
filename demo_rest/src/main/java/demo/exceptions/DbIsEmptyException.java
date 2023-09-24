package demo.exceptions;

public class DbIsEmptyException extends RuntimeException {
 
    public DbIsEmptyException(){

    }

    public DbIsEmptyException(String message){
        super(message);
    }
}
