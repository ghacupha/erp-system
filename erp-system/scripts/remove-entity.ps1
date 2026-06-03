param(
    [Parameter(Mandatory = $true)]
    [string] $EntityName,

    [switch] $Force,    # skip confirmation prompt
    [switch] $DryRun    # list what would be deleted without deleting
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
$clientRoot  = Join-Path $repoRoot 'erp-client'

$utf8NoBom = New-Object System.Text.UTF8Encoding $false
function Write-Utf8NoBom([string]$path, [string]$content) {
    [System.IO.File]::WriteAllText($path, $content, $utf8NoBom)
}

# ── helpers ───────────────────────────────────────────────────────────────────
function To-KebabCase([string]$pascal) {
    # PascalCase → kebab-case  (e.g. PrepaymentAccount → prepayment-account)
    ($pascal -creplace '([A-Z])', '-$1').TrimStart('-').ToLower()
}

function To-CamelCase([string]$pascal) {
    $pascal.Substring(0,1).ToLower() + $pascal.Substring(1)
}

function Remove-IfExists([string]$path) {
    if (Test-Path $path) {
        if ($DryRun) {
            Write-Host "  [dry-run] would delete: $path"
        } else {
            Remove-Item -LiteralPath $path -Recurse -Force
            Write-Host "  deleted: $path"
        }
    }
}

function Remove-GlobMatches([string]$dir, [string]$pattern) {
    if (-not (Test-Path $dir)) { return }
    Get-ChildItem -Path $dir -Filter $pattern -Recurse | ForEach-Object {
        if ($DryRun) {
            Write-Host "  [dry-run] would delete: $($_.FullName)"
        } else {
            Remove-Item -LiteralPath $_.FullName -Force
            Write-Host "  deleted: $($_.FullName)"
        }
    }
}
# ─────────────────────────────────────────────────────────────────────────────

$serverJhipster = Join-Path $systemRoot '.jhipster'
$clientJhipster = Join-Path $clientRoot '.jhipster'
$serverYoRc     = Join-Path $systemRoot '.yo-rc.json'
$clientYoRc     = Join-Path $clientRoot  '.yo-rc.json'

$entityJsonServer = Join-Path $serverJhipster "$EntityName.json"
$entityJsonClient = Join-Path $clientJhipster "$EntityName.json"

if (-not (Test-Path $entityJsonServer)) {
    Write-Error "Entity not found in erp-system: $entityJsonServer"
    exit 1
}

# ── 1. Find all entities that reference this one ──────────────────────────────
Write-Host "`nScanning for entities that depend on '$EntityName'..."

$dependents = [System.Collections.Generic.List[string]]::new()
Get-ChildItem -Path $serverJhipster -Filter '*.json' |
    Where-Object { $_.BaseName -ne $EntityName } |
    ForEach-Object {
        $def = Get-Content -Raw -LiteralPath $_.FullName | ConvertFrom-Json
        $refs = $def.relationships |
            Where-Object { $_.otherEntityName } |
            ForEach-Object {
                $_.otherEntityName.Substring(0,1).ToUpper() + $_.otherEntityName.Substring(1)
            }
        if ($EntityName -in $refs) {
            $dependents.Add($_.BaseName)
        }
    }

if ($dependents.Count -gt 0) {
    Write-Warning "The following entities have relationships that point to '$EntityName':"
    $dependents | ForEach-Object { Write-Host "    $_" }
    Write-Host ""
    Write-Host "You should update those entity JSON files to remove the relationship before"
    Write-Host "regenerating them, otherwise their generators will fail."
    Write-Host ""
}

# ── 2. Confirm ────────────────────────────────────────────────────────────────
if (-not $Force -and -not $DryRun) {
    $answer = Read-Host "Proceed with removing '$EntityName'? [y/N]"
    if ($answer -notmatch '^[Yy]') {
        Write-Host "Aborted."
        exit 0
    }
}

$kebab  = To-KebabCase $EntityName
$camel  = To-CamelCase $EntityName

Write-Host ""
if ($DryRun) { Write-Host "=== DRY RUN — nothing will be deleted ===" }

# ── 3. Remove .jhipster JSON files ───────────────────────────────────────────
Write-Host "`n[.jhipster definitions]"
Remove-IfExists $entityJsonServer
Remove-IfExists $entityJsonClient

# ── 4. Update .yo-rc.json entities lists ─────────────────────────────────────
Write-Host "`n[.yo-rc.json]"
foreach ($yoRcPath in @($serverYoRc, $clientYoRc)) {
    if (Test-Path $yoRcPath) {
        $raw  = Get-Content -Raw -LiteralPath $yoRcPath
        $data = $raw | ConvertFrom-Json
        $before = $data.'generator-jhipster'.entities.Count
        $data.'generator-jhipster'.entities = @(
            $data.'generator-jhipster'.entities | Where-Object { $_ -ne $EntityName }
        )
        $after = $data.'generator-jhipster'.entities.Count
        if ($DryRun) {
            Write-Host "  [dry-run] would remove '$EntityName' from entities list in: $yoRcPath ($before → $after)"
        } else {
            Write-Utf8NoBom $yoRcPath ($data | ConvertTo-Json -Depth 100)
            Write-Host "  updated: $yoRcPath  (removed '$EntityName'; $before → $after entries)"
        }
    }
}

# ── 5. Server-side generated files ───────────────────────────────────────────
Write-Host "`n[erp-system generated files]"
$javaBase = Join-Path $systemRoot 'src\main\java\io\github\erp'
$testBase = Join-Path $systemRoot 'src\test\java\io\github\erp'

foreach ($subdir in @('domain', 'repository', 'web\rest')) {
    Remove-GlobMatches (Join-Path $javaBase $subdir) "$EntityName*.java"
}
foreach ($subdir in @('service', 'service\impl', 'service\dto', 'service\mapper', 'service\criteria')) {
    Remove-GlobMatches (Join-Path $javaBase $subdir) "$EntityName*.java"
}
Remove-GlobMatches $testBase "$EntityName*.java"
Remove-GlobMatches $testBase "$EntityName*.scala"

# ── 6. Liquibase changelogs ───────────────────────────────────────────────────
Write-Host "`n[liquibase changelogs]"
$changelogDir = Join-Path $systemRoot 'src\main\resources\config\liquibase\changelog'
Remove-GlobMatches $changelogDir "*_$EntityName.xml"
Remove-GlobMatches $changelogDir "*_entity_$EntityName.xml"
Remove-GlobMatches $changelogDir "*_entity_constraints_$EntityName.xml"
Remove-GlobMatches $changelogDir "*${kebab}*.xml"

$masterXml = Join-Path $systemRoot 'src\main\resources\config\liquibase\master.xml'
if (Test-Path $masterXml) {
    $masterContent = Get-Content -Raw -LiteralPath $masterXml
    $lines = $masterContent -split "`n"
    $filtered = $lines | Where-Object { $_ -notmatch [regex]::Escape($EntityName) -and $_ -notmatch [regex]::Escape($kebab) }
    if ($lines.Count -ne $filtered.Count) {
        if ($DryRun) {
            Write-Host "  [dry-run] would remove $($lines.Count - $filtered.Count) line(s) from master.xml referencing '$EntityName'"
        } else {
            [System.IO.File]::WriteAllText($masterXml, ($filtered -join "`n"), $utf8NoBom)
            Write-Host "  updated master.xml: removed $($lines.Count - $filtered.Count) line(s) referencing '$EntityName'"
        }
    }
}

# ── 7. Client-side generated files ───────────────────────────────────────────
Write-Host "`n[erp-client generated files]"
$webappBase = Join-Path $clientRoot 'src\main\webapp\app'

# The entity folder may live under any category subdirectory
Get-ChildItem -Path (Join-Path $webappBase 'entities') -Directory -Recurse |
    Where-Object { $_.Name -eq $kebab } |
    ForEach-Object {
        if ($DryRun) {
            Write-Host "  [dry-run] would delete folder: $($_.FullName)"
        } else {
            Remove-Item -LiteralPath $_.FullName -Recurse -Force
            Write-Host "  deleted folder: $($_.FullName)"
        }
    }

# ── 8. Summary ────────────────────────────────────────────────────────────────
Write-Host ""
if ($DryRun) {
    Write-Host "Dry run complete. Re-run without -DryRun to apply changes."
} else {
    Write-Host "Removal of '$EntityName' complete."
}

if ($dependents.Count -gt 0) {
    Write-Host ""
    Write-Host "REMINDER: these entities still reference '$EntityName' in their relationship"
    Write-Host "definitions — update them manually:"
    $dependents | ForEach-Object { Write-Host "    $_" }
}
