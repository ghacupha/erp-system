ERP System
==========

.. |Documentation Status| image:: vertopal_5b12338291164d45a75f3769f2c2dab5/953c326c10eb69335c02e3ea0216a2929daa276f.svg
   :target: https://erp-system.readthedocs.io/en/latest/?badge=latest

This application was generated using JHipster 7.3.1, you can find
documentation and help at
https://www.jhipster.tech/documentation-archive/v7.3.1.

Development
-----------

To start your application in the dev profile, run:

::

   ./mvnw

For further instructions on how to develop with JHipster, have a look at
`Using JHipster in
development <https://www.jhipster.tech/documentation-archive/v7.3.1/development/>`__.

Environment
-----------

A couple of environment variables need to be setup for any of the
containers or the source code itself to run. At least the following are
needed at a minimum:

-  LOCAL_PG_SERVER - String for the postres server in the local
   environment
-  ERP_SYSTEM_PROD_DB - String designation of the production DB
-  SPRING_DATA_JEST_URI - String designation of the search engine
   address
-  ERP_SYSTEM_DEV_DB - String designation of the development DB
-  ERP_SYSTEM_PORT - String designation of the server port. Thee client
   will be looking for 8980
-  PG_DATABASE_DEV_USER - String designation of the development db login
   credentials
-  PG_DATABASE_PROD_USER - String designation of the production db login
   credentials
-  PG_DATABASE_DEV_PASSWORD - String designation of the development db
   login password credentials
-  PG_DATABASE_PROD_PASSWORD - String designation of the production db
   login password credentials
-  ERP_SYSTEM_DEV_PORT - String designation of the development db login
   credentials
-  ERP_SYSTEM_PROD_PORT - String designation of the (development) server
   port. Thee client will be looking for 8980
-  ERP_SYSTEM_PROD_MAIL_BASE_URL
-  ERP_SYSTEM_DEV_MAIL_BASE_URL
-  SECURITY_AUTHENTICATION_JWT_BASE64_SECRET
-  UPLOADS_SIZE - String designation of the chunk size of the Excel
   processing system

JHipster Control Center
~~~~~~~~~~~~~~~~~~~~~~~

JHipster Control Center can help you manage and control your
application(s). You can start a local control center server (accessible
on http://localhost:7419) with:

::

   docker-compose -f src/main/docker/jhipster-control-center.yml up

Building for production
-----------------------

Packaging as jar
~~~~~~~~~~~~~~~~

To build the final jar and optimize the erpSystem application for
production, run:

::

   ./mvnw -Pprod clean verify

To ensure everything worked, run:

::

   java -jar target/*.jar

Refer to `Using JHipster in
production <https://www.jhipster.tech/documentation-archive/v7.3.1/production/>`__
for more details.

Packaging as war
~~~~~~~~~~~~~~~~

To package your application as a war in order to deploy it to an
application server, run:

::

   ./mvnw -Pprod,war clean verify

Testing
-------

To launch your application’s tests, run:

::

   ./mvnw verify

Other tests
~~~~~~~~~~~

Performance tests are run by `Gatling <https://gatling.io/>`__ and
written in Scala. They’re located in
`src/test/gatling <src/test/gatling>`__.

To use those tests, you must install Gatling from https://gatling.io/.

For more information, refer to the `Running tests
page <https://www.jhipster.tech/documentation-archive/v7.3.1/running-tests/>`__.

Code quality
~~~~~~~~~~~~

Sonar is used to analyse code quality. You can start a local Sonar
server (accessible on http://localhost:9001) with:

::

   docker-compose -f src/main/docker/sonar.yml up -d

Note: we have turned off authentication in
`src/main/docker/sonar.yml <src/main/docker/sonar.yml>`__ for out of the
box experience while trying out SonarQube, for real use cases turn it
back on.

You can run a Sonar analysis with using the
`sonar-scanner <https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner>`__
or by using the maven plugin.

Then, run a Sonar analysis:

::

   ./mvnw -Pprod clean verify sonar:sonar

If you need to re-run the Sonar phase, please be sure to specify at
least the ``initialize`` phase since Sonar properties are loaded from
the sonar-project.properties file.

::

   ./mvnw initialize sonar:sonar

For more information, refer to the `Code quality
page <https://www.jhipster.tech/documentation-archive/v7.3.1/code-quality/>`__.

Using Docker to simplify development (optional)
-----------------------------------------------

You can use Docker to improve your JHipster development experience. A
number of docker-compose configuration are available in the
`src/main/docker <src/main/docker>`__ folder to launch required third
party services.

For example, to start a postgresql database in a docker container, run:

::

   docker-compose -f src/main/docker/postgresql.yml up -d

To stop it and remove the container, run:

::

   docker-compose -f src/main/docker/postgresql.yml down

You can also fully dockerize your application and all the services that
it depends on. To achieve this, first build a docker image of your app
by running:

::

   ./mvnw -Pprod verify jib:dockerBuild

Then run:

::

   docker-compose -f src/main/docker/app.yml up -d

For more information refer to `Using Docker and
Docker-Compose <https://www.jhipster.tech/documentation-archive/v7.3.1/docker-compose>`__,
this page also contains information on the docker-compose sub-generator
(``jhipster docker-compose``), which is able to generate docker
configurations for one or several JHipster applications.

Continuous Integration (optional)
---------------------------------

To configure CI for your project, run the ci-cd sub-generator
(``jhipster ci-cd``), this will let you generate configuration files for
a number of Continuous Integration systems. Consult the `Setting up
Continuous
Integration <https://www.jhipster.tech/documentation-archive/v7.3.1/setting-up-ci/>`__
page for more information.

