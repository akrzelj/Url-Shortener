package urlShortener;

import org.json.JSONException;
import org.json.JSONObject;
import urlShortener.url.Url;
import urlShortener.user.User;

import java.util.*;

public class DataRepository {

    private static Map<User, List<Url>> repository = new HashMap<>();

    private DataRepository() {}

    public static List<Url> getListOfAllShortenedUrls()
    {
        List<Url> allShortenedLinks = new ArrayList<>();

        for (List<Url> userUrls : repository.values()) {
            allShortenedLinks.addAll(userUrls);
        }

        return allShortenedLinks;
    }

    static boolean addUser(User user)
    {
        if (repository.containsKey(user)) {
            return false;
        }

        repository.put(user, new ArrayList<>());
        return true;
    }

    static Optional<User> getUserByAccountId(String accountId)
    {
        return repository.keySet().stream().filter(user -> user.getAccountId().equals(accountId)).findFirst();
    }

    static Optional<User> getUserByPassword(String password)
    {
        return repository.keySet().stream().filter(user -> user.getPassword().equals(password)).findFirst();
    }

    static void registerUrl(Url url, User user)
    {
        if (repository.containsKey(user)) {
            repository.get(user).add(url);
            return;
        }

        repository.put(user, new ArrayList<>());
        repository.get(user).add(url);
    }

    static JSONObject getStatisticsForUser(User user) throws JSONException
    {
        if (!repository.containsKey(user)) {
            return new JSONObject();
        }

        List<Url> usersLinks = repository.get(user);
        JSONObject userStatisticsJSON = new JSONObject();

        for (Url url : usersLinks) {
            userStatisticsJSON.put(url.getFullUrl(), url.getVisitCount());
        }

        return userStatisticsJSON;
    }

    static Optional<Url> getUrlByShortUrl(String shortUrl)
    {
        return getListOfAllShortenedUrls().stream().filter(url -> url.getShortUrl().equals(shortUrl)).findFirst();
    }
}
