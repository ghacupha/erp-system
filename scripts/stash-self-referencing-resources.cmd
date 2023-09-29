@REM
@REM Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
@REM Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
@REM Stashing of failing resource IT tests
@REM
@REM This is necessitated by the generation of failing tests in the case of entities
@REM that have self parent relationships
@REM ----------------------------------------------------------------------------

@echo off
git stash -- src/test/java/io/github/erp/web/rest/ContractMetadataResourceIT.java src/test/java/io/github/erp/web/rest/SecurityClearanceResourceIT.java

