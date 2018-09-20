package edepa.model;

import java.util.Objects;

public class UserProfile {

    private String userid;
    private String email;
    private String username;
    private String photoUrl;
    private Boolean allowPhoto;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getAllowPhoto() {
        return allowPhoto;
    }

    public void setAllowPhoto(Boolean allowPhoto) {
        this.allowPhoto = allowPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(getUserid(), that.getUserid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserid());
    }

    public UserProfile() { }

}
