package Users;

public class Table {
    private String user = "";
    private String position = "";

    public Table(String user, String position){
        this.user = user;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
