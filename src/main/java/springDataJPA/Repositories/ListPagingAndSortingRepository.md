# ListPagingAndSortingRepository
ListPagingAndSortingRepository is a Spring Data repository interface that combines:

* CRUD operations
* Pagination
* Sorting
* List return types for collection-based operations

```java
ListPagingAndSortingRepository
            │
            ├── ListCrudRepository
            │       └── CRUD operations
            │
            └── PagingAndSortingRepository
                    └── Pagination + Sorting
```
    public interface EmployeeRepository
    extends ListPagingAndSortingRepository<Employee, Long> {
    }

## Methods

| Method                       | Parameters          | Return Type   | Purpose                                                                   |
| ---------------------------- | ------------------- | ------------- | ------------------------------------------------------------------------- |
| `findAll(Sort sort)`         | `Sort sort`         | **`List<T>`** | Retrieves all entities sorted according to the specified sorting criteria |
| `findAll(Pageable pageable)` | `Pageable pageable` | `Page<T>`     | Retrieves a page of entities using pagination and optional sorting        |

```java
package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
    private double salary;

    public Employee() {
    }

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
```
```java
package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface EmployeeRepository
        extends ListPagingAndSortingRepository<Employee, Long> {
}
```
```java
package com.example.demo.runner;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeRunner implements CommandLineRunner {

    private final EmployeeRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeRunner(
            EmployeeRepository repository,
            JdbcTemplate jdbcTemplate) {

        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        // ---------------------------------------------------------
        // Insert sample data
        // ---------------------------------------------------------

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Aniket", "IT", 75000
        );

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Rahul", "HR", 55000
        );

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Priya", "Finance", 85000
        );

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Amit", "IT", 65000
        );

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Neha", "HR", 60000
        );

        jdbcTemplate.update("""
            INSERT INTO employee (name, department, salary)
            VALUES (?, ?, ?)
            """,
            "Ravi", "Finance", 90000
        );


        // =========================================================
        // 1. findAll(Sort)
        // =========================================================

        Sort salaryDescending =
                Sort.by("salary").descending();

        List<Employee> employees =
                repository.findAll(salaryDescending);

        System.out.println("========== SORTING ==========");

        employees.forEach(System.out::println);

        /*
        Expected:

        Employee{id=6, name='Ravi',
                 department='Finance', salary=90000.0}

        Employee{id=3, name='Priya',
                 department='Finance', salary=85000.0}

        Employee{id=1, name='Aniket',
                 department='IT', salary=75000.0}

        Employee{id=4, name='Amit',
                 department='IT', salary=65000.0}

        Employee{id=5, name='Neha',
                 department='HR', salary=60000.0}

        Employee{id=2, name='Rahul',
                 department='HR', salary=55000.0}
        */


        // =========================================================
        // 2. findAll(Pageable)
        // =========================================================

        Pageable pageable =
                PageRequest.of(0, 3);

        Page<Employee> page =
                repository.findAll(pageable);

        System.out.println("\n========== PAGINATION ==========");

        page.getContent()
                .forEach(System.out::println);

        /*
        Expected:

        First 3 employees from page 0.

        Employee{...}
        Employee{...}
        Employee{...}
        */


        // =========================================================
        // 3. Pagination + Sorting
        // =========================================================

        Pageable sortedPageable =
                PageRequest.of(
                        0,
                        3,
                        Sort.by("salary").descending()
                );

        Page<Employee> sortedPage =
                repository.findAll(sortedPageable);

        System.out.println(
                "\n========== PAGINATION + SORTING =========="
        );

        sortedPage.getContent()
                .forEach(System.out::println);

        /*
        Expected:

        Ravi    → 90000
        Priya   → 85000
        Aniket  → 75000
        */


        // =========================================================
        // 4. Page Information
        // =========================================================

        System.out.println(
                "\nCurrent Page: "
                        + sortedPage.getNumber()
        );

        System.out.println(
                "Page Size: "
                        + sortedPage.getSize()
        );

        System.out.println(
                "Total Elements: "
                        + sortedPage.getTotalElements()
        );

        System.out.println(
                "Total Pages: "
                        + sortedPage.getTotalPages()
        );

        /*
        Expected:

        Current Page: 0
        Page Size: 3
        Total Elements: 6
        Total Pages: 2
        */
    }
}
```