@REM ----------------------------------------------------------------------------
@REM Roll back of liquibase changelog script
@REM
@REM Required git
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM This is necessitated by the default system implementation of incremental
@REM liquibase changes as opposed to write-off of current schema.
@REM The script is meant to be invoked everytime there are massive code changes
@REM to make sure that changelogs are then manually created by the dev
@REM ----------------------------------------------------------------------------

@echo off
git checkout HEAD~1 ../src/main/resources/config/liquibase/changelog/
mvn clean compile liquibase:diff
