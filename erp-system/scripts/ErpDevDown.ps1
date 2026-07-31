param()

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptRoot
$composeFile = Join-Path $repoRoot 'erp-deployment\docker-compose-dev.yml'

Push-Location $repoRoot
try {
    & docker-compose -f $composeFile down
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
