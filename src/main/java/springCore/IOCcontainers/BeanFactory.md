# BeanFactory

Example:

```java
public class PaymentService {

    public void processPayment() {
        System.out.println("Payment processed");
    }
}
```
Define the bean in beans.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="paymentService"
          class="com.example.PaymentService"/>

</beans>
```
loading configuration using BeanFactory
```java
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Main {

    public static void main(String[] args) {

        BeanFactory factory =
                new XmlBeanFactory(
                        new ClassPathResource("beans.xml")
                );

        PaymentService service =
                factory.getBean("paymentService", PaymentService.class);

        service.processPayment();
    }
}
```