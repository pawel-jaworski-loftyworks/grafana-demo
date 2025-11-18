# Feign Clients Documentation

This document describes the Feign clients created for inter-service communication between the microservices.

## Overview

Feign clients have been created to enable communication between microservices:
- **UserServiceClient** - Communicates with the user-service
- **PermissionServiceClient** - Communicates with the permission-service

## Architecture

Based on the docker-compose.yml configuration:
- **user-service**: Runs on port 8081 (external) / 8080 (internal)
- **document-service**: Runs on port 8082 (external) / 8080 (internal)
- **permission-service**: Runs on port 8083 (external) / 8080 (internal)

All services are connected via the `app-network` Docker network.

## Feign Client Files

### 1. UserServiceClient
**Location**: `src/main/java/com/example/client/UserServiceClient.java`

**Endpoints**:
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID

**Example Usage**:
```java
@Autowired
private UserServiceClient userServiceClient;

// Get all users
ResponseEntity<Collection<User>> users = userServiceClient.getAllUsers();

// Get specific user
UUID userId = UUID.fromString("...");
ResponseEntity<User> user = userServiceClient.getUserById(userId);
```

### 2. PermissionServiceClient
**Location**: `src/main/java/com/example/client/PermissionServiceClient.java`

**Endpoints**:
- `GET /api/permissions/user/{userId}` - Get permissions for a user
- `POST /api/permissions/user/{userId}` - Add a permission to a user

**Example Usage**:
```java
@Autowired
private PermissionServiceClient permissionServiceClient;

// Get user permissions
UUID userId = UUID.fromString("...");
ResponseEntity<List<String>> permissions = permissionServiceClient.getPermissionsByUserId(userId);

// Add permission
permissionServiceClient.addPermission(userId, "ADMIN");
```

### 3. FeignClientConfig
**Location**: `src/main/java/com/example/client/FeignClientConfig.java`

**Configuration**:
- **Connection Timeout**: 5 seconds
- **Read Timeout**: 10 seconds
- **Retry Logic**: Up to 3 retries with 1 second interval
- **Logging Level**: FULL (logs headers, body, and metadata)

## Configuration

### Docker Environment (Default)
When running in Docker, the Feign clients use service names for communication:

```properties
feign.client.user-service.url=http://user-service:8080
feign.client.permission-service.url=http://permission-service:8080
```

These are configured in `src/main/resources/application.properties`.

### Local Development
When running services locally (outside Docker), use the `local` profile:

```yaml
# application-local.yml
feign:
  client:
    user-service:
      url: http://localhost:8081
    permission-service:
      url: http://localhost:8083
```

**Run with local profile**:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local,document-service
```

### Profile-Specific Configuration
Each service can override the Feign client URLs in their profile-specific configuration files:
- `application-user-service.yml`
- `application-document-service.yml`
- `application-permission-service.yml`

## Example Service

An example aggregator service has been created to demonstrate Feign client usage:

**Location**: `src/main/java/com/example/service/UserPermissionAggregatorService.java`

This service demonstrates:
- Calling multiple services and aggregating results
- Error handling with Feign clients
- Using both GET and POST endpoints

**Methods**:
- `getAllUsersWithPermissions()` - Fetches all users and their permissions
- `getUserWithPermissions(UUID userId)` - Fetches a specific user with permissions
- `addPermissionToUser(UUID userId, String permission)` - Adds a permission to a user

## Testing the Feign Clients

### 1. Start all services with Docker Compose
```bash
docker-compose up -d
```

### 2. Verify services are running
```bash
docker-compose ps
```

### 3. Test the services directly
```bash
# Test user-service
curl http://localhost:8081/api/users

# Test permission-service
curl http://localhost:8083/api/permissions/user/{userId}
```

### 4. Check Feign client logs
```bash
# View document-service logs (which uses the Feign clients)
docker-compose logs -f document-service
```

The logs will show Feign client requests and responses when DEBUG logging is enabled.

## Troubleshooting

### Connection Refused
If you see "Connection refused" errors:
- Verify all services are running: `docker-compose ps`
- Check service health: `docker-compose logs {service-name}`
- Ensure services are on the same network: `docker network inspect grafana-example_app-network`

### Timeout Errors
If requests are timing out:
- Check the target service logs for errors
- Increase timeout values in `FeignClientConfig.java`
- Verify network connectivity between services

### 404 Not Found
If you get 404 errors:
- Verify the endpoint paths in the Feign client match the controller mappings
- Check the service URL configuration
- Ensure the correct profile is active on the target service

## Logging

Feign client logging is configured at DEBUG level for the `com.example.client` package.

To view detailed Feign logs:
```bash
# In application.properties or application.yml
logging.level.com.example.client=DEBUG
```

This will log:
- Request URL, method, headers, and body
- Response status, headers, and body
- Retry attempts
- Errors and exceptions

## Dependencies

The following dependency was added to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

With Spring Cloud BOM:
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2023.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Best Practices

1. **Error Handling**: Always wrap Feign client calls in try-catch blocks
2. **Timeouts**: Configure appropriate timeouts based on expected response times
3. **Retries**: Use retry logic for transient failures
4. **Circuit Breaker**: Consider adding Resilience4j for circuit breaker pattern
5. **Logging**: Use appropriate log levels (DEBUG for development, INFO for production)
6. **Service Discovery**: For production, consider using service discovery (Eureka, Consul)
7. **Load Balancing**: Use Spring Cloud LoadBalancer for client-side load balancing

## Next Steps

Consider adding:
- **Resilience4j** for circuit breaker and rate limiting
- **Spring Cloud LoadBalancer** for client-side load balancing
- **Service Discovery** (Eureka/Consul) for dynamic service registration
- **Distributed Tracing** (Sleuth/Zipkin) for request tracing across services
- **API Gateway** (Spring Cloud Gateway) for centralized routing

