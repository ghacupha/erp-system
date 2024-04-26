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
@REM Stashing of failing resource IT tests
@REM
@REM This is necessitated by the generation of failing tests in the case of entities
@REM that have self parent relationships
@REM ----------------------------------------------------------------------------

@echo off
git stash -- src/test/java/io/github/erp/web/rest/ContractMetadataResourceIT.java src/main/java/io/github/erp/erp/resources/PrepaymentCompilationRequestResourceProd.java src/test/java/io/github/erp/web/rest/SecurityClearanceResourceIT.java

