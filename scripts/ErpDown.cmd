@echo off
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0ErpDown.ps1" %*
