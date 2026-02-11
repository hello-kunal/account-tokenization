package nz.co.kunal.tokenization.validator;

import nz.co.kunal.tokenization.exception.ValidationFailedException;
import nz.co.kunal.tokenization.utils.StringUtilities;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AccountNumberValidator {
    private static final String EMPTY_ACCOUNT_NUMBER_LIST_ERROR_MESSAGE = "No account numbers were provided";
    private static final String BLANK_ACCOUNT_NUMBER_ERROR_MESSAGE = "At least one account number was empty or blank";

    public void validate(final List<String> accounts) {
        if (CollectionUtils.isEmpty(accounts)) {
            throw new ValidationFailedException(EMPTY_ACCOUNT_NUMBER_LIST_ERROR_MESSAGE);
        }
        final boolean isAnyAccountNumberBlank = accounts.stream()
            .anyMatch(StringUtilities::isBlankOrEmpty);

        if (isAnyAccountNumberBlank) {
            throw new ValidationFailedException(BLANK_ACCOUNT_NUMBER_ERROR_MESSAGE);
        }
    }
}
