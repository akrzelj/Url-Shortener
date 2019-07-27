package urlShortener.user;

import urlShortener.helpers.InputHelpers.UserHelper;

import java.util.Random;
import java.util.stream.Collectors;

public final class User {

    private String accountId;
    private String password;

    User(){}

    public User(String accountId) {
        this.accountId = accountId;
        this.password = generatePassword(8);
    }

    public User(UserHelper userHelper) {
        this.accountId = userHelper.getAccountId();
        this.password = generatePassword(8);
    }

    public String getAccountId()
    {
        return accountId;
    }

    public String getPassword()
    {
        return password;
    }

    @Override public String toString()
    {
        return "User ID " + this.accountId +
                ", password: " + this.password;
    }

    @Override public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof User))
            return false;

        User user = (User) o;

        return user.getAccountId().equals(this.accountId) &&
               user.getPassword().equals(this.getPassword());
    }

    @Override
    public int hashCode()
    {
        int result = accountId.hashCode();
        result = 31 * result + password.hashCode();

        return result;
    }

    private String generatePassword(int passwordLength)
    {
        return new Random().ints(passwordLength, 35, 122)
                .mapToObj(i -> String.valueOf((char)i))
                .collect(Collectors.joining())
                .replace("\\", "A" );
    }
}