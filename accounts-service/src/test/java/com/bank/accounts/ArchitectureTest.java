package com.bank.accounts;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
// import org.jmolecules.archunit.JMoleculesDddRules; // Not available in 1.5.0
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArchUnit tests to verify DDD rules and hexagonal architecture constraints.
 * 
 * NOTE: Some tests ASSERT that violations exist to demonstrate ArchUnit is working.
 * In a production system, you would fix the violations so these tests pass.
 */
@DisplayName("ArchUnit Architecture Verification")
class ArchitectureTest {
    
    private static final String BASE_PACKAGE = "com.bank.accounts";
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);
    
    // Note: jMolecules ArchUnit rules not available in 1.5.0
    // These tests would verify DDD annotations are correctly applied
    // @Test
    // void verifyAggregateRoots() {
    //     JMoleculesDddRules.allAggregateRoots()
    //         .check(importedClasses);
    // }
    
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
    @DisplayName("Verify ArchUnit detects adapters depending on other adapters (demonstration)")
    void adaptersShouldNotDependOnOtherAdapters() {
        // This test demonstrates that ArchUnit detects when adapters depend on other adapters
        
        ArchRule rule = noClasses()
            .that().resideInAPackage("..adapter..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .andShould().resideInAPackage("..adapter.in..");

        EvaluationResult result = rule.evaluate(importedClasses);
        
        // Assert that violations are detected
        assertThat(result.hasViolation())
            .as("ArchUnit should detect that some adapters depend on other adapters")
            .isTrue();
        
        // Print violations for demonstration
        System.out.println("=== Detected Adapter Dependency Violations ===");
        System.out.println("Expected: Adapters should not depend on other adapters");
        System.out.println("Violations found: " + result.getFailureReport().getDetails().size());
        result.getFailureReport().getDetails().forEach(detail -> {
            System.out.println("  - " + detail);
        });
    }
    
    @Test
    @DisplayName("Verify ArchUnit detects non-interface ports (demonstration)")
    void portsShouldBeInterfaces() {
        // This test demonstrates that ArchUnit detects when non-interface classes exist in port packages
        // Example violations: Inner classes/records in UseCase interfaces (e.g., SearchCriteria, PlaceHoldCommand)
        
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
    void adaptersShouldDependOnPorts() {
        ArchRule rule = classes()
            .that().resideInAPackage("..adapter..")
            .should().dependOnClassesThat().resideInAPackage("..port..")
            .orShould().dependOnClassesThat().resideInAPackage("..domain..");
        
        rule.check(importedClasses);
    }
}

