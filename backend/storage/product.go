package storage

import (
	"backend/models"

	"gorm.io/gorm"
)

type ProductInterface interface {
	List(limit, cursor int) ([]*models.Product, int, error)
	GetByID(id int) (*models.Product, error)
	Update(product *models.Product) error
	UpdateInventory(id int, inventory int) error
	Delete(id int) error
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

func (p *ProductDB) GetByID(id int) (*models.Product, error) {
	var product models.Product
	if err := p.read.Where("id = ?", id).First(&product).Error; err != nil {
		return nil, err
	}
	return &product, nil
}

func (p *ProductDB) Update(product *models.Product) error {
	result := p.write.Model(&models.Product{}).Omit("created_at", "updated_at").Where("id = ?", product.ID).Updates(product)
	if result.Error != nil {
		return result.Error
	}

	if result.RowsAffected == 0 {
		return gorm.ErrRecordNotFound
	}

	return nil
}

func (p *ProductDB) UpdateInventory(id int, inventory int) error {
	result := p.write.Model(&models.Product{}).Where("id = ?", id).Update("inventory", inventory)
	if result.Error != nil {
		return result.Error
	}

	if result.RowsAffected == 0 {
		return gorm.ErrRecordNotFound
	}

	return nil
}

func (p *ProductDB) Delete(id int) error {
	result := p.write.Model(&models.Product{}).Where("id = ?", id).Update("is_deleted", true)
	if result.Error != nil {
		return result.Error
	}

	if result.RowsAffected == 0 {
		return gorm.ErrRecordNotFound
	}

	return nil
}
