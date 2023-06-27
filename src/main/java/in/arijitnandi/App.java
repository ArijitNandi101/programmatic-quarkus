package in.arijitnandi;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class App {

    public static void main(String[] args) {
        Quarkus.run(AnotherCLIApplication.class);
        Quarkus.blockingExit();

        Quarkus.run(HelloWorldCLIApplication.class);
        Quarkus.blockingExit();

        System.exit(0);

    }
}
