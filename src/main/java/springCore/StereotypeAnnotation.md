# Stereotype Annotations
## 1. @Component

@Component is the generic stereotype annotation.

It tells Spring:

"Create and manage an instance of this class as a Spring bean."

```java
@Component
public class EmailValidator {

    public boolean isValid(String email) {
        return email.contains("@");
    }
}
```

Spring discovers the class through component scanning and registers it as a bean.

When to use

Use @Component when the class:

1. Needs to be managed by Spring.
2. Does not clearly belong to a specific application layer.
3. Represents a general-purpose component.

### 2. @Service

@Service is a specialization of @Component intended for the service/business layer.

```java
@Service
public class PaymentService {

    public void processPayment() {
        // business logic
    }
}
```

It communicates the architectural intention:

    Controller
    ↓
    Service
    ↓
    Repository
**Important**

@Service does not fundamentally create a different type of Spring bean from @Component.

It is primarily a semantic specialization that makes the class's role clear.

### 3. @Repository

@Repository is intended for the data-access layer.

```java
@Repository
public class PaymentRepository {

    public Payment findById(Long id) {
        // database access
        return null;
    }
}
```

It indicates that the class is responsible for interacting with persistent data.

Typical responsibilities include:

* Database operations
* DAO functionality
* Persistence operations
* Query execution

## 4. @Controller

@Controller is a Spring MVC stereotype used for classes that handle web requests.

```java
@Controller
public class PaymentController {

    @GetMapping("/payments")
    public String payments() {
        return "payments";
    }
}

```
It is typically used when the controller returns a view name, such as with server-side rendering.

Conceptually:

    HTTP Request
    ↓
    @Controller
    ↓
    @Service
    ↓
    @Repository
    ↓
    Database

## @RestController 

@RestController is commonly used for REST APIs.

```java
@RestController
public class PaymentController {

    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return paymentService.getPayments();
    }
}
```

@RestController is effectively:

    @Controller
    @ResponseBody

Therefore, return values are written directly to the HTTP response body, typically as JSON.

Difference

| Annotation        | Typical Response             |
| ----------------- | ---------------------------- |
| `@Controller`     | View/template                |
| `@RestController` | Response body, commonly JSON |

## Component Scanning

Stereotype annotations become useful when Spring performs component scanning.

For example:

```java
@SpringBootApplication
public class Application {
}
```

**@SpringBootApplication** includes component-scanning behavior.

Spring scans the relevant packages and discovers classes annotated with stereotypes such as:

    @Component
    @Service
    @Repository
    @Controller

These classes are registered as beans.

### Relation between them 

More precisely, **@Service, @Repository, and @Controller** are specialized forms of **@Component**.

**@RestController** is a composed annotation based on **@Controller and @ResponseBody**.

                        @Component
                            │
            ┌───────────────┼────────────────┐
            ↓               ↓                ↓
    @Service        @Repository      @Controller
                                            │
                                            ↓
                                            @RestController

### @ComponentScan
@ComponentScan tells Spring which packages to scan for component classes and register them as Spring beans.

It works with stereotype annotations such as:

* @Component
* @Service
* @Repository
* @Controller
* @RestController


    @ComponentScan
    ↓
    Scan specified package(s)
    ↓
    Find stereotype-annotated classes
    ↓
    Register them as Spring beans

Example
```java
Basic Syntax
@Configuration
@ComponentScan("com.example")
public class AppConfig {
}

Spring scans com.example and its subpackages for eligible components.
```
Default Scanning Behavior

If no package is explicitly specified:

#### @ComponentScan

the scan starts from the package of the configuration class and scans its subpackages.

Example:

package com.example;

    @Configuration
    @ComponentScan
    public class AppConfig {
    }

Spring scans:
    
    com.example
    com.example.service
    com.example.repository
    com.example.controller
    ...

but not unrelated packages outside that package hierarchy.