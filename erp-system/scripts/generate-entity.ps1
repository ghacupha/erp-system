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

$ErrorActionPreference = 'Stop'

$scriptRoot  = Split-Path -Parent $MyInvocation.MyCommand.Path
$systemRoot  = Split-Path -Parent $scriptRoot
$jhipsterDir = Join-Path $systemRoot '.jhipster'

# JHipster's JSON parser breaks on a UTF-8 BOM written by Set-Content.
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
function Write-Utf8NoBom([string]$path, [string]$content) {
    [System.IO.File]::WriteAllText($path, $content, $utf8NoBom)
}

$entityFile = Join-Path $jhipsterDir "$EntityName.json"
if (-not (Test-Path $entityFile)) {
    Write-Error "Entity file not found: $entityFile"
    exit 1
}

# ── Strategy ──────────────────────────────────────────────────────────────────
# JHipster's entity generator always composes an 'entities' generator that calls
# composeEachEntity(), which iterates ALL entries in .yo-rc.json's entities array.
# Each composed entity goes through the _loading phase (populates sharedEntities)
# and _preparing phase (loadRelationships reads from sharedEntities).
#
# Problem A – long table names: entities with table names > 30 chars AND
#   relationships > 0 trigger an interactive prompt even with regenerate:true.
#   Stashing their files causes entityStorage.getAll() to return {} (no name,
#   no changelogDate) which then crashes the configuring phase.
#
# Problem B – newly-added interactive relationships: when the user adds a
#   relationship to entity X during the wizard, X must already be in
#   sharedEntities before loadRelationships runs on the target entity.
#
# Solution: Replace every non-closure entity's JSON with a minimal stub that
#   has name + changelogDate but empty fields[] and relationships[].
#   • Empty relationships bypasses the table-name-length prompt (line 278 of
#     prompts.js: "if relationships.length === 0 → return undefined").
#   • The stub is enough for shareEntity() to populate sharedEntities correctly.
#   • Closure entities keep their REAL JSON → proper code generation.
#   • .yo-rc.json is left untouched; all 308 entities are composed, but the
#     307 stubs process in milliseconds (no fields, no relationships, no prompts).
# ──────────────────────────────────────────────────────────────────────────────

# BFS: collect outgoing transitive closure (entities whose real JSON must be kept)
$visited = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::Ordinal)
$queue   = [System.Collections.Generic.Queue[string]]::new()
$queue.Enqueue($EntityName)

while ($queue.Count -gt 0) {
    $current = $queue.Dequeue()
    if (-not $visited.Add($current)) { continue }
    $currentFile = Join-Path $jhipsterDir "$current.json"
    if (-not (Test-Path $currentFile)) { continue }   # built-in (User, etc.)
    $def = Get-Content -Raw -LiteralPath $currentFile | ConvertFrom-Json
    foreach ($rel in $def.relationships) {
        if ($rel.otherEntityName) {
            $pascal = $rel.otherEntityName.Substring(0,1).ToUpper() + $rel.otherEntityName.Substring(1)
            if (-not $visited.Contains($pascal)) { $queue.Enqueue($pascal) }
        }
    }
}

# Extend with one level of INCOMING references — entities that have a
# relationship pointing TO any closure entity. Their real JSON must be kept so
# JHipster sees their otherEntityField values and generates the @Named qualifier
# methods in the closure entity's mapper (e.g. @Named("catalogueNumber") in
# PrepaymentAccountMapper when AmortizationRecurrence references it).
$camelClosure = $visited | ForEach-Object { $_.Substring(0,1).ToLower() + $_.Substring(1) }

Get-ChildItem -Path $jhipsterDir -Filter '*.json' |
    Where-Object { -not $visited.Contains($_.BaseName) } |
    ForEach-Object {
        $def = Get-Content -Raw -LiteralPath $_.FullName | ConvertFrom-Json
        $refsClosureEntity = $def.relationships |
            Where-Object { $_.otherEntityName -and ($camelClosure -contains $_.otherEntityName) }
        if ($refsClosureEntity) {
            $visited.Add($_.BaseName) | Out-Null
        }
    }

Write-Host "Closure contains $($visited.Count) entities (outgoing + 1 level incoming). Stubbing the rest..."

# Replace non-closure entity JSONs with minimal stubs; save originals in memory
$savedJson = @{}   # key = full path, value = original content

try {
    Get-ChildItem -Path $jhipsterDir -Filter '*.json' |
        Where-Object { -not $visited.Contains($_.BaseName) } |
        ForEach-Object {
            $path = $_.FullName
            $savedJson[$path] = Get-Content -Raw -LiteralPath $path
            # Minimal stub: empty relationships bypasses the table-name-length prompt
            $stub = [PSCustomObject]@{
                name            = $_.BaseName
                changelogDate   = '20200101000000'
                fields          = @()
                relationships   = @()
                dto             = 'no'
                service         = 'no'
                pagination      = 'no'
            } | ConvertTo-Json -Depth 5
            Write-Utf8NoBom $path $stub
        }

    Write-Host "Stubbed $($savedJson.Count) entities. Running jhipster for '$EntityName'..."

    Push-Location $systemRoot
    try {
        npx jhipster entity $EntityName --single-entity
        $exitCode = $LASTEXITCODE
    } finally {
        Pop-Location
    }
} finally {
    # Restore every file we stubbed, regardless of success or failure
    foreach ($kv in $savedJson.GetEnumerator()) {
        Write-Utf8NoBom $kv.Key $kv.Value
    }
    Write-Host "Restored $($savedJson.Count) entity file(s)."
}

exit $exitCode
