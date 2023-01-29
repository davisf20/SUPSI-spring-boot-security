package security.backend.server.security;

import java.util.regex.Pattern;

public class InputSanitizer {
    private static final Pattern SANITIZATION_PATTERN = Pattern.compile("[<>\"']");

    public static String sanitize(String input) {
        return SANITIZATION_PATTERN.matcher(input).replaceAll("");
    }
}
