<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
- [Link Tracker](#link-tracker)
  - [About the project](#about-the-project)
    - [Docs](#docs)
    - [Status](#status)
  - [User guide](#user-guide)
    - [Clone repository](#clone-repository)
    - [Editing application.yml files](#editing-applicationyml-files)
    - [Editing docker-compose.yml](#editing-docker-composeyml)
    - [Run from source](#run-from-source)
    - [Run the project with Docker](#run-the-project-with-docker)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

![Bot](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/scrapper.yml/badge.svg)


# Link Tracker

## About the project

This is the app for tracking content updates from links. 
In case some new events happen, notification is sent through Telegram.

Project is written on `Java 21` with use of `Spring Boot 3`.

It consists of two apps (services):

* Bot
* Scrapper

For work, you need `PostgreSQL`, which can be launched with docker-compose. Also, we have optional dependency
on `Kafka`.

### Docs

There are no `.pdf` files with detailed specification,
but you can find Swagger API spec with the appropriate endpoint to every
service (`/api-docs` for docs and `/swagger-ui` for swagger UI).

### Status

The project is ready for deployment.  
Platforms, which are currently available for tracking:
- GitHub
- StackOverflow

## User guide

### Clone repository

You can clone repository locally with this command

```shell
git clone https://github.com/PonomarevAlexxander/telegram-notifier.git
```

### Editing application.yml files

For correct launching, firstly you have to edit `app.telegram-token` setting
in [application.yml](bot/src/main/resources/application.yml),
with use of `BOT_TOKEN` env variable or by providing command line arguments.

Other common settings:

- `app.*client` - http client setting
    - `base-url` - client base url
    - `retry` - retry policy settings
- `server.port` - port on which to start app
- `app.scheduler` - enable/disable scheduled task for fetching updates
- `app.database-access-type` - enum type, which says what Repository (DAO) implementation to use (`JPA`/`JOOQ`/`JDBC`)
- `app.use-queue` - whether to use `Kafka` for async messaging or no

### Editing docker-compose.yml

In the root we have [docker-compose.yml](./docker-compose.yml), which you can edit to configure:

- kafka configuration
- grafana
- prometheus
- postgres

In case you changed something, probably you will have to change settings in `application.yml` files
for [scrapper](scrapper/src/main/resources/application.yml)
and for [bot](bot/src/main/resources/application.yml).

### Run from source

After cloning the repository and editing settings, you can run app with commands:

1. Setup environment with docker-compose

```shell
docker-compose up -d
````

2. Build sources with mvn or from your IDE

```shell
mvn -pl bot -am package
mvn -pl scrapper -am package
```
3. Run from CLI

```shell
java -jar bot/target/bot.jar
java -jar scrapper/target/scrapper.jar
```

### Run the project with Docker

You can download our packages (Docker images) from `ghcr.io` by these commands:

```shell
docker pull ghcr.io/ponomarevalexxander/telegram-notifier/scrapper:pr-22
docker pull ghcr.io/ponomarevalexxander/telegram-notifier/bot:pr-22
```

You can add these containers in [docker-compose.yml](./docker-compose.yml) in the root to start them with needed
infrastructure or just to start them with your Docker.
