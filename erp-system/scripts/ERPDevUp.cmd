@REM
@REM Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
@REM Usage:
@REM   ERPDevUp.cmd                         - docker + client + Maven
@REM   ERPDevUp.cmd --skip-docker           - skip docker, start client + Maven
@REM   ERPDevUp.cmd --docker-only           - docker only, no client or Maven
@REM   ERPDevUp.cmd --skip-client           - no Angular client window
@REM   ERPDevUp.cmd --client-heap-mb 2048   - set client Node.js heap (default 1024)
@REM

@echo off
setlocal

set PS_ARGS=

:parse
if /i "%~1"=="--skip-docker"    ( set PS_ARGS=%PS_ARGS% -SkipDocker   & shift & goto parse )
if /i "%~1"=="--docker-only"    ( set PS_ARGS=%PS_ARGS% -DockerOnly   & shift & goto parse )
if /i "%~1"=="--skip-client"    ( set PS_ARGS=%PS_ARGS% -SkipClient   & shift & goto parse )
if /i "%~1"=="--client-heap-mb" ( set PS_ARGS=%PS_ARGS% -ClientHeapMB %~2 & shift & shift & goto parse )

powershell -ExecutionPolicy Bypass -File "%~dp0ERPDevUp.ps1" %PS_ARGS%
