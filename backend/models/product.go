package models

import (
	"time"

	pq "github.com/lib/pq"
)

type ProductCategory string

const (
	CategoryElectronics ProductCategory = "electronics"
	CategoryClothing    ProductCategory = "clothing"
	CategoryHome        ProductCategory = "home_appliances"
	CategoryBooks       ProductCategory = "books"
	CategoryToys        ProductCategory = "toys"
	CategoryMisc        ProductCategory = "misc"
)

type Product struct {
	ID          uint64          `json:"id" gorm:"primaryKey"`
	Name        string          `json:"name"`
	Price       float64         `json:"price"`
	Inventory   int             `json:"inventory" gorm:"check: inventory >= 0"`
	Category    ProductCategory `json:"category" gorm:"type:product_category;default:misc"`
	Description string          `json:"description"`
	Images      pq.StringArray  `gorm:"type:text[]" json:"images"` // Using pq driver
	CreatedAt   time.Time       `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt   time.Time       `json:"updated_at" gorm:"autoUpdateTime"`
}
