package pl.dotnet.main.expections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED)
public class NotPayedException extends RuntimeException {
    public NotPayedException() {
        super();
    }

    public NotPayedException(String message) {
        super(message);
    }
}