import java.io.Console;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class User {
    private long id;
    private String fname, lname, gender, username, password, role;

    User() {
    }

    private User(String fname, String lname, String gender, String username, String password, String role) {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    User(long id, String fname, String lname, String gender, String username, String password, String role) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    long getId() {
        return id;
    }

    String getFname() {
        return fname;
    }

    String getLname() {
        return lname;
    }

    String getGender() {
        return gender;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getRole() {
        return role;
    }

    void register() {
        Login login = new Login();

        String fname, lname, gender, username, password;
        Database db = new Database();
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);

        System.out.println("Thank you for trusting this app! Now I'll ask you some details to create your account.\nTo begin with, tell me your first name:");

        fname = sc.nextLine();
        while (!fname.matches("^[a-zA-Z]+$")) {
            System.out.println("Please type a valid first name:");
            fname = sc.nextLine();
        }

        System.out.println("And your last name:");
        lname = sc.nextLine();
        while (!lname.matches("^[a-zA-Z]+$")) {
            System.out.println("Please type a valid last name:");
            lname = sc.nextLine();
        }

        System.out.println("Now give me your gender. Type \"male\" for male or \"female\" for female:");
        gender = genderEquals(sc);

        System.out.println("Now it's time to create a username. Type anything you want but no more than 8 characters:");
        username = sc.nextLine();
        username = checkUsername(username, db, sc);

        Console console = System.console();
        console.printf("To secure your account, I want a password. You can use any password containing no more than 16 characters:\n");
        char[] passwordChars = console.readPassword();
        password = new String(passwordChars);
        while (password.length() > 16) {
            System.out.println("Please use a password containing no more than 16 characters:");
            passwordChars = console.readPassword();
            password = new String(passwordChars);
        }

        Messenger.clearConsole();
        if (db.createUser(new User(fname, lname, gender, username, password, "user"))) {
            Log log = new Log();
            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - A new user has been registered with username \"" + username + "\".");
            System.out.println("Your majesty is welcome to this messenger! Start chatting!\n");
            menu.loginMenu(username);
        } else {
            System.out.println("Oops. Something happened and I cannot register your account.");
            login.welcomeScreen();
        }
    }

    void createUser(String lUsername) {
        Role checkRole = new Role();
        String fname = null, lname = null, gender, username = null, password = null, role = null;
        Database db = new Database();
        Menu menu = new Menu();
        Scanner sc = new Scanner(System.in);

        if (checkRole.isSAdmin(lUsername)) {
            System.out.println("OK, now tell me if this user is man or woman. Type \"male\" for male or \"female\" for female:");
            gender = genderEquals(sc);

            if (gender.equals("male")) {
                System.out.println("Great! Now tell me his first name:");
                fname = sc.nextLine();
                while (!fname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid first name:");
                    fname = sc.nextLine();
                }

                System.out.println("And his last name:");
                lname = sc.nextLine();
                while (!lname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid last name:");
                    lname = sc.nextLine();
                }

                System.out.println("OK, now give me his username. You can use any username containing no more than 8 characters:");
                username = sc.nextLine();
                username = setUsername(username, db, sc);

                Console console = System.console();
                console.printf("We are approaching the finishing line. Give me his password. You can use any password containing no more than 16 characters:\n");
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                while (password.length() > 16) {
                    System.out.println("Please use a password containing no more than 16 characters:");
                    passwordChars = console.readPassword();
                    password = new String(passwordChars);
                }

                System.out.println("And last but not least, I want to know his role in this app.\nType \"admin\" if " + fname + " is going to be an administrator or \"user\" if he will be a plain user:");
                role = setRole(sc);

            } else if (gender.equals("female")) {
                System.out.println("Great! Now tell me her first name:");
                fname = sc.nextLine();
                while (!fname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid first name:");
                    fname = sc.nextLine();
                }

                System.out.println("And her last name:");
                lname = sc.nextLine();
                while (!lname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid last name:");
                    lname = sc.nextLine();
                }

                System.out.println("OK, now give me her username. You can use any username containing no more than 8 characters:");
                username = sc.nextLine();
                username = setUsername(username, db, sc);

                Console console = System.console();
                console.printf("We are approaching the finishing line. Give me her password. You can use any password containing no more than 16 characters:\n");
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                while (password.length() > 16) {
                    System.out.println("Please use a password containing no more than 16 characters:");
                    passwordChars = console.readPassword();
                    password = new String(passwordChars);
                }

                System.out.println("And last but not least, I want to know her role in this app.\nType \"admin\" if " + fname + " is going to be an administrator or \"user\" if he will be a plain user:");
                role = setRole(sc);
            } else {
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                menu.manageUsersSubmenu(lUsername);
            }
            Messenger.clearConsole();
            if (db.createUser(new User(fname, lname, gender, username, password, role))) {
                Log log = new Log();
                System.out.println("Great! User " + username + " has been created.\n");
                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " created " + db.getRole(username) + " " + username + ".");
            } else {
                System.out.println("Oops! Something happened.");
            }
            menu.manageUsersSubmenu(lUsername);
        } else if (checkRole.isAdmin(lUsername)) {
            System.out.println("OK, now tell me if this user is man or woman. Type \"male\" for male or \"female\" for female:");
            gender = genderEquals(sc);

            if (gender.equals("male")) {
                System.out.println("Great! Now tell me his first name:");
                fname = sc.nextLine();
                while (!fname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid first name:");
                    fname = sc.nextLine();
                }

                System.out.println("And his last name:");
                lname = sc.nextLine();
                while (!lname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid last name:");
                    lname = sc.nextLine();
                }

                System.out.println("OK, now give me his username. You can use any username containing no more than 8 characters:");
                username = sc.nextLine();
                username = setUsername(username, db, sc);

                Console console = System.console();
                console.printf("And last but not least, give me his password. You can use any password containing no more than 16 characters:\n");
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                while (password.length() > 16) {
                    System.out.println("Please use a password containing no more than 16 characters:");
                    passwordChars = console.readPassword();
                    password = new String(passwordChars);
                }
            } else if (gender.equals("female")) {
                System.out.println("Great! Now tell me her first name:");
                fname = sc.nextLine();
                while (!fname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid first name:");
                    fname = sc.nextLine();
                }

                System.out.println("And her last name:");
                lname = sc.nextLine();
                while (!lname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid last name:");
                    lname = sc.nextLine();
                }

                System.out.println("OK, now give me her username. You can use any username containing no more than 8 characters:");
                username = sc.nextLine();
                username = setUsername(username, db, sc);

                Console console = System.console();
                console.printf("And last but not least, give me her password. You can use any password containing no more than 16 characters:\n");
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                while (password.length() > 16) {
                    System.out.println("Please use a password containing no more than 16 characters:");
                    passwordChars = console.readPassword();
                    password = new String(passwordChars);
                }
            } else {
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                menu.manageUsersSubmenu(lUsername);
            }
            Messenger.clearConsole();
            db.createUser(new User(fname, lname, gender, username, password, role));
            menu.manageUsersSubmenu(lUsername);
        } else {
            Messenger.clearConsole();
            System.out.println("Oops! Something happened.");
            menu.manageUsersSubmenu(lUsername);
        }
    }

    void updateUser(String lUsername) {
        Role checkRole = new Role();
        String fname, lname, gender, username, newUsername, role ;
        Database db = new Database();
        Menu menu = new Menu();
        Scanner ssc = new Scanner(System.in);
        Scanner bsc = new Scanner(System.in);

        System.out.println("OK, first of all, give me the username of the user you want to update:");
        username = ssc.nextLine();
        while (username.length() > 8) {
            System.out.println("Please use a username containing no more than 8 characters:");
            username = ssc.nextLine();
        }
        User userToUpdate = db.getUser(username);

        if (db.usernameEquals(username)) {
            if (checkRole.isSAdmin(lUsername)) {
                System.out.println("OK, now tell me what kind of information you want to update for " + username + ". Choose one of the options below:\n1. Username\n2. First name\n3. Last name\n4. Gender\n5. Role");
                while (!bsc.hasNextByte()) {
                    System.out.println("Please input a valid option number:");
                    bsc.next();
                }
                byte choice = bsc.nextByte();
                if (choice < 1 || choice > 5) {
                    Messenger.clearConsole();
                    System.out.println("You chose an invalid option. Please try again.");
                    updateUser(lUsername);
                }

                switch (choice) {
                    case 1:
                        String oldUsernameRole = db.getRole(username);
                        System.out.println("OK, now give me the new username. You can use any username containing no more than 8 characters:");
                        newUsername = ssc.nextLine();
                        newUsername = setUsername(newUsername, db, ssc);
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), userToUpdate.getFname(), userToUpdate.getLname(), userToUpdate.getGender(), newUsername, userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The username of user " + username + " has been updated to " + newUsername + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the username of " + oldUsernameRole + " " + username + " to \"" + newUsername + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 2:
                        System.out.println("OK, now give me the new first name:");
                        fname = ssc.nextLine();
                        while (!fname.matches("^[a-zA-Z]+$")) {
                            System.out.println("Please type a valid first name:");
                            fname = ssc.nextLine();
                        }
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), fname, userToUpdate.getLname(), userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The first name of user " + username + " has been updated to " + fname + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the first name of " + userToUpdate.getRole() + " " + username + " to \"" + fname + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 3:
                        System.out.println("OK, now give me the new last name:");
                        lname = ssc.nextLine();
                        while (!lname.matches("^[a-zA-Z]+$")) {
                            System.out.println("Please type a valid last name:");
                            lname = ssc.nextLine();
                        }
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), userToUpdate.getFname(), lname, userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The lirst name of user " + username + " has been updated to " + lname + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the last name of " + userToUpdate.getRole() + " " + username + " to \"" + lname + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 4:
                        System.out.println("OK, now type the new gender: \"male\" for male or \"female\" for female:");
                        gender = ssc.nextLine();
                        while (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
                            System.out.println("Please type only \"male\" or \"female\":");
                            gender = ssc.nextLine();
                        }
                        if (userToUpdate.getGender().equals(gender)) {
                            Messenger.clearConsole();
                            System.out.println("The gender of " + userToUpdate.getUsername() + " is already set to " + gender + ".");
                            menu.manageUsersSubmenu(lUsername);
                        } else {
                            Messenger.clearConsole();
                            if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), userToUpdate.getFname(), userToUpdate.getFname(), gender, userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                                Log log = new Log();
                                System.out.println("The gender of user " + userToUpdate.getUsername() + " has been updated to " + gender + ".\n");
                                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the gender of " + userToUpdate.getRole() + " " + userToUpdate.getUsername() + " to \"" + userToUpdate.getGender() + "\".");
                            } else {
                                System.out.println("Oops! Something happened.\n");
                            }
                            menu.manageUsersSubmenu(lUsername);
                        }
                        break;
                    case 5:
                        System.out.println("OK, now type the new role: \"administrator\" for administrator or \"user\" for user:");
                        role = ssc.nextLine();
                        while (!(role.equalsIgnoreCase("administrator") || role.equalsIgnoreCase("user"))) {
                            System.out.println("Please type only \"admin\" or \"user\":");
                            role = ssc.nextLine();
                        }
                        if (db.getRole(username).equals(role)) {
                            Messenger.clearConsole();
                            System.out.println("The role of " + username + " is already set to " + role + ".");
                            menu.manageUsersSubmenu(lUsername);
                        } else {
                            Messenger.clearConsole();
                            if (db.updateUser(new User(db.getIdByUsername(username), userToUpdate.getFname(), userToUpdate.getLname(), userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), role))) {
                                Log log = new Log();
                                System.out.println("The role of user " + username + " has been updated to " + role + ".\n");
                                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the role of " + userToUpdate.getRole() + " " + username + " to \"" + role + "\".");
                            } else {
                                System.out.println("Oops! Something happened.\n");
                            }
                            menu.manageUsersSubmenu(lUsername);
                        }
                        break;
                    default:
                        Messenger.clearConsole();
                        System.out.println("Oops! Something happened.");
                        menu.manageUsersSubmenu(lUsername);
                        break;
                }
            } else if (checkRole.isAdmin(lUsername)) {
                System.out.println("OK, now tell me what kind of information you want to update for " + username + ". Choose one of the options below:\n1. Username\n2. First name\n3. Last name\n4. Gender\n");
                while (!bsc.hasNextByte()) {
                    System.out.println("Please input a valid option number:");
                    bsc.next();
                }
                byte choice = bsc.nextByte();
                if (choice < 1 || choice > 4) {
                    Messenger.clearConsole();
                    System.out.println("You chose an invalid option. Please try again.");
                    updateUser(lUsername);
                }

                switch (choice) {
                    case 1:
                        System.out.println("OK, now give me the new username. You can use any username containing no more than 8 characters:");
                        newUsername = ssc.nextLine();
                        newUsername = setUsername(newUsername, db, ssc);
                        String oldUsernameRole = userToUpdate.getRole();
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), userToUpdate.getFname(), userToUpdate.getLname(), userToUpdate.getGender(), newUsername, userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The username of user " + username + " has been updated to " + newUsername + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the username of " + oldUsernameRole + " " + username + " to \"" + newUsername + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 2:
                        System.out.println("OK, now give me the new first name:");
                        fname = ssc.nextLine();
                        while (!fname.matches("^[a-zA-Z]+$")) {
                            System.out.println("Please type a valid first name:");
                            fname = ssc.nextLine();
                        }
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), fname, userToUpdate.getLname(), userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The first name of user " + username + " has been updated to " + fname + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the first name of " + userToUpdate.getRole() + " " + username + " to \"" + fname + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 3:
                        System.out.println("OK, now give me the new last name:");
                        lname = ssc.nextLine();
                        while (!lname.matches("^[a-zA-Z]+$")) {
                            System.out.println("Please type a valid last name:");
                            lname = ssc.nextLine();
                        }
                        Messenger.clearConsole();
                        if (db.updateUser(new User(db.getIdByUsername(username), userToUpdate.getFname(), lname, userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                            Log log = new Log();
                            System.out.println("The last name of user " + username + " has been updated to " + lname + ".\n");
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the last name of " + userToUpdate.getRole() + " " + username + " to \"" + lname + "\".");
                        } else {
                            System.out.println("Oops! Something happened.\n");
                        }
                        menu.manageUsersSubmenu(lUsername);
                        break;
                    case 4:
                        System.out.println("OK, now type the new gender: \"male\" for male or \"female\" for female:");
                        gender = ssc.nextLine();
                        while (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
                            System.out.println("Please type only \"male\" or \"female\":");
                            gender = ssc.nextLine();
                        }
                        if (userToUpdate.getGender().equals(gender)) {
                            Messenger.clearConsole();
                            System.out.println("The gender of " + userToUpdate.getUsername() + " is already set to " + gender + ".");
                            menu.manageUsersSubmenu(lUsername);
                        } else {
                            Messenger.clearConsole();
                            if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), userToUpdate.getFname(), userToUpdate.getFname(), gender, userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                                Log log = new Log();
                                System.out.println("The gender of user " + userToUpdate.getUsername() + " has been updated to " + gender + ".\n");
                                log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + db.getRole(lUsername).substring(0, 1).toUpperCase() + db.getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " changed the gender of " + userToUpdate.getRole() + " " + userToUpdate.getUsername() + " to \"" + userToUpdate.getGender() + "\".");
                            } else {
                                System.out.println("Oops! Something happened.\n");
                            }
                            menu.manageUsersSubmenu(lUsername);
                        }
                        break;
                    default:
                        Messenger.clearConsole();
                        System.out.println("Oops! Something happened.");
                        menu.manageUsersSubmenu(lUsername);
                        break;
                }
            } else {
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                menu.manageUsersSubmenu(lUsername);
            }
        } else if (db.usernameEquals(lUsername)) { // in case the (super) administrator inputs his/her username
            Messenger.clearConsole();
            System.out.println("You have chosen to update your personal information. You can do it here.");
            updateProfile(lUsername);
        } else {
            Messenger.clearConsole();
            System.out.println("\nThere is no user with this username.");
            menu.manageUsersSubmenu(lUsername);
        }
    }

    void updateProfile(String lUsername) {
        Database db = new Database();
        User userToUpdate = db.getUser(lUsername);
        Role checkRole = new Role();
        String fname, lname, gender;
        Menu menu = new Menu();
        Scanner bsc = new Scanner(System.in);
        Scanner ssc = new Scanner(System.in);

        if (checkRole.isSAdmin(lUsername)) {
            System.out.println("OK, now tell me what kind of information you want to update for yourself. Choose one of the options below:\n1. Password\n2. First name\n3. Last name\n4. Gender\n5. Back");
        } else if (checkRole.isAdmin(lUsername)) {
            System.out.println("OK, now tell me what kind of information you want to update for yourself. Choose one of the options below:\n(To change your username you must contact the super administrator.)\n1. Password\n2. First name\n3. Last name\n4. Gender\n5. Back");
        } else if (checkRole.isUser(lUsername)) {
            System.out.println("OK, now tell me what kind of information you want to update for yourself. Choose one of the options below:\n(To change your username you must contact the administrators.)\n1. Password\n2. First name\n3. Last name\n4. Gender\n5. Back");
        } else {
            Messenger.clearConsole();
            System.out.println("Oops! Something happened.");
            menu.loginMenu(lUsername);
        }

        while (!bsc.hasNextByte()) {
            System.out.println("Please input a valid option number:");
            bsc.next();
        }
        byte choice = bsc.nextByte();
        if (choice < 1 || choice > 5) {
            Messenger.clearConsole();
            System.out.println("You chose an invalid option. Please try again.");
            updateProfile(lUsername);
        }

        switch (choice) {
            case 1:
                Console console = System.console();
                console.printf("Well, give me your new password. You can use any password containing no more than 16 characters:\n");
                char[] passwordChars = console.readPassword();
                String password = new String(passwordChars);
                while (password.length() > 16) {
                    System.out.println("Please use a password containing no more than 16 characters:");
                    passwordChars = console.readPassword();
                    password = new String(passwordChars);
                }
                Messenger.clearConsole();
                if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), userToUpdate.getFname(), userToUpdate.getLname(), userToUpdate.getGender(), userToUpdate.getUsername(), password, userToUpdate.getRole()))) {
                    System.out.println("The password of user " + userToUpdate.getUsername() + " has been changed.\n");
                    Log log = new Log();
                    if (userToUpdate.getGender().equals("male")) {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed his password.");
                    } else {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed her password.");
                    }
                } else {
                    System.out.println("Oops! Something happened.\n");
                }
                menu.loginMenu(lUsername);
                break;
            case 2:
                System.out.println("Well, alter your first name here:");
                fname = ssc.nextLine();
                while (!fname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid first name:");
                    fname = ssc.nextLine();
                }
                Messenger.clearConsole();
                if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), fname, userToUpdate.getLname(), userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                    System.out.println("Your first name has been changed.\n");
                    Log log = new Log();
                    if (userToUpdate.getGender().equals("male")) {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed his first name to " + fname + ".");
                    } else {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed her first name to " + fname + ".");
                    }
                } else {
                    System.out.println("Oops! Something happened.\n");
                }
                menu.loginMenu(lUsername);
                break;
            case 3:
                System.out.println("Well, alter your last name here:");
                lname = ssc.nextLine();
                while (!lname.matches("^[a-zA-Z]+$")) {
                    System.out.println("Please type a valid last name:");
                    lname = ssc.nextLine();
                }
                Messenger.clearConsole();
                if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), userToUpdate.getFname(), lname, userToUpdate.getGender(), userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                    System.out.println("Your last name has been changed.\n");
                    Log log = new Log();
                    if (userToUpdate.getGender().equals("male")) {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed his last name to " + lname + ".");
                    } else {
                        log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed her last name to " + lname + ".");
                    }
                } else {
                    System.out.println("Oops! Something happened.\n");
                }
                menu.loginMenu(lUsername);
                break;
            case 4:
                System.out.println("Well, alter your gender here. Type \"male\" for male or \"female\" for female:");
                gender = ssc.nextLine();
                while (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
                    System.out.println("Please type only \"male\" or \"female\":");
                    gender = ssc.nextLine();
                }
                String oldGender = userToUpdate.getGender();
                if (userToUpdate.getGender().equals(gender)) {
                    Messenger.clearConsole();
                    System.out.println("Your gender is already set to " + gender + ".");
                    menu.loginMenu(lUsername);
                } else {
                    Messenger.clearConsole();
                    if (db.updateUser(new User(db.getIdByUsername(userToUpdate.getUsername()), userToUpdate.getFname(), userToUpdate.getLname(), gender, userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getRole()))) {
                        System.out.println("Your gender has been changed.\n");
                        Log log = new Log();
                        if (oldGender.equals("male")) {
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed his gender to " + gender + ".");
                        } else {
                            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + userToUpdate.getRole().substring(0, 1).toUpperCase() + userToUpdate.getRole().substring(1).toLowerCase() + " " + lUsername + " changed her gender to " + gender + ".");
                        }
                    } else {
                        System.out.println("Oops! Something happened.\n");
                    }
                    menu.loginMenu(lUsername);
                }
                break;
            case 5:
                Messenger.clearConsole();
                menu.loginMenu(lUsername);
                break;
            default:
                Messenger.clearConsole();
                System.out.println("Oops! Something happened.");
                menu.loginMenu(lUsername);
        }
    }

    void deleteUser(String lUsername) {
        Role checkRole = new Role();
        Scanner sc = new Scanner(System.in);
        String username;
        Database db = new Database();
        Menu menu = new Menu();

        System.out.println("OK, now give me the username the account of which will be deleted:");
        username = sc.nextLine();
        while (username.length() > 8) {
            System.out.println("Please enter a username containing no more than 8 characters:");
            username = sc.nextLine();
        }
        if (db.getUser(username).getUsername().equals(username)) {
            String role = db.getRole(username);
            if (checkRole.isAdmin(lUsername) && checkRole.isAdmin(username)) {
                Messenger.clearConsole();
                System.out.println("You do not have permission for this operation.");
                menu.manageUsersSubmenu(lUsername);
            } else if (checkRole.isSAdmin(lUsername) && checkRole.isSAdmin(username)) {
                Messenger.clearConsole();
                System.out.println("You cannot delete yourself. The systems needs at least one super administrator.");
                menu.manageUsersSubmenu(lUsername);
            } else if (checkRole.isSAdmin(lUsername) && checkRole.isAdmin(username)) {
                verifyDelete(lUsername, role, sc, username, db, menu);
            } else if (checkRole.isUser(username)) {
                verifyDelete(lUsername, role, sc, username, db, menu);
            } else {
                System.out.println("Oops! Something happened.");
            }
        } else {
            Messenger.clearConsole();
            System.out.println("There is no user with this username.");
            menu.manageUsersSubmenu(lUsername);
        }
    }

    private void verifyDelete(String lUsername, String role, Scanner sc, String username, Database db, Menu menu) {
        System.out.println("Are you sure you want to delete " + username + "? (y/n)");
        String answer = sc.nextLine();
        while (!(answer.equals("y") || answer.equals("n"))) {
            System.out.println("Please enter \"y\" for yes or \"no\" for no:");
            answer = sc.nextLine();
        }
        if (answer.equals("y")) {
            Messenger.clearConsole();
            db.deleteUser(lUsername, username, role);
            menu.manageUsersSubmenu(lUsername);
        } else {
            menu.manageUsersSubmenu(lUsername);
        }
    }

    private String genderEquals(Scanner sc) {
        String gender = sc.nextLine();
        while(!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
            System.out.println("Please type only \"male\" or \"female\":");
            gender = sc.nextLine();
        }
        return gender;
    }

    private String setRole(Scanner sc) {
        String role = sc.nextLine();
        while (!(role.equalsIgnoreCase("administrator") || role.equalsIgnoreCase("user"))) {
            System.out.println("Please type \"administrator\" for administrator or \"user\" for plain user:");
            role = sc.nextLine();
        }
        return role;
    }

    private String checkUsername(String username, Database db, Scanner sc) {
        while (username.length() > 8 || db.usernameEquals(username)) {
            if (username.length() > 8) {
                System.out.println("Please use a username containing no more than 8 characters:");
                username = sc.nextLine();
            }
            if (db.usernameEquals(username)) {
                System.out.println("Oh no, someone is already using this app with this username. Please try with another username:");
                username = sc.nextLine();
            }
        }
        return username;
    }

    private String setUsername(String newUsername, Database db, Scanner sc) {
        newUsername = checkUsername(newUsername, db, sc);
        return newUsername;
    }
}