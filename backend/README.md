# Simple Backend Service

This is a simple backend service that provides a RESTful API for managing products. It is built using Go, Gin framework, and GORM ORM.

## 🛠️ Quick Start

### ✅ Requirements

- Install [Docker](https://www.docker.com/).

### 🔑 Addkeys

An environment variable file is created named `.env` in the project root folder. Add/Change the following variables:

```env
POSTGRES_USER=""
POSTGRES_PASSWORD=""
POSTGRES_DB=""
POSTGRES_HOST=""
```

> ⚠️ Skipping this step will cause the application to fail to connect to the database. Here are the default values:
>
> ```env
> POSTGRES_USER="postgres"
> POSTGRES_PASSWORD="postgres"
> POSTGRES_DB="backend"
> POSTGRES_HOST="db"
> ```

### 🚀 Run the Servce

```bash
# Clone the repository
git clone https://github.com/yaolongt/thales-assignment.git

# Change directory
cd backend

# Run the service
docker compose --env-file .env up
```
