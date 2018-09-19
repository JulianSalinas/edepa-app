package edepa.model;

import java.util.Objects;

public class Comment {

    private long time;
    private String key;
    private String userid;
    private String content;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment() { }

    private Comment(Builder builder) {
        time = builder.time;
        key = builder.key;
        userid = builder.userid;
        content = builder.content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(key, comment.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }


    public static final class Builder {
        private long time;
        private String key;
        private String userid;
        private String content;

        public Builder() {
        }

        public Builder time(long time) {
            this.time = time;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder userid(String userid) {
            this.userid = userid;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
