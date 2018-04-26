package guru.bonacci.oogway.app;

public class GemCarrier {
    private String saying;
    private String author;

    public GemCarrier() {
    }

    public GemCarrier(String saying, String author) {
        this.saying = saying;
        this.author = author;
    }

    public String getSaying() {
        return saying;
    }

    public String getAuthor() {
        return author;
    }
}