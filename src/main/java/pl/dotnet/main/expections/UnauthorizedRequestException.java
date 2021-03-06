package pl.dotnet.main.expections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException() {
        super();
    }

    public UnauthorizedRequestException(String message) {
        super(message);
    }
}