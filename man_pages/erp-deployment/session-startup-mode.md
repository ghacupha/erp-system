# Session Startup Mode

## Purpose

The production compose file reads cache and search-index startup flags from the shell environment. Operators previously set these values manually before running `docker-compose up -d`, which made index refresh startup easy to mistype.

## Scripts

From `erp-deployment`, enable cache and index rebuild for the current PowerShell terminal:

```powershell
.\scripts\enable-index-refresh-startup.ps1
docker-compose up -d
```

Restore normal startup flags for the same terminal:

```powershell
.\scripts\normal-startup.ps1
docker-compose up -d
```

Review the current terminal values:

```powershell
.\scripts\show-startup-mode.ps1
```

These scripts only set process environment variables in the current terminal. They do not edit `.env.dataupdate`, `.env`, or the compose file.
