package routers

import (
	"backend/configs"
	"backend/controllers"

	"github.com/gin-gonic/gin"
)

func InitRoutes() {
	healthController := controllers.NewHealthController()

	router := gin.Default()

	router.Use(gin.Recovery())
	router.Use(gin.Logger())

	v1 := router.Group("/api/v1")

	health := v1.Group("/health")
	health.GET("", healthController.HealthCheck)

	router.Run(":" + configs.PORT)
}
