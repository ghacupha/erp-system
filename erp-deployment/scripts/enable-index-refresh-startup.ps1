$settings = @{
    ERP_CACHE_REBUILD_ENABLED = 'TRUE'
    ERP_CACHE_TEARDOWN_REBUILD_ENABLED = 'TRUE'
    ERP_INDEX_ENABLED = 'TRUE'
    ERP_INDEX_REBUILD_ENABLED = 'TRUE'
}

foreach ($setting in $settings.GetEnumerator()) {
    Set-Item -Path "Env:\$($setting.Key)" -Value $setting.Value
}

Write-Host 'ERP startup mode: index refresh enabled for this PowerShell session'
$settings.GetEnumerator() |
    Sort-Object Name |
    ForEach-Object { Write-Host "$($_.Key)=$($_.Value)" }
