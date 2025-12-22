# Spring Modulith Banking Bounded Contexts

This project demonstrates Spring Modulith, ArchUnit, and jMolecules for enforcing DDD and architectural standards in a retail banking domain.

## Architecture

This project contains two Spring Boot services, each implementing a bounded context with multiple submodules following hexagonal architecture:

### Accounts Bounded Context

The Accounts bounded context is split into 7 submodules:

- **lifecycle**: Account lifecycle management (open, close, status changes)
- **posting**: Core transaction posting engine
- **holds**: Hold management (funds holds, freezes)
- **pricing**: Fee and interest calculation
- **balances**: Balance read model (event-driven)
- **statements**: Statement generation (event-driven)
- **history**: Transaction history read model (event-driven)

### Transfers Bounded Context

The Transfers bounded context is split into 5 submodules:

- **initiation**: Transfer creation, submission, authorization, and lifecycle management (Transfer aggregate root)
- **beneficiaries**: Beneficiary enrollment, verification, and management
- **fees**: Transfer fee calculation based on rail type (INTERNAL, INTERAC, ACH, WIRE)
- **routing**: Transfer rail selection logic based on destination, amount, currency, and timing
- **limitspolicy**: Transfer limits and policy enforcement (step-up authentication, risk assessment)

## Running Tests

This project contains two Spring Boot services: `accounts-service` and `transfers-service`. Each service has its own comprehensive test suite.

### Running All Tests

#### For a Specific Service

```bash
# Run all tests for accounts-service
./gradlew :accounts-service:test

# Run all tests for transfers-service
./gradlew :transfers-service:test
```

#### For All Services

```bash
# Run all tests for all services
./gradlew test
```

### Listing All Tests

#### For a Specific Service

```bash
# List all tests in accounts-service
./gradlew :accounts-service:test --dry-run

# List all tests in transfers-service
./gradlew :transfers-service:test --dry-run
```

#### For All Services

```bash
# List all tests across all services
./gradlew test --dry-run
```

### Running Individual Test Classes

#### accounts-service

```bash
# Run ModulithTest
./gradlew :accounts-service:test --tests ModulithTest

# Run ModulithEventsTest
./gradlew :accounts-service:test --tests ModulithEventsTest

# Run ArchitectureTest
./gradlew :accounts-service:test --tests ArchitectureTest

# Run AccountsControllerTest (REST API tests)
./gradlew :accounts-service:test --tests AccountsControllerTest
```

#### transfers-service

```bash
# Run ModulithTest
./gradlew :transfers-service:test --tests ModulithTest

# Run ModulithEventsTest
./gradlew :transfers-service:test --tests ModulithEventsTest

# Run ArchitectureTest
./gradlew :transfers-service:test --tests ArchitectureTest

# Run ServiceBoundaryTest (validates no dependencies between services)
./gradlew :transfers-service:test --tests ServiceBoundaryTest
```

### Running Individual Test Methods

#### accounts-service

```bash
# Run specific test method
./gradlew :accounts-service:test --tests ModulithTest.verifyModuleStructure
./gradlew :accounts-service:test --tests ModulithTest.verifyExpectedModules
./gradlew :accounts-service:test --tests ModulithTest.printModuleStructure
./gradlew :accounts-service:test --tests ModulithEventsTest.verifyEventViolationsDetected
./gradlew :accounts-service:test --tests ArchitectureTest.portsShouldBeInterfaces
./gradlew :accounts-service:test --tests AccountsControllerTest.listAccounts
```

#### transfers-service

```bash
# Run specific test method
./gradlew :transfers-service:test --tests ModulithTest.verifyModuleStructure
./gradlew :transfers-service:test --tests ModulithTest.verifyExpectedModules
./gradlew :transfers-service:test --tests ArchitectureTest.portsShouldBeInterfaces
./gradlew :transfers-service:test --tests ArchitectureTest.adaptersShouldDependOnPorts
./gradlew :transfers-service:test --tests ServiceBoundaryTest.transfersServiceShouldNotDependOnAccountsService
```

### Running Tests by Pattern

```bash
# Run all ModulithTest classes across all services
./gradlew test --tests "*ModulithTest"

# Run all ArchitectureTest classes across all services
./gradlew test --tests "*ArchitectureTest"

# Run all tests matching a pattern
./gradlew test --tests "*Test.verify*"
```

### Test Reports

Test reports are generated in HTML format:

- `accounts-service/build/reports/tests/test/index.html`
- `transfers-service/build/reports/tests/test/index.html`

View reports:

