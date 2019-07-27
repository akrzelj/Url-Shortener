package urlShortener.helpers.InputHelpers;

public class UserHelper {
    private String accountId;

    UserHelper() {}

    UserHelper(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
