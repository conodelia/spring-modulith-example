#!/bin/bash

# Script to validate Spring Modulith concerns
# Usage: ./validate-modulith.sh

set -e

echo "=========================================="
echo "Spring Modulith Validation"
echo "=========================================="
echo ""

echo "1. Running Modulith verification tests..."
./gradlew test --tests ModulithTest

echo ""
echo "2. Running Modulith events tests..."
./gradlew test --tests ModulithEventsTest

echo ""
echo "3. Running Architecture tests (ArchUnit + jMolecules)..."
./gradlew test --tests ArchitectureTest

echo ""
echo "4. Generating module documentation..."
./gradlew test --tests ModulithTest.generateDocumentation

echo ""
echo "=========================================="
echo "✅ All validations passed!"
echo "=========================================="
echo ""
echo "Documentation generated in: accounts-service/target/spring-modulith-docs/"
echo ""

