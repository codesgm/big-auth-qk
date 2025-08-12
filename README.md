# big-auth-ms

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Prerequisites

- Docker and Docker Compose installed
- Java 17+ 
- Maven 3.8+

## Initial Setup

### 1. Configure environment variables

```shell script
./setup.sh
```

This will create a `.env` file from `.env.example` with default values. You can customize the variables as needed.

## Running the application in dev mode

### 1. Start the development environment (PostgreSQL)

```shell script
./docker/start-dev.sh
```

This will start a PostgreSQL container using the configuration from your `.env` file:
- Host: localhost (default)
- Port: 5432 (default)
- Database: big_auth_db (default)
- User: big_auth_user (default)
- Password: big_auth_password (default)

### 2. Run the Quarkus application

```shell script
./mvnw quarkus:dev
```

### 3. Stop the development environment

```shell script
./docker/stop-dev.sh
```

## Environment Variables

The application uses the following environment variables (defined in `.env`):

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `POSTGRES_DB_BIG_AUTH` | Database name | `big_auth_db` |
| `POSTGRES_USER` | Database user | `big_auth_user` |
| `POSTGRES_PASSWORD` | Database password | `big_auth_password` |
| `POSTGRES_HOST` | Database host | `localhost` |
| `POSTGRES_PORT` | Database port | `5432` |
| `QUARKUS_HTTP_PORT` | Application port | `8080` |
| `QUARKUS_LOG_LEVEL` | Log level | `INFO` |
| `HIBERNATE_DDL_AUTO` | Hibernate DDL mode | `update` |
| `HIBERNATE_SHOW_SQL` | Show SQL queries | `true` |

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/big-auth-ms-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
