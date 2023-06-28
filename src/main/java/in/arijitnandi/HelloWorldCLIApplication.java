package in.arijitnandi;

import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

public class HelloWorldCLIApplication implements QuarkusApplication {

    @Inject
    LoggingService loggingService;

    @Override
    public int run(String... args) throws Exception {
        loggingService.log("This message is from " + this.getClass().getTypeName());
        return 0;
    }
}