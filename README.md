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

## Important links

 - [jhipster homepage and latest documentation](https://www.jhipster.tech)
 - [jhipster 7.3.1 archive](https://www.jhipster.tech/documentation-archive/v7.3.1)
 - [using jhipster in development](https://www.jhipster.tech/documentation-archive/v7.3.1/development/)
 - [using docker and docker-compose](https://www.jhipster.tech/documentation-archive/v7.3.1/docker-compose)
 - [using jhipster in production](https://www.jhipster.tech/documentation-archive/v7.3.1/production/)
 - [running tests page](https://www.jhipster.tech/documentation-archive/v7.3.1/running-tests/)
 - [code quality page](https://www.jhipster.tech/documentation-archive/v7.3.1/code-quality/)
 - [setting up continuous integration](https://www.jhipster.tech/documentation-archive/v7.3.1/setting-up-ci/)
 - [node.js](https://nodejs.org/)
 - [npm](https://www.npmjs.com/)
 - [gatling](https://gatling.io/)

# DEVELOPMENT NOTES

This an unstructured overview of various design patterns, policies and thoughts behind the patterns

## Hybrid Approach to Depreciation of AssetRegistration Items
The AssetRegistration entity is designed to record details about fixed assets. In the recent days
we have been looking into implementing an pseudo autonomous process of depreciation of the same.
Note that the assetCost field is not itself changing but it is used to come up with the appropriate
value of depreciation at a given depreciationPeriod.

In general the idea revolves around Martin Fowler's [accounting pattern](https://martinfowler.com/eaaDev/AccountingNarrative.html)
in which depreciation is recorded as a series of subsequent depreciation records in a table. Should then
one want to "see" how much depreciation has accrued for a given period, you simply run a query on that
table with parameters for periodicity and there you have a depreciation report. Am accounting for the
fact that SQL tables have become fast enough to run complex queries in a short amount of time. One
is not therefore expected to derive entire datasets from repositories and then to manually managed
then the entries upon queries like in the [accounting pattern](https://martinfowler.com/eaaDev/AccountingNarrative.html).
Depreciation entries are created and stored in the database with the reference to the respective
assetRegistration and thereby depreciation is noted to have taken place. That's the philosophy of 
depreciation here.

The challenge then is how to make such a process efficient in the face of large amount of instances
and the need to view quick results. 
At the heart of the matter is the depreciationCalculator and especially the reducingBalanceMethod calculator
which would need to follow the following steps. With many subsequent adjustments made by the dev to 
improve the process the code might not look exactly the same.

To implement the most efficient reducing balance depreciation method when retrieving asset, depreciation method, asset category, and depreciation period data from a database, you can follow these steps:

1. Retrieve the relevant data from the database:
    - Fetch the asset data, including its initial cost, residual value, and asset category.
    - Retrieve the depreciation method associated with the asset category.
    - Fetch the depreciation periods for the asset, including their start and end dates.

2. Calculate the depreciation for each period:
    - Determine the elapsed time (in months) for each depreciation period based on the start and end dates.
    - Apply the reducing balance depreciation formula to calculate the depreciation amount for each period. The formula typically involves multiplying the asset's net book value by a depreciation rate specific to the asset category.

3. Update the asset's net book value:
    - Subtract the depreciation amount from the asset's net book value for each period.
    - Update the asset's net book value in the database after each depreciation calculation.

Here's a high-level example of how the code flow could look:

```java
    // Retrieve asset data from the database
    Asset asset = assetRepository.findById(assetId);
    BigDecimal initialCost = asset.getInitialCost();
    BigDecimal residualValue = asset.getResidualValue();
    AssetCategory assetCategory = asset.getAssetCategory();
    
    // Retrieve depreciation method from the asset category
    DepreciationMethod depreciationMethod = assetCategory.getDepreciationMethod();
    
    // Retrieve depreciation periods from the database
    List<DepreciationPeriod> depreciationPeriods = depreciationPeriodRepository.findByAsset(asset);
    
    // Calculate reducing balance depreciation for each period
    BigDecimal netBookValue = initialCost;
    for (DepreciationPeriod depreciationPeriod : depreciationPeriods) {
        BigDecimal depreciationAmount = netBookValue.multiply(depreciationMethod.getDepreciationRate());
        
        // Update the asset's net book value
        netBookValue = netBookValue.subtract(depreciationAmount);
        
        // Perform any additional actions, such as storing the depreciation amount or updating the database
        // ...
}
```

Keep in mind that this is a simplified example and that there have been modifications based on our services and the structure repositories. I say "services"
because our stack also runs a several search-index and the services are good at hiding the nature of that complexity and in fact look like exact replicas of repositories, but they are not.
You would need to adjust the code to match your database schema, entity relationships, and persistence framework (e.g., Hibernate). Also I looked
everywhere online and could not find anyone who had suggested a process for assets depreciation while interacting with an SQL sink. So some of the 
overview ideas I was working on while using chatGPT, as something like a `brain-storm partner`. Let's give credit where its due. 
It was interesting to see that I could look at a general idea with this tool and scaffold the same in code
and think about how to integrate that into ERP. It helped me look at many perspectives, and concepts and to "see" others
which I had not considered. Many designs and patterns failed, and I would reiterate again. So proper references do not exist at the moment, and this is really a shot in the dark.

Ultimately I saw that such a process could not be done synchronously with the client waiting for the response. 
Even when done asynchronously it would be too inefficient to attempt this with every single instance, because the 
database I am thinking about should have like 10,000 instances. That could be weeks in processing a single
depreciation period.

We considered batching the database updates instead of performing them individually within the loop to reduce the number of database round-trips.
Overall, by retrieving the necessary data from the database and efficiently calculating the reducing balance depreciation within the code, 
I would (I thought) effectively manage asset depreciation for reporting purposes.
Then I saw what happened to the RAM, and started remembering all those problems from the space-complexity notes. This thing would be
fast, but would require an unfortunate amount of RAM.

So I considered how I could do such a thing with the spring-batch library. Basically conjure up some batch
process and let spring manage the process. By the way this is the same thing we do with uploading lists of data from a file. We upload an Excel file and then read the contents
using [poiji library](https://github.com/ozlerhakan/poiji) into a list. Then initiate a spring-batch process and
persist the list into appropriate entities using split up lists at the reader interface. 

So in this case an event from the API creates a depreciation run entity, and utilizes Spring Batch to process the database of assets in batches while keeping track of the batches fetched using a depreciation batch sequence entity, in the following steps:

1. Define the entities:

    - `DepreciationRun`: Represents the depreciation run entity, which triggers the depreciation process. It may contain information such as the run ID, run date, and any other relevant details.

    - `DepreciationBatchSequence`: Represents the entity used to track the batches of assets fetched during the depreciation process. It may include properties like the batch sequence ID, start index, end index, and the status of the batch (e.g., processed, pending).

2. Create the Spring Batch job and steps:

    - Define a Spring Batch job that encapsulates the depreciation process. This job will execute the depreciation in batches.

    - Create a step within the job that processes a single batch of assets. The step should fetch the assets from the database within a specific range, perform the depreciation calculations for the batch, and update the necessary entities or records accordingly.

3. Implement the service:

    - Create a service class, such as `DepreciationService`, that orchestrates the depreciation process.

    - Implement a method in the service, such as `triggerDepreciation`, that is invoked when a new `DepreciationRun` entity is created. This method starts the Spring Batch job and initiates the depreciation process.

    - The `triggerDepreciation` method can perform additional tasks, such as initializing the depreciation batch sequence entity, setting the initial batch parameters, and persisting the entity in the database.

4. Configure the Spring Batch job:

    - Configure the Spring Batch job using Spring Batch configuration files, annotations, or Java configuration.

    - Define the job parameters, steps, and any necessary listeners or processors for the depreciation batch processing.

    - Implement the logic for fetching assets in batches, performing the depreciation calculations, and updating the database records.

    - Use the depreciation batch sequence entity to keep track of the progress, such as updating the start and end indices of each batch, and marking batches as processed.

The depreciation service orchestrates the process, and the depreciation batch sequence entity keeps track of the progress, allowing you to resume the depreciation process in case of failures or interruptions.

You do have to configure the appropriate transaction management, error handling, and logging mechanisms to ensure the reliability and consistency of the depreciation process.

However, the design of this platform would then become unbelievably complex, would have too much magic and would be hard to maintain. At the moment am still struggling to understand how to keep
liquibase from dropping the spring-batch tables that I've been using for those Excel file uploads, and I felt I did not need this additional spanner to the works.

This is a sample of how the `DepreciationService` would be implemented:

```java
@Service
public class DepreciationService {

    private final DepreciationRunRepository depreciationRunRepository;
    private final AssetRepository assetRepository;
    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;
    private final DepreciationCalculator depreciationCalculator;

    public DepreciationService(DepreciationRunRepository depreciationRunRepository,
                               AssetRepository assetRepository,
                               DepreciationBatchSequenceRepository depreciationBatchSequenceRepository,
                               DepreciationCalculator depreciationCalculator) {
        this.depreciationRunRepository = depreciationRunRepository;
        this.assetRepository = assetRepository;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationCalculator = depreciationCalculator;
    }

    public void triggerDepreciation() {
        // Create a new DepreciationRun entity
        DepreciationRun depreciationRun = new DepreciationRun();
        depreciationRun.setRunDate(LocalDate.now());
        // Set other properties as needed

        // Save the DepreciationRun entity to the database
        depreciationRun = depreciationRunRepository.save(depreciationRun);

        // Retrieve the assets from the database
        List<Asset> assets = assetRepository.findAll();

        // Process the assets in batches
        int batchSize = 100; // Set the batch size as desired
        int totalAssets = assets.size();
        int processedCount = 0;

        while (processedCount < totalAssets) {
            int startIndex = processedCount;
            int endIndex = Math.min(processedCount + batchSize, totalAssets);

            // Get the current batch of assets
            List<Asset> currentBatch = assets.subList(startIndex, endIndex);

            // Perform the depreciation calculations for the current batch
            for (Asset asset : currentBatch) {
                // Calculate the depreciation amount using the DepreciationCalculator
                BigDecimal depreciationAmount = depreciationCalculator.calculateStraightLineDepreciation(asset.getInitialCost(), asset.getResidualValue(), asset.getUsefulLifeYears());

                // Update the asset's net book value and any other relevant data
                // ...

                // Save the updated asset to the database
                assetRepository.save(asset);
            }

            // Create a DepreciationBatchSequence entity to track the processed batch
            DepreciationBatchSequence batchSequence = new DepreciationBatchSequence();
            batchSequence.setDepreciationRun(depreciationRun);
            batchSequence.setStartIndex(startIndex);
            batchSequence.setEndIndex(endIndex);
            batchSequence.setStatus("Processed");

            // Save the DepreciationBatchSequence entity to the database
            depreciationBatchSequenceRepository.save(batchSequence);

            processedCount += batchSize;
        }

        // Mark the depreciation run as completed
        depreciationRun.setStatus("Completed");
        depreciationRunRepository.save(depreciationRun);
    }
}
```

In this example, the `DepreciationService` class triggers the depreciation process when the `triggerDepreciation` method is called. The service depends on repositories (`DepreciationRunRepository`, `AssetRepository`, `DepreciationBatchSequenceRepository`) to access the database and the `DepreciationCalculator` for performing the depreciation calculations.

The `triggerDepreciation` method performs the following steps:

1. Creates a new `DepreciationRun` entity with the current run date and any other relevant properties.
2. Saves the `DepreciationRun` entity to the database.
3. Retrieves all the assets from the database.
4. Processes the assets in batches, with a specified batch size.
5. Calculates the depreciation amount for each asset using the `DepreciationCalculator`.
6. Updates the asset's net book value and other relevant data.
7. Saves

the updated asset to the database.
8. Creates a `DepreciationBatchSequence` entity to track the processed batch and saves it to the database.
9. Repeats the process until all assets are processed.
10. Marks the depreciation run as completed and saves the `DepreciationRun` entity.

This is a sample of how the `DepreciationBatchSequence` entity would be defined:

```java
import javax.persistence.*;

@Entity
@Table(name = "depreciation_batch_sequence")
public class DepreciationBatchSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "depreciation_run_id")
    private DepreciationRun depreciationRun;

    @Column(name = "start_index")
    private int startIndex;

    @Column(name = "end_index")
    private int endIndex;

    @Column(name = "status")
    private String status; // Indicates the status of the batch (e.g., processed, pending)

    // Constructors, getters, and setters, bla! bla! bla!

    // ...
}
```

In this example, the `DepreciationBatchSequence` entity represents a batch sequence within a depreciation run. It includes the following properties:

- `id`: Represents the primary key of the entity.
- `depreciationRun`: Represents the association to the `DepreciationRun` entity using a Many-to-One relationship. It is annotated with `@ManyToOne` and `@JoinColumn` to establish the relationship.
- `startIndex`: Represents the start index of the asset batch.
- `endIndex`: Represents the end index of the asset batch.
- `status`: Indicates the status of the batch, such as "processed" or "pending".

Of course the actual implementation will include stuff we cannot include in this discussion.

So to reduce complexity, I thought I would have to use kafka. Which brings us full circle. In the beginning
of this project, kafka was thought of as an additional complexity item to the stack that would just slow the development process. 
So I this time I made sure I have my justifications down.

I considered the need for the depreciation process to run asynchronously without blocking the response to the client on the API.
A message queue such as KAFKA would have the following utility:

- Instead of directly triggering the depreciation process, you can publish a message to a message queue.
- The API endpoint can quickly return a response to the client, indicating that the depreciation process has been scheduled for execution.
- A separate worker process or a dedicated service subscribes to the message queue and processes the depreciation messages asynchronously.
- The worker process can execute the depreciation process in the background, independently of the API request/response cycle.

This approach decouples the API from the actual depreciation processing, allowing for scalability and fault tolerance.

There is still the question of additional complexity to the system. You need to set up and manage a Kafka cluster, handle topics, and configure consumers and producers. 
If the depreciation processing requirements are relatively simple and the volume is low, simple multithreading might be a more straightforward 
approach without the overhead of managing a separate messaging system. 

But the requirements here are not simple, and the volume is quite high. In fact the whole thing might be another fail if you dare enqueue items in a 
one by one fashion.
We want to achieve high processing speeds, but have limited RAM. To achieve high speeds you can send a single message to trigger the depreciationRun, then all those assets
are read from the database and into the RAM and depreciation begins. To achieve very efficient use of RAM space we could trigger depreciation by sending messages to trigger
depreciation of individual asset instances into the queue in a one by one fashion. But the process would then be slow, and the processor would run to the max as
we are repeatedly serializing and deserializing individual items. These are my considerations.

The choice between sending messages for individual assets or sending a single message to trigger the depreciation of all assets depends on the need for speed efficiencies, and space efficiencies
and processing cheapness. We want it all.  Here are some considerations for both approaches:

1. **Sending messages for individual assets:**

    - **Advantages:**
        - Flexibility: Sending individual messages allows for fine-grained control over each asset's depreciation process. You can process assets independently and potentially parallelize the depreciation calculations for improved performance.
        - Reduced memory usage: Processing assets individually can help manage memory usage, especially if you have a large number of assets. You only need to load and process one asset at a time, which can be beneficial if you have limited RAM.

    - **Considerations:**
        - Message overhead: Sending individual messages for each asset incurs additional overhead due to message serialization, network communication, and the Kafka infrastructure itself. This can impact the overall processing speed, especially if there are a large number of assets.
        - Message ordering: Depending on your requirements, processing assets individually may introduce ordering challenges. If asset order matters for the depreciation calculations, ensuring correct sequencing of messages can be complex.

2. **Sending a single message for all assets:**

    - **Advantages:**
        - Reduced message overhead: Sending a single message to trigger the depreciation of all assets reduces the message serialization and communication overhead compared to sending individual messages. This can improve the overall processing speed.
        - Simplified message ordering: With a single message, you can ensure the correct order of assets for depreciation calculations, as they are processed in a controlled batch sequence.

    - **Considerations:**
        - Increased memory usage: Processing all assets at once may require loading and keeping all assets in memory simultaneously, which can be memory-intensive. If you have limited RAM, this approach might not be feasible for a large number of assets.

In terms of achieving high processing speed and considering limited RAM, processing assets in batches can be more efficient. It allows you to control the memory usage by loading and processing a subset of assets at a time. 
However, the exact performance and resource trade-offs depend on factors such as the number of assets, the complexity of depreciation calculations, and the available hardware.

So we strike a balance by using a hybrid approach: send messages in batches rather than for individual assets. This way, you can process assets in manageable batches while minimizing message overhead.

Bear in mind that we are yet to attempt performance testing and profiling to evaluate the impact of each approach and optimize accordingly, but it seems like it might work. Of course there could be something that we've overlooked simultaneously
wasting weeks of work and shooting ourselves on both feet with the level of complexity we are trying to put together here. When it is complete I will be sure to hide the kafka infrastructure behind opaque walls of
interfaces to make sure we do not interact with the business code itself. 

This is how the depreciation process has been designed to work.

#### Later date update, many many days later
I think I seriosly underestimated the amount of structure needed to bring a reliable depreciation module to life. Again the culprit here is reducing-balance-depreciation
because you need to efficiently calculate netBookValues before calculating the current date depreciation. Now am starting to see why people hate accountants.

The structure I wanted to implement needed computational flexibility and that becomes the genesis of my issues. The workflow generally goes like this: the user creates a 
depreciation-period instance, and then creates a depreciation-job with a specification of that instance effectively creating a one to one relationship between the depreciation-job
and the depreciation-period.

So then the reducing-balance-workflow ends up like this:

```java

@Service("reducingBalanceDepreciationCalculator")
public class ReducingBalanceDepreciationCalculator implements CalculatesDepreciation {

    private static final int DECIMAL_SCALE = 6;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int MONTHS_IN_YEAR = 12;

    public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) {

        // opt out
        if (depreciationMethod.getDepreciationType() != DepreciationTypes.DECLINING_BALANCE ) {

            return BigDecimal.ZERO;
        }

        BigDecimal netBookValue = asset.getAssetCost();
        BigDecimal depreciationRate = assetCategory.getDepreciationRateYearly().setScale(DECIMAL_SCALE, ROUNDING_MODE).divide(BigDecimal.valueOf(MONTHS_IN_YEAR), ROUNDING_MODE).setScale(DECIMAL_SCALE, ROUNDING_MODE);
        LocalDate capitalizationDate = LocalDate.of(2023,6,6);
        LocalDate periodStartDate = period.getStartDate();
        LocalDate periodEndDate = period.getEndDate();

        if (capitalizationDate.isAfter(periodEndDate)) {
            return BigDecimal.ZERO; // No depreciation before capitalization
        }

        BigDecimal depreciationBeforeStartDate = BigDecimal.ZERO;
        if (capitalizationDate.isBefore(periodStartDate)) {
            int elapsedMonthsBeforeStart = Math.toIntExact(ChronoUnit.MONTHS.between(capitalizationDate, periodStartDate));
            for (int month = 1; month <= elapsedMonthsBeforeStart; month++) {
                BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(DECIMAL_SCALE, ROUNDING_MODE);
                depreciationBeforeStartDate = depreciationBeforeStartDate.add(monthlyDepreciation);
                netBookValue = netBookValue.subtract(monthlyDepreciation);
                if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                    netBookValue = BigDecimal.ZERO;
                }
            }
        }


        int elapsedMonths = Math.toIntExact(ChronoUnit.MONTHS.between(periodStartDate, periodEndDate));

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate).setScale(DECIMAL_SCALE, ROUNDING_MODE);
            depreciationAmount = depreciationAmount.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        // TODO calculate accrued depreciation with net period start details
        // TODO net period start is end-date + 1 day
        // accruedDepreciation = depreciationAmount.add(depreciationBeforeStartDate);

        // TODO let's not waste this expensive info. Throw it into a queue
        // TODO enqueueNetBookValueUpdate(asset.getId(), netBookValue);
        // todo enqueueAccruedDepreciationUpdate(asset.getId(), accruedDepreciation);


        return depreciationAmount;
    }

//    private int calculateElapsedMonths(DepreciationPeriodDTO period) {
//        return Math.toIntExact(ChronoUnit.MONTHS.between(period.getStartDate(), period.getEndDate()));
//    }
}


```

But there's a challenge: what do we do with all that juicy netBookValue information and that accruedDepreciation?
So I thought I would other message queues (complicating the process even further), into which I would throw this information
while maintaining the integrity of the method which is, to simply calculate the current period depreciation. 
So again, I went the trusty chatGpt friend and asked what they thought. The poor thing started talking about the complexity that
am now introducing into the code...

Then I went like:

You let me worry about the complexity of the message queue. But now it seems that we have an opportunity to optimize the depreciation 
process further by  checking the database for these values if they align with the netBookValue before the startDate and the 
accruedDepreciation before the startDate. How would we incorporate that into our present code, providing for the following: 
we don't actually want to have a field tracking accrued depreciation amount or netBookValue amount in the assetRegistration as that would turn it into a 
temporal object, and yet I need that object to remain independent of time except with respect to the capitalizationDate. In line with that I 
intend to implement hibernate entities to represent accruedDepreciation  and netBookValue entities in a one-way one to many assetRegistration 
to accruedDepreciation relationship and assetRegistration to netBookValueRelationship. This will have the benefit of keeping the 
assetRegistration "ignorant" about the netBookValue and accruedDepreciation entities, which providing the latter entities a reference to the 
assetRegistration to which they refer for reporting purposes. 

Of course it's thoughts like these that often lead into rabbit holes but chatGpt easily gave gave me some rope to hang myself:

It sounds like you want to optimize the depreciation calculation process by checking the database for pre-calculated values of net book value and accrued depreciation, 
while keeping the `AssetRegistration` object independent of these values in order to maintain its temporal nature. Additionally, you're planning to use 
Hibernate entities to represent `AccruedDepreciation` and `NetBookValue` while establishing relationships between these entities and `AssetRegistration`.

Here's how you could incorporate these ideas into your design:

1. **Hibernate Entities:**
   Define `AccruedDepreciation` and `NetBookValue` Hibernate entities. Establish a one-to-many relationship between `AssetRegistration` and each of these entities.

```java
@Entity
public class AssetRegistration {
    // Other fields
    
    @OneToMany(mappedBy = "assetRegistration")
    private List<AccruedDepreciation> accruedDepreciations;

    @OneToMany(mappedBy = "assetRegistration")
    private List<NetBookValue> netBookValues;

    // Getters and setters
}

@Entity
public class AccruedDepreciation {
    @ManyToOne
    @JoinColumn(name = "asset_registration_id")
    private AssetRegistration assetRegistration;
    
    private BigDecimal value;
    
    // Other fields, getters and setters
}

@Entity
public class NetBookValue {
    @ManyToOne
    @JoinColumn(name = "asset_registration_id")
    private AssetRegistration assetRegistration;
    
    private BigDecimal value;
    
    // Other fields, getters and setters
}
```

2. **Depreciation Calculation:**
   Modify your `calculateDepreciation` method to first check the database for the values of `AccruedDepreciation` and `NetBookValue` before the `periodStartDate`. If the values exist, use them for the calculations. If not, perform the depreciation calculation as before and enqueue messages to update the database asynchronously.

```java
public BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) {
    // Check database for accrued depreciation and net book value before periodStartDate
    BigDecimal accruedDepreciationBeforeStart = fetchAccruedDepreciationFromDatabase(asset.getId(), period.getStartDate());
    BigDecimal netBookValueBeforeStart = fetchNetBookValueFromDatabase(asset.getId(), period.getStartDate());

    // If values exist, use them
    if (accruedDepreciationBeforeStart != null && netBookValueBeforeStart != null) {
        return accruedDepreciationBeforeStart;
    }

    // Calculate and return the depreciation for the specified period as before
    BigDecimal calculatedDepreciation = calculateDepreciationForPeriod(asset, period, assetCategory, depreciationMethod);

    // Enqueue messages to update accrued depreciation and net book value in the database
    enqueueAccruedDepreciationUpdate(asset.getId(), period.getStartDate(), calculatedDepreciation);
    enqueueNetBookValueUpdate(asset.getId(), period.getStartDate(), netBookValueForStart);

    return calculatedDepreciation;
}

private BigDecimal calculateDepreciationForPeriod(AssetRegistrationDTO asset, DepreciationPeriodDTO period, AssetCategoryDTO assetCategory, DepreciationMethodDTO depreciationMethod) {
    // Your existing depreciation calculation logic here
}

private BigDecimal fetchAccruedDepreciationFromDatabase(Long assetId, LocalDate startDate) {
    // Query database to fetch accrued depreciation before startDate for the given assetId
    // Return null if not found, or the value if found
}

private BigDecimal fetchNetBookValueFromDatabase(Long assetId, LocalDate startDate) {
    // Query database to fetch net book value before startDate for the given assetId
    // Return null if not found, or the value if found
}
```

This approach allows you to first check the database for pre-calculated values of accrued depreciation and net book value before the `periodStartDate`. 
If these values exist, you can use them directly without recalculating. If not, you proceed with the depreciation calculation and asynchronously update 
the database for future use.
Remember to implement the database queries and enqueueing of update messages as needed in your application infrastructure.

Thank you GPT. This is not what I wanted, because I needed for there to be actual ignorance about netBookValue and accruedDepreciation within the sphere
of the assetRegistration. This is because it is becoming increasingly difficult to maintain that information in the search index and in fact some entities
just plain refuse to save due to limits in the community elasticsearch engine am forced to use.
From the look of things, the Ehud series will continue for a while longer, may be 2 to 3 weeks as we struggle with the depreciation module.


#### Later date update (Ehud Series version 1.3.4)
So somehow after unbelievable hours on the debugger, we finally managed to draft working algorithms
for both straight-line depreciation and reducing-balance depreciation. We also added unit tests
using a fake data generator. 
Also we tested the results against live data and could see similarities between what is picked up
by an excel calculation and what the system is computing. This is not to say that the coast is definitely
clear.
So then it seems the depreciation workflow is precipitating (at least in my mind) to look like this

Step 1: **Create depreciation period**
The user defines the start-date of the depreciation period and the end-date for the same

Step 2: **Create a depreciation job**
The user creates a depreciation job creation of which triggers the depreciation process. The situation
here is that once the job has finished one can tell from the status of the instance that the 
process is complete.

I also wanted to have a way to tag with a label the depreciation-period for  reporting purposes.
So I am going to create another entity and define it as periodLabel.
This will essentially be part of step 1. But there's more to it.
I think the option to create an independent PeriodLabel entity might be more appropriate because am 
thinking in case of errors one might want to do something like void depreciation entries for a specific 
period, and create another period for the same label which though it might be the same label could have 
varying start and end dates. This would further support something like what-if analysis if you wanted 
to test how depreciation might look like in a year, or how depreciation might look like for a given quarter.

Creating an independent PeriodLabel entity sounds like a good choice given the additional flexibility 
and potential benefits. This approach allows for better separation of concerns and makes it easier to manage 
and track depreciation periods with specific labels.

By having a separate entity, you're able to associate metadata and behavior with each label, which can be useful for auditing, reporting, error handling, and more. Here are a few benefits of using an independent PeriodLabel entity:

**Flexibility for Label Management**: With an independent entity, you can easily manage and modify labels, associate descriptions or other metadata, and make changes without directly impacting the periods themselves.

**Audit and Error Handling**: If you need to perform actions like voiding depreciation entries or making corrections, having a separate entity allows you to do this in a controlled manner while maintaining historical data.

**What-If Analysis and Reporting**: You can use different periods with the same label to perform what-if analyses or generate reports with specific label-based filters.

**Scalability**: If requirements evolve and you need to add more features or relationships related to period labeling, having a dedicated entity can accommodate those changes more easily.

**Data Integrity**: Separating the label information from the core depreciation period information can contribute to data integrity and overall maintainability.

I am noting this down so that it can be a record that explains why the depreciation procedure and input forms have been designed with too much user input detail; because am sure it's going to be costly in terms of UX.

#### Update 2023-08-17

**Explanation of Entities and Justifications in the Depreciation Module:**

In the Depreciation Module of our ERP system, we have carefully designed a set of entities to facilitate accurate, efficient, repeatable, and relatable handling of asset depreciation calculations and reporting. These entities play a crucial role in managing the entire lifecycle of depreciation, ensuring compliance with accounting standards, and providing valuable insights for decision-making. Below, we provide an overview of each entity along with its justification:

1. **AssetRegistration Entity:**
    - **Justification:** This entity represents individual assets within our organization, allowing us to capture essential asset details such as cost, model number, and serial number. These details are pivotal for accurate depreciation calculations and financial reporting.

2. **DepreciationMethod Entity:**
    - **Justification:** The DepreciationMethod entity defines the various methods by which depreciation is calculated. This includes methods like straight-line depreciation and reducing balance depreciation. Defining methods ensures consistency and conformity with accounting principles.

3. **DepreciationPeriod Entity:**
    - **Justification:** The DepreciationPeriod entity allows us to segment the depreciation calculation into specific time periods, such as fiscal years, quarters, and months. This segmentation aligns with accounting practices and provides granularity for accurate financial reporting.
      Further because each of these time segments have independent start and end dates, we can align the depreciation process to real life application, like for instance the case like mine when we do not actually carry out depreciation procedures at the dead end of the month but on designated
      policy dates of from 20th to 21st, and yet report those numbers in the fiscal month that ends after 21st, leaving out all new assets purchased after the 21st. In such a case this model of operation would allow  one to carry out depreciation for new assets within those periods with
      consistency, and but at the same time generate reports at the end of month with the recent purchases excluded from depreciation.

4. **FiscalYear, FiscalQuarter, and FiscalMonth Entities:**
    - **Justification:** These entities help us organize time periods for depreciation calculations. FiscalYear represents a full financial year, while FiscalQuarter and FiscalMonth further break down the year into manageable segments. This structure adheres to reporting standards and allows for better tracking and analysis.

5. **DepreciationJob Entity:**
    - **Justification:** The DepreciationJob entity serves as a container for grouping depreciation periods and initiating the calculation process. This entity streamlines the management of depreciation calculations, provides a clear audit trail, and allows for parallel processing if required.

6. **DepreciationJobNotice Entity:**
    - **Justification:** The DepreciationJobNotice entity captures events and issues that occur during the depreciation calculation process. This entity ensures transparency and accountability, enabling us to identify and address any anomalies or errors without interrupting the entire process.

7. **ExcelReportExport Entity:**
    - **Justification:** This entity handles the generation and storage of Jasper Reports, which provide detailed insights into the calculated depreciation values. ExcelReportExport allows us to create reports based on customizable templates and specific parameters, contributing to informed decision-making.
      As of the date of writing the parameters for the jasper template are implemented through the universally-unique-mapping entity, which allows one to dynamically specify the parameter-name (has to match the parameter in the template), and the parameter value e.g. start date, report date, report period etc.
      That's the best that could be done for dynamic runtime reports. We will endeavour to create more structured and repeatable reports with dedicated forms specifically for the fixed-assets registration module.

8. **ReportDesign Entity:**
    - **Justification:** The ReportDesign entity stores Jasper Report templates, which are used to generate depreciation reports. This separation of templates from data ensures modularity, ease of updates, and consistent formatting across different reports. This also allows users to define new reports and execute them
      using parameters created in the universally-unique-mapping entity. This means basically that for someone to create a new report we would not need to stop the server to encode the new report, but simply to define the JRXML file (so you will need a jasper-reports expert), in which is contained the report query,
      (so you will need and SQL expert, who is also aware of the postgresql tables on the database), save the file with a new instance of report design, ensure the parameters in the report exist in the universally-unique-mapping entity and save the file.
      The end-user will then query the report-design when generating a report.

By meticulously defining and integrating these entities, we achieve a comprehensive and streamlined solution for handling asset depreciation. This setup adheres to accounting standards, provides flexibility for different depreciation methods and time periods, supports efficient report generation, 
and ensures accuracy in financial reporting. The architecture is designed to accommodate future enhancements, therefore the final copy might look and work differently from the one implied in this discussion.



