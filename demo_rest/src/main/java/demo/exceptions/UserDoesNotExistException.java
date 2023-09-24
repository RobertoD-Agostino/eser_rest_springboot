package demo.exceptions;

public class UserDoesNotExistException extends RuntimeException{
    

    public UserDoesNotExistException(){

    }

    public UserDoesNotExistException(String message){
        super(message);
    }
}
