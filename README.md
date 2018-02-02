# API for Virus scanning files...

An api that will:

* Receive a message from a configured message queue (SQS)
* Retrieve a file from a storage client (S3) 
* Submit the file for a scan against a virus scan client (ClamAv)
* Add a response to a message queue (SQS)

## Configuring the service
To run the service create an application.properties file in the same folder as the spring jar.
Additionally as this is a SpringBoot application the values can be stored as systems environment properties.
See [Springboot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for full details.
As a short hand through out this document these are refered to as *parameters*.

## Setting the server's port

By default the service runs by default on port 8083. But to change this, add to the configuration the following:
```
--server.port=8080
```
This would put the service on port 8080.

## Monitoring the service
The service exposes a number of end-points for monitoring:
* `/health` A standard status health end-point, which will show the type of db in use.
* `/info` A standard informational endpoint which reveals the application name & version.
* `/healthz` An K8 endpoint for monitoring if the application is up. Http:200 or Http:500 if in trouble (No body content).
* `/readiness` A copy of the above; Subject to change.
* `/metrics` A K8 endpoint for monitoring the memory usage and other stats of the service.



## Running locally (without calls to S3, SQS and ClamAV)
To run the application locally with a H2 database the following profile can be specified at startup.
```
--spring.profiles.active=file-mocks,scan-mocks,jms-disabled
```
## Changing the queue names
To change the default file scan request and response queue names the following properties need to be configured
```
--egar.vscan.req.queue={request_queue}
--egar.vscan.res.queue={response_queue}
##Running with S3
To run against S3 we need to remove the s3-mocks profile and add the following parameters:
```
--aws.s3.region={region}
--aws.s3.bucket={bucket}
--aws.s3.quarantinebucket={quarantinebucket}
--aws.s3.cleanbucket={cleanbucket}
--aws.s3.access.key={access_key}
--aws.s3.secret.key={secret_key}
```
## Running against SQS
To run the application to use Amazon SQS the following properties need to be configured
```
--aws.sqs.access.key={access_key}
--aws.sqs.secret.key={secret_key}
--aws.sqs.region={region}
```
This will then connect to the sqs queues defined in the application for processing.

## Running with ClamAv
Run a local instance of the clam av docker image using the following command:
```
docker run --name clamav -d -p 3310:3310 quay.io/ukhomeofficedigital/clamav:v1.3.5
```
To run the application with the ClamAv the scan-mocks profile needs to be removed and the following parameters added:
```
--clamav.host={host_name}
--clamav.port={port}
```
## Test and code coverage
When the following test command runs:
```
mvn test
```
The unit tests for the application will execute.

This will include generation of a Jacoco report at the following location:
```
/target/jacoco-ut/
```

As the project has used lombok there is a lot of auto generated code which would normally not be picked up under code coverage. 
The addition of the lombok.config file on the top level allows for the "@Generated" tag to be applied to lombok methods.
This will then be excluded from code coverage tests enabling us to generate a more accurate code coverage report.

