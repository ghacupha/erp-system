param(
    # Light = pg_dump + configs only (frequent)
    # Full  = Light + data volumes with brief downtime (occasional)
    [ValidateSet('Light', 'Full')]
    [string] $Mode = 'Light',

    # Prod uses docker-compose.yml; Dev uses services-dev.yml
    [ValidateSet('Prod', 'Dev')]
    [string] $Stack = 'Prod',

    # Root directory for all backup archives
    [string] $BackupRoot = 'D:\backups\erp',

    # PostgreSQL connection (local host — not the Docker container)
    [string] $PgHost = 'localhost',
    [int]    $PgPort = 5432
)

#
# Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
# Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$deployRoot = Split-Path -Parent $scriptRoot
$repoRoot   = Split-Path -Parent $deployRoot
$deployEnv  = Join-Path $deployRoot '.env'
$systemEnv  = Join-Path $repoRoot   'erp-system\.env'

# -- Load an .env file into the current process environment ------------------
function Import-EnvFile([string]$Path) {
    if (-not (Test-Path $Path)) { return }
    Get-Content $Path | ForEach-Object {
        $line = $_.Trim()
        if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith('#')) { return }
        $idx = $line.IndexOf('=')
        if ($idx -le 0) { return }
        $key   = $line.Substring(0, $idx).Trim()
        $value = $line.Substring($idx + 1).Trim().Trim('"')
        [System.Environment]::SetEnvironmentVariable($key, $value, 'Process')
    }
}

Write-Host "Loading environment..."
Import-EnvFile $deployEnv
Import-EnvFile $systemEnv

# -- Stack-specific settings -------------------------------------------------
if ($Stack -eq 'Prod') {
    $composeFile = Join-Path $deployRoot 'docker-compose.yml'
    $pgDb        = if ($env:ERP_SYSTEM_PROD_DB)          { $env:ERP_SYSTEM_PROD_DB }          else { 'erpSystem' }
    $pgUser      = if ($env:PG_DATABASE_PROD_USER)        { $env:PG_DATABASE_PROD_USER }        else { 'postgres' }
    $pgPass      = $env:PG_DATABASE_PROD_PASSWORD
    $esDataDir   = Join-Path $deployRoot 'search-index-directory'
    $kafkaDir    = Join-Path $deployRoot 'kafka-queue'
    $zkDataDir   = Join-Path $deployRoot 'zookeeper-data'
    $zkLogDir    = Join-Path $deployRoot 'zookeeper-logs'
} else {
    $composeFile = Join-Path $deployRoot 'services-dev.yml'
    $pgDb        = if ($env:ERP_SYSTEM_DEV_DB)            { $env:ERP_SYSTEM_DEV_DB }            else { 'erpSystemDev' }
    $pgUser      = if ($env:PG_DATABASE_DEV_USER)         { $env:PG_DATABASE_DEV_USER }         else { 'postgres' }
    $pgPass      = $env:PG_DATABASE_DEV_PASSWORD
    $esDataDir   = Join-Path $deployRoot 'search-index-dev-directory'
    $kafkaDir    = Join-Path $deployRoot 'kafka-dev-queue'
    $zkDataDir   = Join-Path $deployRoot 'zookeeper-dev-data'
    $zkLogDir    = Join-Path $deployRoot 'zookeeper-dev-logs'
}

# -- Validate prerequisites --------------------------------------------------
if (-not (Get-Command pg_dump -ErrorAction SilentlyContinue)) {
    Write-Error "pg_dump not found on PATH. Install PostgreSQL client tools and try again."
    exit 1
}
if ($Mode -eq 'Full' -and -not (Test-Path $composeFile)) {
    Write-Error "Compose file not found: $composeFile"
    exit 1
}

# -- Prepare staging and archive directories ---------------------------------
$timestamp  = Get-Date -Format 'yyyyMMdd_HHmmss'
$modeTag    = $Mode.ToLower()
$stackTag   = $Stack.ToLower()
$stagingDir = Join-Path $env:TEMP "erp-backup-$stackTag-$modeTag-$timestamp"
$archiveDir = Join-Path $BackupRoot $stackTag

New-Item -ItemType Directory -Force -Path $stagingDir | Out-Null
New-Item -ItemType Directory -Force -Path $archiveDir  | Out-Null

Write-Host "`n[ERPBackup] Stack: $Stack  |  Mode: $Mode  |  Timestamp: $timestamp"
Write-Host "[ERPBackup] Staging: $stagingDir"
Write-Host "[ERPBackup] Archive: $archiveDir"

