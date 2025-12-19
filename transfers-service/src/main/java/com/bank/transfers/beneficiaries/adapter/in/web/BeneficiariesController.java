package com.bank.transfers.beneficiaries.adapter.in.web;

import com.bank.transfers.beneficiaries.application.port.in.AddBeneficiaryUseCase;
import com.bank.transfers.beneficiaries.application.port.in.ListBeneficiariesUseCase;
import com.bank.transfers.beneficiaries.application.port.in.VerifyBeneficiaryUseCase;
import com.bank.transfers.beneficiaries.domain.Beneficiary;
import com.bank.transfers.beneficiaries.domain.BeneficiaryId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@Tag(name = "Beneficiaries", description = "Beneficiary management API")
@RequiredArgsConstructor
public class BeneficiariesController {

    private final AddBeneficiaryUseCase addBeneficiaryUseCase;
    private final VerifyBeneficiaryUseCase verifyBeneficiaryUseCase;
    private final ListBeneficiariesUseCase listBeneficiariesUseCase;

    @PostMapping
    @Operation(summary = "Add a new beneficiary")
    public ResponseEntity<BeneficiaryResponse> addBeneficiary(@RequestBody AddBeneficiaryRequest request) {
        AddBeneficiaryUseCase.AddBeneficiaryCommand command = new AddBeneficiaryUseCase.AddBeneficiaryCommand(
            request.customerId(),
            request.tokenizedReference(),
            request.displayName(),
            request.institutionName()
        );

        Beneficiary beneficiary = addBeneficiaryUseCase.addBeneficiary(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(BeneficiaryResponse.from(beneficiary));
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify a beneficiary")
    public ResponseEntity<BeneficiaryResponse> verifyBeneficiary(@PathVariable String id) {
        Beneficiary beneficiary = verifyBeneficiaryUseCase.verifyBeneficiary(BeneficiaryId.of(id));
        return ResponseEntity.ok(BeneficiaryResponse.from(beneficiary));
    }

    @GetMapping
    @Operation(summary = "List beneficiaries for a customer")
    public ResponseEntity<List<BeneficiaryResponse>> listBeneficiaries(@RequestParam String customerId) {
        List<Beneficiary> beneficiaries = listBeneficiariesUseCase.listBeneficiaries(customerId);
        List<BeneficiaryResponse> responses = beneficiaries.stream()
            .map(BeneficiaryResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    // DTOs
    public record AddBeneficiaryRequest(
        String customerId,
        String tokenizedReference,
        String displayName,
        String institutionName
    ) {}

    public record BeneficiaryResponse(
        String beneficiaryId,
        String customerId,
        String status,
        String displayName,
        String institutionName,
        String createdAt
    ) {
        static BeneficiaryResponse from(Beneficiary beneficiary) {
            return new BeneficiaryResponse(
                beneficiary.getBeneficiaryId().toString(),
                beneficiary.getCustomerId(),
                beneficiary.getStatus().name(),
                beneficiary.getDestinationDetails().getDisplayName(),
                beneficiary.getDestinationDetails().getInstitutionName(),
                beneficiary.getCreatedAt().toString()
            );
        }
    }
}

