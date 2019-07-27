package urlShortener;

public class HelpGuide {
    public static String HELP_GUIDE = "<h1>User guide for UrlShortener</h1>"+
            ""+
            "<h2>-----------------CONFIGURATION---------------------</h2>"+
            ""+
            "<p>#To shorten any link you have to open an account first.</p>"+
            "<h3>##You open an account by sending following http request:</h3>"+
            "<p>---- HTTP method: POST</br>"+
            "---- URI : localhost:8080/account</br>"+
            "---- Request body: json object with accountId (String, required)</br>"+
            "-------- example: { \"accountId\": \"users123\"}</br>"+
            "---- Request type: application/json</br>"+
            "---- Response type: application/json</br>"+
            "---- Response</br>"+
            "-------- if success {\"password\":\":$kl;VWc\",\"success\":\"true\",\"description\":\"Account was successful created\"}</br>"+
            "-------- if failed   {\"success\":\"false\",\"description\":\"Account eith provided ID allready exsists\"} </p> </br>"+
            "<h3>##URL shortening is done by sending following http request:</h3></br>"+
            "<p>---- HTTP method: POST</br>"+
            "---- URI : localhost:8080/register</br>"+
            "---- Request header: set property \"password\" to your password which you got at account creation</br>"+
            "-------- in this example you would set \"password\" to \":$kl;VWc\"</br>"+
            "---- Request body: json object with: url which has to be shortened(required), redirect type(optional, 302 by default)</br>"+
            "-------- example: { \"fullUrl\": \"www.fesb.hr\", \"redirectType\": 301}</br>"+
            "-------- example: { \"fullUrl\": \"www.fesb.hr\"}</br>"+
            "---- Request type: application/json</br>"+
            "---- Response type: application/json</br>"+
            "---- Response"+
            "-------- {\"shortUrl\":\"http://short.com/IOIoVtJo\"}</p> </br>"+
            "<h3>##You can get visit statistics for given user by sending following http request</h3></br>"+
            "<p>---- HTTP method: GET</br>"+
            "---- URI : localhost:8080/statistics/{accountId}</br>"+
            "---- Request header: set property \"password\" to your password which you got at registration</br>"+
            "-------- in this example you would set \"password\" to \":$kl;VWc\"</br>"+
            "---- Response type: application/json</br>"+
            "---- Response</br>"+
            "-------- {\"www.fesb.hr\":0,\"www.fesb.hr\":4}</p> </br>"+
            "<h2>--------------CONSUMING THE API-----------------</h2>"+
            "<h3>##User can get full URL for short link by sending following http request</h3>"+
            "<p>---- HTTP method: GET</br>"+
            "---- URI : localhost:8080/statistics/</br>"+
            "---- Request body: json object with: shortUrl (required)</br>"+
            "-------- example: {\"shortUrl\":\"http://short.com/fJhWUpCM\"}</br>"+
            "---- Response type: application/json</br>"+
            "---- Response</br>"+
            "-------- {\"Full URL: \":\"www.fesb.hr\"}</p> </br>";
}
