package nitro;

import nitro.cli.ConsoleApplication;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleApplication app = new ConsoleApplication();
            app.run();
        } catch (RuntimeException e) {
            System.exit(1);
        }
    }
}
