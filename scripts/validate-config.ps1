$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$required = @(
  "infra/docker-compose.yml",
  "infra/.env.example",
  "proxy/velocity.toml.example",
  "servers/regions.yml",
  "plugins/EldorasCore/build.gradle.kts",
  "plugins/EldorasCore/src/main/resources/plugin.yml",
  "plugins/EldorasCore/src/main/resources/config.yml"
)
$failed = $false
foreach ($relative in $required) {
  $path = Join-Path $root $relative
  if (Test-Path $path) { Write-Host "[OK] $relative" -ForegroundColor Green }
  else { Write-Host "[MISSING] $relative" -ForegroundColor Red; $failed = $true }
}
$velocity = Get-Content (Join-Path $root "proxy/velocity.toml.example") -Raw
if ($velocity -match 'player-info-forwarding-mode\s*=\s*"modern"') {
  Write-Host "[OK] Velocity modern forwarding mode" -ForegroundColor Green
} else {
  Write-Host "[FAIL] Velocity forwarding mode" -ForegroundColor Red
  $failed = $true
}
$regions = Get-Content (Join-Path $root "servers/regions.yml") -Raw
$regionCount = ([regex]::Matches($regions, '(?m)^\s{2}[a-z0-9_-]+:\s*$')).Count
if ($regionCount -eq 7) { Write-Host "[OK] 7 region entries" -ForegroundColor Green }
else { Write-Host "[FAIL] Expected 7 regions, found $regionCount" -ForegroundColor Red; $failed = $true }
if ($failed) { exit 1 }
Write-Host "Static configuration checks passed. This does not prove servers can start or connect." -ForegroundColor Yellow
