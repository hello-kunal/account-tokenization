package nz.co.kunal.tokenization.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(
    name = "token_mappings",
    indexes = {
        @Index(name = "index_token", columnList = "token", unique = true),
        @Index(name = "index_token_normalized_account", columnList = "normalizedAccount", unique = true)
    }
)
public class TokenMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String normalizedAccount;

    @Column(nullable = false)
    private String originalAccount;

    @Column(nullable = false, unique = true)
    private String token;

    public TokenMapping() {
    }

    public TokenMapping(final String normalizedAccount, final String originalAccount, final String token) {
        this.normalizedAccount = normalizedAccount;
        this.originalAccount = originalAccount;
        this.token = token;
    }

    public Long id() {
        return id;
    }

    public String normalizedAccount() {
        return normalizedAccount;
    }

    public String originalAccount() {
        return originalAccount;
    }

    public String token() {
        return token;
    }

    public TokenMapping setId(final Long id) {
        this.id = id;
        return this;
    }

    public TokenMapping setNormalizedAccount(final String normalizedAccount) {
        this.normalizedAccount = normalizedAccount;
        return this;
    }

    public TokenMapping setOriginalAccount(final String originalAccount) {
        this.originalAccount = originalAccount;
        return this;
    }

    public TokenMapping setToken(final String token) {
        this.token = token;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final TokenMapping that)) {
            return false;
        }
        return Objects.equals(id, that.id) && Objects.equals(normalizedAccount, that.normalizedAccount) && Objects.equals(originalAccount, that.originalAccount) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, normalizedAccount, originalAccount, token);
    }

    @Override
    public String toString() {
        return "TokenMapping{" +
            "id=" + id +
            ", normalizedAccount='" + normalizedAccount + '\'' +
            ", originalAccount='" + originalAccount + '\'' +
            ", token='" + token + '\'' +
            '}';
    }
}
