# Spring Core

## 1. Inversion Of Control

Inversion of Control is a principle in software engineering which transfers the control
of objects or portions of a program to a container or framework.

The advantages of this architecture are:

* decoupling the execution of a task from its implementation
* making it easier to switch between different implementations
* greater modularity of a program
* greater ease in testing a program by isolating a component or mocking its dependencies.

Instead of:

You are creating and passing dependencies manually.

    Car car = new Car();

We let Spring handle it:

Spring creates , injects it, and gives it to you.

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    Car car = context.getBean("car", Car.class);

📘 Benefit:

* Loose coupling
* Better separation of concerns

Example:
Control of Creation (Dependency Injection)
When developers talk about IoC today, they are usually talking about Dependency Injection (DI). This inverts how objects get the other objects they need to function.

Traditional Control (No IoC):
A Car object needs an Engine object to work. In a traditional setup, the Car is responsible for building its own engine.

```Java
class Car {
private V8Engine engine;

    public Car() {
        // The Car is tightly coupled to a specific V8Engine.
        // It controls the creation of its dependency.
        this.engine = new V8Engine(); 
    }

    public void drive() {
        engine.start();
    }
}
```

The problem: If you want to test the car without actually firing up a real database/engine, or if you want to build an electric car, you have to rewrite the Car class.

Inversion of Control:
Instead of the Car building the engine, we invert the control. We build the engine somewhere else and hand it (inject it) to the car.

```java

// We use an interface so the Car doesn't care what kind of engine it gets
interface Engine {
void start();
}

class Car {
private Engine engine;

    // Control is inverted! The Car no longer creates the engine.
    // An outside system passes the engine in.
    public Car(Engine engine) {
        this.engine = engine; 
    }

    public void drive() {
        engine.start();
    }
}
```
Now, some other part of your application (often an "IoC Container" like Spring in Java ) handles the assembly:

## 2. Spring IoC Container

The Spring IoC Container is responsible for managing application objects called Beans.

Its major responsibilities are:

1. Create objects
2. Configure objects
3. Resolve dependencies
4. Inject dependencies
5. Manage bean lifecycle
6. Manage bean scopes

### Spring provide two major container interfaces:
Spring provides two main container interfaces:

1. BeanFactory [note](./IOCcontainers/BeanFactory.md)
2. ApplicationContext [ note](./IOCcontainers/ApplicationContext.md)

```

    IoC Container
    │
    ├── BeanFactory
    │
    └── ApplicationContext
    │
    ├── ClassPathXmlApplicationContext
    ├── FileSystemXmlApplicationContext
    ├── AnnotationConfigApplicationContext
    └── WebApplicationContext

```
| Feature | `BeanFactory` | `ApplicationContext` |
| :--- | :--- | :--- |
| **Initialization** | **Lazy Loading:** Beans are only created when you explicitly ask for them using `getBean()`. | **Eager Loading:** Singleton beans are created immediately when the application starts up. |
| **Memory Footprint** | Extremely lightweight, as it only loads what is strictly necessary. | Heavier, as it pre-loads beans and extra enterprise features. |
| **Event Publishing** | Not supported. | Supported via `ApplicationEventPublisher` (e.g., `ContextRefreshedEvent`). |
| **Internationalization (i18n)** | Not supported. | Supported via `MessageSource` for multiple languages. |
| **Annotation Support** | Requires manual registration of post-processors (like `@Autowired`). | Automatically registers post-processors for seamless annotation support. |


#### Spring Configuration

Spring needs configuration to know what beans to create, how to inject dependencies, and how to manage them.

| Type                   | How You Define Beans                              | Modern Usage |
|------------------------|---------------------------------------------------|--------------|
| **XML Configuration**  | In an XML file (`applicationContext.xml`)         | Legacy       |
| **Java Configuration** | Using `@Configuration` and `@Bean`                | ✅ Preferred  |
| **Annotation-Based**   | Using annotations like `@Component`, `@Autowired` | ✅ Preferred  |

## 3. Dependency Injection 
[Detailed notes](./DependencyInjection/DependencyInjection.md)

Dependency Injection means:

Giving an object what it needs (its dependencies) from outside rather than letting it create them itself.

Dependency injection is a pattern we can use to implement IoC,
where the control being inverted is setting an object’s dependencies.

Traditional way:- tightly coupled, hard to test or change.

```
    public class Store {
    private Item item;
 
    public Store() {
        item = new Item();    // tight coupling
        }
    }
```

with Dependency injection

* Store doesn't care where item come from
* item is injected into store

```

    public class Store {
        private Item item;
        public Store(Item item) {
            this.item = item;
        }
    }

```

### Types of Dependency Injection

| Type                      | How it works                        | Example                        |
|---------------------------|-------------------------------------|--------------------------------|
| **Constructor Injection** | Dependency is passed in constructor | ✅ Recommended                  |
| **Setter Injection**      | Dependency is set via setter method | ✔️ Optional                    |
| **Field Injection**       | Spring directly sets the field      | ⚠️ Not recommended for testing |

## Stereotype Annotation 
[Detailed Notes](./StereotypeAnnotation.md)

