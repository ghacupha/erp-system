param()

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$deployRoot = Split-Path -Parent $scriptRoot
$composeFile = Join-Path $deployRoot 'docker-compose.yml'

Push-Location $deployRoot
try {
    & docker-compose -f $composeFile down
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
