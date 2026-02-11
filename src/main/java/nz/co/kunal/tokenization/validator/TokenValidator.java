package nz.co.kunal.tokenization.validator;

import nz.co.kunal.tokenization.exception.ValidationFailedException;
import nz.co.kunal.tokenization.utils.StringUtilities;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TokenValidator {
    private static final String EMPTY_TOKEN_LIST_ERROR_MESSAGE = "No tokens were provided";
    private static final String BLANK_TOKEN_ERROR_MESSAGE = "At least one token was empty or blank";

    public void validate(final List<String> tokens) {
        if (CollectionUtils.isEmpty(tokens)) {
            throw new ValidationFailedException(EMPTY_TOKEN_LIST_ERROR_MESSAGE);
        }
        final boolean isAnyTokenBlank = tokens.stream()
            .anyMatch(StringUtilities::isBlankOrEmpty);

        if (isAnyTokenBlank) {
            throw new ValidationFailedException(BLANK_TOKEN_ERROR_MESSAGE);
        }
    }
}
