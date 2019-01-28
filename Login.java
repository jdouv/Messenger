import java.io.Console;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Login {
    void launch() {
        Database db = new Database();
        Log log = new Log();

        System.out.println("Preparing...\n");
        if (db.connectDB()) {
            if (db.testConnection()) {
                Messenger.clearConsole();
                welcomeScreen();
            } else {
                System.out.println("Connection to database failed.");
                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Unsuccessful database integrity validation.");
                System.exit(1);
            }
        }
        else {
            System.out.println("Oops! Something happened. Please restart me.");
            System.exit(0);
        }
    }

    void welcomeScreen() {
        Database db = new Database();
        User user = new User();
        Menu menu = new Menu();
        Log log = new Log();
        Scanner bsc = new Scanner(System.in);
        Scanner ssc = new Scanner(System.in);

        System.out.println("\u001B[33m" + " __  __                                          \n|  \\/  |                                         \n| \\  / | ___  ___ ___  ___ _ __   __ _  ___ _ __ \n| |\\/| |/ _ \\/ __/ __|/ _ \\ '_ \\ / _` |/ _ \\ '__|\n| |  | |  __/\\__ \\__ \\  __/ | | | (_| |  __/ |   \n|_|  |_|\\___||___/___/\\___|_| |_|\\__, |\\___|_|   \n                                  __/ |          \n                                 |___/            " + "\u001B[0m");
        System.out.println("\n\nWelcome to this mind-blowing messenger!\n");

        System.out.println("Enter one of the numbers below to login or register:\n1. Login\n2. Register\n3. Exit");
        while (!bsc.hasNextByte()) {
            System.out.println("Please input a valid number:");
            bsc.next();
        }
        byte choice = bsc.nextByte();
        if (choice == 1) {
            System.out.println("Username:");
            String lUsername = ssc.nextLine();
            while (lUsername.length() > 8) {
                System.out.println("Please enter a valid username:");
                lUsername = ssc.nextLine();
            }

            Console console = System.console();
            console.printf("Password:\n");
            char[] passwordChars = console.readPassword();
            String password = new String(passwordChars);
            while (password.length() > 16) {
                System.out.println("Please enter a valid password:");
                passwordChars = console.readPassword();
                password = new String(passwordChars);
            }

            if (db.checkCredentials(lUsername, password)) {
                Messenger.clearConsole();
                System.out.println("\nSuccessful login.\n");
                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " logged in.");
                menu.loginMenu(lUsername);
            } else {
                Messenger.clearConsole();
                System.out.println("\nThere is no user with such credentials.\n");
                welcomeScreen();
            }
        } else if (choice == 2){
            Messenger.clearConsole();
            user.register();
        } else if (choice == 3) {
            System.exit(0);
        } else {
            Messenger.clearConsole();
            System.out.println("You chose an invalid option. Please try again.");
            welcomeScreen();
        }
    }

    void logout(String lUsername) {
        Database db = new Database();

        System.out.println("Logging out...");
        Log log = new Log();
        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " logged out.");
        Messenger.main(null);
    }
}