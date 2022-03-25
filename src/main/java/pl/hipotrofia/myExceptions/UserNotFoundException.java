package pl.hipotrofia.myExceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String error) {
        super(error);
    }

}