import java.util.Scanner;

class Menu {
    void loginMenu(String lUsername) {
        Database db = new Database();
        Login login = new Login();
        User user = new User();
        Menu menu = new Menu();
        Message message = new Message();
        Scanner sc = new Scanner(System.in);

        switch (db.getRole(lUsername)) {
            case "super administrator": {
                System.out.println("=============\n     MENU\n=============\n\nChoose one from the following options:\n1. Create message\n2. Inbox\n3. Sent\n4. Trash\n5. Manage users\n6. View all users' messages\n7. Update profile\n8. Logout\n");
                while (!sc.hasNextByte()) {
                    System.out.println("Please input a valid option number:");
                    sc.next();
                }
                byte choice = sc.nextByte();
                if (choice < 1 || choice > 8) {
                    Messenger.clearConsole();
                    System.out.println("You chose an invalid option. Please try again.");
                    loginMenu(lUsername);
                }

                switch (choice) {
                    case 1:
                        Messenger.clearConsole();
                        message.createMessage(lUsername);
                        break;
                    case 2:
                        Messenger.clearConsole();
                        inboxSubmenu(lUsername);
                        break;
                    case 3:
                        Messenger.clearConsole();
                        sentSubmenu(lUsername);
                        break;
                    case 4:
                        Messenger.clearConsole();
                        menu.trashSubmenu(lUsername);
                        break;
                    case 5:
                        Messenger.clearConsole();
                        manageUsersSubmenu(lUsername);
                        break;
                    case 6:
                        Messenger.clearConsole();
                        db.viewAllMessages(lUsername);
                        loginMenu(lUsername);
                    break;
                    case 7:
                        Messenger.clearConsole();
                        user.updateProfile(lUsername);
                        break;
                    case 8:
                        Messenger.clearConsole();
                        login.logout(lUsername);
                        break;
                    default:
                        Messenger.clearConsole();
                        System.out.println("Oops! Something happened.");
                        loginMenu(lUsername);
                } break;
            }
            case "administrator": {
                System.out.println("==============\n     MENU\n==============\n\nChoose one from the following options:\n1. Create message\n2. Inbox\n3. Sent\n4. Trash\n5. Manage users\n6. View all users' messages\n7. Update profile\n8. Logout\n");
                while (!sc.hasNextByte()) {
                    System.out.println("Please input a valid option number:");
                    sc.next();
                }
                byte choice = sc.nextByte();
                if (choice < 1 || choice > 8) {
                    Messenger.clearConsole();
                    System.out.println("You chose an invalid option. Please try again.");
                    loginMenu(lUsername);
                }

                switch (choice) {
                    case 1:
                        Messenger.clearConsole();
                        message.createMessage(lUsername);
                        break;
                    case 2:
                        Messenger.clearConsole();
                        inboxSubmenu(lUsername);
                        break;
                    case 3:
                        Messenger.clearConsole();
                        sentSubmenu(lUsername);
                        break;
                    case 4:
                        Messenger.clearConsole();
                        trashSubmenu(lUsername);
                        break;
                    case 5:
                        Messenger.clearConsole();
                        manageUsersSubmenu(lUsername);
                        break;
                    case 6:
                        Messenger.clearConsole();
                        db.viewAllMessages(lUsername);
                        loginMenu(lUsername);
                        break;
                    case 7:
                        Messenger.clearConsole();
                        user.updateProfile(lUsername);
                        break;
                    case 8:
                        Messenger.clearConsole();
                        login.logout(lUsername);
                        break;
                    default:
                        Messenger.clearConsole();
                        System.out.println("Oops! Something happened.");
                        loginMenu(lUsername);
                } break;
            }
            case "user": {
                System.out.println("==============\n     MENU\n==============\n\nChoose one from the following options:\n1. Create message\n2. Inbox\n3. Sent\n4. Trash\n5. Update profile\n6. Logout");
                while (!sc.hasNextByte()) {
                    System.out.println("Please input a valid option number:");
                    sc.next();
                }
                byte choice = sc.nextByte();
                if (choice < 1 || choice > 6) {
                    Messenger.clearConsole();
                    System.out.println("You chose an invalid option. Please try again.");
                    loginMenu(lUsername);
                }

                switch (choice) {
                    case 1:
                        Messenger.clearConsole();
                        message.createMessage(lUsername);
                        break;
                    case 2:
                        Messenger.clearConsole();
                        inboxSubmenu(lUsername);
                        break;
                    case 3:
                        Messenger.clearConsole();
                        sentSubmenu(lUsername);
                        break;
                    case 4:
                        Messenger.clearConsole();
                        trashSubmenu(lUsername);
                        break;
                    case 5:
                        Messenger.clearConsole();
                        user.updateProfile(lUsername);
                        break;
                    case 6:
                        Messenger.clearConsole();
                        login.logout(lUsername);
                        break;
                    default:
                        Messenger.clearConsole();
                        System.out.println("Oops! Something happened.");
                        loginMenu(lUsername);
                } break;
            }
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                login.welcomeScreen();
                break;
        }
    }