Stereotype annotations are annotations that identify a Java class as a Spring-managed component and, in some cases, indicate the role of that component within the application architecture.

They allow Spring to discover classes automatically through component scanning and register them as beans in the IoC container.

    Stereotype Annotation
    ↓
    Class identified as Spring component
    ↓
    Component Scanning
    ↓
    Bean registered in IoC Container

### Main Stereotype Annotation

| Annotation    | Typical Role                     | Layer                  |
| ------------- | -------------------------------- | ---------------------- |
| `@Component`  | Generic Spring-managed component | Any/general            |
| `@Service`    | Business/service logic           | Service layer          |
| `@Repository` | Data access/persistence          | Repository/DAO layer   |
| `@Controller` | Handles web requests             | Presentation/Web layer |

There are also specialized web stereotypes such as:

Annotation	Typical Role
@RestController	REST API controller


## Bean

A Spring Bean is an object whose creation, configuration, dependency injection, lifecycle, and scope are managed by the Spring IoC Container.

In normal Java, we create objects ourselves:

    PaymentService service = new PaymentService();

With Spring, the container creates and manages the object:

    PaymentService service = context.getBean(PaymentService.class);

When an object becomes a Spring Bean, Spring can manage several aspects of that object:

    Spring Bean
    │
    ├── Object creation
    ├── Dependency Injection
    ├── Configuration
    ├── Scope
    ├── Initialization
    ├── Lifecycle callbacks
    └── Destruction
[Bean Detailed Notes](../springCore/Beans/Bean.md)

## Autowired
Autowiring is a Spring Dependency Injection mechanism in which the Spring IoC container automatically identifies and injects the required dependency into a bean

### @Autowired
@Autowired is a Spring annotation used for dependency injection (DI). It tells the Spring IoC container to automatically find a suitable bean and inject it into the required dependency.

It is primarily resolved by type.

@Autowired can be applied to:

* Constructor
* Setter method
* Field
* General methods

Constructor Injection — Recommended

```java
@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    @Autowired
    public PaymentService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
}
```

If the class has only one constructor, @Autowired can be omitted:

```java
@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
}
```
### @Autowired Resolves Dependencies

Suppose:

    public interface PaymentProcessor {
    void process();
    }

and:

```java
@Component
public class CardPaymentProcessor implements PaymentProcessor {
}
@Component
public class UpiPaymentProcessor implements PaymentProcessor {
}

```
There are now two beans of type PaymentProcessor.

If Spring encounters:

```java
@Autowired
public PaymentService(PaymentProcessor paymentProcessor) {
this.paymentProcessor = paymentProcessor;
}

```
Spring cannot determine which implementation should be injected.

This results in an ambiguity error such as:

NoUniqueBeanDefinitionException

To resolve multiple candidates, Spring commonly uses:

* @Primary
* @Qualifier
* Bean name / other resolution rules

### 3. @Primary

@Primary tells Spring:

"When multiple beans of this type are available, prefer this bean by default."

Example:

```java
@Component
@Primary
public class CardPaymentProcessor implements PaymentProcessor {
}
@Component
public class UpiPaymentProcessor implements PaymentProcessor {
}

```
Now:

```java
@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    public PaymentService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
}
```

Spring finds two PaymentProcessor beans but chooses:

CardPaymentProcessor

because it is marked @Primary.

### @Qualifier

@Qualifier is used when you want to explicitly specify which bean should be injected.

Example:

```java
@Component("cardProcessor")
public class CardPaymentProcessor implements PaymentProcessor {
}
@Component("upiProcessor")
public class UpiPaymentProcessor implements PaymentProcessor {
}

```
Then:

```java
@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;

    public PaymentService(
            @Qualifier("upiProcessor") PaymentProcessor paymentProcessor) {

        this.paymentProcessor = paymentProcessor;
    }
}

```
Spring injects:

    UpiPaymentProcessor

even if another PaymentProcessor exists.

@Qualifier is more specific than @Primary

For example:

```java
@Component
@Primary
public class CardPaymentProcessor implements PaymentProcessor {
}
@Component("upiProcessor")
public class UpiPaymentProcessor implements PaymentProcessor {
}

```
This:

```java
public PaymentService(
@Qualifier("upiProcessor") PaymentProcessor processor) {
}

```
still injects:

    UpiPaymentProcessor

The explicit qualifier takes precedence over the default @Primary choice.
### Modes

| Autowire Mode | Description                                                     | Resolution                                 | Usage                              |
| ------------- | --------------------------------------------------------------- | ------------------------------------------ | ---------------------------------- |
| `no`          | No automatic dependency injection                               | Dependencies must be configured explicitly | Default                            |
| `byName`      | Injects dependency by matching **property name** with bean name | By bean name                               | Legacy XML configuration           |
| `byType`      | Injects dependency based on the **property type**               | By type                                    | Legacy XML configuration           |
| `constructor` | Injects dependencies through the constructor                    | By constructor argument type               | Legacy XML configuration           |
| `autodetect`  | Automatically chooses between constructor and `byType`          | Automatic                                  | **Removed/deprecated; do not use** |
