package nz.co.kunal.tokenization.domain;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

class SecureRandomTokenGeneratorTest {
    private static final Pattern URL_SAFE_BASE64 = Pattern.compile("^[A-Za-z0-9_-]+$");

    private final SecureRandomTokenGenerator tokenGenerator = new SecureRandomTokenGenerator();

    @Test
    void Can_generate_valid_tokens() {
        final String token = tokenGenerator.generate();

        assertThat(token, notNullValue());
        assertThat(token, not(blankOrNullString()));
    }

    @Test
    void Can_generate_unique_tokens() {
        int sampleSize = 10_000;
        final var tokens = IntStream.range(0, sampleSize)
            .mapToObj(__ -> tokenGenerator.generate())
            .collect(toSet());

        assertThat(tokens, hasSize(sampleSize));
    }

    @Test
    void Can_generate_url_safe_token() {
        String token = tokenGenerator.generate();

        assertThat(token, not(containsString("=")));
        assertThat(URL_SAFE_BASE64.matcher(token).matches(), is(true));
    }
}
