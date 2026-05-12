# Move files directly under upload root into loads\doc (legacy material files).
# Run from repo: powershell -ExecutionPolicy Bypass -File .\scripts\migrate-material-uploads.ps1
param(
    [string]$UploadRoot = ""
)
$ErrorActionPreference = "Stop"
if (-not $UploadRoot) {
    $UploadRoot = Join-Path (Join-Path $PSScriptRoot "..") "uploads" | Resolve-Path | Select-Object -ExpandProperty Path
}
if (-not (Test-Path $UploadRoot)) {
    Write-Host "Upload root not found: $UploadRoot"
    exit 1
}
$docDir = Join-Path (Join-Path $UploadRoot "loads") "doc"
if (-not (Test-Path $docDir)) {
    New-Item -ItemType Directory -Path $docDir -Force | Out-Null
}
$moved = 0
Get-ChildItem -LiteralPath $UploadRoot -File -ErrorAction SilentlyContinue | ForEach-Object {
    $dest = Join-Path $docDir $_.Name
    Move-Item -LiteralPath $_.FullName -Destination $dest -Force
    $moved++
    Write-Host "Moved:" $_.Name
}
Write-Host "Done. Moved file count:" $moved
Write-Host "Next: run scripts/migrate-material-file-path.sql in MySQL, OR set migrate-legacy-materials=true and start Spring Boot once."
