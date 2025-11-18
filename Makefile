.PHONY: help build build-user build-document build-permission up down logs clean test

# Default target
help:
	@echo "Available targets:"
	@echo "  make build              - Build all Docker images"
	@echo "  make build-user         - Build User Service image"
	@echo "  make build-document     - Build Document Service image"
	@echo "  make build-permission   - Build Permission Service image"
	@echo "  make up                 - Start all services with docker-compose"
	@echo "  make down               - Stop all services"
	@echo "  make restart            - Restart all services"
	@echo "  make logs               - View logs from all services"
	@echo "  make logs-user          - View logs from User Service"
	@echo "  make logs-document      - View logs from Document Service"
	@echo "  make logs-permission    - View logs from Permission Service"
	@echo "  make ps                 - Show running containers"
	@echo "  make clean              - Remove all containers and images"
	@echo "  make test               - Test all services"
	@echo "  make test-user          - Test User Service"
	@echo "  make test-document      - Test Document Service"
	@echo "  make test-permission    - Test Permission Service"

# Build all images
build:
	docker-compose build

# Build individual services
build-user:
	docker build -f Dockerfile.user-service -t user-service:latest .

build-document:
	docker build -f Dockerfile.document-service -t document-service:latest .

build-permission:
	docker build -f Dockerfile.permission-service -t permission-service:latest .

# Start all services
up:
	docker-compose up -d

# Stop all services
down:
	docker-compose down

# Restart all services
restart: down up

# View logs
logs:
	docker-compose logs -f

logs-user:
	docker-compose logs -f user-service

logs-document:
	docker-compose logs -f document-service

logs-permission:
	docker-compose logs -f permission-service

# Show running containers
ps:
	docker-compose ps

# Clean up
clean:
	docker-compose down -v
	docker rmi user-service:latest document-service:latest permission-service:latest 2>/dev/null || true

# Test services
test: test-user test-document test-permission

test-user:
	@echo "Testing User Service..."
	@curl -s http://localhost:8081/api/users | jq . || echo "User Service not responding"

test-document:
	@echo "Testing Document Service..."
	@echo "Note: You need a valid userId to test document service"

test-permission:
	@echo "Testing Permission Service..."
	@echo "Note: You need a valid userId to test permission service"

# Rebuild and restart
rebuild: clean build up

