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

### 1. Constructor Injection

* In the case of constructor-based dependency injection, the container will invoke a constructor with arguments each
  representing a dependency we want to set.

* Spring resolves each argument primarily by type, followed by name of the attribute, and index for disambiguation.

#### Annotations config

```java

@Configuration
public class AppConfig {

    @Bean
    public Item item1() {
        return new ItemImpl1();
    }

    @Bean
    public Store store() {
        return new Store(item1());
    }
}
```

#### XML Based

```xml

<bean id="item1" class="org.baeldung.store.ItemImpl1"/>
<bean id="store" class="org.baeldung.store.Store">
<constructor-arg type="ItemImpl1" index="0" name="item" ref="item1"/>
</bean>
```

### 2. Setter Injection

For setter-based DI,

the container will call setter methods of our class after invoking a no-argument constructor or
no-argument static factory method to instantiate the bean.

#### Annotations config

```java

@Bean
public Store store() {
    Store store = new Store();
    store.setItem(item1());
    return store;
}
```

#### XML Based

```xml

<bean id="store" class="org.baeldung.store.Store">
    <property name="item" ref="item1"/>
</bean>
```

### 3. Field Based Injection

In case of Field-Based DI, we can inject the dependencies by marking them with an @Autowired annotation:

While constructing the Store object, if there’s no constructor or setter method to inject the Item bean, the container
will use reflection to inject Item into Store.

```java
public class Store {
    @Autowired
    private Item item;
}
```