@echo off
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0ErpDevBuildUp.ps1" %*
