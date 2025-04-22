# Go Version
FROM golang:1.23-alpine3.21

# Environment variables which CompileDaemon requires to run
ENV PROJECT_DIR=/app \
    GO111MODULE=on \
    CGO_ENABLED=0

# Basic setup of the container
WORKDIR /app

# Copy directory
COPY . .

# Install dependencies
RUN go mod download

# Get CompileDaemon
RUN go get github.com/githubnemo/CompileDaemon
RUN go install github.com/githubnemo/CompileDaemon

EXPOSE 8080

# The build flag sets how to build after a change has been detected in the source code
# The command flag sets how to run the app after it has been built
ENTRYPOINT CompileDaemon -build="go build -o main" -command="./main"
