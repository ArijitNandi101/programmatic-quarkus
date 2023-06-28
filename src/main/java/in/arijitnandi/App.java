package in.arijitnandi;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@QuarkusMain
public class App {

    public static void main(String[] args) throws InterruptedException {
        Quarkus.run(HelloWorldCLIApplication.class);

        System.out.println("Blocking exit: " + HelloWorldCLIApplication.class.getTypeName());
        Quarkus.blockingExit();

        CountDownLatch latch = new CountDownLatch(1);

        Quarkus.run(HTTPApplication.class, (exitCode, throwable) -> {
            latch.countDown();
        });

        boolean await = latch.await(600, TimeUnit.SECONDS);
        System.out.println("Blocking exit: " + HTTPApplication.class.getTypeName());

        Quarkus.run(AnotherCLIApplication.class);
        Quarkus.blockingExit();
        System.out.println("Blocking exit: " + AnotherCLIApplication.class.getTypeName());

        System.exit(0);

    }
}
