# Fluent

Unit-test Rest applications.

# Build status

[![Java CI with Maven](https://github.com/CodeStory/fluent-rest-test/actions/workflows/maven.yml/badge.svg)](https://github.com/CodeStory/fluent-rest-test/actions/workflows/maven.yml)

# Environment

- `java-1.8`

## Maven

Release versions are deployed on Maven Central:

```xml
<dependency>
  <groupId>net.code-story</groupId>
  <artifactId>fluent-rest-test</artifactId>
  <version>1.7</version>
</dependency>
```

# Build

```bash
mvn clean verify
```

# Deploy on Maven Central

Build the release:

```bash
mvn release:clean release:prepare release:perform
```
