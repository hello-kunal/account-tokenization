package nz.co.kunal.tokenization.api;

import nz.co.kunal.tokenization.service.TokenizationService;
import nz.co.kunal.tokenization.validator.AccountNumberValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TokenizationController {
    private final AccountNumberValidator accountNumberValidator;
    private final TokenizationService tokenizationService;

    public TokenizationController(final AccountNumberValidator accountNumberValidator, final TokenizationService tokenizationService) {
        this.accountNumberValidator = accountNumberValidator;
        this.tokenizationService = tokenizationService;
    }

    @PostMapping("/tokenize")
    public ResponseEntity<List<String>> tokenize(@RequestBody List<String> accounts) {
        accountNumberValidator.validate(accounts);
        final List<String> tokenizedAccounts = tokenizationService.tokenize(accounts);
        return ResponseEntity.ok(tokenizedAccounts);
    }
}
