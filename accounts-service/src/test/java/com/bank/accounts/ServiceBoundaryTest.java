package com.bank.accounts;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Critical test to ensure accounts-service has NO dependencies on transfers-service.
 * This enforces self-contained system principles.
 */
class ServiceBoundaryTest {

    private static final String ACCOUNTS_PACKAGE = "com.bank.accounts";
    private static final String TRANSFERS_PACKAGE = "com.bank.transfers";
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(ACCOUNTS_PACKAGE);

    @Test
    void accountsServiceShouldNotDependOnTransfersService() {
        ArchRule rule = noClasses()
            .that().resideInAPackage(ACCOUNTS_PACKAGE + "..")
            .should().dependOnClassesThat().resideInAPackage(TRANSFERS_PACKAGE + "..");

        rule.check(importedClasses);
    }

    @Test
    void accountsServiceShouldNotImportTransfersServiceClasses() {
        ArchRule rule = noClasses()
            .that().resideInAPackage(ACCOUNTS_PACKAGE + "..")
            .should().accessClassesThat().resideInAPackage(TRANSFERS_PACKAGE + "..");

        rule.check(importedClasses);
    }

    @Test
    void accountsServiceShouldNotHaveDirectClassDependenciesOnTransfersService() {
        // Verify that no classes in accounts-service directly reference transfers-service classes
        ArchRule rule = noClasses()
            .that().resideInAPackage(ACCOUNTS_PACKAGE + "..")
            .should().dependOnClassesThat()
            .haveSimpleNameContaining("Transfer")
            .andShould().resideInAPackage(TRANSFERS_PACKAGE + "..");

        // This is a more lenient check - we allow Transfer-related types in our own domain
        // but not Transfer from transfers-service
        rule.check(importedClasses);
    }
}

