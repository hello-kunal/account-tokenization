package nz.co.kunal.tokenization.service;

import nz.co.kunal.tokenization.domain.TokenMapping;
import nz.co.kunal.tokenization.exception.UnknownTokenException;
import nz.co.kunal.tokenization.repository.TokenMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeTokenizationService {
    private final TokenMappingRepository tokenMappingRepository;

    public DeTokenizationService(final TokenMappingRepository tokenMappingRepository) {
        this.tokenMappingRepository = tokenMappingRepository;
    }

    @Transactional(readOnly = true)
    public List<String> detokenize(List<String> tokens) {
        final List<String> accounts = new ArrayList<>(tokens.size());

        for (String token : tokens) {
            final String account = tokenMappingRepository.findByToken(token)
                .map(TokenMapping::originalAccount)
                .orElseThrow(() -> new UnknownTokenException("Unknown token: " + token));
            accounts.add(account);
        }
        return accounts;
    }
}
