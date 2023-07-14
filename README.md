# Drones

This project is a Spring Boot REST API application for managing drones. It uses Java 8, Maven, and an H2 in-memory database.

## Prerequisites

- Java 8 or higher
- Maven

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/hackersdelima/drones.git
cd drones
```

2. Build the application using Maven:

```bash
mvn clean install
```

3. Run the application:

```bash
java -jar target/drones-1.0.jar
```

The application will start and listen on the default port 8088. You can update the port from application.properties file `server.port` as well.

## Configuration

The application can be configured through the `application.properties` file located in the `src/main/resources` directory. You can modify this file to adjust various settings such as the database connection details or server port.

Here is an example of the `application.properties` file:

```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:dronedb
spring.datasource.username=root
spring.datasource.password=Shishir@123
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Server Configuration
server.port=8088

# Drone Battery Level Audit Timer Configuration
drone.log.battery.level.timer=60000
```

## API Documentation

The API documentation is available in the Postman collection. You can import the collection into Postman using the following link:

[Postman API Documentation](https://api.postman.com/collections/25891149-95184a7f-6849-4f00-864f-eca88744a403?access_key=PMAT-01H5AASFM4K41TV55TFSDJJH55)

The collection provides detailed information about the available REST endpoints and how to interact with them.

### Available Rest APIs
1. Register Drone
2. Get Available Drones
3. Check Drone Battery Level
4. Load Medications in Drone
5. Get Medications for specific Drone

## CI/CD

This project includes a CI/CD pipeline set up with GitHub Actions. The pipeline automatically builds the application, runs tests, and uploads the artifact to GitHub Releases. You can find the generated artifact. You can download the JAR file from there and use it to run and test the application.