package models

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
	Images      []string        `json:"images" gorm:"type:text[]"`
	CreatedAt   string          `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt   string          `json:"updated_at" gorm:"autoUpdateTime"`
}
