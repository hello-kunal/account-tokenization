package nz.co.kunal.tokenization.service;

import nz.co.kunal.tokenization.domain.TokenMapping;
import nz.co.kunal.tokenization.exception.UnknownTokenException;
import nz.co.kunal.tokenization.repository.TokenMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeTokenizationServiceTest {

    @Mock
    private TokenMappingRepository tokenMappingRepository;

    @InjectMocks
    private DeTokenizationService deTokenizationService;

    @Test
    void deTokenization_can_preserve_retrival_order() {
        final String token1 = "someRandomToken";
        final String expectedAccountNumber1 = "1111-2222-3333-4444";
        final Optional<TokenMapping> existingTokenMapping1 = Optional.of(new TokenMapping(null, expectedAccountNumber1, token1));

        final String token2 = "someOtherRandomToken";
        final String expectedAccountNumber2 = "4444-3333-2222-1111";
        final Optional<TokenMapping> existingTokenMapping2 = Optional.of(new TokenMapping(null, expectedAccountNumber2, token2));

        final String token3 = "someToken";
        final String expectedAccountNumber3 = "4444-1111";
        final Optional<TokenMapping> existingTokenMapping3 = Optional.of(new TokenMapping(null, expectedAccountNumber3, token3));

        given(tokenMappingRepository.findByToken(token1)).willReturn(existingTokenMapping1);
        given(tokenMappingRepository.findByToken(token2)).willReturn(existingTokenMapping2);
        given(tokenMappingRepository.findByToken(token3)).willReturn(existingTokenMapping3);

        final List<String> accountNumbers = deTokenizationService.detokenize(List.of(token1, token2, token3));

        assertThat(accountNumbers, hasSize(3));
        assertThat(accountNumbers.get(0), is(expectedAccountNumber1));
        assertThat(accountNumbers.get(1), is(expectedAccountNumber2));
        assertThat(accountNumbers.get(2), is(expectedAccountNumber3));
    }

    @Test
    void DeTokenization_should_fail_if_any_one_token_does_not_exist() {
        final String token1 = "someRandomToken";
        final String expectedAccountNumber1 = "1111-2222-3333-4444";
        final Optional<TokenMapping> existingTokenMapping1 = Optional.of(new TokenMapping(null, expectedAccountNumber1, token1));

        final String token2 = "someOtherRandomToken";

        given(tokenMappingRepository.findByToken(token1)).willReturn(existingTokenMapping1);
        given(tokenMappingRepository.findByToken(token2)).willReturn(Optional.empty());

        final Exception exception = assertThrows(UnknownTokenException.class,
            () -> deTokenizationService.detokenize(List.of(token1, token2)));

        assertThat(exception.getMessage(), containsString(token2));
    }
}