```bash
# Open test report (macOS)
open accounts-service/build/reports/tests/test/index.html
open transfers-service/build/reports/tests/test/index.html

# Open test report (Linux)
xdg-open accounts-service/build/reports/tests/test/index.html
xdg-open transfers-service/build/reports/tests/test/index.html
```

## Test Documentation

**Important Note**: Some tests are intentionally designed to **demonstrate that violations exist** to prove Spring Modulith's and ArchUnit's detection capabilities are working. In a production system, you would fix these violations so the tests pass. These violation-demonstration tests are clearly marked below.

### accounts-service Tests

#### ModulithTest Class

**`verifyModuleStructure()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that Spring Modulith correctly detects architectural violations
- **What it validates**:
  - Cyclic dependencies: Detects the cycle `posting → pricing → posting` (via `PostingType`)
  - Non-exposed type access: Detects `balances` accessing `lifecycle.domain.AccountId` (should use `api` package)
  - Non-exposed type access: Detects `balances` accessing `posting.domain.events.*` (events should be in `api` package)
- **Expected behavior**: Test **asserts violations exist** and prints them to console. Handles test isolation gracefully.
- **In production**: Fix violations by moving types to `api` packages or externalizing events

**`verifyExpectedModules()`**
- **Purpose**: Verifies that all 7 expected modules are detected by Spring Modulith
- **What it validates**:
  - Module detection: Confirms all modules are found (lifecycle, posting, balances, holds, pricing, statements, history)
  - Module count: Ensures at least 7 modules are detected
- **Expected behavior**: Test passes if all modules are detected

**`printModuleStructure()`**
- **Purpose**: Utility test for debugging module structure
- **What it does**: Prints all detected modules to console
- **Usage**: Run when debugging module detection issues

#### ModulithEventsTest Class

**`verifyEventViolationsDetected()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that Spring Modulith detects event-related violations
- **What it validates**:
  - Event externalization: Detects when events from `posting.domain.events` are consumed by other modules
  - API package requirement: Events crossing module boundaries should be in an `api` package or properly externalized
- **Expected behavior**: Test **asserts violations exist** and prints them to console. Handles test isolation gracefully.
- **In production**: Move events to `posting.api.events` or mark them with `@Externalized`

**`verifyModuleStructureExists()`**
- **Purpose**: Verifies all 7 modules can be retrieved by name
- **What it validates**:
  - Module retrieval: Confirms each module (lifecycle, posting, balances, holds, pricing, statements, history) can be accessed
  - Module structure: Validates module structure regardless of violations
- **Expected behavior**: Test passes if all modules are accessible

#### ArchitectureTest Class

**`domainShouldNotDependOnAdapters()`**
- **Purpose**: Verifies domain layer independence
- **What it validates**: Domain classes cannot depend on adapter classes
- **Architectural rule**: Domain should be independent of infrastructure/adapters
- **Expected behavior**: Test passes if domain layer is independent

**`domainShouldNotDependOnApplication()`**
- **Purpose**: Verifies domain layer independence
- **What it validates**: Domain classes cannot depend on application layer classes
- **Architectural rule**: Domain should be independent of application services
- **Expected behavior**: Test passes if domain layer is independent

**`adaptersShouldNotDependOnOtherAdapters()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that ArchUnit detects when adapters depend on other adapters
- **What it validates**: Adapters should not depend on other adapters
- **Architectural rule**: Adapters should be isolated and only depend on ports/domain
- **Expected behavior**: Test **asserts violations exist** and prints them to console
- **In production**: Refactor adapters to remove cross-adapter dependencies

**`portsShouldBeInterfaces()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that ArchUnit detects when non-interface classes exist in port packages
- **What it validates**: All classes in `port` packages must be interfaces
- **Architectural rule**: Ports define contracts, not implementations
- **Expected behavior**: Test **asserts violations exist** (e.g., inner classes/records in UseCase interfaces) and prints them to console
- **In production**: Move non-interface classes out of port packages or convert them to interfaces

**`adaptersShouldDependOnPorts()`**
- **Purpose**: Verifies hexagonal architecture adapter dependencies
- **What it validates**: Adapters must depend on ports or domain classes
- **Architectural rule**: Adapters implement ports and use domain, not other adapters
- **Expected behavior**: Test passes if adapters properly depend on ports or domain

#### ServiceBoundaryTest Class

**`accountsServiceShouldNotDependOnTransfersService()`**
- **Purpose**: Enforces self-contained system principles - ensures accounts-service has no dependencies on transfers-service
- **What it validates**: No classes in `com.bank.accounts` packages should depend on `com.bank.transfers` packages
- **Architectural rule**: Services should be self-contained and communicate via well-defined interfaces (HTTP/REST, events)
- **Expected behavior**: Test passes if no cross-service dependencies exist

