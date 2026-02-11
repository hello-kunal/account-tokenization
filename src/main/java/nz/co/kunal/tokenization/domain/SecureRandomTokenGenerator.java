package nz.co.kunal.tokenization.domain;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureRandomTokenGenerator implements TokenGenerator {
    private static final SecureRandom RNG = new SecureRandom();
    private static final int BYTES = 24;

    @Override
    public String generate() {
        final byte[] buffer = new byte[BYTES];
        RNG.nextBytes(buffer);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }
}
