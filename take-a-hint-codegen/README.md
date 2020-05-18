# take-a-hint-codegen
This module helps you auto-generate reflection configuration for your application which uses GraalVM native-image feature.

## What is all this about

### GraalVM Native Image
Graal is a new just-in-time compiler for the JVM focused on peak performance and multi-language support.
One of its amazing features is `GraalVM native image`.
GraalVM Native Image (or Substrate VM) allows you to ahead-of-time compile Java code to a standalone executable, called a native image.

More on GraalVM native-image : https://www.graalvm.org/docs/reference-manual/native-image/

### Reflection Support
Java reflection support (the java.lang.reflect.* API) enables Java code to examine its own classes, methods and fields and their properties at runtime.

Substrate VM has partial support for reflection and it needs to know ahead of time the reflectively accessed program elements.
Examining and accessing program elements through java.lang.reflect.* or loading classes with Class.forName(String) at run time requires preparing additional metadata for those program elements.

SubstrateVM tries to resolve the target elements through a static analysis that detects calls to the reflection API.
A manual configuration can be specified in order to tell Substrate VM which program elements are reflectively accessed at run time.

More on reflection : https://github.com/oracle/graal/blob/master/substratevm/REFLECTION.md

A more comprehensive list of restrictions can be found in : https://github.com/oracle/graal/blob/master/substratevm/LIMITATIONS.md.

### Why bother ?
take-a-hint uses reflection to handle configuration using annotations.
If you are interested by making your application native-image friendly, you can use this module to auto-generate the necessary reflection configuration.

## How it works
In Maven, you can use one of the options bellow:

- Use `annotationProcessorPaths` with `maven-compiler-plugin` 3.5 or higher:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven-compiler-plugin-version}</version>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>io.hint</groupId>
                <artifactId>take-a-hint-codegen</artifactId>
                <version>0.1-SNAPSHOT</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```
- Add `take-a-hint-codegen` module as dependency with scope `provided` so it don't show up on your artifact dependencies:
```xml
<dependencies>
    <dependency>
        <groupId>io.hint</groupId>
        <artifactId>take-a-hint</artifactId>
        <version>0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>io.hint</groupId>
        <artifactId>take-a-hint-codegen</artifactId>
        <version>0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```