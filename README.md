# Spring Modulith Accounts Bounded Context

This project demonstrates Spring Modulith, ArchUnit, and jMolecules for enforcing DDD and architectural standards in a retail banking domain.

## Architecture

The Accounts bounded context is split into 7 submodules following hexagonal architecture:

- **lifecycle**: Account lifecycle management (open, close, status changes)
- **posting**: Core transaction posting engine
- **holds**: Hold management (funds holds, freezes)
- **pricing**: Fee and interest calculation
- **balances**: Balance read model (event-driven)
- **statements**: Statement generation (event-driven)
- **history**: Transaction history read model (event-driven)

## Validating Spring Modulith Concerns

### 1. Run the Modulith Tests

The primary way to validate Spring Modulith concerns is through the test suite:

```bash
# Run all Modulith tests
./gradlew test --tests ModulithTest

# Run specific test
./gradlew test --tests ModulithTest.verifyModuleStructure

# Run all tests including Modulith verification
./gradlew test
```

### 2. What Gets Validated

The test suite validates architectural concerns across three test classes:

- **ModulithTest**: Spring Modulith module boundaries, API visibility, and dependency rules
- **ModulithEventsTest**: Event-driven communication patterns and event externalization
- **ArchitectureTest**: Hexagonal architecture constraints and DDD rules via ArchUnit

**Important Note**: Some tests are intentionally designed to **demonstrate that violations exist** to prove Spring Modulith's detection capabilities are working. In a production system, you would fix these violations so the tests pass. These violation-demonstration tests are clearly marked below.

### 2.1 Detailed Test Breakdown

#### ModulithTest Class

**`verifyModuleStructure()`** - **Violation Demonstration Test**
- **Purpose**: Demonstrates that Spring Modulith correctly detects architectural violations
- **What it validates**:
  - Cyclic dependencies: Detects the cycle `posting → pricing → posting` (via `PostingType`)
  - Non-exposed type access: Detects `balances` accessing `lifecycle.domain.AccountId` (should use `api` package)
  - Non-exposed type access: Detects `balances` accessing `posting.domain.events.*` (events should be in `api` package)
- **Expected behavior**: Test **asserts violations exist** and prints them to console
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
- **Expected behavior**: Test **asserts violations exist** and prints them to console
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

**`domainShouldNotDependOnApplication()`**
- **Purpose**: Verifies domain layer independence
- **What it validates**: Domain classes cannot depend on application layer classes
- **Architectural rule**: Domain should be independent of application services

**`adaptersShouldNotDependOnOtherAdapters()`**
- **Purpose**: Verifies adapter isolation
- **What it validates**: Adapters should not depend on other adapters
- **Architectural rule**: Adapters should be isolated and only depend on ports/domain

**`portsShouldBeInterfaces()`**
- **Purpose**: Verifies hexagonal architecture port contracts
- **What it validates**: All classes in `port` packages must be interfaces
- **Architectural rule**: Ports define contracts, not implementations

**`adaptersShouldDependOnPorts()`**
- **Purpose**: Verifies hexagonal architecture adapter dependencies
- **What it validates**: Adapters must depend on ports or domain classes
- **Architectural rule**: Adapters implement ports and use domain, not other adapters

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
./gradlew test --tests ModulithTest.generateDocumentation
```

This creates documentation in `target/spring-modulith-docs/` including:
- Module structure diagrams (UML)
- C4 component diagrams
- Dependency graphs

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

