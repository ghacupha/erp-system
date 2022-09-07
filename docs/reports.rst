Design of the reports platform
==============================

The report system is based on jasper reports. This is a challenging implementation but one that has been
deemed worth it, because it allows for a user to define a report template at runtime
and run the report successfully.

In previous systems whenever I had an idea for a report, I would have to put down the server
and start coding away at a JPA specification or some repository. Instead the idea behind this
implementation was that we could design the report template itself containing a plain old
postgresql query and defining the report we upload and somehow run it, without ever powering down
the server. Many will no doubt balk at use of native postgresql query, because this is a Java
application, right? And and Java devs are known for the love of abstraction, right? And and Java
devs do not get the time to study ANSI SQL let alone postgresql, so what gives? In my mind there's
a bunch of Java devs with pitch forks, fire brands and sharp objects with banners that read, "death
to ANSI SQL" and "JPA specification is everything" and "We do not negotiate with breakers of abstraction",
and the startled on-lookers join in anger when they learn that the proud dev did not even tell people that he was
actually using postgre-what-now?, and even more pissed because they did not want to find out,
and there just this relentless match towards the hill where lies my otherworldly polymath tower...
Anyway when you are through these notes, you will see how that breaking of abstraction is kind of the least of
my sins, but I have a good heart, I have not judged others, and so based on that hope to be judged less harshly.

JPA spec is amazing and I have used it in code that could be unit-tested (yes, I did unit tests on report generation code
and look where that got me) to compile mission-critical queries and feeding that into a simple excel file execution with quick satisfactory results.
Well I'll tell you this, that the JPA implementation code while really awesome and powerful does not
really provide full runtime capabilities that could possibly support the fickle demands of an ERP
user. You could define a perfect report on performance report summarizing by, country and currency only
for some marketer to ask, "what if we wanted to see the performance by our relationship managers?". The next
exec to lift their hands is not going to shush the indignant philistine marketer, but himself to ask
if he could see the numbers for his division, and what about products and stuff. Anyway you get the point, I hope. The system is still
in development and forever will be, and future developers while adding away entities at the fantastic speed
of a jhipster entity command might appreciate not having to spend additional hours hacking away at JPA code
and excel file POI stuff.

Future development might take a different direction when a better one comes along.

Report templates
*****************

The implementation of the reporting platform while thoroughly basic from an outsider's perspective has been truly
tough. At the end I settled "uneasily" for the following workflows.
There's an entity that contains the metadata for a freshly created report template and the JRXML file itself.
An instance of this entity is created by a user after they have created a JRXML file from their favorite studio. The
template should contain both the query and the report design. This means that this user AFAIK is not going to be some
run-of-the-mill accountant. They will, to my unfortunate regret, need to know the tables on the actual postgresql database and
they will to have knowledge of ANSI SQL and maybe even postgresql itself.
Once the report-template instance is created it is saved into the database in a byte-stream.

Report Request
**************

The report request is created by a user who will need to know that a particular template exists and will need to have
thorough knowledge of the template's parameters.
The request defines metadata as well including the report password and a UUID identifier for the report generated itself.
When this request is registered at the controller, it is intercepted using AOP observers and the report generation sequence
begins.

Report generation
******************

Here is where things get really uncomfortable because even after a thorough search we found that jasper reports really do
depend on having a file-system access on which the report-compilation process is ran. There are not alternate approaches
as far as version 6.19 is concerned.
So we fetch the appropriate report-template from the database in it's byte-stream form and reconstitute it from scratch
on the configured reports directory. We even give it a new UUID identifier.
Because of that, the report-template suffers the probability of corruption, so even though I have not had the opportunity
to do so, we will to eventually have a subsystem to checksum the hell out of that file.
I chickened out of that because I expected the process will be eating away from precious milliseconds. By the way the targeted
maximum is supposed to be 2000 milliseconds and that's not a lot of time, and this is just phase 1.

Phase 2 of the process confirms that the file exists and start compiling the report with a different service for each of four
report types, CSV, HTML, PDF and HTML. And the report is ready for presentation. This is still part of the 2000 millisecond
window but phase 3 is coming.

Report presentation
*******************

For anyone familiar with the jhipster entity design at the front end know the button that you click to view details
of an entity. For the reporting framework, when the user clicks that button that's when the magic happens.
This is because the report does not exist in the database, so even that field that is supposed to contain the file
byte-stream is going to be blank at the response.
But AOP comes to the resque again, and we intercept that request and attach the compiled report as byte-stream to
the "report-file" field.
This means that we are maintaining a reference to the file on the file-system, and that's easy because we are using
UUID. There is however again the possibility of corruption of the file because it is going to be "dissolved" into byte-stream
and then reconstituted at the front-end.
That is why regardless of "precious milliseconds" we compute the checksum of the file while in its physical form on the
file-system and share the checksum together with the file byte-stream when sending the file to the front-end on the
request entity's data transfer object.
When viewed on the browser one should see a checksum that will match the file checksum once it's been reconstituted and
downloaded by the user. Will this be enough to stall man-in-the-middle or compromised-man-in-the-server attacks? I don't know
but research is in progress.

And that's how we have implemented the reporting platform.
