package storage

import (
	"backend/configs"
	"fmt"
	"sync"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Storage struct {
	read  *gorm.DB
	write *gorm.DB
}

var storageInstance *Storage
var once sync.Once

func GetStorageInstance() *Storage {
	once.Do(func() {
		storageInstance = &Storage{}
		storageInstance.InitDB()
	})

	return storageInstance
}

func (s *Storage) InitDB() {
	var err error
	s.write, err = gorm.Open(postgres.Open(configs.POSTGRESQL_CONN_STRING_MASTER), &gorm.Config{})
	if err != nil {
		fmt.Errorf("Error establishing connection to write DB: %v", err)
		return
	}
	writeDB, err := s.write.DB()
	if err != nil {
		fmt.Errorf("Error getting write DB: %v", err)
		return
	}
	writeDB.SetMaxIdleConns(configs.POSTGRESQL_MAX_IDLE_CONNECTIONS)
	writeDB.SetMaxOpenConns(configs.POSTGRESQL_MAX_OPEN_CONNECTIONS)

	s.read, err = gorm.Open(postgres.Open(configs.POSTGRESQL_CONN_STRING_SLAVE), &gorm.Config{})
	if err != nil {
		fmt.Errorf("Error establishing connection to read DB: %v", err)
		return
	}
	readDB, err := s.read.DB()
	if err != nil {
		fmt.Errorf("Error getting read DB: %v", err)
		return
	}
	readDB.SetMaxIdleConns(configs.POSTGRESQL_MAX_IDLE_CONNECTIONS)
	readDB.SetMaxOpenConns(configs.POSTGRESQL_MAX_OPEN_CONNECTIONS)
}

func (s *Storage) CloseDB() {
	var err error
	readDB, err := s.read.DB()
	if err != nil {
		fmt.Printf("Error getting read DB: %v", err)
	}
	err = readDB.Close()
	if err != nil {
		fmt.Printf("Unable to close read DB: %v", err)
	}

	writeDB, err := s.write.DB()
	if err != nil {
		fmt.Printf("Error getting write DB: %v", err)
	}
	err = writeDB.Close()
	if err != nil {
		fmt.Printf("Unable to close write DB: %v", err)
	}
}

func (s *Storage) AutoMigrate(model interface{}) {
	var err error
	err = s.write.AutoMigrate(model)
	if err != nil {
		fmt.Printf("Error auto-migrating model to write DB %T: %v", model, err)
		return
	}
	err = s.read.AutoMigrate(model)
	if err != nil {
		fmt.Printf("Error auto-migrating model to read DB %T: %v", model, err)
		return
	}
}
