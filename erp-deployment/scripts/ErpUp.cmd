@echo off
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0ErpUp.ps1" %*
