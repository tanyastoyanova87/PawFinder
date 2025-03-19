package app.util;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public final class StringFormatter {

    public static String formatCountries(String country) {
        return Arrays.stream(country.toLowerCase().replace("_", " ").split(" "))
                .map(word -> word.equals("and") ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
