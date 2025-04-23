package storage

import (
	"backend/models"

	"gorm.io/gorm"
)

type ProductInterface interface {
	List(limit, cursor int) ([]*models.Product, int, error)
}

type ProductDB struct {
	read  *gorm.DB
	write *gorm.DB
}

func NewProductDB(read, write *gorm.DB) ProductInterface {
	GetStorageInstance().AutoMigrate(&models.Product{})
	return &ProductDB{
		read:  read,
		write: write,
	}
}

func (p *ProductDB) List(limit, cursor int) ([]*models.Product, int, error) {
	var products []*models.Product
	query := p.read.Where("is_deleted = false").Order("updated_at DESC").Order("id ASC").Limit(limit)

	// count total products
	var total int64
	if err := query.Model(&models.Product{}).Count(&total).Error; err != nil {
		return nil, 0, err
	}

	if cursor > 0 {
		query = query.Where("id > ?", cursor)
	}

	if err := query.Find(&products).Error; err != nil {
		return nil, 0, err
	}

	return products, int(total), nil
}
