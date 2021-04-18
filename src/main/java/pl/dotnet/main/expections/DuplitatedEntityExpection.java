package pl.dotnet.main.expections;

public class DuplitatedEntityExpection extends RuntimeException {

    public DuplitatedEntityExpection(String message) {
        super(message);
    }

}
