param(
    [Parameter(Mandatory = $true)]
    [string] $EntityName
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
# See erp-system/scripts/generate-entity.ps1 for full strategy explanation.
# Client-side difference: --skip-server flag prevents back-end investigation.
#

$ErrorActionPreference = 'Stop'

$scriptRoot  = Split-Path -Parent $MyInvocation.MyCommand.Path
$clientRoot  = Split-Path -Parent $scriptRoot
$jhipsterDir = Join-Path $clientRoot '.jhipster'

$utf8NoBom = New-Object System.Text.UTF8Encoding $false
function Write-Utf8NoBom([string]$path, [string]$content) {
    [System.IO.File]::WriteAllText($path, $content, $utf8NoBom)
}

$entityFile = Join-Path $jhipsterDir "$EntityName.json"
if (-not (Test-Path $entityFile)) {
    Write-Error "Entity file not found: $entityFile"
    exit 1
}

# BFS: collect transitive closure
$visited = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::Ordinal)
$queue   = [System.Collections.Generic.Queue[string]]::new()
$queue.Enqueue($EntityName)

while ($queue.Count -gt 0) {
    $current = $queue.Dequeue()
    if (-not $visited.Add($current)) { continue }
    $currentFile = Join-Path $jhipsterDir "$current.json"
    if (-not (Test-Path $currentFile)) { continue }
    $def = Get-Content -Raw -LiteralPath $currentFile | ConvertFrom-Json
    foreach ($rel in $def.relationships) {
        if ($rel.otherEntityName) {
            $pascal = $rel.otherEntityName.Substring(0,1).ToUpper() + $rel.otherEntityName.Substring(1)
            if (-not $visited.Contains($pascal)) { $queue.Enqueue($pascal) }
        }
    }
}

# Extend with one level of incoming references (see erp-system version for rationale)
$camelClosure = $visited | ForEach-Object { $_.Substring(0,1).ToLower() + $_.Substring(1) }
Get-ChildItem -Path $jhipsterDir -Filter '*.json' |
    Where-Object { -not $visited.Contains($_.BaseName) } |
    ForEach-Object {
        $def = Get-Content -Raw -LiteralPath $_.FullName | ConvertFrom-Json
        if ($def.relationships | Where-Object { $_.otherEntityName -and ($camelClosure -contains $_.otherEntityName) }) {
            $visited.Add($_.BaseName) | Out-Null
        }
    }

Write-Host "Closure contains $($visited.Count) entities (outgoing + 1 level incoming). Stubbing the rest..."

$savedJson = @{}

try {
    Get-ChildItem -Path $jhipsterDir -Filter '*.json' |
        Where-Object { -not $visited.Contains($_.BaseName) } |
        ForEach-Object {
            $path = $_.FullName
            $savedJson[$path] = Get-Content -Raw -LiteralPath $path
            $stub = [PSCustomObject]@{
                name          = $_.BaseName
                changelogDate = '20200101000000'
                fields        = @()
                relationships = @()
                dto           = 'no'
                service       = 'no'
                pagination    = 'no'
            } | ConvertTo-Json -Depth 5
            Write-Utf8NoBom $path $stub
        }

    Write-Host "Stubbed $($savedJson.Count) entities. Running jhipster for '$EntityName' (client only)..."

    Push-Location $clientRoot
    try {
        npx jhipster entity $EntityName --single-entity --skip-server
        $exitCode = $LASTEXITCODE
    } finally {
        Pop-Location
    }
} finally {
    foreach ($kv in $savedJson.GetEnumerator()) {
        Write-Utf8NoBom $kv.Key $kv.Value
    }
    Write-Host "Restored $($savedJson.Count) entity file(s)."
}

exit $exitCode
