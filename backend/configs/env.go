package configs

import (
	"fmt"
	"os"
	"strconv"

	"github.com/joho/godotenv"
)

var (
	PORT                            string
	POSTGRESQL_CONN_STRING_MASTER   string
	POSTGRESQL_CONN_STRING_SLAVE    string
	POSTGRESQL_MAX_IDLE_CONNECTIONS int
	POSTGRESQL_MAX_OPEN_CONNECTIONS int
	S3_BUCKET                       string
	S3_ENDPOINT                     string
	S3_ACCESS_KEY                   string
	S3_SECRET_KEY                   string
)

func InitEnv() {
	err := godotenv.Load(".env")
	if err != nil {
		fmt.Errorf("Error loading .env file")
	}

	PORT = getEnv("PORT", "8080")

	setDBConnectionString()

	POSTGRESQL_MAX_IDLE_CONNECTIONS = getEnvAsInt("POSTGRESQL_MAX_IDLE_CONNECTIONS", 10)
	POSTGRESQL_MAX_OPEN_CONNECTIONS = getEnvAsInt("POSTGRESQL_MAX_OPEN_CONNECTIONS", 5)

	S3_BUCKET = getEnv("S3_BUCKET", "backend")
	S3_ENDPOINT = getEnv("S3_ENDPOINT", "http://localhost:9000")
	S3_ACCESS_KEY = getEnv("S3_ACCESS_KEY", "minioadmin")
	S3_SECRET_KEY = getEnv("S3_SECRET_KEY", "minioadmin")

	fmt.Println("Initialization of environment variables complete")
}

func setDBConnectionString() {
	POSTGRES_HOST := getEnv("POSTGRES_HOST", "localhost")
	POSTGRES_PORT := getEnv("POSTGRES_PORT", "5432")
	POSTGRES_USER := getEnv("POSTGRES_USER", "postgres")
	POSTGRES_PASSWORD := getEnv("POSTGRES_PASSWORD", "postgres")
	POSTGRES_DB := getEnv("POSTGRES_DB", "backend")

	POSTGRESQL_CONN_STRING_MASTER = fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		POSTGRES_HOST,
		POSTGRES_PORT,
		POSTGRES_USER,
		POSTGRES_PASSWORD,
		POSTGRES_DB,
	)

	POSTGRESQL_CONN_STRING_SLAVE = fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		POSTGRES_HOST,
		POSTGRES_PORT,
		POSTGRES_USER,
		POSTGRES_PASSWORD,
		POSTGRES_DB,
	)
}

// Helper function to get environment variable with fallback
func getEnv(key string, fallback string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return fallback
}

// Helper function to get environment variable as int
func getEnvAsInt(key string, fallback int) int {
	if value, exists := os.LookupEnv(key); exists {
		if intVal, err := strconv.Atoi(value); err == nil {
			return intVal
		}
	}
	return fallback
}
