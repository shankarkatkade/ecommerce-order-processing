Setup & Deployment Guide
Prerequisites
Java 17 or higher
Maven 3.8+
IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)
Step 1: Project Setup
# Create project directory
mkdir ecommerce-order-processing
cd ecommerce-order-processing

# Create the Maven project structure
mkdir -p src/main/java/com/ecommerce
mkdir -p src/main/resources
mkdir -p src/test/java/com/ecommerce

# Copy all Java files to their respective packages
# Copy pom.xml to project root
# Copy application.properties to src/main/resources
Step 2: Configuration
The application uses H2 in-memory database by default. No additional database setup required!

H2 database will be automatically created and populated with sample data on startup.

Step 3: Build the Project
# Clean and build
mvn clean install

# Skip tests (if needed)
mvn clean install -DskipTests
Step 4: Run the Application
# Run with Maven and H2 profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"

# Or run the JAR file
java -jar target/ecommerce-order-processing-1.0.0.jar --spring.profiles.active=h2
Step 5: Verify Installation
Once the application starts, verify it's running:

Application URL: http://localhost:8080
H2 Console: http://localhost:8080/h2-console
Swagger UI: http://localhost:8080/swagger-ui.html
API Base: http://localhost:8080/api/v1
H2 Database Console Access
Access the H2 console at: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:ecommercedb
Username: sa
Password: (leave empty)
Step 6: Test the API
Create an order:

curl -X POST http://localhost:8080/api/v1/orders \
-H "Content-Type: application/json" \
-d '{
"customerName": "John Doe",
"customerEmail": "john.doe@example.com",
"orderItems": [
{
"productId": 101,
"productName": "Laptop",
"quantity": 1,
"price": 1299.99
}
]
}'
Get order by ID:

curl -X GET http://localhost:8080/api/v1/orders/1
List all orders:

curl -X GET "http://localhost:8080/api/v1/orders?page=0&size=10"
Monitoring & Logs
Application logs are available in:

Console output (default)
Log file: logs/application.log (if configured)
Production Deployment
# Build production JAR
mvn clean package -Pprod

# Run with production profile
java -jar target/ecommerce-order-processing-1.0.0.jar \
--spring.profiles.active=prod

# Or with external configuration
java -jar target/ecommerce-order-processing-1.0.0.jar \
--spring.config.location=file:./config/application.properties
Docker Deployment (Optional)
Create a Dockerfile:

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ecommerce-order-processing-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
Build and run:

# Build Docker image
docker build -t ecommerce-order-processing .

# Run container
docker run -p 8080:8080 \
-e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/ecommerce_order_db \
-e SPRING_DATASOURCE_USERNAME=root \
-e SPRING_DATASOURCE_PASSWORD=password \
ecommerce-order-processing