**`accountsServiceShouldNotImportTransfersServiceClasses()`**
- **Purpose**: Ensures accounts-service does not import any classes from transfers-service
- **What it validates**: No classes in accounts-service should access classes from transfers-service
- **Expected behavior**: Test passes if no imports from transfers-service exist

**`accountsServiceShouldNotHaveDirectClassDependenciesOnTransfersService()`**
- **Purpose**: Ensures no direct class references to transfers-service classes
- **What it validates**: No classes in accounts-service should have direct dependencies on transfers-service classes (e.g., classes with "Transfer" in name from transfers-service)
- **Expected behavior**: Test passes if no direct class dependencies exist

#### AccountsControllerTest Class

**`getAccounts_shouldReturnListOfAccounts()`**
- **Purpose**: Tests the REST API endpoint `GET /api/accounts`
- **What it validates**:
  - Endpoint returns HTTP 200 OK
  - Response is valid JSON array
  - Response contains account data (accountId, customerId, productType, status)
- **Expected behavior**: Test passes when API correctly returns list of accounts

**`getTransactions_shouldReturnTransactionHistory()`**
- **Purpose**: Tests the REST API endpoint `GET /api/accounts/{accountId}/transactions`
- **What it validates**:
  - Endpoint returns HTTP 200 OK for valid account
  - Response is valid JSON array
  - Account lookup works correctly
- **Expected behavior**: Test passes when API correctly returns transaction history

**`getTransactions_shouldReturn404WhenAccountNotFound()`**
- **Purpose**: Tests error handling for non-existent accounts
- **What it validates**:
  - Endpoint returns HTTP 404 Not Found when account doesn't exist
  - Proper error handling for invalid account IDs
- **Expected behavior**: Test passes when API correctly handles missing accounts

### transfers-service Tests

#### ModulithTest Class

**`verifyModuleStructure()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that Spring Modulith correctly detects architectural violations
- **What it validates**:
  - Cyclic dependencies: Detects the cycle `fees → initiation → fees` (via `TransferPricingService` and `TransferService`)
  - Modules accessing non-exposed (internal) types from other modules
  - Violations of package visibility rules
- **Expected behavior**: Test **asserts violations exist** and prints them to console. Handles test isolation gracefully.
- **In production**: Fix violations by moving types to `api` packages or breaking cycles

**`verifyExpectedModules()`**
- **Purpose**: Verifies that expected modules are detected by Spring Modulith
- **What it validates**:
  - Module detection: Confirms key modules are found (initiation, beneficiaries, fees, etc.)
  - Module count: Ensures at least 2 modules are detected
- **Expected behavior**: Test passes if expected modules are detected

**`printModuleStructure()`**
- **Purpose**: Utility test for debugging module structure
- **What it does**: Prints all detected modules to console
- **Usage**: Run when debugging module detection issues

#### ModulithEventsTest Class

**`verifyEventViolationsDetected()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that Spring Modulith detects event-related violations
- **What it validates**:
  - Event externalization: Detects when events cross module boundaries without proper externalization
  - API package requirement: Events crossing module boundaries should be in an `api` package or properly externalized
- **Expected behavior**: Test **asserts violations exist** and prints them to console. Handles test isolation gracefully.
- **In production**: Move events to appropriate `api` packages or mark them with `@Externalized`

**`verifyModuleStructureExists()`**
- **Purpose**: Verifies that key modules can be retrieved by name
- **What it validates**:
  - Module retrieval: Confirms key modules (initiation, beneficiaries) can be accessed
  - Module structure: Validates module structure regardless of violations
- **Expected behavior**: Test passes if modules are accessible

#### ArchitectureTest Class

**`domainShouldNotDependOnAdapters()`**
- **Purpose**: Verifies domain layer independence
- **What it validates**: Domain classes cannot depend on adapter classes
- **Architectural rule**: Domain should be independent of infrastructure/adapters
- **Expected behavior**: Test passes if domain layer is independent

**`domainShouldNotDependOnApplication()`**
- **Purpose**: Verifies domain layer independence
- **What it validates**: Domain classes cannot depend on application layer classes
- **Architectural rule**: Domain should be independent of application services
- **Expected behavior**: Test passes if domain layer is independent

