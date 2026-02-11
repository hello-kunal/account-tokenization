package nz.co.kunal.tokenization.api;

import nz.co.kunal.tokenization.service.DeTokenizationService;
import nz.co.kunal.tokenization.validator.TokenValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeTokenizationController {
    private final TokenValidator tokenValidator;
    private final DeTokenizationService deTokenizationService;

    public DeTokenizationController(final TokenValidator tokenValidator, final DeTokenizationService deTokenizationService) {
        this.tokenValidator = tokenValidator;
        this.deTokenizationService = deTokenizationService;
    }

    @PostMapping("/detokenize")
    public ResponseEntity<List<String>> detokenize(@RequestBody List<String> tokens) {
        tokenValidator.validate(tokens);
        final List<String> accounts = deTokenizationService.detokenize(tokens);
        return ResponseEntity.ok(accounts);
    }
}
