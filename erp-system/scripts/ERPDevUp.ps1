param(
    [switch] $SkipDocker,    # start Maven without (re)starting docker services
    [switch] $DockerOnly     # start docker services and exit without starting Maven
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

$scriptRoot  = Split-Path -Parent $MyInvocation.MyCommand.Path
$systemRoot  = Split-Path -Parent $scriptRoot
$repoRoot    = Split-Path -Parent $systemRoot
$envFile     = Join-Path $systemRoot '.env'
$composeFile = Join-Path $repoRoot 'erp-deployment\services-dev.yml'

# -- Validate prerequisites --------------------------------------------------
if (-not (Test-Path $envFile)) {
    Write-Error ".env file not found: $envFile"
    exit 1
}
if (-not (Test-Path $composeFile)) {
    Write-Error "Docker Compose file not found: $composeFile"
    exit 1
}

# -- Load .env into current process environment ------------------------------
Write-Host "Loading environment from: $envFile"
$loadedVars = [System.Collections.Generic.List[string]]::new()

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith('#')) { return }
    $eqIdx = $line.IndexOf('=')
    if ($eqIdx -le 0) { return }
    $key   = $line.Substring(0, $eqIdx).Trim()
    $value = $line.Substring($eqIdx + 1).Trim().Trim('"')
    [System.Environment]::SetEnvironmentVariable($key, $value, 'Process')
    $loadedVars.Add($key)
}

Write-Host "Loaded $($loadedVars.Count) environment variable(s)."

# -- Inject Elasticsearch credentials into Spring's connection URIs ----------
# When xpack.security is enabled in the ES container (via ELASTICSEARCH_PASSWORD),
# the Spring app must authenticate. We rewrite the URI vars to embed credentials
# so that neither the .env file nor application.yml need hard-coded passwords.
$esPassword = $env:ELASTICSEARCH_PASSWORD
if (-not [string]::IsNullOrWhiteSpace($esPassword)) {
    foreach ($uriVar in @('SPRING_DATA_JEST_URI', 'SPRING_ELASTICSEARCH_REST_URIS')) {
        $uri = [System.Environment]::GetEnvironmentVariable($uriVar, 'Process')
        if ($uri -and $uri -notmatch '@') {
            $uri = $uri -replace '://', "://elastic:$esPassword@"
            [System.Environment]::SetEnvironmentVariable($uriVar, $uri, 'Process')
        }
    }
    Write-Host "Elasticsearch URIs updated with credentials."
}

# -- Start infrastructure services -------------------------------------------
if (-not $SkipDocker) {
    Write-Host "`nStarting dev infrastructure: $composeFile"
    & docker-compose -f $composeFile up -d
    if ($LASTEXITCODE -ne 0) {
        Write-Error "docker-compose failed with exit code $LASTEXITCODE"
        exit $LASTEXITCODE
    }
    Write-Host "Infrastructure services started."
}

if ($DockerOnly) {
    Write-Host "`nDockerOnly: infrastructure is up. Skipping Maven."
    exit 0
}

# -- Start the application ---------------------------------------------------
Write-Host "`nStarting ERP System (dev profile)..."
Push-Location $systemRoot
try {
    & .\mvnw.cmd
    $exitCode = $LASTEXITCODE
} finally {
    Pop-Location
}

exit $exitCode
