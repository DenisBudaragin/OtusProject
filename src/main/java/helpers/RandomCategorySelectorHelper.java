package helpers;

public class RandomCategorySelectorHelper {


    public static String extractAfterEquals(String url) {
        int equalsIndex = url.indexOf('=');
        if (equalsIndex == -1) {
            throw new IllegalArgumentException("URL не содержит символ '=': " + url);
        }
        return url.substring(equalsIndex + 1);
    }

    public static String extractAfterLastSlash(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            throw new IllegalArgumentException("URL не содержит '/': " + url);
        }
        return url.substring(lastSlashIndex + 1);
    }
}
