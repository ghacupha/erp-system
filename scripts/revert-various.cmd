@REM
@REM Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
@REM Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

@echo off
git checkout HEAD~1 src/test/java/io/github/erp/web/rest/ContractMetadataResourceIT.java src/test/java/io/github/erp/web/rest/SecurityClearanceResourceIT.java src/test/java/io/github/erp/web/rest/PrepaymentCompilationRequestResourceIT.java
git checkout HEAD~1 src/main/resources/config/liquibase/changelog/
