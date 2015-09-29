# Fluent

Unit-test Rest applications.

# Build status

[![Build Status](https://api.travis-ci.org/CodeStory/fluent-rest-test.png)](https://travis-ci.org/CodeStory/fluent-rest-test)

# Environment

- `java-1.8`

## Maven

Release versions are deployed on Maven Central:

```xml
<dependency>
  <groupId>net.code-story</groupId>
  <artifactId>fluent-rest-test</artifactId>
  <version>1.6</version>
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
