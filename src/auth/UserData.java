package auth;

import java.io.Serializable;

public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uid;
    private String name;
    private String pictureUrl;
    private String email;
    private Integer stage;
    private double difficulty;
    private String timestampString;

    public UserData(String uid, String name, String pictureUrl, String email, String timestampString) {
        this.uid = uid;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.email = email;
        this.timestampString = timestampString;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getTimestamp() {
        return timestampString;
    }

    public void setUserStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getUserStage() {
        return stage;
    }

    public void setUserDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getUserDifficulty() {
        return difficulty;
    }
}