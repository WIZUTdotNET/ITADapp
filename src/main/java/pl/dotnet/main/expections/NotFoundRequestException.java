package pl.dotnet.main.expections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException() {
        super();
    }

    public NotFoundRequestException(String message) {
        super(message);
    }
}