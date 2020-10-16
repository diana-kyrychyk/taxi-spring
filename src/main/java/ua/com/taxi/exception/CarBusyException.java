package ua.com.taxi.exception;

public class CarBusyException extends RuntimeException {

    public CarBusyException(String message) {
        super(message);
    }
}
