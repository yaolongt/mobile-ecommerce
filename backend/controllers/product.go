package controllers

import (
	"backend/services"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
)

type ProductController struct {
	service *services.ProductService
}

func NewProductController(service *services.ProductService) *ProductController {
	return &ProductController{
		service: service,
	}
}

func (p *ProductController) GetAllProducts(c *gin.Context) {
	cursorQuery := c.Query("cursor")

	var err error
	cursor := 0
	if cursorQuery != "" {
		cursor, err = strconv.Atoi(cursorQuery)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid cursor"})
			return
		}
	}

	products, err := p.service.GetAllProducts(cursor)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("Failed to get products. %v", err)})
		return
	}
	c.JSON(http.StatusOK, gin.H{"products": products})
}

func (p *ProductController) UploadProductImages(c *gin.Context) {
	if err := c.Request.ParseMultipartForm(10 << 20); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Unable to parse form"})
		return
	}

	files := c.Request.MultipartForm.File["images"]
	if len(files) == 0 {
		c.JSON(http.StatusBadRequest, gin.H{"error": "No files found"})
		return
	}

	for _, file := range files {
		contentType := file.Header.Get("Content-Type")
		if contentType != "image/jpeg" && contentType != "image/png" && contentType != "image/jpg" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid file type"})
			return
		}

		if file.Size > 10<<20 { // 10MB
			c.JSON(http.StatusBadRequest, gin.H{"error": "File size exceeds limit"})
			return
		}
	}

	resp, err := p.service.UploadProductImages(files)
	if err != nil {
		c.JSON(http.StatusInternalServerError,
			gin.H{"error": fmt.Sprintf("Failed to upload images. %v", err)},
		)
		return
	}

	c.JSON(http.StatusOK, gin.H{"urls": resp})
}
