@REM
@REM Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
@REM Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
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
@REM These files are reverted after generator run to maintain consistency with the
@REM project design
@REM ----------------------------------------------------------------------------

@echo off
@REM git checkout HEAD~1 src/main/webapp/app/entities/
@REM git checkout HEAD~1 src/main/webapp/app/entities/entity-routing.module.ts
