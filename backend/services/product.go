package services

import (
	"backend/models"
	"backend/storage"
	"bytes"
	"io"
	"mime/multipart"
)

type ProductService struct {
	s3 storage.S3Interface
	db storage.ProductInterface
}

func NewProductService() *ProductService {
	return &ProductService{
		s3: storage.GetStorageInstance().S3,
		db: storage.GetStorageInstance().Product,
	}
}

func (p *ProductService) GetAllProducts(cursor int) ([]*models.Product, error) {
	limit := 20
	products, _, err := p.db.List(limit, cursor)
	if err != nil {
		return nil, err
	}
	return products, nil
}

func (p *ProductService) UploadProductImages(files []*multipart.FileHeader) ([]string, error) {
	var urls []string

	for _, file := range files {
		fileRead, err := file.Open()
		if err != nil {
			return nil, err
		}
		defer fileRead.Close()

		buf := new(bytes.Buffer)

		_, err = io.Copy(buf, fileRead)
		if err != nil {
			return nil, err
		}

		url, err := p.s3.Put(file.Filename, buf)
		if err != nil {
			return nil, err
		}

		urls = append(urls, url)
	}

	return urls, nil
}
