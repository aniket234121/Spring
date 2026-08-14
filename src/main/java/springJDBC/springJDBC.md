# Spring JDBC

Spring JDBC is a Spring Framework module that simplifies database access using JDBC APIs.

It provides abstractions such as JdbcTemplate to reduce repetitive JDBC code while still allowing developers to execute SQL directly.

Spring JDBC

Spring handles most of this infrastructure code.

## 2. Why Spring JDBC over plain JDBC?

Spring JDBC does not replace JDBC. It builds on top of JDBC and removes much of its repetitive infrastructure code.

| Plain JDBC                      | Spring JDBC                                             |
| ------------------------------- | ------------------------------------------------------- |
| Manually manage `Connection`    | Spring manages connection handling                      |
| Manually create/close resources | Automatic resource cleanup                              |
| Handle `SQLException`           | Translates exceptions into Spring `DataAccessException` |
| Repetitive query execution code | `JdbcTemplate` simplifies execution                     |
| Manual transaction management   | Integrates with Spring transaction management           |
| More boilerplate                | Significantly less boilerplate                          |


## 3. Problems with Traditional JDBC
### A. Boilerplate Code

Traditional JDBC requires repetitive code for:

* Obtaining Connection
* Creating PreparedStatement
* Setting parameters
* Executing SQL
* Processing ResultSet
* Closing resources
* Handling exceptions

Spring JDBC encapsulates most of this infrastructure through JdbcTemplate.

### B. Resource Management

Plain JDBC requires developers to correctly close:

    Connection
    ↓
    Statement / PreparedStatement
    ↓
    ResultSet

Failure to release resources can cause connection leaks and negatively affect application performance.

Spring JDBC handles resource cleanup through its template infrastructure.

### C. Exception Handling

JDBC primarily uses the checked SQLException hierarchy.

Spring JDBC translates database-specific JDBC exceptions into Spring's consistent DataAccessException hierarchy.

This provides:

* Database-independent exception handling
* Unchecked exceptions
* Consistent exception model across Spring data-access technologies

## Spring JDBC Architecture

    Application / Service Layer
    ↓
    DAO / Repository
    ↓
    JdbcTemplate
    ↓
    Spring JDBC Infrastructure
    ↓
    JDBC API
    ↓
    JDBC Driver
    ↓
    Database

## Important components
| Component                     | Responsibility                               |
| ----------------------------- | -------------------------------------------- |
| `JdbcTemplate`                | Executes SQL and manages JDBC infrastructure |
| `RowMapper`                   | Maps each `ResultSet` row to a Java object   |
| `JdbcTemplate` callbacks      | Customize JDBC operations when required      |
| `DataSource`                  | Provides database connections                |
| Spring Transaction Management | Manages transaction boundaries               |

# DataSource and Connection Pooling

DataSource is the central component through which Spring JDBC obtains database connections. 
Understanding it is important because JdbcTemplate, transaction management, and connection pooling all depend on it.

### What is DataSource?

DataSource is a standard JDBC interface used to obtain database connections.

    javax.sql.DataSource

Its primary method is:

    Connection getConnection() throws SQLException;

Instead of application code directly using:

    DriverManager.getConnection(...)

a DataSource provides the connection.

### Why use DataSource instead of DriverManager?

With plain JDBC, you might write:

    Connection connection =
    DriverManager.getConnection(url, username, password);

This directly asks DriverManager to establish a connection.

With Spring JDBC:

    DataSource dataSource = ...;


    Connection connection = dataSource.getConnection();

The application does not need to know how the connection is created or managed.

This provides a level of abstraction:
    
    Application
          ↓
    DataSource
          ↓
    Implementation can change
          ↓
    HikariCP / Application Server / Other

For example, your application code can continue using:

    dataSource.getConnection();

even if the underlying implementation changes from one connection pool to another.

## JDBC Connection Pooling
What is connection pooling?

