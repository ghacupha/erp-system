param(
    [ValidateSet('status', 'updateSQL', 'update', 'releaseLocks', 'validate', 'history', 'changelogSyncSQL', 'changelogSync', 'clearCheckSums')]
    [string] $Command = 'status',

    [ValidateSet('prod', 'dev')]
    [string] $Profile = 'prod',

    [string] $ComposeFile,

    [string] $ComposeService = 'erp-system-server',

    [string] $Contexts,

    [switch] $DryRun
)

$ErrorActionPreference = 'Stop'

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

function Find-RepoRoot {
    param([string] $StartPath)

    $current = Resolve-Path $StartPath
    while ($current) {
        if (Test-Path (Join-Path $current 'erp-system\mvnw.cmd')) {
            return $current.Path
        }

        $parent = Split-Path -Parent $current.Path
        if ($parent -eq $current.Path) {
            break
        }

        $current = Resolve-Path $parent
    }

    throw "Could not locate repository root from '$StartPath'. Expected to find erp-system\mvnw.cmd."
}

$repoRoot = Find-RepoRoot -StartPath $scriptRoot
$systemRoot = Join-Path $repoRoot 'erp-system'

if (-not $ComposeFile) {
    $ComposeFile = Join-Path $repoRoot 'erp-deployment\docker-compose.yml'
}

$changeLogFile = Join-Path $systemRoot 'src\main\resources\config\liquibase\master.xml'
$changeLogFileForLiquibase = $changeLogFile -replace '\\', '/'
$liquibaseVersion = '4.8.0'

function ConvertTo-EnvMap {
    param([string[]] $Lines)

    $map = @{}
    foreach ($line in $Lines) {
        if ([string]::IsNullOrWhiteSpace($line) -or -not $line.Contains('=')) {
            continue
        }

        $key, $value = $line.Split('=', 2)
        $map[$key] = $value
    }

    return $map
}

function Get-ContainerEnvironment {
    param(
        [string] $ComposeFilePath,
        [string] $ServiceName
    )

    $containerId = (& docker-compose -f $ComposeFilePath ps -q $ServiceName 2>$null | Select-Object -First 1)

    if (-not $containerId) {
        $containerId = (& docker ps -a --filter "name=$ServiceName" --format "{{.ID}}" 2>$null | Select-Object -First 1)
    }

    if (-not $containerId) {
        return @{}
    }

    $envLines = & docker inspect $containerId --format '{{range .Config.Env}}{{println .}}{{end}}'
    return ConvertTo-EnvMap -Lines $envLines
}

function Get-Value {
    param(
        [hashtable] $ContainerEnv,
        [string[]] $Names
    )

    foreach ($name in $Names) {
        if ($ContainerEnv.ContainsKey($name) -and -not [string]::IsNullOrWhiteSpace($ContainerEnv[$name])) {
            return $ContainerEnv[$name]
        }

        $processValue = [Environment]::GetEnvironmentVariable($name)
        if (-not [string]::IsNullOrWhiteSpace($processValue)) {
            return $processValue
        }
    }

    return $null
}

function Join-JdbcUrl {
    param(
        [string] $Server,
        [string] $Database
    )

    if ([string]::IsNullOrWhiteSpace($Server) -or [string]::IsNullOrWhiteSpace($Database)) {
        return $null
    }

    return $Server.TrimEnd('/') + '/' + $Database.TrimStart('/')
}

$containerEnv = Get-ContainerEnvironment -ComposeFilePath $ComposeFile -ServiceName $ComposeService

if (-not $Contexts) {
    $Contexts = $Profile
}

if ($Profile -eq 'prod') {
    $databaseName = Get-Value -ContainerEnv $containerEnv -Names @('ERP_SYSTEM_PROD_DB')
    $username = Get-Value -ContainerEnv $containerEnv -Names @('SPRING_LIQUIBASE_USER', 'SPRING_LIQUIBASE_USERNAME', 'SPRING_DATASOURCE_USERNAME', 'PG_DATABASE_PROD_USER')
    $password = Get-Value -ContainerEnv $containerEnv -Names @('SPRING_LIQUIBASE_PASSWORD', 'SPRING_DATASOURCE_PASSWORD', 'PG_DATABASE_PROD_PASSWORD')
} else {
    $databaseName = Get-Value -ContainerEnv $containerEnv -Names @('ERP_SYSTEM_DEV_DB')
    $username = Get-Value -ContainerEnv $containerEnv -Names @('SPRING_LIQUIBASE_USER', 'SPRING_LIQUIBASE_USERNAME', 'SPRING_DATASOURCE_USERNAME', 'PG_DATABASE_DEV_USER')
    $password = Get-Value -ContainerEnv $containerEnv -Names @('SPRING_LIQUIBASE_PASSWORD', 'SPRING_DATASOURCE_PASSWORD', 'PG_DATABASE_DEV_PASSWORD')
}

$url = Get-Value -ContainerEnv $containerEnv -Names @('SPRING_LIQUIBASE_URL', 'SPRING_DATASOURCE_URL')
if (-not $url) {
    $server = Get-Value -ContainerEnv $containerEnv -Names @('LOCAL_PG_SERVER')
    $url = Join-JdbcUrl -Server $server -Database $databaseName
}

if (-not $url) {
    throw "Could not resolve Liquibase URL. Set SPRING_LIQUIBASE_URL, SPRING_DATASOURCE_URL, or LOCAL_PG_SERVER plus ERP_SYSTEM_${Profile}_DB."
}

if (-not $username) {
    throw "Could not resolve database user for profile '$Profile'."
}

if (-not $password) {
    throw "Could not resolve database password for profile '$Profile'."
}

Write-Host "Liquibase command : $Command"
Write-Host "Profile           : $Profile"
Write-Host "Contexts          : $Contexts"
Write-Host "URL               : $url"
Write-Host "Username          : $username"
Write-Host "Password          : ********"
Write-Host "Changelog         : $changeLogFile"

$propertyFileName = "erp-liquibase-" + [System.Guid]::NewGuid().ToString('N') + ".properties"
$propertyFileRelativePath = Join-Path 'target' $propertyFileName
$propertyFile = Join-Path $systemRoot $propertyFileRelativePath

try {
    New-Item -ItemType Directory -Path (Join-Path $systemRoot 'target') -Force | Out-Null

    @(
        "changeLogFile=$changeLogFileForLiquibase"
        "driver=org.postgresql.Driver"
        "url=$url"
        "username=$username"
        "password=$password"
        "contexts=$Contexts"
        "verbose=true"
    ) | Set-Content -Path $propertyFile -Encoding UTF8

    $mavenArgs = @(
        "-Dliquibase.propertyFile=$propertyFileRelativePath",
        "-Dliquibase.propertyFileWillOverride=true",
        "org.liquibase:liquibase-maven-plugin:${liquibaseVersion}:$Command"
    )

    if ($DryRun) {
        Write-Host "Dry run enabled. Maven command will not be executed."
        Write-Host "Working directory : $systemRoot"
        Write-Host "Maven goal        : org.liquibase:liquibase-maven-plugin:${liquibaseVersion}:$Command"
        exit 0
    }

    Push-Location $systemRoot
    try {
        & .\mvnw.cmd @mavenArgs
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    } finally {
        Pop-Location
    }
} finally {
    if (Test-Path $propertyFile) {
        Remove-Item -LiteralPath $propertyFile -Force
    }
}
