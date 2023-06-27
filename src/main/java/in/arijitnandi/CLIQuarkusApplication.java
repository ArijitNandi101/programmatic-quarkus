package in.arijitnandi;

import io.quarkus.runtime.QuarkusApplication;

public class CLIQuarkusApplication implements QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Hello World!! I am a CLI quarkus application");

        return 0;
    }
}