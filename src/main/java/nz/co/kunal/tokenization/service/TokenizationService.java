package nz.co.kunal.tokenization.service;

import nz.co.kunal.tokenization.domain.TokenGenerator;
import nz.co.kunal.tokenization.domain.TokenMapping;
import nz.co.kunal.tokenization.repository.TokenMappingRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenizationService {
    private final TokenMappingRepository tokenMappingRepository;
    private final TokenGenerator tokenGenerator;

    public TokenizationService(final TokenMappingRepository tokenMappingRepository, final TokenGenerator tokenGenerator) {
        this.tokenMappingRepository = tokenMappingRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Transactional
    public List<String> tokenize(final List<String> accounts) {
        List<String> tokens = new ArrayList<>(accounts.size());

        for (String original : accounts) {
            final String normalizedAccountNumber = normalizeAccountNumber(original);

            final Optional<TokenMapping> existing = tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber);
            if (existing.isPresent()) {
                tokens.add(existing.get().token());
                continue;
            }
            tokens.add(createMappingWithRetry(normalizedAccountNumber, original));
        }

        return tokens;
    }

    private String createMappingWithRetry(final String normalizedAccountNumber, final String original) {
        final int maxAttempts = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            final String token = tokenGenerator.generate();

            try {
                final TokenMapping saved = tokenMappingRepository.save(new TokenMapping(normalizedAccountNumber, original, token));
                return saved.token();
            } catch (final DataIntegrityViolationException e) {
                final Optional<TokenMapping> existing = tokenMappingRepository.findByNormalizedAccount(normalizedAccountNumber);
                if (existing.isPresent()) {
                    return existing.get().token();
                }
            }
        }

        throw new IllegalStateException("Failed to create token mapping after retries");
    }

    private String normalizeAccountNumber(final String account) {
        return account.replaceAll("\\D", "");
    }
}