    void inboxSubmenu(String lUsername) {
        Database db = new Database();
        Message message = new Message();
        Scanner sc = new Scanner(System.in);

        db.viewInbox(lUsername);
        System.out.println("==============\n    Inbox\n==============\n\nChoose one from the following options:\n1. Delete a message\n2. Delete all messages\n3. Back\n");
        while (!sc.hasNextByte()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        byte choice = sc.nextByte();
        if (choice < 1 || choice > 3) {
            Messenger.clearConsole();
            System.out.println("You chose an invalid option. Please try again.");
            inboxSubmenu(lUsername);
        }

        switch (choice) {
            case 1:
                Messenger.clearConsole();
                message.deleteFromInbox(lUsername);
                inboxSubmenu(lUsername);
                break;
            case 2:
                Messenger.clearConsole();
                db.deleteAllFromInbox(lUsername);
                loginMenu(lUsername);
                break;
            case 3:
                Messenger.clearConsole();
                loginMenu(lUsername);
                break;
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                loginMenu(lUsername);
        }
    }

    void sentSubmenu(String lUsername) {
        Database db = new Database();
        Message message = new Message();
        Scanner sc = new Scanner(System.in);

        db.viewSent(lUsername);
        System.out.println("==============\n     Sent\n==============\n\nChoose one from the following options:\n1. Edit a sent message\n2. Delete a message\n3. Delete all messages\n4. Back\n");
        while (!sc.hasNextByte()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        byte choice = sc.nextByte();
        if (choice < 1 || choice > 4) {
            Messenger.clearConsole();
            System.out.println("You choose an invalid option. Please try again.");
            sentSubmenu(lUsername);
        }

        switch (choice) {
            case 1:
                Messenger.clearConsole();
                message.editSentMessage(lUsername);
                break;
            case 2:
                Messenger.clearConsole();
                message.deleteFromSent(lUsername);
            case 3:
                Messenger.clearConsole();
                db.deleteAllFromSent(lUsername);
                loginMenu(lUsername);
                break;
            case 4:
                Messenger.clearConsole();
                loginMenu(lUsername);
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                loginMenu(lUsername);
        }
    }

    void trashSubmenu(String lUsername) {
        Database db = new Database();
        Message message = new Message();
        Scanner sc = new Scanner(System.in);

        db.viewTrash(lUsername);
        System.out.println("==============\n     Trash\n==============\n\nChoose one from the following options:\n1. Delete a message\n2. Empty trash\n3. Back\n");
        while (!sc.hasNextByte()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        byte choice = sc.nextByte();
        if (choice < 1 || choice > 3) {
            Messenger.clearConsole();
            System.out.println("You chose an invalid option. Please try again.");
            trashSubmenu(lUsername);
        }

        switch (choice) {
            case 1:
                Messenger.clearConsole();
                message.deleteFromTrash(lUsername);
                break;
            case 2:
                Messenger.clearConsole();
                db.deleteAllFromTrash(lUsername);
                loginMenu(lUsername);
                break;
            case 3:
                Messenger.clearConsole();
                loginMenu(lUsername);
                break;
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                loginMenu(lUsername);
        }
    }

    void manageUsersSubmenu(String lUsername) {
        Database db = new Database();
        User user = new User();
        Scanner sc = new Scanner(System.in);

        db.viewAllUsers(lUsername);
        System.out.println("==============\n Manage users\n==============\n\nChoose one from the following options:\n1. Create user\n2. Update user\n3. Delete user\n4. Back");
        while (!sc.hasNextByte()) {
            System.out.println("Please input a valid message number:");
            sc.next();
        }
        byte choice = sc.nextByte();
        if (choice < 1 || choice > 4) {
            Messenger.clearConsole();
            System.out.println("You chose an invalid option. Please try again.");
            manageUsersSubmenu(lUsername);
        }

        switch (choice) {
            case 1:
                Messenger.clearConsole();
                user.createUser(lUsername);
                break;
            case 2:
                Messenger.clearConsole();
                db.viewAllUsers(lUsername);
                user.updateUser(lUsername);
                break;
            case 3:
                Messenger.clearConsole();
                db.viewAllUsers(lUsername);
                user.deleteUser(lUsername);
                break;
            case 4:
                Messenger.clearConsole();
                loginMenu(lUsername);
                break;
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                loginMenu(lUsername);
        }
    }
}