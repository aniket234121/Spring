# SpringBoot and Extra Things

@SpringBootApplication is the main configuration annotation used in a Spring Boot application. It is typically placed on the application's main class.

```java
@SpringBootApplication
public class MyApplication {


    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```
### What does @SpringBootApplication do?

@SpringBootApplication is a composed annotation that combines three important annotations:

* @SpringBootConfiguration
* @EnableAutoConfiguration
* @ComponentScan

### current version
| Technology           | Current stable version | Minimum Java version | Key point                       |
| -------------------- | ---------------------: | -------------------: | ------------------------------- |
| **Spring Framework** |              **7.0.8** |          **Java 17** | Core Spring Framework version   |
| **Spring Boot**      |              **4.1.0** |          **Java 17** | Built on Spring Framework 7.0.x |

### Can we perform Dependency Injection without @Autowired.

#### 1. Constructor Injection — no @Autowired

This is the most important example:

```java
@Service
public class OrderService {


    private final PaymentService paymentService;


    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```
You don't need: @Autowired

Spring Boot/Spring Framework recognizes the single constructor and injects the required PaymentService bean.

If OrderService has only one constructor, Spring automatically uses that constructor for dependency injection.

#### 2. @Bean method parameters — no @Autowired

You can also perform DI through a configuration class:

```java

@Configuration
public class AppConfig {


    @Bean
    public PaymentService paymentService() {
        return new PaymentService();
    }


    @Bean
    public OrderService orderService(PaymentService paymentService) {
        return new OrderService(paymentService);
    }
}
```
Here:

    public OrderService orderService(PaymentService paymentService)

Spring sees that PaymentService is required and automatically provides the corresponding bean.

No @Autowired is needed.

## @Import

@Import is used to import one or more configuration classes, component classes, or registered bean definitions into the current Spring ApplicationContext.

It is commonly used to modularize configuration instead of putting everything into one @Configuration class.

Syntax

    @Import(ConfigurationClass.class)

Multiple classes can be imported:

    @Import({DatabaseConfig.class, SecurityConfig.class})

#### Why use @Import?

Suppose an application has separate configurations:

    Application
    ├── DatabaseConfig
    ├── SecurityConfig
    └── PaymentConfig

Instead of component-scanning everything, you can explicitly import the required configurations:

```java
@Configuration
@Import({
DatabaseConfig.class,
SecurityConfig.class,
PaymentConfig.class
})
public class AppConfig {
}

```
Spring registers the imported configurations and their beans in the same ApplicationContext.