# -- Step 1: pg_dump (runs while services are still up) ----------------------
Write-Host "`n[ERPBackup] Dumping PostgreSQL database '$pgDb' ($PgHost`:$PgPort)..."
$dbDir    = Join-Path $stagingDir 'db'
$dumpFile = Join-Path $dbDir "${pgDb}_${timestamp}.dump"
New-Item -ItemType Directory -Force -Path $dbDir | Out-Null

$env:PGPASSWORD = $pgPass
try {
    & pg_dump -h $PgHost -p $PgPort -U $pgUser -F c -f $dumpFile $pgDb
    if ($LASTEXITCODE -ne 0) { throw "pg_dump exited with code $LASTEXITCODE" }
    Write-Host "[ERPBackup] Database dump: $dumpFile"
} finally {
    $env:PGPASSWORD = $null
}

# -- Step 2: Config and .env files (always) ----------------------------------
Write-Host "`n[ERPBackup] Copying config files..."
$configDir = Join-Path $stagingDir 'config'
New-Item -ItemType Directory -Force -Path $configDir | Out-Null

foreach ($src in @(
        (Join-Path $deployRoot 'central-server-config'),
        (Join-Path $deployRoot 'central-server-dev-config')
    )) {
    if (Test-Path $src) {
        $dest = Join-Path $configDir (Split-Path -Leaf $src)
        Copy-Item -Path $src -Destination $dest -Recurse -Force
    }
}

if (Test-Path $deployEnv)  { Copy-Item $deployEnv  (Join-Path $configDir 'deployment.env') -Force }
if (Test-Path $systemEnv)  { Copy-Item $systemEnv  (Join-Path $configDir 'system.env')     -Force }

# -- Step 3: Full-mode data volumes (requires brief downtime) ----------------
if ($Mode -eq 'Full') {
    Write-Host "`n[ERPBackup] Stopping $Stack stack for consistent snapshot..."
    & docker-compose -f $composeFile down
    if ($LASTEXITCODE -ne 0) { throw "docker-compose down failed (exit $LASTEXITCODE)" }
    Write-Host "[ERPBackup] Services stopped."

    try {
        $volumeDir = Join-Path $stagingDir 'volumes'
        New-Item -ItemType Directory -Force -Path $volumeDir | Out-Null

        foreach ($entry in @(
                @{ Path = $esDataDir; Label = 'elasticsearch' },
                @{ Path = $kafkaDir;  Label = 'kafka'         },
                @{ Path = $zkDataDir; Label = 'zookeeper-data'},
                @{ Path = $zkLogDir;  Label = 'zookeeper-logs'}
            )) {
            if (Test-Path $entry.Path) {
                Write-Host "[ERPBackup] Copying $($entry.Label)..."
                Copy-Item -Path $entry.Path -Destination (Join-Path $volumeDir $entry.Label) -Recurse -Force
            } else {
                Write-Warning "$($entry.Label) directory not found: $($entry.Path) — skipping"
            }
        }
    } finally {
        Write-Host "`n[ERPBackup] Restarting $Stack stack..."
        & docker-compose -f $composeFile up -d
        if ($LASTEXITCODE -ne 0) {
            Write-Warning "docker-compose up returned exit code $LASTEXITCODE — check service status manually."
        } else {
            Write-Host "[ERPBackup] Services restarted."
        }
    }
}

# -- Step 4: Compress staging area to a timestamped zip ---------------------
$zipName = "erp-backup-$stackTag-$modeTag-$timestamp.zip"
$zipPath = Join-Path $archiveDir $zipName
Write-Host "`n[ERPBackup] Compressing to: $zipPath"
Compress-Archive -Path "$stagingDir\*" -DestinationPath $zipPath -CompressionLevel Optimal
Remove-Item -Recurse -Force $stagingDir
Write-Host "[ERPBackup] Archive created: $zipPath"

# -- Step 5: Prune old backups -----------------------------------------------
$retainCount = if ($Mode -eq 'Full') { 3 } else { 7 }
$existing = Get-ChildItem -Path $archiveDir -Filter "erp-backup-$stackTag-$modeTag-*.zip" |
            Sort-Object LastWriteTime -Descending
if ($existing.Count -gt $retainCount) {
    $existing | Select-Object -Skip $retainCount | ForEach-Object {
        Write-Host "[ERPBackup] Pruning: $($_.Name)"
        Remove-Item $_.FullName -Force
    }
}

Write-Host "`n[ERPBackup] Complete. Archive: $zipPath ($([math]::Round((Get-Item $zipPath).Length / 1MB, 1)) MB)"
