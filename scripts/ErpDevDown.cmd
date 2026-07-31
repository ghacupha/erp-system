@echo off
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0ErpDevDown.ps1" %*
