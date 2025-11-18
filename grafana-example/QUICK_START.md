# Quick Start Guide

## 🚀 Start All Services (Easiest Way)

```bash
docker-compose up -d
```

## 📊 Check Status

```bash
docker-compose ps
```

## 📝 View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
```

## 🧪 Test the Services

### User Service (Port 8081)
```bash
curl http://localhost:8081/api/users
```

### Document Service (Port 8082)
```bash
# First, get a userId from the user service, then:
curl http://localhost:8082/api/documents/user/{userId}
```

### Permission Service (Port 8083)
```bash
# First, get a userId from the user service, then:
curl http://localhost:8083/api/permissions/user/{userId}
```

## 🛑 Stop All Services

```bash
docker-compose down
```

## 🔄 Restart Services

```bash
docker-compose restart
```

## 🧹 Clean Up Everything

```bash
docker-compose down -v
docker rmi user-service:latest document-service:latest permission-service:latest
```

## 📦 Service Ports

| Service | Internal Port | External Port | Endpoint |
|---------|--------------|---------------|----------|
| User Service | 8080 | 8081 | http://localhost:8081/api/users |
| Document Service | 8080 | 8082 | http://localhost:8082/api/documents |
| Permission Service | 8080 | 8083 | http://localhost:8083/api/permissions |

## 🛠️ Using Makefile (Alternative)

If you prefer using make commands:

```bash
# Start all services
make up

# View logs
make logs

# Stop all services
make down

# Rebuild everything
make rebuild

# Test services
make test
```

## 🐛 Troubleshooting

### Service won't start?
```bash
# Check logs
docker-compose logs [service-name]

# Rebuild
docker-compose up -d --build
```

### Port already in use?
Edit `docker-compose.yml` and change the external port numbers.

### Need to access container shell?
```bash
docker exec -it user-service sh
```

