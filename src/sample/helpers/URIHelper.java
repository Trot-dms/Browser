package sample.helpers;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by trot on 17.02.17.
 */
public class URIHelper {

    public static String getParsedURI(String uriToParse) {
        String URL = null;
        try {
            URI uri = new URI(uriToParse);
            if (uri.getHost() == null) {
                URL = "http://" + uri.toString();
            } else {
                URL = uriToParse.toString();
            }
        } catch (URISyntaxException e) {
            System.err.println("Wrong page address.");
        }
        System.out.println(URL);
        return URL;
    }
}
