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
