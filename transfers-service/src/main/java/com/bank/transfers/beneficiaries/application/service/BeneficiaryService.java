package com.bank.transfers.beneficiaries.application.service;

import com.bank.transfers.beneficiaries.application.port.in.AddBeneficiaryUseCase;
import com.bank.transfers.beneficiaries.application.port.in.ListBeneficiariesUseCase;
import com.bank.transfers.beneficiaries.application.port.in.VerifyBeneficiaryUseCase;
import com.bank.transfers.beneficiaries.application.port.out.BeneficiaryRepositoryPort;
import com.bank.transfers.beneficiaries.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaryService implements AddBeneficiaryUseCase, VerifyBeneficiaryUseCase, ListBeneficiariesUseCase {

    private final BeneficiaryRepositoryPort repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Beneficiary addBeneficiary(AddBeneficiaryCommand command) {
        DestinationDetails destinationDetails = DestinationDetails.of(
            command.tokenizedReference(),
            command.displayName(),
            command.institutionName()
        );

        Beneficiary beneficiary = Beneficiary.enroll(
            BeneficiaryId.generate(),
            command.customerId(),
            destinationDetails
        );

        beneficiary = repository.save(beneficiary);
        publishDomainEvents(beneficiary);
        return beneficiary;
    }

    @Override
    public Beneficiary verifyBeneficiary(BeneficiaryId beneficiaryId) {
        Beneficiary beneficiary = repository.findById(beneficiaryId)
            .orElseThrow(() -> new IllegalArgumentException("Beneficiary not found: " + beneficiaryId));

        beneficiary.verify();
        beneficiary = repository.save(beneficiary);
        publishDomainEvents(beneficiary);
        return beneficiary;
    }

    @Override
    public List<Beneficiary> listBeneficiaries(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    private void publishDomainEvents(Beneficiary beneficiary) {
        List<Object> events = beneficiary.getDomainEvents();
        events.forEach(event -> eventPublisher.publishEvent(event));
        beneficiary.clearDomainEvents();
    }
}

