import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

class Database {
    private static final String DB_URL = "localhost:3306";
    private static final String DB_USER = "root";
    private static final String DB_PASSWD = "root";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "?zeroDateTimeBehavior=convertToNull&serverTimezone=Europe/Athens&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true";

    boolean connectDB() {
        Connection con;

//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("MySQL JDBC Driver has not been imported.\n");
//            e.printStackTrace();
//            return false;
//        }

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (con != null) {
            PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null, stmt6 = null, stmt7 = null, stmt8 = null, stmt9 = null, stmt10 = null, stmt11 = null, stmt12 = null;
            ResultSet rs = null;

            try {
                con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                stmt1 = con.prepareStatement("SHOW DATABASES LIKE ?;");
                stmt1.setString(1, "messenger");
                rs = stmt1.executeQuery();
                if (rs.first() && rs.getString(1).equals("messenger")) {
                    return true;
                } else {
                    System.out.println("Creating database... ");
                    stmt2 = con.prepareStatement("CREATE DATABASE IF NOT EXISTS `messenger`;");
                    int rows1 = stmt2.executeUpdate();
                    stmt3 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`users` (`id` INT NOT NULL AUTO_INCREMENT, `fname` VARCHAR(20) NOT NULL, `lname` VARCHAR(45) NOT NULL, `gender` ENUM('male', 'female'), `username` VARCHAR(8) NOT NULL, `password` VARCHAR(16) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY (`id`), UNIQUE KEY (`username`));");
                    int rows2 = stmt3.executeUpdate();
                    stmt4 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`roles` (`id` INT NOT NULL AUTO_INCREMENT, `users_id` INT NOT NULL, `role` ENUM('sadmin', 'administrator', 'user'), FOREIGN KEY (`users_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, PRIMARY KEY (`id`), UNIQUE KEY (`id`), UNIQUE KEY (`users_id`));");
                    int rows3 = stmt4.executeUpdate();
                    stmt5 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`messages` (`id` INT NOT NULL AUTO_INCREMENT, `datetime` DATETIME NOT NULL, `from_id` INT NOT NULL, `to_id` INT NOT NULL, `text` VARCHAR(260), FOREIGN KEY (`from_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, FOREIGN KEY (`to_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, PRIMARY KEY (`id`), UNIQUE KEY (`id`));");
                    int rows4 = stmt5.executeUpdate();
                    stmt6 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`inbox` (`id` INT NOT NULL AUTO_INCREMENT, `users_id` INT NOT NULL, `datetime` DATETIME NOT NULL, `from_id` INT NOT NULL, `text` VARCHAR(260), PRIMARY KEY (`id`), UNIQUE KEY (`id`), FOREIGN KEY (`users_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, FOREIGN KEY (`from_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE);");
                    int rows5 = stmt6.executeUpdate();
                    stmt7 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`sent` (`id` INT NOT NULL AUTO_INCREMENT, `users_id` INT NOT NULL, `datetime` DATETIME NOT NULL, `to_id` INT NOT NULL, `text` VARCHAR(260), PRIMARY KEY (`id`), UNIQUE KEY (`id`), FOREIGN KEY (`users_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, FOREIGN KEY (`to_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE);");
                    int rows6 = stmt7.executeUpdate();
                    stmt8 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `messenger`.`trash` (`id` INT NOT NULL AUTO_INCREMENT, `users_id` INT NOT NULL, `datetime` DATETIME NOT NULL, `from_id` INT, `to_id` INT, `text` VARCHAR(260), PRIMARY KEY (`id`), UNIQUE KEY (`id`), FOREIGN KEY (`users_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, FOREIGN KEY (`from_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE, FOREIGN KEY (`to_id`) REFERENCES `messenger`.`users` (`id`) ON DELETE CASCADE);");
                    int rows7 = stmt8.executeUpdate();
                    stmt9 = con.prepareStatement("INSERT IGNORE INTO `messenger`.`users` (`fname`, `lname`, `gender`, `username`, `password`) VALUES (?, ?, ?, ?, ?);");
                    stmt9.setString(1, "Nikos");
                    stmt9.setString(2, "Delistavrou");
                    stmt9.setString(3, "male");
                    stmt9.setString(4, "sadmin");
                    stmt9.setString(5, "sadmin");
                    int rows8 = stmt9.executeUpdate();
                    stmt10 = con.prepareStatement("INSERT IGNORE INTO `messenger`.`roles` (`users_id`, `role`) VALUES (?, ?);");
                    stmt10.setLong(1, getIdByUsername("sadmin"));
                    stmt10.setString(2, "sadmin");
                    int rows9 = stmt10.executeUpdate();
                    stmt11 = con.prepareStatement("CREATE TRIGGER `messenger`.`delete_inbox` BEFORE DELETE ON `messenger`.`inbox` FOR EACH ROW BEGIN INSERT INTO `messenger`.`trash` (`users_id`, `datetime`, `from_id`, `text`) VALUES (OLD.`users_id`, OLD.`datetime`, OLD.`from_id`, OLD.`text`); END;");
                    int rows10 = stmt11.executeUpdate();
                    stmt12 = con.prepareStatement("CREATE TRIGGER `messenger`.`delete_sent` BEFORE DELETE ON `messenger`.`sent`  FOR EACH ROW BEGIN INSERT INTO `messenger`.`trash` (`users_id`, `datetime`, `to_id`, `text`) VALUES (OLD.`users_id`, OLD.`datetime`, OLD.`to_id`, OLD.`text`); END;");
                    int rows11 = stmt12.executeUpdate();
                    if (rows1 + rows2 + rows3 + rows4 + rows5 + rows6 + rows7 + rows8 + rows9 + rows10 + rows11 > 0) {
                        System.out.println("The database has been created successfully.\n");
                        return true;
                    } else {
                        System.out.println("Database creation failed.\n");
                        return false;
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt12 != null) {
                        stmt12.close();
                    }
                    if (stmt11 != null) {
                        stmt11.close();
                    }
                    if (stmt10 != null) {
                        stmt10.close();
                    }
                    if (stmt9 != null) {
                        stmt9.close();
                    }
                    if (stmt8 != null) {
                        stmt8.close();
                    }
                    if (stmt7 != null) {
                        stmt7.close();
                    }
                    if (stmt6 != null) {
                        stmt6.close();
                    }
                    if (stmt5 != null) {
                        stmt5.close();
                    }
                    if (stmt4 != null) {
                        stmt4.close();
                    }
                    if (stmt3 != null) {
                        stmt3.close();
                    }
                    if (stmt2 != null) {
                        stmt2.close();
                    }
                    closeConnection5(con, stmt1, rs);
                } catch (SQLException e) {
                    System.out.println("Oops! Something happened.\n");
                }
            }
        } else {
            System.out.println("Connection failed.\n");
        } return false;
    }

    boolean testConnection() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT `users`.`fname`, `users`.`lname` FROM `messenger`.`users`;");
            rs = stmt.executeQuery();
            if (rs.first()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection5(con, stmt, rs);
            } catch (SQLException | NullPointerException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return false;
    }

    boolean checkCredentials(String username, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT * FROM `messenger`.`users` WHERE `username` = ? AND `password` = ?;");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            return rs.first() && rs.getString("username").equals(username) && rs.getString("password").equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
        return false;
    }

    boolean createUser(User user) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT `username` FROM `messenger`.`users` WHERE `username` = ?;");
            stmt1.setString(1, user.getUsername());
            rs = stmt1.executeQuery();
            if (rs.first() && rs.getString("username").equals(user.getUsername())) {
                System.out.println("\nThere is already a user with this username. Please try with another username.");
            } else {
                stmt2 = con.prepareStatement("INSERT INTO `messenger`.`users` (`fname`, `lname`, `gender`, `username`, `password`) VALUES (?, ?, ?, ?, ?);");
                stmt2.setString(1, user.getFname());
                stmt2.setString(2, user.getLname());
                stmt2.setString(3, user.getGender());
                stmt2.setString(4, user.getUsername());
                stmt2.setString(5, user.getPassword());
                int rows1 = stmt2.executeUpdate();
                stmt3 = con.prepareStatement("INSERT INTO `messenger`.`roles` (`users_id`, `role`) VALUES (?, ?);");
                stmt3.setLong(1, getIdByUsername(user.getUsername()));
                stmt3.setString(2, user.getRole());
                int rows2 = stmt3.executeUpdate();
                return rows1 + rows2 == 2;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection4(con, stmt1, stmt2, stmt3, rs);
            } catch (SQLException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return false;
    }

    void deleteUser(String lUsername, String username, String role) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT * FROM `messenger`.`users` WHERE `username` = ?;");
            stmt1.setString(1, username);
            rs = stmt1.executeQuery();
            if (rs.first() && rs.getString("username").equals(username)) {
                stmt2 = con.prepareStatement("DELETE FROM `messenger`.`roles` WHERE `users_id` = ?;");
                stmt2.setLong(1, getIdByUsername(username));
                int rows1 = stmt2.executeUpdate();
                stmt3 = con.prepareStatement("DELETE FROM `messenger`.`users` WHERE `username` = ?;");
                stmt3.setString(1, username);
                int rows2 = stmt3.executeUpdate();
                if (rows1 + rows2 > 0) {
                    Log log = new Log();
                    System.out.println("User " + username + " has been deleted.\n");
                    log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + getRole(lUsername).substring(0, 1).toUpperCase() + getRole(lUsername).substring(1).toLowerCase() + " " + lUsername + " deleted " + role + " " + username + ".");
                } else {
                    System.out.println("Oops! Something happened.\n");
                }
            } else {
                System.out.println("\nThere is no user with username " + username + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection4(con, stmt1, stmt2, stmt3, rs);
            } catch (SQLException | NullPointerException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
    }

    boolean updateUser(User user) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT `username` FROM `messenger`.`users` WHERE `username` = ?;");
            stmt1.setString(1, getUsernameById(user.getId()));
            rs = stmt1.executeQuery();
            if (rs.first() && rs.getString("username").equals(getUsernameById(user.getId()))) {
                stmt2 = con.prepareStatement("UPDATE `messenger`.`users` SET `fname` = ?, `lname` = ?, `gender` = ?, `username` = ?, `password` = ? WHERE `users`.`id` = ?;");
                stmt2.setString(1, user.getFname());
                stmt2.setString(2, user.getLname());
                stmt2.setString(3, user.getGender());
                stmt2.setString(4, user.getUsername());
                stmt2.setString(5, user.getPassword());
                stmt2.setLong(6, user.getId());
                int rows1 = stmt2.executeUpdate();
                stmt3 = con.prepareStatement("UPDATE `messenger`.`roles` SET `role` = ? WHERE `roles`.`users_id` = ?;");
                stmt3.setString(1, user.getRole());
                stmt3.setLong(2, getIdByUsername(user.getUsername()));
                int rows2 = stmt3.executeUpdate();
                return rows1 + rows2 > 0;
            } else {
                System.out.println("\nThere is no user with username " + getUsernameById(user.getId()) + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection4(con, stmt1, stmt2, stmt3, rs);
            } catch (SQLException | NullPointerException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return false;
    }

    User getUser(String username) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT * FROM `messenger`.`users` WHERE `id` = ?;");
            stmt1.setLong(1, getIdByUsername(username));
            rs1 = stmt1.executeQuery();
            stmt2 = con.prepareStatement("SELECT `role` FROM `messenger`.`roles` INNER JOIN `messenger`.`users` ON `messenger`.`roles`.`users_id` = `messenger`.`users`.`id` WHERE `users`.`username` = ?;");
            stmt2.setString(1, username);
            rs2 = stmt2.executeQuery();
            if (rs1.first() && rs2.first()) {
                return new User(rs1.getLong("id"), rs1.getString("fname"), rs1.getString("lname"), rs1.getString("gender"), rs1.getString("username"), rs1.getString("password"), rs2.getString("role"));
            } else {
                System.out.println("Oops! Something happened.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return null;
    }

    boolean userExists(String username) {
        Connection con = null;
        PreparedStatement stmt = null, stmt2 = null;
        ResultSet rs = null, rs2 = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT `username` FROM `messenger`.`users` WHERE `username` = ?;");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            return rs.first();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return false;
    }

    boolean createMessage(Message message) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("INSERT INTO `messenger`.`messages` (`datetime`, `from_id`, `to_id`, `text`) VALUES (?, ?, ?, ?);");
            stmt1.setString(1, message.getDatetime());
            stmt1.setLong(2, message.getFrom_id());
            stmt1.setLong(3, message.getTo_id());
            stmt1.setString(4, message.getText());
            int rows1 = stmt1.executeUpdate();
            stmt2 = con.prepareStatement("INSERT INTO `messenger`.`sent` (`users_id`, `datetime`, `to_id`, `text`) VALUES (?, ?, ?, ?);");
            stmt2.setLong(1, message.getFrom_id());
            stmt2.setString(2, message.getDatetime());
            stmt2.setLong(3, message.getTo_id());
            stmt2.setString(4, message.getText());
            int rows2 = stmt2.executeUpdate();
            stmt3 = con.prepareStatement("INSERT INTO `messenger`.`inbox` (`users_id`, `datetime`, `from_id`, `text`) VALUES (?, ?, ?, ?);");
            stmt3.setLong(1, message.getTo_id());
            stmt3.setString(2, message.getDatetime());
            stmt3.setLong(3, message.getFrom_id());
            stmt3.setString(4, message.getText());
            int rows3 = stmt3.executeUpdate();
            if (rows1 + rows2 + rows3 == 3) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt3 != null) {
                    stmt3.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException | NullPointerException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
        return false;
    }

    void editSentMessage(String username, long id, String text) {
        Connection con = null;
        PreparedStatement stmt1 = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null, stmt6 = null;
        ResultSet rs1 = null, rs2 = null, rs3 = null;
        String newText;

        if (text.length() > 10) {
            if (!text.substring(0, 9).contains("(Updated) ")) {
                newText = "(Updated) " + text;
            } else {
                newText = text;
            }
        } else {
            newText = "(Updated) " + text;
        }

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `to_id` FROM `messenger`.`sent` JOIN (SELECT @a:= 0) r WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt1.setLong(1, getIdByUsername(username));
            stmt1.setLong(2, id - 1);
            rs1 = stmt1.executeQuery();
            if (rs1.first()) {
                stmt2 = con.prepareStatement("SELECT `sent`.`id` AS `sent_id`, `messages`.`id` AS `messages_id`, `inbox`.`id` AS `inbox_id` FROM `messenger`.`sent` INNER JOIN `messenger`.`messages` INNER JOIN `messenger`.`inbox` WHERE `messenger`.`messages`.`text` = ? AND `messenger`.`sent`.`datetime` = `messenger`.`messages`.`datetime` AND `messenger`.`inbox`.`datetime` = `messenger`.`messages`.`datetime` AND `messenger`.`messages`.`from_id` = ? AND `messenger`.`messages`.`to_id` = ? LIMIT 1;");
                stmt2.setString(1, getTextFromSentNo(username, id - 1));
                stmt2.setLong(2, getIdByUsername(username));
                stmt2.setLong(3, rs1.getInt("to_id"));
                rs2 = stmt2.executeQuery();
                if (rs2.first()) { // in case the recipient has deleted the message to be updated from his/her inbox
                    stmt3 = con.prepareStatement("UPDATE `messenger`.`sent` SET `text` = ? WHERE `id` = ?;");
                    stmt3.setString(1, newText);
                    stmt3.setLong(2, rs2.getInt("sent_id"));
                    int rows1 = stmt3.executeUpdate();
                    stmt4 = con.prepareStatement("UPDATE `messenger`.`messages` SET `text` = ? WHERE `id` = ?;");
                    stmt4.setString(1, newText);
                    stmt4.setLong(2, rs2.getInt("messages_id"));
                    int rows2 = stmt4.executeUpdate();
                    stmt5 = con.prepareStatement("UPDATE `messenger`.`inbox` SET `text` = ? WHERE `id` = ?;");
                    stmt5.setString(1, newText);
                    stmt5.setLong(2, rs2.getInt("inbox_id"));
                    logOnEditMessage(username, id, text, stmt5, rs1, rows1, rows2);
                } else {
                    stmt3 = con.prepareStatement("INSERT INTO `messenger`.`inbox` (`users_id`, `datetime`, `from_id`, `text`) VALUES (?, ?, ?, ?);");
                    stmt3.setLong(1, rs1.getInt("to_id"));
                    stmt3.setString(2, getDateByMessageNo(username, id));
                    stmt3.setLong(3, getIdByUsername(username));
                    stmt3.setString(4, newText);
                    int rows1 = stmt3.executeUpdate();
                    stmt4 = con.prepareStatement("SELECT `sent`.`id` AS `sent_id`, `messages`.`id` AS `messages_id`  FROM `messenger`.`sent` INNER JOIN `messenger`.`messages` WHERE `messenger`.`messages`.`text` = ? AND `messenger`.`sent`.`datetime` = `messenger`.`messages`.`datetime` AND `messenger`.`messages`.`from_id` = ? AND `messenger`.`messages`.`to_id` = ? LIMIT 1;");
                    stmt4.setString(1, getTextFromSentNo(username, id - 1));
                    stmt4.setLong(2, getIdByUsername(username));
                    stmt4.setLong(3, rs1.getInt("to_id"));
                    rs3 = stmt4.executeQuery();
                    if (rs3.first()) {
                        stmt5 = con.prepareStatement("UPDATE `messenger`.`messages` SET `text` = ? WHERE `id` = ?;");
                        stmt5.setString(1, newText);
                        stmt5.setLong(2, rs2.getInt("messages_id"));
                        int rows2 = stmt5.executeUpdate();
                        stmt6 = con.prepareStatement("UPDATE `messenger`.`sent` SET `text` = ? WHERE `id` = ?;");
                        stmt6.setString(1, newText);
                        stmt6.setLong(2, rs2.getInt("sent_id"));
                        logOnEditMessage(username, id, text, stmt6, rs1, rows1, rows2);
                    } else {
                        System.out.println("Oops! Something happened.");
                    }
                }
            } else {
                System.out.println("\nThere is no message with number " + id + ".\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt6 != null) {
                    stmt6.close();
                }
                if (stmt5 != null) {
                    stmt5.close();
                }
                if (rs3 != null) {
                    rs3.close();
                }
                if (stmt4 != null) {
                    stmt4.close();
                }
                if (stmt3 != null) {
                    stmt3.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                closeConnection5(con, stmt1, rs1);
            } catch (SQLException | NullPointerException e) {
                System.out.println("Oops! Something happened.\n");
            }
        }
    }

    void deleteFromInbox(String username, long id) {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT `inbox`.`id` FROM `messenger`.`inbox` WHERE `inbox`.`users_id` = ? ORDER BY `inbox`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt1.setLong(1, getIdByUsername(username));
            stmt1.setLong(2, id - 1);
            rs = stmt1.executeQuery();
            if (rs.first()) {
                long inbox_id = rs.getLong("id");
                stmt2 = con.prepareStatement("DELETE FROM `messenger`.`inbox` WHERE `inbox`.`id` IN (SELECT `id` FROM (SELECT `id` FROM `messenger`.`inbox` WHERE `users_id` = ? ORDER BY `datetime` DESC LIMIT 1 OFFSET ?) x);");
                stmt2.setLong(1, getIdByUsername(username));
                stmt2.setLong(2, id - 1);
                int rows = stmt2.executeUpdate();
                if (rows == 1) {
                    System.out.println("OK. This message has been deleted from your inbox folder.");
                    Log log = new Log();
                    if (Objects.equals(getGender(username), "male")) {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + inbox_id + ") from his inbox folder.");
                    } else {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + inbox_id + ") from her inbox folder.");
                    }
                } else {
                    System.out.println("Your inbox folder is empty.\n");
                }
            } else {
                System.out.println("Oops! Something happened.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection3(con, stmt1, stmt2, rs);
        }
    }

    void deleteAllFromInbox(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("DELETE FROM `messenger`.`inbox` WHERE `users_id` = ?;");
            stmt.setLong(1, getIdByUsername(username));
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All the messages have been deleted from your inbox folder.");
                Log log = new Log();
                if (Objects.equals(getGender(username), "male")) {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied his inbox folder.");
                } else {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied her inbox folder.");
                }
            } else {
                System.out.println("Your inbox folder is empty.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection1(con, stmt);
        }
    }

    void deleteFromSent(String username, long id) {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT `sent`.`id` FROM `messenger`.`sent` WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt1.setLong(1, getIdByUsername(username));
            stmt1.setLong(2, id - 1);
            rs = stmt1.executeQuery();
            if (rs.first()) {
                long sent_id = rs.getLong("id");
                stmt2 = con.prepareStatement("DELETE FROM `messenger`.`sent` WHERE `sent`.`id` IN (SELECT `id` FROM (SELECT `id` FROM `messenger`.`sent` WHERE `users_id` = ? ORDER BY `datetime` DESC LIMIT 1 OFFSET ?) x);");
                stmt2.setLong(1, getIdByUsername(username));
                stmt2.setLong(2, id - 1);
                int rows = stmt2.executeUpdate();
                if (rows == 1) {
                    System.out.println("OK. This message has been deleted from your sent folder.");
                    Log log = new Log();
                    if (Objects.equals(getGender(username), "male")) {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + sent_id + ") from his sent folder.");
                    } else {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + sent_id + ") from her sent folder.");
                    }
                } else {
                    System.out.println("Your sent folder is empty.\n");
                }
            } else {
                System.out.println("Oops! Something happened.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection3(con, stmt1, stmt2, rs);
        }
    }

    void deleteAllFromSent(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("DELETE FROM `messenger`.`sent` WHERE `users_id` = ?;");
            stmt.setLong(1, getIdByUsername(username));
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All the messages have been deleted from your sent folder.");
                Log log = new Log();
                if (Objects.equals(getGender(username), "male")) {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied his sent folder.");
                } else {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied her sent folder.");
                }
            } else {
                System.out.println("Your sent folder is empty.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection1(con, stmt);
        }
    }

    void deleteFromTrash(String username, long id) {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt1 = con.prepareStatement("SELECT `trash`.`id` FROM `messenger`.`trash` WHERE `trash`.`users_id` = ? ORDER BY `trash`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt1.setLong(1, getIdByUsername(username));
            stmt1.setLong(2, id - 1);
            rs = stmt1.executeQuery();
            if (rs.first()) {
                long trash_id = rs.getLong("id");
                stmt2 = con.prepareStatement("DELETE FROM `messenger`.`trash` WHERE `trash`.`id` IN (SELECT `id` FROM (SELECT `id` FROM `messenger`.`trash` WHERE `users_id` = ? ORDER BY `datetime` DESC LIMIT 1 OFFSET ?) x);");
                stmt2.setLong(1, getIdByUsername(username));
                stmt2.setLong(2, id - 1);
                int rows = stmt2.executeUpdate();
                if (rows == 1) {
                    Log log = new Log();
                    System.out.println("OK. This message has been deleted from your trash folder.\n");
                    if (Objects.equals(getGender(username), "male")) {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + trash_id + ") from his trash folder.");
                    } else {
                        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " deleted message with number " + id + " (id: " + trash_id + ") from her trash folder.");
                    }
                } else {
                    System.out.println("Your trash folder is empty.\n");
                }
            } else {
                System.out.println("Oops! Something happened.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection3(con, stmt1, stmt2, rs);
        }
    }

    void deleteAllFromTrash(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("DELETE FROM `messenger`.`trash` WHERE `users_id` = ?;");
            stmt.setLong(1, getIdByUsername(username));
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All the messages have been deleted from your trash folder.");
                Log log = new Log();
                if (Objects.equals(getGender(username), "male")) {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied his trash folder.");
                } else {
                    log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " emptied her trash folder.");
                }
            } else {
                System.out.println("Your trash folder is empty.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection1(con, stmt);
        }
    }

    void viewInbox(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `inbox`.`datetime` AS `Date - Time`, `users`.`username` AS `From`, `inbox`.`text` AS `Message` FROM `messenger`.`inbox` INNER JOIN `messenger`.`users` ON `messenger`.`inbox`.`from_id` = `messenger`.`users`.`id` JOIN (SELECT @a:= 0) r WHERE `inbox`.`users_id` = ? ORDER BY `inbox`.`datetime` DESC;");
            stmt.setLong(1, getIdByUsername(username));
            rs = stmt.executeQuery();
            if (rs.next()) {
                extractTable1(rs);
            } else {
                System.out.println("Your inbox folder is empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
    }

    void viewSent(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `sent`.`datetime` AS `Date - Time`, `users`.`username` AS `To`, `sent`.`text` AS `Message` FROM `messenger`.`sent` INNER JOIN `messenger`.`users` ON `messenger`.`sent`.`to_id` = `messenger`.`users`.`id` JOIN (SELECT @a:= 0) r WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC;");
            stmt.setLong(1, getIdByUsername(username));
            rs = stmt.executeQuery();
            if (rs.next()) {
                extractTable1(rs);
            } else {
                System.out.println("Your sent folder is empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
    }

    void viewTrash(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `trash`.`datetime` AS `Date - Time`, `users1`.`username` AS `From`, `users2`.`username` AS `To`, `trash`.`text` AS `Message` FROM `messenger`.`trash` LEFT JOIN `messenger`.`users` AS `users1` ON `messenger`.`trash`.`from_id` = `users1`.`id` LEFT JOIN `messenger`.`users` AS `users2` ON `messenger`.`trash`.`to_id` = `users2`.`id` JOIN (SELECT @a:= 0) r WHERE `trash`.`users_id` = ? ORDER BY `trash`.`datetime` DESC;");
            stmt.setLong(1, getIdByUsername(username));
            rs = stmt.executeQuery();
            if (rs.next()) {
                extractTable2(rs);
            } else {
                System.out.println("Your trash folder is empty.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
    }

    void viewAllUsers(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            if (getRole(username).equals("super administrator")) {
                stmt = con.prepareStatement("SELECT `users`.`id` AS `No`, `users`.`fname` AS `First name`, `users`.`lname` AS `Last name`, `users`.`gender` AS `Gender`, `users`.`username` AS `Username`, `roles`.`role` AS `Role` FROM `messenger`.`roles` INNER JOIN `messenger`.`users` ON `messenger`.`roles`.`users_id` = `messenger`.`users`.`id` WHERE `roles`.`role` != ?;");
                stmt.setString(1, "sadmin");
                rs = stmt.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Table table = new Table();
                    table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4), rsmd.getColumnLabel(5) == null ? "" : rsmd.getColumnLabel(5), rsmd.getColumnLabel(6) == null ? "" : rsmd.getColumnLabel(6));
                    rs.beforeFirst();
                    while (rs.next()) {
                        table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : rs.getString(rsmd.getColumnLabel(1)), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)) == null ? "" : rs.getString(rsmd.getColumnLabel(4)), rs.getString(rsmd.getColumnLabel(5)) == null ? "" : rs.getString(rsmd.getColumnLabel(5)), getRole(rs.getString(rsmd.getColumnLabel(5))).substring(0, 1).toUpperCase() + getRole(rs.getString(rsmd.getColumnLabel(5))).substring(1).toLowerCase());
                    }
                    logViewUsersStatus(username, datetime, table);
                } else {
                    System.out.println("Nobody has signed up yet.");
                }
            } else if (getRole(username).equals("administrator")){
                stmt = con.prepareStatement("SELECT `users`.`id` AS `No`, `users`.`fname` AS `First name`, `users`.`lname` AS `Last name`, `users`.`gender` AS `Gender`, `users`.`username` AS `Username`, `roles`.`role` AS `Role` FROM `messenger`.`roles` INNER JOIN `messenger`.`users` ON `messenger`.`roles`.`users_id` = `messenger`.`users`.`id` WHERE `roles`.`role` != ? AND `roles`.`role` != ?;");
                stmt.setString(1, "sadmin");
                stmt.setString(2, "admin");
                rs = stmt.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Table table = new Table();
                    table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4), rsmd.getColumnLabel(5) == null ? "" : rsmd.getColumnLabel(5), rsmd.getColumnLabel(6) == null ? "" : rsmd.getColumnLabel(6));
                    rs.beforeFirst();
                    while (rs.next()) {
                        table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : rs.getString(rsmd.getColumnLabel(1)), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)).substring(0, 1).toUpperCase() + rs.getString(rsmd.getColumnLabel(4)).substring(1).toLowerCase(), rs.getString(rsmd.getColumnLabel(5)) == null ? "" : rs.getString(rsmd.getColumnLabel(5)), getRole(rs.getString(rsmd.getColumnLabel(5))).substring(0, 1).toUpperCase() + getRole(rs.getString(rsmd.getColumnLabel(5))).substring(1).toLowerCase());
                    }
                    logViewUsersStatus(username, datetime, table);
                } else {
                    System.out.println("Nobody has signed up yet.");
                }
            } else {
                System.out.println("An internal conflict prevented from retrieving the users.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
    }

    void viewAllMessages(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            if (getRole(username).equals("super administrator")) {
                stmt = con.prepareStatement("SELECT `messages`.`id` AS `No`, `messages`.`datetime` AS `Date - Time`, `users1`.`username` AS `From`, `users2`.`username` AS `To`, `messages`.`text` AS `Message` FROM `messenger`.`messages` INNER JOIN `messenger`.`users` AS `users1` ON `messenger`.`messages`.`from_id` = `users1`.`id` INNER JOIN `messenger`.`users` AS `users2` ON `messenger`.`messages`.`to_id` = `users2`.`id` ORDER BY `messages`.`datetime` DESC;");
                rs = stmt.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Table table = new Table();
                    table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4), rsmd.getColumnLabel(5) == null ? "" : rsmd.getColumnLabel(5));
                    rs.beforeFirst();
                    while (rs.next()) {
                        table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : String.valueOf(rs.getInt(rsmd.getColumnLabel(1))), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)) == null ? "" : rs.getString(rsmd.getColumnLabel(4)), rs.getString(rsmd.getColumnLabel(5)) == null ? "" : rs.getString(rsmd.getColumnLabel(5)));
                    }
                    logViewMessagesStatus(username, datetime, table);
                } else {
                    System.out.println("Nobody has sent something yet.");
                }
            } else if (getRole(username).equals("administrator")){
                stmt = con.prepareStatement("SELECT `messages`.`id` AS `No`, `messages`.`datetime` AS `Date - Time`, `users1`.`username` AS `From`, `users2`.`username` AS `To`, `messages`.`text` AS `Message` FROM `messenger`.`messages` INNER JOIN `messenger`.`users` AS `users1` ON `messenger`.`messages`.`from_id` = `users1`.`id` INNER JOIN `messenger`.`users` AS `users2` ON `messenger`.`messages`.`to_id` = `users2`.`id` WHERE `users1`.`username` NOT IN (SELECT `users`.`username` FROM `messenger`.`users` INNER JOIN `messenger`.`roles` ON `messenger`.`users`.`id` = `messenger`.`roles`.`users_id`WHERE `messenger`.`roles`.`role` = ?) AND `users2`.`username` NOT IN (SELECT `users`.`username` FROM `messenger`.`users` INNER JOIN `messenger`.`roles` ON `messenger`.`users`.`id` = `messenger`.`roles`.`users_id` WHERE `messenger`.`roles`.`role` = ?) ORDER BY `messages`.`datetime` DESC;");
                stmt.setString(1, "sadmin");
                stmt.setString(2, "administrator");
                rs = stmt.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Table table = new Table();
                    table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4), rsmd.getColumnLabel(5) == null ? "" : rsmd.getColumnLabel(5));
                    rs.beforeFirst();
                    while (rs.next()) {
                        table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : String.valueOf(rs.getInt(rsmd.getColumnLabel(1))), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)) == null ? "" : rs.getString(rsmd.getColumnLabel(4)), rs.getString(rsmd.getColumnLabel(5)) == null ? "" : rs.getString(rsmd.getColumnLabel(5)));
                    }
                    logViewMessagesStatus(username, datetime, table);
                } else {
                    System.out.println("Nobody has sent something yet.");
                }
            } else {
                System.out.println("An internal conflict prevented from retrieving the users' data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        }
    }

    long getIdByUsername(String username) {
        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT * FROM `messenger`.`users` WHERE `username` = ?;");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.first() && rs.getString("username").equals(username)) {
                return rs.getInt("id");
            } else {
                System.out.println("\nThere is no user with this username.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return 0;
    }

    private String getUsernameById(long id) {
        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT * FROM `messenger`.`users` WHERE `id` = ?;");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.first() && rs.getInt("id") == id) {
                return rs.getString("username");
            } else {
                System.out.println("\nThere is no user with this ID.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }

    String getGender(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT `gender` FROM `messenger`.`users` WHERE `username` = ?;");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.first()) {
                switch (rs.getString("gender")) {
                    case "male":
                        return rs.getString("gender");
                    case "female":
                        return rs.getString("gender");
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return null;
    }

    String getRole(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT `username`, `role` FROM `messenger`.`roles` INNER JOIN `messenger`.`users` ON `messenger`.`roles`.`users_id` = `messenger`.`users`.`id` WHERE `users`.`username` = ?;");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.first()) {
                if (rs.getString("role").equals("sadmin")) {
                    return "super administrator";
                } else {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return null;
    }

    boolean usernameEquals(String username) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT `username` FROM `messenger`.`users` WHERE `users`.`username` = ?;");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            return rs.first() && rs.getString("username").equals(username);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return false;
    }

    boolean inboxNoIs(String username, long no) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `inbox`.`datetime` AS `Date - Time`, `users`.`username` AS `From`, `inbox`.`text` AS `Message` FROM `messenger`.`inbox` INNER JOIN `messenger`.`users` ON `messenger`.`inbox`.`from_id` = `messenger`.`users`.`id` JOIN (SELECT @a:= 0) r WHERE `inbox`.`users_id` = ? ORDER BY `inbox`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt.setLong(1, getIdByUsername(username));
            stmt.setLong(2, no - 1);
            rs = stmt.executeQuery();
            return rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return false;
    }

    boolean sentNoIs(String username, long no) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `sent`.`datetime` AS `Date - Time`, `users`.`username` AS `From`, `sent`.`text` AS `Message` FROM `messenger`.`sent` INNER JOIN `messenger`.`users` ON `messenger`.`sent`.`to_id` = `messenger`.`users`.`id` JOIN (SELECT @a:= 0) r WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt.setLong(1, getIdByUsername(username));
            stmt.setLong(2, no - 1);
            rs = stmt.executeQuery();
            return rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return false;
    }

    boolean trashNoIs(String username, long no) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `trash`.`datetime` AS `Date - Time` FROM `messenger`.`trash` JOIN (SELECT @a:= 0) r WHERE `trash`.`users_id` = ? ORDER BY `trash`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt.setLong(1, getIdByUsername(username));
            stmt.setLong(2, no - 1);
            rs = stmt.executeQuery();
            return rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection2(con, stmt, rs);
        } return false;
    }

    private String getTextFromSentNo(String username, long id) {
        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `sent`.`text` FROM `messenger`.`sent` JOIN (SELECT @a:= 0) r WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt.setLong(1, getIdByUsername(username));
            stmt.setLong(2, id);
            rs = stmt.executeQuery();
            if (rs.first()) {
                return rs.getString("text");
            } else {
                System.out.println("\nThere is no such message.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }

    private String getDateByMessageNo(String username, long id) {
        Connection con;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            con = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            stmt = con.prepareStatement("SELECT @a:=@a+1 AS `No`, `sent`.`datetime` FROM `messenger`.`sent` JOIN (SELECT @a:= 0) r WHERE `sent`.`users_id` = ? ORDER BY `sent`.`datetime` DESC LIMIT 1 OFFSET ?;");
            stmt.setLong(1, getIdByUsername(username));
            stmt.setLong(2, id - 1);
            rs = stmt.executeQuery();
            if (rs.first()) {
                return rs.getTimestamp("datetime").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                System.out.println("\nThere is no such message.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }

    private void logViewUsersStatus(String username, String datetime, Table table) {
        table.printTable();
        Log log = new Log();
        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " reviewed the users' status.");
    }

    private void logViewMessagesStatus(String username, String datetime, Table table) {
        table.printTable();
        Log log = new Log();
        log.writeToLog(datetime + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " reviewed the messenger's messages.");
    }

    private void extractTable1(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Table table = new Table();
        table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4));
        rs.beforeFirst();
        while (rs.next()) {
            table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : String.valueOf(rs.getInt(rsmd.getColumnLabel(1))), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)) == null ? "" : rs.getString(rsmd.getColumnLabel(4)));
        }
        table.printTable();
    }

    private void extractTable2(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Table table = new Table();
        table.headers(rsmd.getColumnLabel(1) == null ? "" : rsmd.getColumnLabel(1), rsmd.getColumnLabel(2) == null ? "" : rsmd.getColumnLabel(2), rsmd.getColumnLabel(3) == null ? "" : rsmd.getColumnLabel(3), rsmd.getColumnLabel(4) == null ? "" : rsmd.getColumnLabel(4), rsmd.getColumnLabel(5) == null ? "" : rsmd.getColumnLabel(5));
        rs.beforeFirst();
        while (rs.next()) {
            table.addRow(rs.getString(rsmd.getColumnLabel(1)) == null ? "" : String.valueOf(rs.getInt(rsmd.getColumnLabel(1))), rs.getString(rsmd.getColumnLabel(2)) == null ? "" : rs.getString(rsmd.getColumnLabel(2)), rs.getString(rsmd.getColumnLabel(3)) == null ? "" : rs.getString(rsmd.getColumnLabel(3)), rs.getString(rsmd.getColumnLabel(4)) == null ? "" : rs.getString(rsmd.getColumnLabel(4)), rs.getString(rsmd.getColumnLabel(5)) == null ? "" : rs.getString(rsmd.getColumnLabel(5)));
        }
        table.printTable();
    }

    private void logOnEditMessage(String username, long id, String text, PreparedStatement stmt5, ResultSet rs1, int rows1, int rows2) throws SQLException {
        int rows3 = stmt5.executeUpdate();
        if (rows1 + rows2 + rows3 == 3) {
            System.out.println("The message with number " + id + " has been updated.\n");
            Log log = new Log();
            log.writeToLog(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + getRole(username).substring(0, 1).toUpperCase() + getRole(username).substring(1).toLowerCase() + " " + username + " updated the message (no: " + id + ") that had sent to " + getRole(getUsernameById(rs1.getInt("to_id"))) + " " + getUsernameById(rs1.getInt("to_id")) + ". The new message is \"" + text + "\"");
        } else {
            System.out.println("Oops! Something happened.\n");
        }
    }

    private void closeConnection1(Connection con, PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Oops! Something happened.\n");
        }
    }

    private void closeConnection2(Connection con, PreparedStatement stmt, ResultSet rs) {
        try {
            closeConnection5(con, stmt, rs);
        } catch (SQLException e) {
            System.out.println("Oops! Something happened.\n");
        }
    }

    private void closeConnection3(Connection con, PreparedStatement stmt1, PreparedStatement stmt2, ResultSet rs) {
        try {
            if (stmt2 != null) {
                stmt2.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (stmt1 != null) {
                stmt1.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Oops! Something happened.\n");
        }
    }

    private void closeConnection4(Connection con, PreparedStatement stmt1, PreparedStatement stmt2, PreparedStatement stmt3, ResultSet rs) throws SQLException {
        if (stmt3 != null) {
            stmt3.close();
        }
        if (stmt2 != null) {
            stmt2.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (stmt1 != null) {
            stmt1.close();
        }
        if (con != null) {
            con.close();
        }
    }

    private void closeConnection5(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
    }
}