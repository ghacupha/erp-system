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

@echo off

@setlocal

set NPMW_DIR=%~dp0

if exist "%NPMW_DIR%\mvnw.cmd" (
  set NPM_EXE=%NPMW_DIR%\target\node\npm.cmd
  set INSTALL_NPM_COMMAND=%NPMW_DIR%\mvnw.cmd -Pwebapp frontend:install-node-and-npm@install-node-and-npm
) else (
  set NPM_EXE=%NPMW_DIR%\.gradle\npm\npm.cmd
  set INSTALL_NPM_COMMAND=%NPMW_DIR%\gradlew.bat npmSetup
)

if not exist %NPM_EXE% (
  call %INSTALL_NPM_COMMAND%
)

if not exist %NPM_EXE% goto globalNpm

%NPM_EXE% %*

:globalNpm
npm %*
