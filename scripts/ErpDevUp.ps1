param()

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Split-Path -Parent $scriptRoot
$systemRoot = Join-Path $repoRoot 'erp-system'
$composeFile = Join-Path $repoRoot 'erp-deployment\docker-compose-dev.yml'

Push-Location $systemRoot
try {
    $backendImage = & docker image inspect ghacupha/erp-system:1.8.3 2>$null
    if (-not $backendImage) {
        & .\mvnw.cmd -ntp "-Pdev,webapp" -DskipTests jib:dockerBuild
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    }
} finally {
    Pop-Location
}

Push-Location $repoRoot
try {
    & docker-compose -f $composeFile up -d
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
