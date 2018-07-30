package imagisoft.model;


import java.util.Objects;

/**
 * Clase usada para recibir en la sección de noticias
 */
public class NewsItem {

    private Long time;
    private String key;
    private String title;
    private String content;
    private String imageUrl;
    private Integer viewedAmount;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getViewedAmount() {
        return viewedAmount;
    }

    public void setViewedAmount(Integer viewedAmount) {
        this.viewedAmount = viewedAmount;
    }

    public NewsItem() {
        // Requerido por Firebase
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsItem)) return false;
        NewsItem newsItem = (NewsItem) o;
        return Objects.equals(key, newsItem.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
