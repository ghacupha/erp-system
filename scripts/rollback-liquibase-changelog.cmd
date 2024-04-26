@REM
@REM Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

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
git checkout HEAD~1 src/main/resources/config/liquibase/changelog/
@REM TODO mvn clean compile liquibase:diff
