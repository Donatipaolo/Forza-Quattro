package data;

import enums.Status;

public class Player {

    private String username;
    private Status status;

    public Player() {
    }

    public Player(String username, Status status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
