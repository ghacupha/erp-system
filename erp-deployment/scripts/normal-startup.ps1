$settings = @{
    ERP_CACHE_REBUILD_ENABLED = 'FALSE'
    ERP_CACHE_TEARDOWN_REBUILD_ENABLED = 'FALSE'
    ERP_INDEX_ENABLED = 'FALSE'
    ERP_INDEX_REBUILD_ENABLED = 'FALSE'
}

foreach ($setting in $settings.GetEnumerator()) {
    Set-Item -Path "Env:\$($setting.Key)" -Value $setting.Value
}

Write-Host 'ERP startup mode: normal startup enabled for this PowerShell session'
$settings.GetEnumerator() |
    Sort-Object Name |
    ForEach-Object { Write-Host "$($_.Key)=$($_.Value)" }
