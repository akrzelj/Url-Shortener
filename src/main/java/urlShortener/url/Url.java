package urlShortener.url;

import urlShortener.DataRepository;
import urlShortener.helpers.InputHelpers.UrlHelper;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Url {

    public static final String SHORT_URL_PREFIX = "http://short.com/";
    private String fullUrl;
    private String shortUrl;
    private Integer redirectType;
    private Integer visitCount;

    public Url() {}

    public Url(String fullUrl) {
        this.fullUrl = fullUrl;
        this.shortUrl = generateShortUrl();
        this.redirectType = 302;
        this.visitCount = 0;
    }

    public Url(String fullUrl, int redirectType) {
        this.fullUrl = fullUrl;
        this.shortUrl = generateShortUrl();
        this.redirectType = redirectType;
        this.visitCount = 0;
    }

    public Url(UrlHelper urlHelper) {
        if (urlHelper.getRedirectType() == null) {
            this.fullUrl = urlHelper.getFullUrl();
            this.shortUrl = generateShortUrl();
            this.redirectType = 302;
            this.visitCount = 0;
        } else {
            this.fullUrl = urlHelper.getFullUrl();
            this.shortUrl = generateShortUrl();
            this.redirectType = urlHelper.getRedirectType();
            this.visitCount = 0;
        }
    }

    private String createShortUrl(String fullUrl)
    {
        return fullUrl;
    }

    public String getFullUrl()
    {
        return this.fullUrl;
    }

    public Integer getRedirectType()
    {
        return this.redirectType;
    }

    public String getShortUrl()
    {
        return shortUrl;
    }

    public void increaseVisitCount()
    {
        this.visitCount++;
    }

    public Integer getVisitCount()
    {
        return visitCount;
    }

    @Override public String toString()
    {
        return "Full url: " + this.fullUrl +
                ", short url: " + this.shortUrl +
                ", redirect type: " + this.redirectType;
    }

    @Override public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof Url))
            return false;

        Url url = (Url) o;
        return url.getFullUrl().equals(this.fullUrl) &&
               url.getShortUrl().equals(this.shortUrl);
    }

    @Override
    public int hashCode()
    {
        int result = this.fullUrl.hashCode();
        result = 31 * result + this.shortUrl.hashCode();
        return result;
    }


    private String generateShortUrl()
    {
        for (int i = 0; i < 20; i++) {

            String shortUrlId = new Random().ints(8, 65, 123)
                    .mapToObj(num -> String.valueOf((char) num))
                    .collect(Collectors.joining())
                    .replace("\\", "A" );

            String shortUrl = SHORT_URL_PREFIX + shortUrlId;
            Optional<Url> wantedLink =  DataRepository.getListOfAllShortenedUrls().stream()
                    .filter(link -> link.getShortUrl().equals(shortUrl))
                    .findFirst();

            if (!wantedLink.isPresent()) {
                return SHORT_URL_PREFIX + shortUrlId;
            }
        }

        throw new RuntimeException("Link could not be created after 20 tries");
    }
}