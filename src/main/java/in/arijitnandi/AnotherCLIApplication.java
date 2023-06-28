package in.arijitnandi;

import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

public class AnotherCLIApplication implements QuarkusApplication {

    @Inject
    LoggingService loggingService;

    @Override
    public int run(String... args) throws Exception {
        Thread.sleep(5000);
        loggingService.log("This message is from " + this.getClass().getTypeName());
        return 0;
    }
}