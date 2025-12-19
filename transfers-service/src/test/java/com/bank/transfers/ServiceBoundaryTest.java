package com.bank.transfers;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Critical test to ensure transfers-service has NO dependencies on accounts-service.
 * This enforces self-contained system principles.
 */
class ServiceBoundaryTest {

    private static final String TRANSFERS_PACKAGE = "com.bank.transfers";
    private static final String ACCOUNTS_PACKAGE = "com.bank.accounts";
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(TRANSFERS_PACKAGE);

    @Test
    void transfersServiceShouldNotDependOnAccountsService() {
        ArchRule rule = noClasses()
            .that().resideInAPackage(TRANSFERS_PACKAGE + "..")
            .should().dependOnClassesThat().resideInAPackage(ACCOUNTS_PACKAGE + "..");

        rule.check(importedClasses);
    }

    @Test
    void transfersServiceShouldNotImportAccountsServiceClasses() {
        ArchRule rule = noClasses()
            .that().resideInAPackage(TRANSFERS_PACKAGE + "..")
            .should().accessClassesThat().resideInAPackage(ACCOUNTS_PACKAGE + "..");

        rule.check(importedClasses);
    }

    @Test
    void transfersServiceShouldNotHaveDirectClassDependenciesOnAccountsService() {
        // Verify that no classes in transfers-service directly reference accounts-service classes
        ArchRule rule = noClasses()
            .that().resideInAPackage(TRANSFERS_PACKAGE + "..")
            .should().dependOnClassesThat()
            .haveSimpleNameContaining("Account")
            .andShould().resideInAPackage(ACCOUNTS_PACKAGE + "..");

        // This is a more lenient check - we allow AccountRef in our own domain
        // but not Account from accounts-service
        rule.check(importedClasses);
    }
}

