# Move loose files from legacy upload root into doc root (align with file.upload.legacy-path flat files -> file.upload.doc-path).
# Example: powershell -ExecutionPolicy Bypass -File .\scripts\migrate-material-uploads.ps1
# Optional: -LegacyRoot "D:\uploads" -DocRoot "D:\exam-doc"
param(
    [string]$LegacyRoot = "",
    [string]$DocRoot = ""
)
$ErrorActionPreference = "Stop"
$repoRoot = Join-Path $PSScriptRoot ".."
if (-not $LegacyRoot) {
    $LegacyRoot = Join-Path $repoRoot "uploads" | Resolve-Path | Select-Object -ExpandProperty Path
}
if (-not $DocRoot) {
    $DocRoot = Join-Path (Join-Path $repoRoot "uploads") "loads\doc" | Resolve-Path | Select-Object -ExpandProperty Path
}
if (-not (Test-Path $LegacyRoot)) {
    Write-Host "Legacy root not found: $LegacyRoot"
    exit 1
}
if (-not (Test-Path $DocRoot)) {
    New-Item -ItemType Directory -Path $DocRoot -Force | Out-Null
}
$moved = 0
Get-ChildItem -LiteralPath $LegacyRoot -File -ErrorAction SilentlyContinue | ForEach-Object {
    $dest = Join-Path $DocRoot $_.Name
    Move-Item -LiteralPath $_.FullName -Destination $dest -Force
    $moved++
    Write-Host "Moved:" $_.Name
}
Write-Host "Done. Moved file count:" $moved
Write-Host "Next: run migrate-material-file-path.sql in MySQL, or migrate-legacy-materials=true once."
