param()

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptRoot
$composeFile = Join-Path $repoRoot 'erp-deployment\docker-compose.yml'

Push-Location $repoRoot
try {
    & docker-compose -f $composeFile up -d
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
