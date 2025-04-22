package main

import (
	"backend/configs"
	routers "backend/routes"
)

func main() {
	configs.InitEnv()

	// dbInstance := configs.GetStorageInstance()
	// defer dbInstance.CloseDB()

	routers.InitRoutes()
}
