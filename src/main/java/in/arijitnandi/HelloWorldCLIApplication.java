package in.arijitnandi;

import io.quarkus.runtime.QuarkusApplication;

public class HelloWorldCLIApplication implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        Thread.sleep(1000);
        System.out.println("Hello World!! I am a CLI quarkus application");
        return 0;
    }
}