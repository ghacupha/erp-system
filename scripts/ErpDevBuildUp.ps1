param()

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptRoot
$systemRoot = Join-Path $repoRoot 'erp-system'
$composeFile = Join-Path $repoRoot 'erp-deployment\docker-compose-dev.yml'

Push-Location $systemRoot
try {
    & .\mvnw.cmd -ntp "-Pdev,webapp" -DskipTests jib:dockerBuild
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
} finally {
    Pop-Location
}

Push-Location $repoRoot
try {
    & docker-compose -f $composeFile up --build -d
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
