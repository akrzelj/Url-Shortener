package urlShortener.helpers.InputHelpers;

public class UrlHelper {

    private Integer redirectType;
    private String fullUrl;

    UrlHelper() {}

    UrlHelper(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    UrlHelper(String fullUrl, String redirectType) {
        this.fullUrl = fullUrl;
        this.redirectType = Integer.valueOf(redirectType);
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public Integer getRedirectType() {
        return redirectType;
    }
}
