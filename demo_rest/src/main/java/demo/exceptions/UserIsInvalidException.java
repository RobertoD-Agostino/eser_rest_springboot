package demo.exceptions;

public class UserIsInvalidException extends RuntimeException{
    
    public UserIsInvalidException(){

    }

    public UserIsInvalidException(String message){
        super(message);
    }

}
