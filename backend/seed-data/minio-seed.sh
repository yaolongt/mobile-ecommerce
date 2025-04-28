#!/bin/sh

echo '⏳ Waiting for Minio to be ready...'
until mc alias set minio http://minio:9000 $MINIO_ROOT_USER $MINIO_ROOT_PASSWORD; do
  sleep 1
done

echo '✅ Minio connected!'

if mc ls minio/backend; then
  echo '✅ Bucket already exists, skipping seeding.'
else
  echo '🚀 Creating bucket and seeding images...'
  mc mb minio/backend
  mc cp --recursive /seed-data/images/ minio/backend/
  echo '🎉 Minio seeding completed!'
fi

echo '🔓 Setting Minio backend bucket to public read...'
mc anonymous set public minio/backend
echo '🎉 Minio backend bucket is now publicly readable!'
