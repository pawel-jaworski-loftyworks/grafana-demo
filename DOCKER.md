# Docker Deployment Guide

This guide explains how to build and run the Spring Boot microservices using Docker.

## Overview

The application is split into three microservices, each with its own Spring profile:
- **User Service** - Manages user data (port 8081)
- **Document Service** - Manages documents (port 8082)
- **Permission Service** - Manages permissions (port 8083)

## Prerequisites

- Docker installed (version 20.10 or higher)
- Docker Compose installed (version 2.0 or higher)

## Quick Start with Docker Compose

### Start All Services

```bash
docker-compose up -d
```

This will build and start all three services in detached mode.

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
docker-compose logs -f document-service
docker-compose logs -f permission-service
```

### Stop All Services

```bash
docker-compose down
```

### Rebuild and Restart

```bash
docker-compose up -d --build
```

## Individual Service Deployment

### Build Individual Docker Images

```bash
# User Service
docker build -f Dockerfile.user-service -t user-service:latest .

# Document Service
docker build -f Dockerfile.document-service -t document-service:latest .

# Permission Service
docker build -f Dockerfile.permission-service -t permission-service:latest .
```

### Run Individual Containers

```bash
# User Service
docker run -d -p 8081:8080 --name user-service user-service:latest

# Document Service
docker run -d -p 8082:8080 --name document-service document-service:latest

# Permission Service
docker run -d -p 8083:8080 --name permission-service permission-service:latest
```

### Stop Individual Containers

```bash
docker stop user-service document-service permission-service
docker rm user-service document-service permission-service
```

## Using the Generic Dockerfile with Build Arguments

You can also use the generic `Dockerfile` with build arguments:

```bash
# Build with specific profile
docker build --build-arg PROFILE=user-service -t user-service:latest .
docker build --build-arg PROFILE=document-service -t document-service:latest .
docker build --build-arg PROFILE=permission-service -t permission-service:latest .
```

## Testing the Services

### User Service (Port 8081)

```bash
# Get all users
curl http://localhost:8081/api/users

# Get user by ID (replace with actual UUID from the response above)
curl http://localhost:8081/api/users/{userId}
```

### Document Service (Port 8082)

```bash
# Get documents for a user
curl http://localhost:8082/api/documents/user/{userId}

# Add a document
curl -X POST http://localhost:8082/api/documents/user/{userId} \
  -H "Content-Type: text/plain" \
  -d "New Document Content"
```

### Permission Service (Port 8083)

```bash
# Get permissions for a user
curl http://localhost:8083/api/permissions/user/{userId}

# Add a permission
curl -X POST http://localhost:8083/api/permissions/user/{userId} \
  -H "Content-Type: text/plain" \
  -d "EXECUTE"
```

## Docker Compose Configuration

The `docker-compose.yml` file configures:
- **Port Mappings**: Each service runs on port 8080 internally, mapped to different external ports
  - User Service: 8081
  - Document Service: 8082
  - Permission Service: 8083
- **Networks**: All services are on the same `app-network` bridge network
- **Health Checks**: Each service has health check configuration
- **Restart Policy**: Services restart automatically unless stopped manually

## Environment Variables

You can override environment variables in `docker-compose.yml` or when running containers:

```bash
docker run -d -p 8081:8080 \
  -e SPRING_PROFILES_ACTIVE=user-service \
  -e SERVER_PORT=8080 \
  --name user-service \
  user-service:latest
```

## Troubleshooting

### Check Container Status

```bash
docker-compose ps
```

### View Container Logs

```bash
docker-compose logs -f [service-name]
```

### Inspect Container

```bash
docker inspect [container-name]
```

### Execute Commands in Container

```bash
docker exec -it user-service sh
```

### Check Health Status

```bash
docker inspect --format='{{json .State.Health}}' user-service | jq
```

## Production Considerations

1. **Resource Limits**: Add resource limits in docker-compose.yml:
   ```yaml
   deploy:
     resources:
       limits:
         cpus: '0.5'
         memory: 512M
       reservations:
         cpus: '0.25'
         memory: 256M
   ```

2. **Logging**: Configure logging drivers for production:
   ```yaml
   logging:
     driver: "json-file"
     options:
       max-size: "10m"
       max-file: "3"
   ```

3. **Secrets**: Use Docker secrets or environment files for sensitive data

4. **Monitoring**: Add monitoring tools like Prometheus and Grafana

5. **Load Balancing**: Use a reverse proxy like Nginx or Traefik

## Clean Up

### Remove All Containers and Images

```bash
# Stop and remove containers
docker-compose down

# Remove images
docker rmi user-service:latest document-service:latest permission-service:latest

# Remove all unused images, containers, and networks
docker system prune -a
```

## Multi-Architecture Builds

To build for multiple architectures (e.g., ARM64 for Apple Silicon):

```bash
docker buildx build --platform linux/amd64,linux/arm64 \
  -f Dockerfile.user-service \
  -t user-service:latest .
```

