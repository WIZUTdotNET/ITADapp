package pl.dotnet.main.expections;


public class ConnectException extends RuntimeException {
    public ConnectException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public ConnectException(String exMessage) {
        super(exMessage);
    }
}