package nz.co.kunal.tokenization.repository;

import nz.co.kunal.tokenization.domain.TokenMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenMappingRepository extends JpaRepository<TokenMapping, Long> {

    Optional<TokenMapping> findByNormalizedAccount(final String normalizedAccount);

    Optional<TokenMapping> findByToken(final String token);
}
