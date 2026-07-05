$keys = @(
    'ERP_CACHE_REBUILD_ENABLED',
    'ERP_CACHE_TEARDOWN_REBUILD_ENABLED',
    'ERP_INDEX_ENABLED',
    'ERP_INDEX_REBUILD_ENABLED'
)

foreach ($key in $keys) {
    $value = [Environment]::GetEnvironmentVariable($key, 'Process')
    if ([string]::IsNullOrWhiteSpace($value)) {
        $value = '<not set>'
    }

    Write-Host "$key=$value"
}
