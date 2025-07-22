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

## 2. Spring IoC Container

In the Spring framework, the interface **ApplicationContext** represents the IoC container.

What Does It Do?

1. Reads configuration (XML or annotations)
2. Creates and manages beans
3. Wires dependencies between beans
4. Cleans up beans when the app shuts down

#### ApplicationContext implementations

| Class                                  | Usage                                      |
|----------------------------------------|--------------------------------------------|
| **AnnotationConfigApplicationContext** | Java/Annotation-based config (modern apps) |
| **ClassPathXmlApplicationContext**     | Reads from XML file inside project         |
| **FileSystemXmlApplicationContext**    | Reads XML config from file system          |
| **WebApplicationContext**              | Used in web applications (Spring MVC)      |

The Spring container is responsible for instantiating, configuring and assembling
objects known as beans, as well as managing their life cycles.

#### Spring Configuration
Spring needs configuration to know what beans to create, how to inject dependencies, and how to manage them.

| Type                   | How You Define Beans                              | Modern Usage |
|------------------------|---------------------------------------------------|--------------|
| **XML Configuration**  | In an XML file (`applicationContext.xml`)         | Legacy       |
| **Java Configuration** | Using `@Configuration` and `@Bean`                | ✅ Preferred  |
| **Annotation-Based**   | Using annotations like `@Component`, `@Autowired` | ✅ Preferred  |


## 3. Dependency Injection

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