**`portsShouldBeInterfaces()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that ArchUnit detects when non-interface classes exist in port packages
- **What it validates**: All classes in `port` packages must be interfaces
- **Architectural rule**: Ports define contracts, not implementations
- **Expected behavior**: Test **asserts violations exist** (e.g., `RiskPort` has nested record `RiskAssessment`) and prints them to console
- **In production**: Move non-interface classes out of port packages or convert them to interfaces

**`adaptersShouldDependOnPorts()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that ArchUnit detects when adapters don't properly depend on ports or domain
- **What it validates**: Adapters must depend on ports or domain classes
- **Architectural rule**: Adapters implement ports and use domain, not other adapters
- **Expected behavior**: Test **asserts violations exist** and prints them to console
- **In production**: Refactor adapters to properly depend on ports or domain

#### ServiceBoundaryTest Class

**`transfersServiceShouldNotDependOnAccountsService()`**
- **Purpose**: Enforces self-contained system principles - ensures transfers-service has no dependencies on accounts-service
- **What it validates**: No classes in `com.bank.transfers` packages should depend on `com.bank.accounts` packages
- **Architectural rule**: Services should be self-contained and communicate via well-defined interfaces (HTTP/REST, events)
- **Expected behavior**: Test passes if no cross-service dependencies exist

**`transfersServiceShouldNotImportAccountsServiceClasses()`**
- **Purpose**: Ensures transfers-service does not import any classes from accounts-service
- **What it validates**: No classes in transfers-service should access classes from accounts-service
- **Expected behavior**: Test passes if no imports from accounts-service exist

**`transfersServiceShouldNotHaveDirectClassDependenciesOnAccountsService()`**
- **Purpose**: Ensures no direct class references to accounts-service classes
- **What it validates**: No classes in transfers-service should have direct dependencies on accounts-service classes (e.g., classes with "Account" in name from accounts-service)
- **Expected behavior**: Test passes if no direct class dependencies exist

### 3. Module Detection

Spring Modulith automatically detects modules based on package structure:

```
com.bank.accounts/
├── lifecycle/          → Module: lifecycle
├── posting/            → Module: posting
├── holds/
│   └── api/            → Published API (accessible from other modules)
├── pricing/
│   └── api/            → Published API (accessible from other modules)
├── balances/           → Module: balances
├── statements/         → Module: statements
└── history/            → Module: history
```

#### Modulith Marker Interfaces

Each bounded context uses a marker interface annotated with `@Modulith` to enable Spring Modulith analysis:

**Accounts Bounded Context:**
```java
package com.bank.accounts;

import org.springframework.modulith.Modulith;

@Modulith
public interface AccountsModule {
}
```

**Transfers Bounded Context:**
```java
package com.bank.transfers;

import org.springframework.modulith.Modulith;

@Modulith
public interface TransfersModule {
}
```

**Purpose:**
- **Mark the package as a Modulith application**: The `@Modulith` annotation tells Spring Modulith to analyze the package structure
- **Enable architectural verification**: Spring Modulith uses this to detect modules and enforce architectural boundaries
- **Do NOT need implementations**: These are marker interfaces - they are not meant to be implemented by any classes

**How it works:**
- The `@Modulith` annotation is processed by Spring Modulith at runtime/compile time
- Spring Modulith scans the package structure starting from the marker interface's package (e.g., `com.bank.accounts.*`)
- The framework identifies submodules based on first-level subdirectories under the base package
- In tests, you reference the `@SpringBootApplication` class (e.g., `AccountsApplication.class`), not the marker interface

**Location in codebase:**
- `accounts-service/src/main/java/com/bank/accounts/AccountsModule.java`
- `transfers-service/src/main/java/com/bank/transfers/TransfersModule.java`

### 4. API Package Rules

Spring Modulith enforces that:
- Only packages named `api` are accessible from other modules
- Internal packages (`domain`, `application`, `adapter`) are private
- Cross-module access must go through published APIs

Example:
- ✅ `posting` can access `holds.api.HoldsQueryPort`
- ❌ `posting` cannot access `holds.domain.Hold`

### 5. Event-Driven Communication

Modules communicate via events:
- **Publishing**: Use `EventPublicationRegistry` to publish events
- **Consuming**: Use `@ApplicationModuleEventListener` to listen to events
- **Externalization**: Events crossing module boundaries must be `@Externalized`

### 6. Generate Documentation

Generate module documentation with diagrams:

```bash
# For accounts-service
./gradlew :accounts-service:test --tests ModulithTest.generateDocumentation

# For transfers-service
./gradlew :transfers-service:test --tests ModulithTest.generateDocumentation

# For all services
./gradlew test --tests ModulithTest.generateDocumentation
```

This creates documentation in `build/spring-modulith-docs/` (relative to each service directory). The following document types are generated:

