package nz.co.kunal.tokenization.validator;

import nz.co.kunal.tokenization.exception.ValidationFailedException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountNumberValidatorTest {

    private final AccountNumberValidator accountNumberValidator = new AccountNumberValidator();

    @Test
    void Validation_fails_if_no_account_number_is_provided() {
        final List<String> accountNumbers = Collections.emptyList();

        final Exception exception = assertThrows(ValidationFailedException.class,
            () -> accountNumberValidator.validate(accountNumbers)
        );

        assertThat(exception, instanceOf(ValidationFailedException.class));
        assertThat(exception.getMessage(), is("No account numbers were provided"));
    }

    @Test
    void Validation_fails_if_at_least_one_account_number_is_blank() {
        final List<String> accountNumbers = List.of("4444", "", "11111", " ");

        final Exception exception = assertThrows(ValidationFailedException.class,
            () -> accountNumberValidator.validate(accountNumbers)
        );

        assertThat(exception.getMessage(), is("At least one account number was empty or blank"));
    }

    @Test
    void Validation_succeeds_if_all_account_numbers_are_valid() {
        final List<String> accountNumbers = List.of("4444", "3333", "11111", "2222");

        accountNumberValidator.validate(accountNumbers);

        // Test should pass
    }
}
