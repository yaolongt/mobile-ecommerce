package main

import (
	"backend/configs"
	routers "backend/routes"
	"backend/storage"
)

func main() {
	configs.InitEnv()

	dbInstance := storage.GetStorageInstance()
	defer dbInstance.CloseDB()

	routers.InitRoutes()
}
