# Tuum assignment - Banking

## Table of contents
* [Technologies](#technologies)
* [Setup](#setup)
* [How to test by testers](#how-to-test-by-testers)
* [Development](#development)
* [Suggestions how you plan to enhance](#suggestions-how-you-plan-to-enhance)


## General info
This application is a demo application for postgesql operations for a sample banking account.

It has a swagger ui. http://localhost:8091/swagger-ui/index.html


## Technologies

- Java 17
- Spring boot 2.5.6
- Rabbitmq
- Postgresql 9.x
- Gradle 7.x for build automation
- Docker version 20.10.2
- docker-compose 1.26

## Setup

Check out project from here https://github.com/msinansahin/tuum-banking.git

1. Go to root folder where `.env` and  `docker-compose.yml` are located
2. `gradle clean build`
3. Run tests if you want to be sure that the project is stable.

       gradle test    
       gradle integrationTest  

5. You can use dockerized local development environment. For that just run below commands, then your Rabbitmq and Postgresql will be ready.

        docker-compose up rabbitmq  
   	docker-compose up db  


## How to test by testers

docker-compose is fully parameterized by `.env` file.
There is a default `.env` file at the root path. The defined parameters are as following
Please check .env file for the parameters which can be changed.
Create your own `.env` file to test against different environments and run command below.

`docker-compose --env-file .your.env.file up`

### To start up full application by docker

Go to root folder of application.

1- Build banking-account

`./gradlew build`

`docker-compose build banking-account`

2- Start up infrastructure which contains rabbitmq, and postgresql

`docker-compose up rabbitmq`  
`docker-compose up db`

3- Running application

`docker-compose up banking-account` -- It needs db starting up. Re-call this if db is not accessible! needs to run-check sh should be written for db.


### Default ports

- Banking-account: `8091` Accessible by http://localhost:8091/swagger-ui/index.html
- Rabbitmq management: `15672` Accessible by http://localhost:15672/
- Postgresql: `5432`

## Development

Clone project from here https://github.com/msinansahin/tuum-banking.git

You can use Postgresql and Rabbitmq which are provided by docker-compose in the root project. Or you can use your own or already installed.
For verifying if project is stable, go to project root folder and run commands.

### Unit tests

`gradle test`

### Integration tests

`gradle integrationTest` -- it needs time to get postgresql and rabbitmq containers. So, be patient :)


## Suggestions how you plan to enhance

- Continuous Integration (CI) and orchestration files (jenkins, kubernetes) are not included. Due to that passwords are plaintext.
- Versioning the artifact are not provided. As you can see they are snapshot. Versioning should be done on artifacts.
- Security must be implemented.
- Messages are sent byte arrays, It is better to send them in json format text.
- Customer control is not done. It must be.
- For horizontal scaling, in terms of db check, transaction balance check must be done at db level OR a lock could be established.
- Rabbitmq documentation should be read deeply !

## Explanation of important choices in your solution

- Due to all insert and update operations be sent to message queue, EntityInterceptor is used for that. A gateway is deciding which will be sent.
- An async service is used for send queue messages not to block request thread.
- Balance entity is defined by valid_from and valid_until values. Thereby any balance can be known in any date. Valid_until IS NULL means the current balance of the account.

## Estimation on how many transactions can your account application can handle per second

- It depends on machine memory and CPU, by the threads which jvm can handle and rabbitmq capabilities.

## Horizontal scale issues

- For horizontal scaling, the main issue the current balance of any account. The last balance is stored in database. The most secure way is to lock the account for each transaction. 
