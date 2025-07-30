# ERP System

[![Documentation Status](https://readthedocs.org/projects/erp-system/badge/?version=latest)](https://erp-system.readthedocs.io/en/latest/?badge=latest)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)

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

## PROJECT TITLES

### Artaxerxes Series (ERP System Mark I)
In the context of versioning Docker images and maintaining project progress, the author drew inspiration
from the movie "Matrix" and its depiction of the hovercraft Nebuchadnezzar. 
This vessel's inscription, "Mark III No 11 Nebuchadnezzar Made in USA YEAR 2060," inspired the idea 
that ambitious projects require both development and marketing to succeed. The author applied 
this concept to their software project, emphasizing the importance of releasing and using the
product while continuous development occurs. They named this project phase "Artaxerxes" to highlight 
the Achaemenid people's affinity for records, drawing parallels to the biblical story of 
King Artaxerxes and Mordecai, where records played a critical role in saving a faithful servant. 
The goal of the "Artaxerxes" phase is to develop a system that efficiently tracks business 
transactions, generates flexible reports, ensures record security, and allows for easy access. 
Once these objectives are met, the milestone is considered complete.

### Baruch Series (ERP System Mark II)
Baruch, the faithful servant of Jeremiah the prophet, serves as the inspiration for this series. 
Baruch's unwavering commitment to recording and preserving the prophet's messages, even in the 
face of adversity, highlights the importance of data persistence and accuracy. In this series, 
the author aims to establish data stability within the database, ensuring consistency despite code changes. The overarching theme centers on the persistence of data, the preservation of information integrity, and the profound implications of accurate information.

### Caleb Series (ERP System Mark III)
The 'Caleb' series pays homage to a remarkable individual whose story serves as a constant source of motivation to tackle formidable challenges. Drawing inspiration from the biblical account of 'little faith' moving mountains, we encounter the extraordinary character of Caleb. In the context of the Israelites' settlement into Canaan, facing a land inhabited by formidable residents, Caleb's unwavering determination stands out. He does not seek divine intervention to remove his obstacles; instead, he boldly declares, 'Give me this mountain!' This spirit of determination and resilience resonates with the persistence required in overcoming modern challenges.

The 'Caleb' series embodies the essence of Caleb's unwavering resolve. It represents the relentless pursuit of solutions in the face of adversity, much like the challenges encountered in software development—be it debugging errors, addressing null pointer exceptions, navigating frontend-backend disparities, or other complex issues. This series aims to enhance the system further by placing reporting modules at the forefront. Additionally, it ventures into the intricate domains of lease accounting in IFRS 16, contract management, and fixed assets management and reporting.

Through this series, we celebrate the courage to confront challenges head-on, even when success seems elusive. It symbolizes the commitment to persevere, whether feeling inspired or not, and to embrace failure as an opportunity to try again or approach the problem from a different angle. Caleb's enduring determination echoes in our pursuit not to move mountains or wish away obstacles but to make these challenges our own.

The goals of the 'Caleb' series revolve around the implementation of a robust reporting framework, leveraging the Jasper reporting library. This framework empowers database administrators to structure and create reusable reports without necessitating changes to the system's underlying code. Just as Caleb chose to make the mountain his home, we aim to conquer challenges, provide enduring solutions, and, in the process, draw inspiration from Caleb's unwavering spirit.

### David Series (ERP System Mark IV)
The 'David' series embodies the spirit of daring to attempt the seemingly impossible, reminiscent of the young man who faced a giant with unwavering resolve. In this series, the authors take on formidable challenges, such as upgrading the project to the latest Angular framework, reaching Angular 16 from Angular 12—a daunting endeavor that ultimately resulted in valuable lessons.

The 'David' series also reflects the audacity to explore multiple front-end support options, including ReactJS, despite encountering obstacles like containerization and dynamic environment configuration. While these attempts faced difficulties and failures, they served as rich sources of knowledge and a return to the basics.

Despite knowing the daunting nature of the tasks, the authors remained undeterred, demonstrating the courage to embrace challenges head-on. The 'David' series stands as a testament to the pursuit of the extraordinary and the invaluable lessons learned along the way

### Ehud Series (ERP System Mark V)
The 'Ehud' series represents a significant accomplishment in the establishment of a fixed assets depreciation framework. Although not yet fully complete, this framework addresses essential aspects such as accrued values, net book values, and accounting depreciation records. The journey involved rigorous experimentation, including the depreciation of over 10,000 asset records in the shortest possible time.

The 'Ehud' series explores the versatility of depreciation methods, offering options for both straight-line and reducing-balance depreciation, with dynamic front-end configuration. This exploration led to the integration of new tools like Kafka and a workflow management framework, underscoring the adaptability required for complex projects.

The title of this series draws inspiration from Ehud's unique trait of being left-handed, which positioned him for leadership opportunities. It serves as a reminder that in situations where one feels like they don't fit in, there lies potential for leadership and positive community impact. Similarly, the project encountered a lack of open standards, specifications, and design patterns for depreciation workflows, necessitating creative solutions and problem-solving akin to Ehud's resourcefulness.

The 'Ehud' series exemplifies the author's commitment to research and development, with ongoing efforts to enhance feasibility and robustness, enabling the framework to handle even larger volumes of records within acceptable timeframes. The series parallels Ehud's ability to draw a sword with his left arm, allowing him to navigate challenging situations strategically. This determination and focus on precise standards have paved the way for project success and leadership.

### Phoebe Series (ERP System Mark VI)
The 'Phoebe' series is named in honor of an astute deaconess from biblical times, introduced in the book of Acts, 
who exemplifies excellence in service and support. This title reflects the core values of dedication, commitment, community, 
and collaboration—qualities that resonate with the Mark VI series.

In this phase of development, the primary focus is on GDI (Granular Data Integration) work, which plays 
a pivotal role in supporting users and customers effectively. The series entails the creation of multiple 
entities to facilitate data updates, particularly in the context of central banks' requirements for 
granular data integration. 

The 'Phoebe' series is designed to streamline the data staging process across various organizational departments, 
including financial institutions, payment service providers, credit reference bureaus, and forex bureaus. 
Additionally, it strives to implement validation and submission mechanisms through authenticated and encrypted APIs.

As Phoebe's role as a deaconess exemplified unwavering service and support, this series embodies a commitment to 
providing reliable and collaborative solutions for data integration, echoing Phoebe's legacy of excellence.

### Gideon Series (ERP System Mark VII)

The 'Gideon' series marks a significant milestone as it reflects the successful completion of Phase 1 within the work-in-progress module. Named after the lesser-known biblical leader Gideon, this series embodies the spirit of resourcefulness and leadership in the face of challenges.

In this phase, we have achieved a comprehensive understanding of work-in-progress items, laying a solid foundation for further developments. Gideon's story of leading a small and resourceful army to victory resonates with our commitment to overcome obstacles and complexities in the world of work-in-progress management.

The 'Gideon' series sets the stage for enhanced insights into work-in-progress items, drawing inspiration from the biblical leader's courage and tenacity. With Phase 1 now complete, we look forward to future milestones in our ongoing journey.

### Hilkiah Series: Advancement Odyssey Series (ERP System Mark VIII)

"The 'Hilkiah: Advancement Odyssey' series heralds the introduction of the prepayments module within our ERP system. Inspired by Hilkiah's rediscovery of the Book of the Law, this series embodies a journey toward progress and exploration within the realm of financial management.

This module is meticulously structured, featuring comprehensive components designed to handle prepayment-related data with precision and efficiency. Through the incorporation of crucial fields such as catalog numbers, particulars, and prepayment amounts, alongside universally unique identifiers (UUIDs) for precise identification, the module ensures robust data organization.

The relationships established within this module reflect an interconnected web facilitating a holistic approach to prepayment management. The establishment of many-to-one and many-to-many relationships with settlement currencies, transactions, service outlets, vendors, and financial accounts signifies a comprehensive and versatile system.

Implementation details underscore the module's sophistication, leveraging specialized service implementations, DTO mapping using MapStruct, JPA metamodel filtering, and the incorporation of pagination for handling larger datasets efficiently.

This module marks a pivotal step in our ERP system's evolution, embracing Hilkiah's spirit of discovery to navigate the complexities of prepayment management with a well-structured, interconnected, and advanced approach."

This summary highlights the key aspects and significance of the "Hilkiah: Advancement Odyssey" series in implementing the prepayments module within the ERP system.

### Iddo Series: Report Realm Architects (ERP System Mark IX) 2023.12.06

The 'Iddo: Report Realm Architects' series introduces a pioneering autonomous reporting module, drawing inspiration from Iddo, the revered biblical priest and seer known for his meticulous chronicling of historical events.

Much like Iddo's careful recording of history, this module operates behind the scenes, akin to a vigilant seer, creating comprehensive reports asynchronously and unobtrusively, mirroring Iddo's detailed and meticulous fashion of documenting chronicles.

In technical terms, this module functions like a silent daemon, generating detailed reports in the background as users request specific reports. It operates autonomously, seamlessly crafting CSV-formatted reports without pagination, ensuring comprehensive data capture without interrupting the user experience.

Similar to Iddo's dedication to meticulous documentation, this module captures and stores detailed parameters, user information, and report specifics in a dedicated registration entity. It anticipates user needs, preemptively generating thorough reports for export upon user request.

This innovative reporting framework embodies Iddo's commitment to meticulous recording, showcasing proactive foresight and detailed documentation. The module's silent operations and comprehensive data handling echo Iddo's legacy of detailed chronicles, establishing an advanced reporting system designed for seamless functionality and thorough data provision

### Jehoiada Series: Lease Automation Vanguard series (ERP System Mark X) 2023.12.27

The 'Jehoiada: Lease Automation Vanguard' series embarks on a transformative journey to implement an IFRS 16 leases module, drawing inspiration from Jehoiada's influential leadership in ancient Judah's religious reforms.

This series represents a pioneering step towards automated lease management in alignment with IFRS 16 standards. The focus lies on meticulously maintaining lease accounts and streamlining reporting procedures.

The planned implementation encompasses automation for reconciling lease liability accounts and accurately managing right-of-use depreciation. The aim is to establish a robust framework that automates these processes, ensuring compliance with IFRS 16 regulations while optimizing efficiency and accuracy.

There are many aspects of automating management of leases but this series focusses on that which is considered a major pain point when adopting IFRS 16. It's not necessarily the part of reading a contract and classifying the lease, or from the same coming up with a full schedule of all future payments. 

There are systems that can come up with that in a second and we would be re-inventing the wheel. Just to be clear a lot of this work re-invents the wheel, and we do not see it as a vice. However the major challenge remains after all the bells and whistles, and after the developers have packaged the system and left

your organization, the pain points begin to show up, especially in the maintenance of leases liability. Because what you envisioned in the beginning of the lease may not become the reality of implementation. The invoices by the vendors, or providers may be late, or amended to reflect changing economic environment, and environments

are changing. How do we ensure we can reconcile and explain and then report such changes, and also explain how the accounting system does not adhere to our initial models? How can we reliably report in each reporting period and present auditable data? Such questions are the hum drum of this series; maintenance, reporting, and auditable evidence.

The envisioned module will revolutionize lease management by automating the reconciliation of accounts, minimizing manual intervention, and ensuring precision in right-of-use depreciation calculations. It reflects Jehoiada's leadership qualities by initiating transformative changes in lease accounting procedures.

This series signifies a commitment to embracing automation as a vanguard in lease management, ensuring adherence to IFRS 16 standards while optimizing accuracy, efficiency, and compliance in lease account maintenance and reporting.

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

#### Update 2024-03-19 (Several levels deep into Jehoiada Series)

One of the most enduring challenges with the implementation of the depreciation module was the project requirement for maximum flexibility and verifiability. So with the Jehoiada series we were essentially supposed to be working on an IFRS16 leases management module, but we reiterated again to the prodigal depreciation
module to understand why it would not work. We did not even want to solve the problem, but just to for once and for all know why. And that's when it became clear that there were issues with the queue configuration that lead to duplication of our calculations
producing something like an eternal calculation that kept repeating. 
This would not be apparent if I'd not used the API developed back in Iddo series for exporting reports essentially as CSV. We could run depreciation and then write the report in the filesystem in a matter of mere seconds. So from there we could transparently see what the algorithms were doing to the assets as a report, and compare to manual excel calculations.
One of the obvious challenges became evident was duplicated asset depreciation in the same depreciation-period in the same depreciation-job. So now the question was why were duplication messages being received by the processors? And that's when we figured
that we needed to use Kafka's manual acknowledgement API. And that was that, and Kafka is your uncle, there was no more duplication, like at all.


Having done that we realised accuracy to the tune of around 98% percent, making the 2% even more annoying. It was a matter of may be using the wrong period count here, the wrong depreciation rate there in the end the thing worked out, but it took somewhere between 50 minuted to 1 hour and 30 minutes
to run around 10,000 items. What can you do in that time? May be go for coffee, go for a walk, go for lunch...
It was actually on one of these walks that I figured that my design was stupid enough to warrant throwing the whole thing away and starting from scratch. Who can justify a process that takes 1 hour to depreciate 10,000 items. This is just bad engineering. So we acknowledged (begrudgingly) that there were bottle necks 
in our perfectly accurate design. This acknowledgement more than anything is what led us to the next discovery.
We simply made the calculations asynchronous, and then the process was through in like 5 minutes. Yes, you read that right, and then it took 1 hour 30 minutes to save the calculations in the database.

Now hold my drink, it takes 1 hour, 30 minutes to save stuff in the database!!! Those expensive calculations, with all the data we are pulling from the same database, can take just 5 minutes!
If only we could find a way to batch persistence processes like we were batching calculations, right? I mean we were batching assets and throwing them into a kafka queue and finishing the entire process in (IN JUST) 5 minutes, and then waste all that efficiency because we were saving items on a one by one basis.
And that's where the idea of creating a persistence buffer came about. We calculate depreciation entries and push them into our buffer. From there what happens to the entries is not a function of the depreciation algorithm. There we configure buffer sizes and we apply data to the sink once the buffer is full.
The buffer has a predefined size which is the maximum amount of entries it can hold after that size is attained, the buffer saves the items into the database in bulk. We literally leave the buffer to its own devices, and there is bunch of stuff am leaving out. This goes on until the depreciation process is complete.
This alone changed the depreciation process time from the previous between 50 minutes and 1 hour and 30 minutes, all the way down to average of between 6 minutes and 10 minutes for 10,000 assets. 
Who knew persistence operations were so expensive?


Now the remaining challenge (and we ask accountants to leave the room for like 20 minutes) is how do we calculate depreciation for disposed assets? Of course the simple accountant-like answer is we don't. But this is from a design perspective and engineering because disposal is just a state, somewhere in the life of an asset. 
At some point it was not disposed. So accountants would obviously mistakenly think of disposal as a state; but you know, and we know, and everybody knows that disposal is an event. In fact in the real world, disposal is an event.
So the idea is if we have an entity tracking disposal, we could have the relevant disposal dates and proceeds and dispoal values subtracted from our calculation at just the right moment. 
This means that when we do back-in-time calculations we get real values of depreciation for an asset; the same which in the present or the future will get disposed. By synchronizing disposal data with our calculations, we achieve true representation—a glimpse into an asset's past and future.

So instead of simply flagging our asset as disposed, what we ought to do is simply match disposal with the relevant fiscal month, (in fact depreciation-period representation is better), okay depreciation period, and then subtract the disposed amount from the calculation. If the asset is disposed, you should a zero value calculation.
We would implement a similar entity to recognize decommissioned items, which rather than say they were disposed, it's that they are simply written off. In fact we will simply refer to them as written off items. For instance, let's say that you had capitalised some lease hold improvements for a period of like 10 years, but before the realization 
of that period, you lost the lease by unexpected termination, or that something happened to the premises. You cannot possibly continue holding such lease hold improvement as an asset of any value in your books, but you cannot also support the idea that you disposed it. So, it's just that you wrote the thing off. 

So why don't we just delete the asset from the register? For the same reason as before, we will no longer be able to report accurate point-in-time past depreciation or NBV for that matter. And here at ERP-Systems we believe that all depreciation calculations should be point in time calculations. Simply deleting an item
from the register is just bad (read unethical) practice, and can be attributed to one of those create-a-fixed-assets-management-system-in-10-minutes tutorials, that can get you fired from your organization, or cost you your professional certification. So we do it the hard way: recognize disposals separately in a full-blown entity, and then recognize written-off assets
in a separate full-blown entity. Then when running reports match the two with the registration items. Do the same when running depreciation so you don't end up depreciating items you had zerorised. This is a symptom of the point-in-time calculation meaning that we are getting rid of the need-to-know previous net book value, and using the cost to calculate either straight line
depreciation, or declining balance depreciation.


Another milestone in this series (Jehoiada series) was implementation of application-user context. This technique is a derivation of something we did to count processed items across multiple end-points of the depreciation queue.
The idea is that the request object carries the context information, and that's what JHipster is actually doing in their JWT implementation. So harnessing that we are able to retrieve the current application-user which is something that we now use to monitor and actively record user activity. In sensitive entities
like business-documents where we needed to track who is creating, and accessing and modifying documents, we desperately needed an automatic way to tracking who is doing what with our precious documents. So that how the application-user context was created.
It retrieves the user information from the JWT signature of the request, and then retrieves the appropriate user and records the information. And therefore doing the same with other sensitive entities we have been able to improve the coverage of our monitors by that much.

#### Update 2024-12-18 1643hours (Several levels deep into Jehoiada Series)

Took a lot of time on this and even attempted to come up with accounting systems in [other](https://github.com/ghacupha/book-keeper) projects. This time though
we thought not to do anything but think about it first. The notion was clear that the transaction-account was supposed itself
to be merely but a label. Anything more than that would be a disaster.
Having agreed (with myself, I generally hate simplicity) to make the account merely a label, I went even
further and simplified the concept of double entry by doing it like this: 
We create a table with 3 important columns
 - debit account
 - credit account
 - amount

It's as simple as that. We have had many experiments and all of them failed with amazing consistency. It's either we could not post
transactions as the transaction query became too hard, or the transaction query became easy and then we just couldn't
run a report.
In this case we decide it was necessary to have some sort of comprehension around what is happening as a result of IFRS16 multiple modules
and transactions because otherwise we reconcile nothing. This means pooling information from the lease-liability-schedule
as well as the depreciation-schedule (for ROU), and the initial recognition routines. We define several tables with rules for posting. For instance
there would be a table about initial recognition rules, that dictate that the transaction for initial recognition debits this rou account and credits
that lease liability account. Another table might dictate that an rou amortization transaction debits this instance of a transaction-account (for instance
say rou depreciation account), and credits that rou account. And so on and so forth. We have tables for lease liability, interest expense rules,
posting of lease payment to principal and even lease payment to interest and bunch of others. The modules are so many and diverse, so that for as little
as 50 lease contracts and depending on the length of the period, a single compilation of that 50-contract register could render a 1000+ transactions.
This right here by the way is the thing about leases in IFRS16. Accounting complexity. The posting of transaction consist of entries of volumes which are just crazy.
If you have more than a hundred contracts don't even think of attempting to use Excel unless you have hired like several accountants and all of them with god-tier
excel skills. If you only have a few of those lying around you have no choice but to automate. Then valuation is another beast I have
not even attempted to think about and that too could have book-keeping consequences. Just imagine two-years in, an auditor discovers
a flaw in your present-value computation. Which accounting entries would you apply to make the correction?

So the true test of the ability of this model was of course the ability to generate a working trial balance. Which should now be easy because every transaction
faces database rules enforcing that every amount posted needs to have a debit and credit amount, right? Wrong.
If you dump the transaction-details table and take a summary of all credits subtracting all debits and group by account the total will always be zero. Because items in
credit balance have positive amounts while accounts in debit balance will have negative amounts.
That's how the double-entry rule is enforced. It's not in the logic or the application layer or client, but in the database itself. Didn't even bother to label with the infamous
dr and cr tags.

In fact this is the foundation for the reporting on accounts (here at erp-systems): we select the transaction account (number and name), then for the amount (and we are thinking about the account balance here)
we take a sum of all transaction-account credits, and a subtraction of all transaction-account debits for each account.
This then tells you that for accounts in which we post credits, i.e. liability, equity, gain in OCI, you get a positive amount; and for accounts
where we normally post debits i.e. assets, expenses, loss in OCI we get negative amounts. If you take a sum of all amounts, you will get a total of zero.
The challenge then is with equity account, i.e. expenses and income, in which in accounting practice we
are to reflect only the transactions that have taken place in the present accounting period excluding previous periods that
have been or should have been included with the retained earnings account. Here we separate ourselves from any garb of assuming simplicity.
Because we require a  reliable and global retained-earnings-account configuration into which we add all the credits from previous periods
and subtract all the debits from the previous periods. This is happening within the time in which the query runs, which thankfully is stupid fast, because nothing
is taking place within the application server itself but within the 100+ loc sql query. I suppose the resulting query could have been simpler
if we delegated some bits in the application server and may be give the system just a little bit of control. I know this will have the unintended 
consequences such as expensive time-taking queries, but it would unshackle the system from the punitive maintenance cost of the query we have had to create
to produce this report. This would then transfer consequences to the Java API, and that's not an easy road to travel is someone else other
than you (me in this case) will ever need to maintain the code. So what we essentially have are stories about how the pattern
works but the implementation is not something I can show you from the code; only the grand theme of the transaction-details design pattern. Perhaps this is 
why not no one actually discusses their pattern since the pattern is smeared all over the code and implementation and obviously there's got be
tight controls over implementation of the front-end

Next round of complexity will include: 
  - introducing the concept of accounting-period, 
  - functional-currency, conversion and presentation, 
  - tracking of FX gains and losses automatically
  - dummy accounts

The last one is an important part as it will enable someone to post unbalanced transactions. We are not
talking about the amounts per se, we are taking about transactions that could involve a posting of a 
single debit against 2+ credit accounts or the vice versa, or even post transactions where the debit is a different currency
from the credit or vice versa. The idea is that the unbalanced transaction presents as a balanced transaction in the transaction-details
table because for each entry the system creates an opposite transaction-entry in a dummy account.

This done we can dare say we have reinvented the wheel on the accounting module for purposes of an erp-system, with which we can track
financial consequences of fixed assets, and assets depreciation, capitalization, prepayments and leases. Am
saying "re-invented" because am certain many proprietary software companies have already accomplished this but no one dares
even try a white paper to explain how to handle accounting transactions. Am not talking about pseudocode here, but the design patterns
for simple double-entry accounting simply don't exist even in the open source space. The ones that do, like GNUCash have never
thought to do a write-up of the accounting design pattern in the context of persistent data. If they do, am certain it would be such an
enthralling read; because the efficiency of their model is simply out of this world. I would certainly do away with this "transaction-details" model
(ok that's the name of the design pattern for now), and use the GNUCash accounting design pattern itself. Try looking, and I did, and you will find only
academic illustrations of the accounting process such as the one I have done [myself](https://github.com/ghacupha/book-keeper) with very 
little regard for concerns like persistence.
Why someone would create an accounting tool without instructions on how to save data beyond some school illustration is shows that we need as much contributors writing the documentation
as much as those who are coding the code, and that's not easy to orchestrate; it's a real struggle within the open source community, and it's unfortunate 
because book-keeping is about keeping records. 

## Code Structure and Usage Notes

We have created a general guideline for code structure and usage on this [link](https://chatgpt.com/s/cd_6889e186ef688191a2f5e28e6d11614a)
