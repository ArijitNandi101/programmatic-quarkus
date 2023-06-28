package in.arijitnandi;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggingService {

    public void log(String message) {
        System.out.printf("LOG-LINE: %s%n", message);
    }
}
