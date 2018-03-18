package imagisoft.edepa;

public class Message {

    private int userid;
    private Long time;
    private String username;
    private String content;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(int userid, String username, String content, Long time) {
        this.userid = userid;
        this.username = username;
        this.content = content;
        this.time = time;
    }

}
