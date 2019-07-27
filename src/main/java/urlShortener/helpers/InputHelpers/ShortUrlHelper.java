package urlShortener.helpers.InputHelpers;

public final class ShortUrlHelper {

    private String shortUrl;

    ShortUrlHelper() {}

    ShortUrlHelper(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
