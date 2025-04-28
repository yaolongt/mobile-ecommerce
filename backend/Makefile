# run
run:
	echo "Running"
	docker compose --env-file .env up

# stop
stop:
	echo "Stopping"
	docker compose down

# clean up
clean:
	echo "Cleaning up"
	docker compose down -v
	rm ./main
	rm -rf ./minio-data
