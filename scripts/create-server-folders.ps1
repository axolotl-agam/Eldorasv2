$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$regions = @("valoria","nordia","aetherion","thalassor","varkhazan","mechanoria","eryndor")
$ports = 25566..25572
for ($i=0; $i -lt $regions.Count; $i++) {
  $dir = Join-Path $root ("servers/" + $regions[$i])
  New-Item -ItemType Directory -Force -Path (Join-Path $dir "plugins") | Out-Null
  $props = @(
    "server-port=$($ports[$i])",
    "online-mode=false",
    "server-ip=127.0.0.1",
    "motd=Eldoras V2 - $($regions[$i])",
    "max-players=100",
    "view-distance=6",
    "simulation-distance=4",
    "spawn-protection=0",
    "enable-rcon=false"
  )
  Set-Content -Path (Join-Path $dir "server.properties") -Value $props -Encoding utf8
  @"
@echo off
cd /d "%~dp0"
if not exist paper.jar (
  echo paper.jar eksik. Paper 1.21.4 sunucu jar dosyasini bu klasore koyun.
  pause
  exit /b 1
)
java -Xms2G -Xmx4G -jar paper.jar --nogui
pause
"@ | Set-Content -Path (Join-Path $dir "start.bat") -Encoding ascii
}
Write-Host "7 bolge klasoru ve baslangic dosyalari olusturuldu. Paper JAR'lari ve proxy ayarlari ayri kurulmalidir."
