@REM
@REM Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
@REM Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
@REM
@REM This program is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program. If not, see <http://www.gnu.org/licenses/>.
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
