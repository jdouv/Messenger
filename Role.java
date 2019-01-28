class Role {
    boolean isSAdmin(String username) {
        Database db = new Database();
        return db.getRole(username).equals("super administrator");
    }

    boolean isAdmin(String username) {
        Database db = new Database();
        return db.getRole(username).equals("administrator");
    }

    boolean isUser(String username) {
        Database db = new Database();
        return db.getRole(username).equals("user");
    }
}
