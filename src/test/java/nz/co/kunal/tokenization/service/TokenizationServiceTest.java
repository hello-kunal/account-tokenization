package nz.co.kunal.tokenization.service;

import nz.co.kunal.tokenization.domain.TokenGenerator;
import nz.co.kunal.tokenization.domain.TokenMapping;
import nz.co.kunal.tokenization.repository.TokenMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TokenizationServiceTest {

    @Mock
    private TokenMappingRepository tokenMappingRepository;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private TokenizationService tokenizationService;

    @Test
    void Tokenize_returns_existing_token_if_already_exists() {
        final String accountNumber = "4111-s111-1g11-1111";
        final String normalizedAccountNumber = "41111111111111";
        final String expectedToken = "someRandomToken";

        given(tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber)).willReturn(Optional.of(
            new TokenMapping(normalizedAccountNumber, accountNumber, expectedToken))
        );

        final List<String> tokens = tokenizationService.tokenize(List.of(accountNumber));

        assertThat(tokens, hasSize(1));
        assertThat(tokens.getFirst(), is(expectedToken));
    }

    @Test
    void Tokenize_returns_existing_token_if_doesnt_already_exists() {
        final String accountNumber = "4444-3333-2222-1111";
        final String normalizedAccountNumber = "4444333322221111";
        final String newToken = "someOtherRandomToken";
        final TokenMapping expectedTokenMapping = new TokenMapping(normalizedAccountNumber, accountNumber, newToken);
        final TokenMapping savedToken = new TokenMapping(normalizedAccountNumber, accountNumber, newToken);
        savedToken.setId(1L);

        given(tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber)).willReturn(Optional.empty());
        given(tokenGenerator.generate()).willReturn(newToken);
        given(tokenMappingRepository.save(expectedTokenMapping)).willReturn(savedToken);

        final List<String> tokens = tokenizationService.tokenize(List.of(accountNumber));

        assertThat(tokens, hasSize(1));
        assertThat(tokens.getFirst(), is(newToken));
    }

    @Test
    void Tokenization_can_handle_combination_of_known_and_unknown_account_numbers() {
        final String accountNumber1 = "4444-3333-2222-1111";
        final String normalizedAccountNumber1 = "4444333322221111";
        final String existingToken = "someRandomToken";

        given(tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber1)).willReturn(Optional.of(
            new TokenMapping(normalizedAccountNumber1, accountNumber1, existingToken))
        );

        final String accountNumber2 = "4111-1111-1111-1111";
        final String normalizedAccountNumber2 = "4111111111111111";
        final String newToken = "someOtherRandomToken";

        final TokenMapping expectedTokenMapping = new TokenMapping(normalizedAccountNumber2, accountNumber2, newToken);
        final TokenMapping savedToken = new TokenMapping(normalizedAccountNumber2, accountNumber2, newToken);
        savedToken.setId(1L);

        given(tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber2)).willReturn(Optional.empty());
        given(tokenGenerator.generate()).willReturn(newToken);
        given(tokenMappingRepository.save(expectedTokenMapping)).willReturn(savedToken);

        final List<String> tokens = tokenizationService.tokenize(List.of(accountNumber1, accountNumber2));

        assertThat(tokens, hasSize(2));
        assertThat(tokens.get(0), is(existingToken));
        assertThat(tokens.get(1), is(newToken));
    }

    @Test
    void Can_get_existing_token_if_it_being_created_concurrently() {
        final String accountNumber = "4111-1111-1111-1111";
        final String normalizedAccountNumber = "4111111111111111";
        final String expectedToken = "someRandomToken";
        final TokenMapping expectedTokenMapping = new TokenMapping(normalizedAccountNumber, accountNumber, expectedToken);

        given(tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber)).willReturn(
            Optional.empty(),
            Optional.of(new TokenMapping(normalizedAccountNumber, accountNumber, expectedToken))
        );
        given(tokenGenerator.generate()).willReturn(expectedToken);
        given(tokenMappingRepository.save(expectedTokenMapping)).willThrow(DataIntegrityViolationException.class);

        final List<String> tokens = tokenizationService.tokenize(List.of(accountNumber));

        then(tokenMappingRepository).should(times(2)).findByNormalizedAccount(normalizedAccountNumber);
        assertThat(tokens, hasSize(1));
        assertThat(tokens.getFirst(), is(expectedToken));
    }
}
