package in.arijitnandi;

import io.quarkus.runtime.QuarkusApplication;

public class AnotherCLIApplication implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        Thread.sleep(1000);
        System.out.println("I am another CLI quarkus application");
        return 0;
    }
}