Creating a database connection is relatively expensive because it can involve:

Application
↓
Network connection
↓
Database authentication
↓
Session establishment
↓
Connection becomes available

Creating a new connection for every database operation is inefficient.

A connection pool solves this by maintaining a collection of already-established database connections.

Without pooling

    Request 1 → Create Connection → Use → Close
    Request 2 → Create Connection → Use → Close
    Request 3 → Create Connection → Use → Close

Every request repeatedly creates a physical database connection.

With pooling

    Connection Pool
    ┌───────────────────┐
    │ Connection 1      │
    │ Connection 2      │
    │ Connection 3      │
    │ Connection 4      │
    │ Connection 5      │
    └───────────────────┘
    ↑    ↑    ↑
    │    │    │
    Borrow  Borrow
    │    │
    Application

The application borrows an available connection, uses it, and then returns it to the pool.

It normally does not physically destroy the connection when application code calls:

    connection.close();

With a pool, close() generally means:

Return this connection to the pool so it can be reused.

## Connection Pool Concept

A connection pool manages a set of database connections.

Typical lifecycle:

```java
Application requests connection
            ↓
      DataSource
            ↓
    Connection Pool
            ↓
   Available connection?
       ↙          ↘
     Yes           No
      ↓             ↓
   Borrow       Wait / timeout
      ↓
 Execute SQL
      ↓
 connection.close()
      ↓
Return to pool

```
### Maximum pool size

Maximum number of connections that the pool can maintain/use concurrently.

Example:

    maximum-pool-size=10

Conceptually:

    Pool
    ├── Connection 1
    ├── Connection 2
    ├── ...
    └── Connection 10

If all 10 are currently being used, another request must wait until a connection becomes available or the configured timeout is reached.

### Spring Boot DataSource Auto-Configuration

Spring Boot can automatically configure a DataSource based on:

* JDBC database driver on the classpath
* Database connection properties
* Available connection-pool implementation

For example, with a JDBC starter and MySQL driver:

```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>


<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

and:

    spring.datasource.url=jdbc:mysql://localhost:3306/companydb
    spring.datasource.username=root
    spring.datasource.password=secret

Spring Boot can automatically create the DataSource.

### How Spring Boot Chooses the Connection Pool

Spring Boot's JDBC starter uses a connection-pool implementation when one is available.

HikariCP is the default connection pool in modern Spring Boot applications when Hikari is available, which is normally the case with spring-boot-starter-jdbc.

So the typical stack is:

    Spring Boot
        ↓
    DataSource
        ↓
    HikariCP
        ↓
    JDBC Driver
        ↓
    Database

This distinction is important:

DataSource is the JDBC abstraction

HikariCP is the connection-pool implementation behind the DataSource.

```java
package com.example.demo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void testConnection() throws SQLException {

        try (Connection connection = dataSource.getConnection()) {

            System.out.println(
                "Database: " + connection.getMetaData().getDatabaseProductName()
            );
        }
    }
}
```
When Spring creates DatabaseService:
```
    Spring Container
          ↓
    Injects DataSource
          ↓
    DataSource is backed by HikariCP
```
## CRUD Operations with JdbcTemplate

JdbcTemplate is the primary Spring JDBC class used to execute SQL statements. 

It reduces JDBC boilerplate such as connection handling, statement creation, parameter binding, and resource cleanup.

```java
Controller
    ↓
Service
    ↓
Repository / DAO
    ↓
JdbcTemplate
    ↓
DataSource
    ↓
Database
```

### 1. Create — INSERT
**Use update() for INSERT, UPDATE, and DELETE operations.**

```java
int rows = jdbcTemplate.update(
"INSERT INTO employees(name, email, salary) VALUES (?, ?, ?)",
"Aniket",
"aniket@example.com",
75000
);
```

The returned int represents the number of affected rows.

Generated Keys

If the database generates the primary key, KeyHolder can retrieve it.

```java
KeyHolder keyHolder = new GeneratedKeyHolder();

