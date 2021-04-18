package pl.dotnet.main.expections;


public class ConnectExpection extends RuntimeException {
    public ConnectExpection(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public ConnectExpection(String exMessage) {
        super(exMessage);
    }
}