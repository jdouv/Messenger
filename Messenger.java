import java.io.IOException;

class Messenger {
    public static void main(String[] args) {
        Login login = new Login();
        login.launch();
    }

    static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033\143");
            }
        }
        catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}