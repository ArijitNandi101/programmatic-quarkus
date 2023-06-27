package in.arijitnandi;

import io.quarkus.runtime.Quarkus;

public class App
{
    public static void main(String[] args)
    {
        Quarkus.run(CLIQuarkusApplication.class, new String[0]);
        Quarkus.blockingExit();

        System.exit(0);
    }
}
