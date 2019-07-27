package urlShortener;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import urlShortener.helpers.InputHelpers.UrlHelper;
import urlShortener.helpers.InputHelpers.ShortUrlHelper;
import urlShortener.url.Url;
import urlShortener.user.User;
import urlShortener.helpers.InputHelpers.UserHelper;

import java.util.Optional;

@RestController
class UrlShortenerController {

    /**
     * CONSTRUCTOR
     */
    UrlShortenerController() {}

    /**
     * PUBLIC METHODS
     */
    @PostMapping("/account")
    ResponseEntity<?> createUser(@RequestBody UserHelper userHelper) throws JSONException
    {
        Optional<ResponseEntity> dataCheckResult = checkProvidedAccountCreationData(userHelper);

        if (dataCheckResult.isPresent()) {
            return dataCheckResult.get();
        }

        User createdUser = new User(userHelper);
        DataRepository.addUser(createdUser);

        return new ResponseEntity<>(succesfulAccountCreationMsg(createdUser).toString().replace("\\", ""), HttpStatus.OK);
    }

    @PostMapping("/register")
    ResponseEntity<?> createShortUrl(@RequestBody UrlHelper urlHelper,
                                     @RequestHeader("password") String password) throws JSONException
    {
        Optional<ResponseEntity> dataCheckResult = checkProvidedUrlRegistrationData(urlHelper, password);

        if (dataCheckResult.isPresent()) {
            return dataCheckResult.get();
        }

        Url newUrl = new Url(urlHelper);
        User urlCreator = DataRepository.getUserByPassword(password).get();

        DataRepository.registerUrl(newUrl, urlCreator);
        JSONObject createdShortUrl = new JSONObject();


        createdShortUrl.put("shortUrl", newUrl.getShortUrl());

        return new ResponseEntity<>(createdShortUrl.toString().replace("\\", ""), HttpStatus.OK);
    }

    @GetMapping("/statistics/{accountId}")
    ResponseEntity<?> getStatisticsForAccount(@PathVariable String accountId,
                                              @RequestHeader("password") String password) throws JSONException {
        Optional<ResponseEntity> dataCheck = checkProvidedData(accountId, password);

        if (dataCheck.isPresent()) {
            return dataCheck.get();
        }

        User currUser = DataRepository.getUserByAccountId(accountId).get();

        JSONObject usersStatistics =  DataRepository.getStatisticsForUser(currUser);

        return new ResponseEntity<>(usersStatistics.toString().equals("{}") ? "User has not created any links" :
                                                                              usersStatistics.toString().replace("\\", ""),
                                    HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity localRedirect(@RequestBody ShortUrlHelper shortUrl) throws JSONException
    {
        if (shortUrl.getShortUrl() == null) {
            return new ResponseEntity<>("Request is not valid. You have to provide \"shortUrl\" property in your request", HttpStatus.BAD_REQUEST);
        }

        Optional<Url> wantedLink =  DataRepository.getUrlByShortUrl(shortUrl.getShortUrl());

        if (!wantedLink.isPresent()) {
            return new ResponseEntity<>("Full url for given short Url not found", HttpStatus.NOT_FOUND);
        }

        wantedLink.get().increaseVisitCount();

        JSONObject responseBody = new JSONObject();
        responseBody.put("Full URL: ", wantedLink.get().getFullUrl());

        return new ResponseEntity<>(responseBody.toString().replace("\\", ""), HttpStatus.valueOf(wantedLink.get().getRedirectType()));
    }

    @GetMapping("/help")
    @ResponseBody
    public String help()
    {
        return HelpGuide.HELP_GUIDE;
    }

    /**
     * PRIVATE METHODS
     */

    private Optional<ResponseEntity> checkProvidedAccountCreationData(UserHelper userHelper) throws JSONException
    {
        if (userHelper.getAccountId() == null) {
            return Optional.of( new ResponseEntity<>("Request is not valid. You have to provide \"accountId\" property in your request", HttpStatus.BAD_REQUEST));
        }

        if (userHelper.getAccountId().length() < 3) {
            return Optional.of( new ResponseEntity<>("Request is not valid. You have to provide \"accountId\" property longer than 2 characters", HttpStatus.BAD_REQUEST));
        }

        boolean currUserAlreadyExists = DataRepository.getUserByAccountId(userHelper.getAccountId()).isPresent();

        if (currUserAlreadyExists) {

            JSONObject json = new JSONObject();
            json.put("success", "false");
            json.put("description", "Account eith provided ID allready exsists");

            return Optional.of( new ResponseEntity<>(unsuccesfulAccountCreationMsg().toString(), HttpStatus.FORBIDDEN));
        }

        return Optional.empty();
    }

    private JSONObject succesfulAccountCreationMsg(User createdUser) throws JSONException
    {
        JSONObject msg = new JSONObject();
        msg.put("success", "true");
        msg.put("description", "Account was successful created");
        msg.put("password", createdUser.getPassword());

        return msg;
    }

    private JSONObject unsuccesfulAccountCreationMsg() throws JSONException
    {
        JSONObject msg = new JSONObject();
        msg.put("success", "false");
        msg.put("description", "Account eith provided ID allready exsists");

        return msg;
    }


    private Optional<ResponseEntity> checkProvidedUrlRegistrationData(UrlHelper urlHelper, String password)
    {
        if (urlHelper.getFullUrl() == null) {
            return Optional.of(new ResponseEntity<>("Request is not valid. You have to provide \"fullUrl\" property in your request", HttpStatus.BAD_REQUEST));
        }

        if (urlHelper.getFullUrl().length() < 4) {
            return Optional.of( new ResponseEntity<>("Request is not valid. You have to provide \"fullUrl\" property longer than 3 characters", HttpStatus.BAD_REQUEST));
        }

        boolean userIsRegistered = DataRepository.getUserByPassword(password).isPresent();

        if (!userIsRegistered) {
            return Optional.of( new ResponseEntity<>("You must be registered for this operation", HttpStatus.FORBIDDEN));
        }

        return Optional.empty();
    }

    private Optional<ResponseEntity> checkProvidedData(String accountId, String password)
    {
        Optional<User> userByAccountId = DataRepository.getUserByAccountId(accountId);
        Optional<User> userByPassword = DataRepository.getUserByPassword(password);

        if (!userByAccountId.isPresent()) {
            return  Optional.of( new ResponseEntity<>("User with provided accountId doesn't exist", HttpStatus.NOT_FOUND));
        }

        if (!userByPassword.isPresent()) {
            return  Optional.of( new ResponseEntity<>("User with provided password doesn't exist", HttpStatus.NOT_FOUND));
        }

        if (!userByAccountId.get().equals(userByPassword.get())) {
            return Optional.of( new ResponseEntity<>("Provided accountId and password does not match!", HttpStatus.FORBIDDEN));
        }

        return Optional.empty();
    }
}