package asserts;

import helpers.RandomCategorySelectorHelper;

public class RandomCategorySelectorAsserts {
    public static void assertUrlsAfterDelimitersEqual(String urlWithEquals, String urlWithSlash) {
        String processed1 = RandomCategorySelectorHelper.extractAfterEquals(urlWithEquals);
        String processed2 = RandomCategorySelectorHelper.extractAfterLastSlash(urlWithSlash);

        org.junit.jupiter.api.Assertions.assertEquals(processed1, processed2,
                "Части URL после разделителей не совпадают");
    }
}
