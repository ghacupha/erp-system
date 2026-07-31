@echo off
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0ErpDevUp.ps1" %*
