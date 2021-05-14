package pl.dotnet.main.expections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class NotPayedException extends RuntimeException {
    public NotPayedException() {
        super();
    }

    public NotPayedException(String message) {
        super(message);
    }
}