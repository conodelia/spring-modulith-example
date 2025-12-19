package com.bank.accounts;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
// import org.jmolecules.archunit.JMoleculesDddRules; // Not available in 1.5.0
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * ArchUnit tests to verify DDD rules and hexagonal architecture constraints.
 */
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
    void adaptersShouldNotDependOnOtherAdapters() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..adapter..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .andShould().resideInAPackage("..adapter.in..");
        
        rule.check(importedClasses);
    }
    
    @Test
    void portsShouldBeInterfaces() {
        ArchRule rule = classes()
            .that().resideInAPackage("..port..")
            .should().beInterfaces();
        
        rule.check(importedClasses);
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