#### Document Types

1. **`all-docs.adoc`** - Master AsciiDoc file
   - Aggregates all module documentation into a single document
   - Includes PlantUML diagram references for rendering
   - Can be processed with AsciiDoc tooling to generate HTML/PDF documentation
   - Contains sections for each module with its diagrams and metadata

2. **`components.puml`** - Application-level C4 Component Diagram
   - PlantUML diagram showing the complete application structure
   - Uses C4 model notation for component visualization
   - Displays all modules and their inter-module relationships
   - Shows dependency types: "uses", "depends on", "listens to" (event-driven)
   - Provides a high-level architectural overview of the entire application

3. **`module-{name}.adoc`** - Module Metadata Files (AsciiDoc)
   - One file per module containing structured metadata
   - Includes:
     - Base package name for the module
     - Bean references (dependencies on beans from other modules)
     - Module dependencies and relationships
   - Formatted as AsciiDoc tables for easy reading

4. **`module-{name}.puml`** - Module-level C4 Component Diagrams
   - PlantUML diagram for each individual module
   - Uses C4 component model notation
   - Shows the module's internal structure and boundaries
   - Displays how the module relates to the overall application context
   - Useful for understanding individual module architecture

#### Example Generated Files

**accounts-service** generates:
- `all-docs.adoc`, `components.puml`
- `module-lifecycle.adoc`, `module-lifecycle.puml`
- `module-posting.adoc`, `module-posting.puml`
- `module-balances.adoc`, `module-balances.puml`
- `module-holds.adoc`, `module-holds.puml`
- `module-pricing.adoc`, `module-pricing.puml`
- `module-statements.adoc`, `module-statements.puml`
- `module-history.adoc`, `module-history.puml`
- `module-adapter.adoc`, `module-adapter.puml`
- `module-config.adoc`, `module-config.puml`

**transfers-service** generates similar files for its modules (initiation, beneficiaries, fees, routing, limitspolicy, config).

#### Viewing the Documentation

- **PlantUML files (`.puml`)**: Can be rendered using PlantUML tools, IDEs with PlantUML plugins, or online viewers
- **AsciiDoc files (`.adoc`)**: Can be processed with AsciiDoc/Asciidoctor to generate HTML, PDF, or other formats
- **Master document**: Process `all-docs.adoc` to generate a complete documentation set with all diagrams embedded

### 7. Continuous Validation

Add to your CI/CD pipeline:

```yaml
# Example GitHub Actions
- name: Verify Spring Modulith
  run: ./gradlew test --tests ModulithTest
```

### 8. Manual Verification

You can also programmatically verify:

```java
ApplicationModules modules = ApplicationModules.of(AccountsApplication.class);
modules.verify(); // Throws exception if violations found
```

### 9. Common Violations

Spring Modulith will fail if:

1. **Direct dependency on internal package**:
   ```java
   // ❌ This will fail
   import com.bank.accounts.holds.domain.Hold;
   ```

2. **Cyclic dependency**:
   ```
   posting → holds → posting (cycle!)
   ```

3. **Missing @Externalized on events**:
   ```java
   // ❌ Event crossing module boundary without @Externalized
   @DomainEvent
   public record SomeEvent(...) {}
   ```

4. **Accessing non-api package**:
   ```java
   // ❌ Cannot access internal package
   import com.bank.accounts.pricing.application.service.PricingService;
   ```

### 10. Debugging Module Structure

Print the detected module structure:

```bash
./gradlew test --tests ModulithTest.printModuleStructure
```

## Running the Application

```bash
./gradlew bootRun
```

- API: http://localhost:8080/api/accounts
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

## Project Structure

```
accounts-service/
├── src/main/java/com/bank/accounts/
│   ├── lifecycle/          # Account lifecycle module
│   ├── posting/            # Transaction posting module
│   ├── holds/
│   │   └── api/            # Published API for holds
│   ├── pricing/
│   │   └── api/            # Published API for pricing
│   ├── balances/           # Balance read model
│   ├── statements/         # Statement generation
│   └── history/            # Transaction history
└── src/test/java/
    ├── ModulithTest.java           # Spring Modulith verification
    ├── ModulithEventsTest.java     # Event verification
    └── ArchitectureTest.java       # ArchUnit + jMolecules verification
```

## Key Technologies

- **Spring Modulith**: Module boundaries and verification
- **jMolecules**: DDD annotations (@AggregateRoot, @Entity, @ValueObject, etc.)
- **ArchUnit**: Architectural rule verification
- **Spring Modulith Events**: In-process event handling

