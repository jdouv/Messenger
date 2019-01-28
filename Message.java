import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Message {
    private long from_id, to_id;
    private String text;
    private String datetime;

    Message() {
    }

    private Message(String datetime, long from_id, long to_id, String text) {
        this.datetime = datetime;
        this.from_id = from_id;
        this.to_id = to_id;
        this.text = text;
    }

    long getFrom_id() {
        return from_id;
    }

    long getTo_id() {
        return to_id;
    }

    String getText() {
        return text;
    }

    String getDatetime() {
        return datetime;
    }

    void createMessage(String lUsername) {
        Database db = new Database();
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("Give me the username of the person you want to send a message:");
        String username = sc.nextLine();
        while (username.length() > 8) {
            System.out.println("Please enter a username containing no more than 8 characters:");
            username = sc.nextLine();
        }

        if (db.userExists(username)) {
            if (db.getGender(username).equals("male")) {
                System.out.println("OK, what do you want to send him? Write a text up to 250 characters:");
            } else if (db.getUser(username).getGender().equals("female")) {
                System.out.println("OK, what do you want to send her? Write a text up to 250 characters:");
            }
            String text = sc.nextLine();
            while (text.length() > 250) {
                System.out.println("You cannot exceed the 250 characters limit. Please try again:");
                text = sc.nextLine();
            }
            Messenger.clearConsole();
            if (db.createMessage(new Message(datetime, db.getIdByUsername(lUsername), db.getIdByUsername(username), text))) {
                System.out.println("OK. This message has been sent.\n");
                Log log = new Log();
                log.writeToLog(datetime + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " sent " + db.getRole(username) + " " + username + " the following message: \"" + text + "\"");
            } else {
                System.out.println("Oops! Something happened.\n");
            }
            menu.loginMenu(lUsername);
        } else {
            Messenger.clearConsole();
            System.out.println("There is no user with this username.");
            menu.loginMenu(lUsername);
        }
    }

    void editSentMessage(String lUsername) {
        Database db = new Database();
        Menu menu = new Menu();
        Scanner ssc = new Scanner(System.in);
        Scanner lsc = new Scanner(System.in);

        db.viewSent(lUsername);
        System.out.println("Well, give me the number of the message you want to edit:");
        while (!lsc.hasNextLong()) {
            System.out.println("Please input a valid message number:");
            lsc.next();
        }
        long id = lsc.nextLong();

        if (db.sentNoIs(lUsername, id)) {
            System.out.println("Remember, you cannot exceed the 250 characters limit. OK, update your message:");
            String text = ssc.nextLine();
            while (text.length() > 250) {
                System.out.println("You cannot exceed the 250 characters limit. Please try again:");
                text = ssc.nextLine();
            }
            Messenger.clearConsole();
            db.editSentMessage(lUsername, id, text);
            menu.sentSubmenu(lUsername);
        } else {
            Messenger.clearConsole();
            System.out.println("There is no message with this number.");
            menu.sentSubmenu(lUsername);
        }
    }

    void deleteFromInbox(String lUsername) {
        Database db = new Database();
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();

        db.viewInbox(lUsername);
        System.out.println("Give me the number of the message you want to delete:");
        while (!sc.hasNextLong()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        long id = sc.nextLong();

        if (db.inboxNoIs(lUsername, id)) {
            System.out.println("Are you sure you want to delete message with number " + id + "? (y/n)");
            String answer = sc.nextLine();
            while (!(answer.equals("y") || answer.equals("n"))) {
                System.out.println("Please enter \"y\" for yes or \"n\" for no:");
                answer = sc.nextLine();
            }
            if (answer.equals("y")) {
                Messenger.clearConsole();
                db.deleteFromInbox(lUsername, id);
                menu.inboxSubmenu(lUsername);
            } else {
                Messenger.clearConsole();
                menu.inboxSubmenu(lUsername);
            }
        } else {
            Messenger.clearConsole();
            System.out.println("There is no message with this number.");
            menu.sentSubmenu(lUsername);
        }
    }

    void deleteFromSent(String lUsername) {
        Database db = new Database();
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);

        db.viewSent(lUsername);
        System.out.println("Give me the number of the message you want to delete:");
        while (!sc.hasNextLong()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        long id = sc.nextLong();

        if (db.sentNoIs(lUsername, id)) {
            System.out.println("Are you sure you want to delete message with number " + id + "? (y/n)");
            String answer = sc.nextLine();
            while (!(answer.equals("y") || answer.equals("n"))) {
                System.out.println("Please enter \"y\" for yes or \"n\" for no:");
                answer = sc.nextLine();
            }
            if (answer.equals("y")) {
                Messenger.clearConsole();
                db.deleteFromSent(lUsername, id);
                menu.sentSubmenu(lUsername);
            } else {
                Messenger.clearConsole();
                menu.sentSubmenu(lUsername);
            }
        } else {
            Messenger.clearConsole();
            System.out.println("There is no message with this number.");
            menu.sentSubmenu(lUsername);
        }
    }

    void deleteFromTrash(String lUsername) {
        Database db = new Database();
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);

        db.viewTrash(lUsername);
        System.out.println("Give me the number of the message you want to delete:");
        while (!sc.hasNextLong()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        long id = sc.nextLong();

        if (db.trashNoIs(lUsername, id)) {
            System.out.println("Are you sure you want to delete message with number " + id + "? (y/n)");
            String answer = sc.nextLine();
            while (!(answer.equals("y") || answer.equals("n"))) {
                System.out.println("Please enter \"y\" for yes or \"n\" for no:");
                answer = sc.nextLine();
            }
            if (answer.equals("y")) {
                Messenger.clearConsole();
                db.deleteFromTrash(lUsername, id);
                menu.trashSubmenu(lUsername);
            } else {
                Messenger.clearConsole();
                menu.trashSubmenu(lUsername);
            }
        } else {
            Messenger.clearConsole();
            System.out.println("There is no message with this number.");
            menu.trashSubmenu(lUsername);
        }
    }
}
