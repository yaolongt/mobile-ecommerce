package routers

import (
	"backend/configs"
	"backend/controllers"
	"backend/services"

	"github.com/gin-gonic/gin"
)

func InitRoutes() {
	healthController := controllers.NewHealthController()
	productController := controllers.NewProductController(services.NewProductService())

	router := gin.Default()

	router.Use(gin.Recovery())
	router.Use(gin.Logger())

	v1 := router.Group("/api/v1")

	health := v1.Group("/health")
	health.GET("", healthController.HealthCheck)

	product := v1.Group("/product")
	product.GET("", productController.GetAllProducts)
	product.POST("/upload", productController.UploadProductImages)

	router.Run(":" + configs.PORT)
}
