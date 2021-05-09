package pl.dotnet.main.expections;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)

public class EventFullException extends RuntimeException {
    public EventFullException() {
        super();
    }

    public EventFullException(String message) {
        super(message);
    }
}
