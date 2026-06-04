@echo off
title Brproject - Login
color 0A

call "%~dp0cache\brproject-java.inc.bat"

REM ===== Inicializador sem dashboard elaborado By Eduardo.SilvaL2J =====
call "%~dp0cache\brproject-g1-reclaim.inc.bat"
set JVM_FLAGS=-Xms256m -Xmx256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=8m -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+UseCompactObjectHeaders -XX:+TieredCompilation -XX:TieredStopAtLevel=4 %G1_RECLAIM_FLAGS%

cd /d "%~dp0login"

if not exist cache mkdir cache
if exist cache\brproject_cds.jsa del /f /q cache\brproject_cds.jsa 2>nul
if exist cache\brproject_cds.gc del /f /q cache\brproject_cds.gc 2>nul

call "%~dp0cache\brproject-classpath.inc.bat" "%~dp0libs"

"%JAVA_CMD%" %JVM_FLAGS% -cp "%BRPROJECT_CP%" ext.mods.loginserver.LoginServer

pause
