# Simple Backend Service

This is a simple backend service that provides a RESTful API for managing products. It is built using Go, Gin framework, and GORM ORM.

## 🛠️ Quick Start

### ✅ Requirements

- Install [Docker](https://www.docker.com/).

### 🚀 Run the service

```bash
# Clone the repository
git clone https://github.com/yaolongt/thales-assignment.git

# Change directory
cd backend

# Run the service
make run
```

### ⏹️ Stop the service

```bash
make stop
```

### 🗑️Clean up

```bash
make clean
```

> ⚠️ Running this command will remove all existing data in the database and MinIO.

# 🌐Database and Storage

## MinIO Web Portal

Once the services is up and running, you can view the seeded images that are being loaded into MinIO, serving as a S3 bucket. Login to the [web portal](http://localhost:9001) using the following credentials:

- Username: `miniouser`
- Password: `miniopassword`

## PostgreSQL Adminer Web Portal

Once the services is up and running, you can access the [adminer web portal](http://localhost:8081) to see the seeded data, using the following credentials:

- System: `PostgreSQL`
- Server: `db`
- Username: `postgres`
- Password: `postgres`
- Database: `backend`
