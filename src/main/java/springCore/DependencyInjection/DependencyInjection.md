# Dependency Injection

    Dependency Injection (values that can be injected through constructor or setter)
    │
    ├── 1. Primitive / String-based values
    │      ├── String
    │      ├── int
    │      ├── double
    │      └── boolean
    │
    ├── 2. Dependent objects
    │      └── One bean depends on another bean
    │
    └── 3. Collection values
    ├── List
    ├── Set
    ├── Map
    └── Properties

* [Dependecy injection XML Based Config all Examples](../DependencyInjection/XMLBasedDI.md)
* [Dependecy injection Java Based Config all Examples](../DependencyInjection/JavaConfigBasedDI.md)
* [Dependecy injection Annotation Based Config all Examples](../DependencyInjection/AnnotationConfigDI.md)


## 1. Constructor Injection

* In the case of constructor-based dependency injection, the container will invoke a constructor with arguments each
  representing a dependency we want to set.

* Spring resolves each argument primarily by type, followed by name of the attribute, and index for disambiguation.

#### java config

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
