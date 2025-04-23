package controllers

import (
	"backend/models"
	"backend/services"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
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
	cursorQuery := c.Param("cursor")

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

func (p *ProductController) GetProductByID(c *gin.Context) {
	id := c.Param("id")
	if id == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Product ID is required"})
		return
	}

	productID, err := strconv.Atoi(id)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid product ID"})
		return
	}

	product, err := p.service.GetProductByID(productID)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			c.JSON(http.StatusNotFound, gin.H{"error": "Product not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("Failed to get product. %v", err)})
		return
	}
	c.JSON(http.StatusOK, gin.H{"product": product})
}

func (p *ProductController) UpdateProduct(c *gin.Context) {
	var product models.Product
	if err := c.ShouldBindJSON(&product); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input"})
		return
	}

	if product.ID == 0 {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Product ID is required"})
		return
	}

	if err := p.service.UpdateProduct(&product); err != nil {
		if err == gorm.ErrRecordNotFound {
			c.JSON(http.StatusNotFound, gin.H{"error": "Product not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, gin.H{"error": fmt.Sprintf("Failed to update product. %v", err)})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "Product updated successfully"})
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
