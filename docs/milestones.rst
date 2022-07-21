Milestones
==========

Achievement of the various intended goals is tracked by names of old time characters
that I find interesting and whose life stories could be inspiring to someone. The lower
level designation serves to show atomic changes in the code while maintaining
compatibility between the server and client, which will inevitably have containers of
different versions, and exist in different repos.

This plan allows flexibility in fixing client issues without having to change server
versions, and also flexibility to work on either at chosen blocks of time and changing
the other only when the said compatibility is impaired.

Why Artaxerxes Series?
********************

Well the idea of breaking down versions into series sounds corny, but
it’s sort of a workaround for versioning the docker images in a flexible manner.
 I figured, I do not have a lot of resources to keep up with a global semantic versioning
especially because both the license headers which are important to me,
and the container versions read the code version from each project's "project object
model" (pom) file. This means I cannot just start changing the versions
because each change would likely produce an additional partition in the
docker repository and there can only be so much free space, and soon
would need to start paying that service. Since this is not a project I
get paid for, “free = good” and so with every release we change the tag
of whatever the app I am working on at the time; either the server of the
client. The container versions cannot therefore practically remain in tandem.
So versioning is not important here, but I still needed a way to mark
progress and milestone achievements and then I got my inspiration from
Matrix (the 1999 movie). I’ll give you some time to roll your eyes…

Are you done? Good welcome back. So you recall that scene as Neo is
introduced, by Morpheus to the crew of the ship Nebuchadnezzar, and
there’s this inscription on the hull of the ship “Mark III No 11
Nebuchadnezzar Made in USA YEAR 2060”. I figured of course, if someone
was going to run a project as ambitious as a hovercraft that size, a
vehicle that completely denies the existence of gravity, carry the
weight of that engine, the weaponry, the ammunition, food, creature
comforts for the crew, communication equipment and still fly at that
speed, you are going to need to be able to make money from the project
in order to fuel the production of the product. That is you will need to
be able to see the product hit the market, at the earliest opportunity,
while still creating the product and the goal is not perfection, and
there’s probably no end goal except to make it better. What there is
though, is this short list of MVP specifications which once achieved you
start marketing the product while the production and even research is
still on going. So you don’t wait to finish the product, you use it as
soon as you can while continuing the development. It’s a
research-in-motion kind of thing. Of course the trilogy likely uses that
production code to hint at (I believe) the idea of Neo being “the One”,
because if you look at that particular verse it says (Mark 3:11) “you
are the Son of God”, and Neo in these movies, is a savior whose gifts
enable him to achieve the overarching goal of saving humanity. As for
me, I am (AFAIK) to my knowledge the most frequent user of ERP and because of resource
and time constraints have developed a non-finished product which I use
in office as I develop better techniques, carry out more research and
produce both client and server images with better enhanced features in
record keeping and reporting. So why did I use the title Artaxerxes?

Well the Achaemenid people were known to be lovers of records, it is a
generally accepted historical opinion that distinguishes them from their
predecessors, the Assyrians. For instance in the book of Esther(the Bible again) you
see the Queen has been fasting and interceding for the salvation of the
people of Israel. Unbeknownst to them Haman the main antagonist in the
book is plotting the death of Mordecai. Am imagining a conversation in
heaven as the powers that be discussed how they were going to save
Mordecai and disrupt Haman’s plans, and an idea comes up: let’s give the
King a sleepless night, and we know how much he loves his records.
Right? He will likely start going through the records and in them will
find the story of Mordecai, how this servant had uncovered another plot
to kill the king and had saved the king’s life from those who were
plotting evil against him and in the end... no reward was given to the
faithful servant. The king might then be prompted to do something in the
servant’s favor, knowing that he owed him one, making it impossible, and
if not, politically inexpedient for Haman to hang to death the same man
for whatever reason.Of course, you know how that unfolds (if not, it’s a
nice story, read it), because the king found himself sleepless at night,
and sure enough he calls his servants to have them read through records.
If you don’t find that attachment to records odd, ask yourself this:
when was the last time that you lacked sleep in the night and so you
decided to go get some business records for entertainment (or whatever)?
Not many will answer in the affirmative, and I will tell you why. Not
many will you find keeping logs of their transactions, receipts,
contracts and so on and so forth. When one of your appliances break
down, you find you don’t even know where you left the warranty documents.
If you do keep such records, how many sleepless nights did you find
yourself perusing them for whatever reason as opposed to tv, Facebook,
Twitter,Whatsapp; most will not even leave the bed. So if you were king instead,
Mordecai would hang despite all the good he’s done for you. So that’s
what it about, a desire to keep track of my personal day-to-day
transactions at the office and have a way of recalling that information
quickly when needed. So you will find all kinds of entities, for all my
various concerns in my own work. This milestone describes a system with
the ability to do that, and the flexibility to fashion reports as
desired and have them produced quickly and as needed. We are able to
define reports and run them without recompiling the code, ensure
security of records and access them at any time, thanks to the search
functionality (by the way big ups to Elasticsearch on that one!) Once
that basic need is fulfilled, the milestone is complete.

Oh, and that king was called Artaxerxes.

Milestones achieved in the Artaxerxes Series
---------------------------------------------
- Institute reliable deployment for both server and client using docker containers
- Implement custom user roles apart from jhipster with role-based navigation views and
  access to both front-end client components and API
- Create payment recording entities which serve as the base for all future models
- Setup base end-points for dynamically creating reports with jasper-reports
- Setup base end-points for recording prepayment transactions
- Setup base end-points for management and recording of fixed assets


Baruch Series
**************


Milestones for the Baruch Series
----------------------------------
