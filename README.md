# blob-file-processor
Spring Batch app that reads file from blob storage, decrypts file, performs line transform, encrypts & writes back to blob storage.

- custom ItemReaders & ItemWriters along with StepExecutionListeners
- Testcontainer + docker-compose to setup infra for JUnits
- Spring Batch job testing using syncJobLauncher
- devcontainer - Unix + Java + Docker to test custom encryption libs

## Dev
1. Format Code
```shell
./mvnw com.spotify.fmt:fmt-maven-plugin:format
```


## How to run on Local
1. Setup docker compose
```shell
rm -rf azurite; docker compose kill; docker compose up -d
```
2. run spring boot api
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
3. upload file to docker azure blob
```shell
## only available for local profile
curl --location 'http://localhost:8080/blob/upload' \
--header 'Content-Type: application/json' \
--data '{
    "fileType": "FIXED",
    "sourceFilePath": "src/main/resources/data/input/fixed_length.txt",
    "destinationFilePath": "test-data/fixed_length.txt"
}'
```
4. trigger job
```shell
curl --location 'http://localhost:8080/job' \
--header 'Content-Type: application/json' \
--data '{
"fileType": "FIXED",
    "sourceFilePath": "test-data/fixed_length.txt",
    "destinationFilePath": "test-data/fixed_length_processed.txt"
}'
```


### Azure Blob
```shell
## only available for local profile
# list blobs in a container
curl --location 'http://localhost:8080/blob/list/test-data'
```

### Postman Collection
- [Collection](./docs/file_processing_postman.json)
