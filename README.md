# ERP System

[![Documentation Status](https://readthedocs.org/projects/erp-system/badge/?version=latest)](https://erp-system.readthedocs.io/en/latest/?badge=latest)

This application was generated using JHipster 7.3.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.3.1](https://www.jhipster.tech/documentation-archive/v7.3.1).

## Development

To start your application in the dev profile, run:

```
./mvnw
```

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Environment

A couple of environment variables need to be setup for any of the containers or the source code itself to run. At least the following are needed at a minimum:

 - LOCAL_PG_SERVER - String for the postres server in the local environment
- ERP_SYSTEM_PROD_DB - String designation of the production DB
- SPRING_DATA_JEST_URI - String designation of the search engine address
- ERP_SYSTEM_DEV_DB - String designation of the development DB
- ERP_SYSTEM_PORT - String designation of the server port. Thee client will be looking for 8980
- PG_DATABASE_DEV_USER - String designation of the development db login credentials
- PG_DATABASE_PROD_USER - String designation of the production db login credentials
- PG_DATABASE_DEV_PASSWORD - String designation of the development db login password credentials
- PG_DATABASE_PROD_PASSWORD - String designation of the production db login password credentials
- ERP_SYSTEM_DEV_PORT - String designation of the development db login credentials
- ERP_SYSTEM_PROD_PORT - String designation of the (development) server port. Thee client will be looking for 8980
- ERP_SYSTEM_PROD_MAIL_BASE_URL
- ERP_SYSTEM_DEV_MAIL_BASE_URL
- SECURITY_AUTHENTICATION_JWT_BASE64_SECRET
- UPLOADS_SIZE - String designation of the chunk size of the Excel processing system

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the erpSystem application for production, run:

```
./mvnw -Pprod clean verify
```

To ensure everything worked, run:

```
java -jar target/*.jar
```

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling).

To use those tests, you must install Gatling from [https://gatling.io/](https://gatling.io/).

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

## Artaxerxes Series?
Well the idea of breaking down versions into series sounds corny, but it's sort of a workaround for versioning the docker images. I figured, I do not have a lot of resources to keep up with semantic versioning especially because both the license headers
which are important to me, and the container versions read the code version from the project object model (pom) file. This means I cannot just start changing the versions because each change would likely produce an additional partition in the docker repository and
there can only be so much free space, and soon would need to start paying that service.
Since this is not a project I get paid for, "free = good" and so with every release we overwrite the previous version but still maintain the "0.0.1-SNAPSHOT" tag.
So versioning is not important here, but I still needed a way to mark progress and milestone achievements and then I got my inspiration from Matrix (the 1999 movie). I'll give you some time to roll your eyes...


Are you done? Good welcome back. So you recall that scene as Neo is introduced, by Morpheus to the crew of the ship Nebuchadnezzar, and there's this inscription on the hull of the ship "Mark III No 11 Nebuchadnezzar Made in USA YEAR 2060". I figured of course, if someone was going to
run a project as ambitious as a hovercraft that size, a vehicle that completely denies the existence of gravity, carry the weight of that engine, the weaponry, the ammunition, food, creature comforts for the crew, communication equipment and still fly at that speed, 
you are going to need to be able to make money from the project in order to fuel the production of the product. That is you will need to be able to
see the product hit the market, at the earliest opportunity, while still creating the product and the goal is not perfection, and there's probably no end goal except to make it better. What there is though, is this short list of MVP specifications which once achieved you start marketing the product while the production and even research is
still on going. So you don't wait to finish the product, you use it as soon as you can while continuing the development. It's a research-in-motion kind of thing. Of course the trilogy likely uses that production code to hint at (I believe) the idea of Neo being "the One",
because if you look at that particular verse it says (Mark 3:11) "you are the Son of God", and Neo in these movies, is a savior whose gifts enable him to achieve the overarching goal of saving humanity. 
As for me, I am (AFAIK) to my knowledge the user of ERP and because of resource and time constraints have developed a non-finished product which I use in office as I develop better techniques, carry out more research and produce both client and server images with better enhanced
features in record keeping and reporting. So why did I use the title Artaxerxes? 

Well the Achaemenid people were known to be lovers of records, it is a generally accepted historical opinion that distinguishes them from their predecessors. For instance in the book of Esther(the Bible again) you see the Queen has been fasting and interceding for the salvation of the people of Israel.
Unbeknownst to them Haman the main antagonist in the book is plotting the death of Mordecai. Am imagining a conversation in heaven as the powers that be discussed how they were going to save Mordecai and disrupt Haman's plans, and an idea comes up: let's give the King a sleepless night, 
and we know how much he loves his records. Right? He will likely start going through the records and in them will find the story of Mordecai, how this servant had uncovered another plot to kill the king and had saved the king's life from those who were plotting evil against him and in the end, no reward was given to the
faithful servant. The king might then be prompted to do something in the servant's favor, knowing that he owed him one, making it impossible, and if not, politically inexpedient for Haman to hang to death the same man for whatever reason.Of course, you know how that unfolds (if not, it's a nice story, read it), because the king found
himself sleepless at night, and sure enough he calls his servants to have them read through records. If you don't find that attachment to records odd, ask yourself this: when was the last time that you lacked sleep in the night and so you decided to go get some business
records for entertainment (or whatever)? Not many will answer in the affirmative, and I will tell you why. Not many will you find keeping logs of their transactions, receipts, contracts and so on and so forth. When one of your appliances break down, you find you don't even know where you left
the warranty document. If you do keep such records, how many sleepless nights did you find yourself perusing them for whatever reason as opposed to tv, Facebook, Twitter; most will not even leave the bed. So if you were king instead, Mordecai would hang despite all the good he's done for you.
So that's what it about, a desire to keep track of my personal day-to-day transactions at the office and have a way of recalling that information quickly when needed. So you will find all kinds of entities, for all my various concerns in my own work. This milestone describes a system with
the ability to do that, and the flexibility to fashion reports as desired and have them produced quickly and as needed. We are able to define reports and run them without recompiling the code, ensure security of records and access them at any time, thanks to the search functionality (by the way big ups to Elasticsearch on that one!) 
Once that basic need is fulfilled, the milestone is complete. 

Oh, and that king was called Artaxerxes.

## Caleb Series?
The Caleb series designation is a hats off recognition of a certain person whose story keeps motivating me to keep on taking on difficult challenges. If you are a person of the book then you know of the person with little faith whose command given in faith could move a mountain and cast it into the sea and
thereby perhaps clearing their way, or establishing a suitable settlement, or surmounting some great challenge. I often wonder, that if such a powerful act is orchestrated by a person with little faith, what wonders would great faith manifest?
We meet such a person in Caleb. As Israelites settled into Caanan, there was a challenge in that though the land was "theirs" as per the promise to Abraham, the residents were not magically removed to
"make way" for them. They had to deal with the reality that they would have to take the land by conquest. 
The last time a company had been sent to "measure up the enemy" the spies were truly freaked out and scared the crowd into a frenzy so much so that they were sent back into the desert for disobedience and lack of faith.
And even now when they came back, this time under the leadership of Joshua, these giants and well-fed residents of Canaan had gone nowhere. The Lord had not removed them to prepare a place for this special people.
So this is where the character of Caleb inspires, in that the man is not going to pray his obstacles away. No. Instead, he says "give me this mountain!". That inspiration drives my persistence through overwhelming failure.
Obstacles exist, training may not suffice, errors without proper logs, null pointer exceptions, stack overflows, front end systems that disagree with your backend thinking, the list goes on. The challenges I seek to surmount will have to be addressed by someone eventually. 
In this series I seek to improve the system further by keeping reporting modules front and centre. Additionally, I make my life even harder by trying to address the challenges posed by lease accounting in IFRS 16, contract management,
and fixed assets management and reporting. This calls for that much extra dose of courage to take on a task that could lead you into one rabbit hole today; that you get out of in like a month having achieved nothing, and having the tenacity
to not simply give up or despair, but to try something else, or the same problem differently.
When these modules are done we will remember choosing to stay with the problem and embrace failure with open arms, repeat gruelling work whether feeling inspired or not inspired. Those words come to me every time. Caleb sets a lofty example seeking not that the 
mountain be moved, or that his enemies be magically done away with, or for that to become someone else's problem. He chose to make the mountain his home. 

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.3.1 archive]: https://www.jhipster.tech/documentation-archive/v7.3.1
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.3.1/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.3.1/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.3.1/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.3.1/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.3.1/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.3.1/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[gatling]: https://gatling.io/
