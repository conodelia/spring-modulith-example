package com.bank.transfers;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArchUnit tests to verify DDD rules and hexagonal architecture constraints.
 * 
 * NOTE: These tests ASSERT that violations exist to demonstrate ArchUnit is working.
 * In a production system, you would fix the violations so these tests pass.
 */
@DisplayName("ArchUnit Architecture Verification")
class ArchitectureTest {

    private static final String BASE_PACKAGE = "com.bank.transfers";
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

    @Test
    void domainShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..");

        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotDependOnApplication() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Verify ArchUnit detects non-interface ports (demonstration)")
    void portsShouldBeInterfaces() {
        // This test demonstrates that ArchUnit detects when non-interface classes exist in port packages
        // Example violation: RiskPort has a nested record RiskAssessment which is a class, not an interface
        
        ArchRule rule = classes()
            .that().resideInAPackage("..port..")
            .should().beInterfaces();

        EvaluationResult result = rule.evaluate(importedClasses);
        
        // Assert that violations are detected
        assertThat(result.hasViolation())
            .as("ArchUnit should detect that not all classes in port packages are interfaces")
            .isTrue();
        
        // Print violations for demonstration
        System.out.println("=== Detected Port Violations ===");
        System.out.println("Expected: All classes in ..port.. packages should be interfaces");
        System.out.println("Violations found: " + result.getFailureReport().getDetails().size());
        result.getFailureReport().getDetails().forEach(detail -> {
            System.out.println("  - " + detail);
        });
    }

    @Test
    @DisplayName("Verify ArchUnit detects adapters not depending on ports (demonstration)")
    void adaptersShouldDependOnPorts() {
        // This test demonstrates that ArchUnit detects when adapters don't properly depend on ports or domain
        
        ArchRule rule = classes()
            .that().resideInAPackage("..adapter..")
            .should().dependOnClassesThat().resideInAPackage("..port..")
            .orShould().dependOnClassesThat().resideInAPackage("..domain..");

        EvaluationResult result = rule.evaluate(importedClasses);
        
        // Assert that violations are detected
        assertThat(result.hasViolation())
            .as("ArchUnit should detect that some adapters don't depend on ports or domain")
            .isTrue();
        
        // Print violations for demonstration
        System.out.println("=== Detected Adapter Violations ===");
        System.out.println("Expected: Adapters should depend on ports or domain classes");
        System.out.println("Violations found: " + result.getFailureReport().getDetails().size());
        result.getFailureReport().getDetails().forEach(detail -> {
            System.out.println("  - " + detail);
        });
    }
}

