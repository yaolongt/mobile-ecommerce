package storage

import (
	"backend/configs"
	"bytes"
	"context"
	"fmt"
	"log"
	"strings"

	"github.com/google/uuid"
	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
)

type S3Interface interface {
	CreateBucketIfNotExists() error
	Put(filename string, file *bytes.Buffer) (string, error)
}

type Minio struct {
	client *minio.Client
	bucket string
}

func NewMinio() S3Interface {
	minioClient, err := minio.New(configs.S3_ENDPOINT, &minio.Options{
		Creds: credentials.NewStaticV4(configs.S3_ACCESS_KEY, configs.S3_SECRET_KEY, ""),
	})
	if err != nil {
		log.Fatalf("Error creating Minio client: %v", err)
		return nil
	}

	return &Minio{
		client: minioClient,
		bucket: configs.S3_BUCKET,
	}
}

func (m *Minio) CreateBucketIfNotExists() error {
	ctx := context.Background()
	defer ctx.Done()

	exists, err := m.client.BucketExists(ctx, m.bucket)
	if err != nil {
		return err
	}

	if !exists {
		err = m.client.MakeBucket(ctx, m.bucket, minio.MakeBucketOptions{})
		if err != nil {
			return err
		}
	}
	err = m.client.SetBucketPolicy(ctx, m.bucket,
		`{
"Version": "2012-10-17",
"Statement": [
{
	"Effect": "Allow",
	"Principal": {
		"AWS": [
			"*"
		]
	},
	"Action": [
		"s3:GetBucketLocation",
		"s3:ListBucket"
	],
	"Resource": [
		"arn:aws:s3:::`+m.bucket+`"
	]
},
{
	"Effect": "Allow",
	"Principal": {
		"AWS": [
			"*"
		]
	},
	"Action": [
		"s3:GetObject"
	],
	"Resource": [
		"arn:aws:s3:::`+m.bucket+`/*"
	]
}
]
}`)
	if err != nil {
		return err
	}

	return nil
}

func (m *Minio) Put(filename string, file *bytes.Buffer) (string, error) {
	// Ensure that bucket exists
	err := m.CreateBucketIfNotExists()
	if err != nil {
		return "", err
	}

	ctx := context.Background()
	defer ctx.Done()

	filenameSplit := strings.Split(filename, ".")
	fileType := filenameSplit[len(filenameSplit)-1] // Get the last part of the filename
	if fileType == "" {
		return "", fmt.Errorf("File type is not specified.")
	}
	if fileType != "jpeg" && fileType != "jpg" && fileType != "png" {
		return "", fmt.Errorf("File type %s is not allowed. Filename: %s", fileType, filename)
	}

	if file.Len() > 10*2<<20 { // 10MB
		return "", fmt.Errorf("File size must be less than 10MB.")
	}

	uniqueFilename := fmt.Sprintf("%s-%s.%s", strings.Split(filename, ".")[0], uuid.New().String(), fileType)
	_, err = m.client.PutObject(
		ctx,
		m.bucket,
		uniqueFilename,
		bytes.NewReader(file.Bytes()),
		int64(file.Len()),
		minio.PutObjectOptions{ContentType: "application/octet-stream"},
	)
	if err != nil {
		return "", err
	}
	return fmt.Sprintf("http://localhost:9000/%s/%s", m.bucket, uniqueFilename), nil
}