jdbcTemplate.update(connection -> {
    PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO employees(name, email, salary) VALUES (?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    );

    ps.setString(1, "Aniket");
    ps.setString(2, "aniket@example.com");
    ps.setBigDecimal(3, new BigDecimal("75000"));

    return ps;
}, keyHolder);

Long id = keyHolder.getKey().longValue();
```
### 2. Read — SELECT
#### query()

Used when a query can return multiple rows.

```java
List<Employee> employees = jdbcTemplate.query(
"SELECT id, name, email, salary FROM employees",
new EmployeeRowMapper()
);
queryForObject()
```

Used when exactly one result is expected.

```java
Employee employee = jdbcTemplate.queryForObject(
"SELECT id, name, email, salary FROM employees WHERE id = ?",
new EmployeeRowMapper(),
id
);
```

If no row or an unexpected number of rows is returned, an appropriate exception is raised.

#### queryForList()

Used when you want rows represented as Map<String, Object> rather than mapping them directly to an entity.

```java
List<Map<String, Object>> employees =
jdbcTemplate.queryForList(
"SELECT id, name, email FROM employees"
);
```

For application/domain code, query() with a RowMapper is generally preferable when you need typed objects.

### 3. Update — UPDATE

Use update():

```java

int rows = jdbcTemplate.update(
"UPDATE employees SET salary = ? WHERE id = ?",
80000,
id
);

```
The return value is the number of affected rows.

### 4. Delete — DELETE

Again, use update():

```java
int rows = jdbcTemplate.update(
"DELETE FROM employees WHERE id = ?",
id
);

```

So the basic rule is:

| Operation       | `JdbcTemplate` method |
| --------------- | --------------------- |
| INSERT          | `update()`            |
| SELECT multiple | `query()`             |
| SELECT one      | `queryForObject()`    |
| SELECT as maps  | `queryForList()`      |
| UPDATE          | `update()`            |
| DELETE          | `update()`            |

# Complete Example

## SQL
```sql
CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    salary DECIMAL(10,2)
);
```
```java
public class Employee {

    private Long id;
    private String name;
    private String email;
    private BigDecimal salary;

    // constructors, getters and setters
}
```
Repository / DAO Layer

The repository is responsible for database access and SQL execution.
```java
@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Employee> rowMapper = (rs, rowNum) ->
        new Employee(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getBigDecimal("salary")
        );

    // CREATE
    public int save(Employee employee) {
        return jdbcTemplate.update(
            "INSERT INTO employees(name, email, salary) VALUES (?, ?, ?)",
            employee.getName(),
            employee.getEmail(),
            employee.getSalary()
        );
    }

    // READ - multiple
    public List<Employee> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, email, salary FROM employees",
            rowMapper
        );
    }

    // READ - single
    public Employee findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT id, name, email, salary FROM employees WHERE id = ?",
            rowMapper,
            id
        );
    }

    // UPDATE
    public int update(Employee employee) {
        return jdbcTemplate.update(
            "UPDATE employees SET name = ?, email = ?, salary = ? WHERE id = ?",
            employee.getName(),
            employee.getEmail(),
            employee.getSalary(),
            employee.getId()
        );
    }

    // DELETE
    public int deleteById(Long id) {
        return jdbcTemplate.update(
            "DELETE FROM employees WHERE id = ?",
            id
        );
    }
}
```
Service Layer

The service layer contains business logic and coordinates repository operations.
```java
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public int create(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {
        return repository.findById(id);
    }

    public int update(Employee employee) {
        return repository.update(employee);
    }

    public int delete(Long id) {
        return repository.deleteById(id);
    }
}
```
Controller Layer

The controller exposes the CRUD operations through HTTP endpoints.
```java
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody Employee employee) {
        service.create(employee);
    }

    @GetMapping
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        employee.setId(id);
        service.update(employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```