# webapi

Web API application providing banking services for the De Llama Bank

## Notices

- Mysql database is assumed to be running on localhost:3306
    - `docker-compose-mysql.yml` can be used with docker-compose to automatically set up a database for testing
    - if you're not using the docker-compose file, run `/src/main/resources/manual_create_database_and_user.sql`
      on the database before starting the application.
    - you can use the `dummydata-populate` property in `/src/main/resources/application.properties` to enable/disable
      the generation of dummy data

## Getting started with development

- Requirements:
  - Java 18+
  - MySQL 8.0.29
  - Modern web browser
- Setup database (see Notices)

## Deployment

- Check application.properties for correct DB settings
- Modify frontend properties if necessary
- Use maven to package the application or create a docker image
- Follow the platform-specific instructions to deploy the application