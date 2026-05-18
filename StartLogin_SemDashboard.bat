@echo off
title L2JBR - LoginServer
color 0A

set JAVA_CMD=java

REM ===== Inicializador sem dashboard elaborado By Eduardo.SilvaL2J =====
call "%~dp0cache\brproject-g1-reclaim.inc.bat"
set JVM_FLAGS=-Xms256m -Xmx256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=8m -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+UseCompactObjectHeaders -XX:+TieredCompilation -XX:TieredStopAtLevel=4 %G1_RECLAIM_FLAGS% -XX:+AutoCreateSharedArchive -XX:SharedArchiveFile=cache/brproject_cds.jsa -Xlog:cds=error

echo.
echo =====================================
echo        INICIANDO LOGIN SERVER
echo =====================================
echo.

cd /d "%~dp0login"

if not exist cache mkdir cache

call "%~dp0cache\brproject-cds-check.inc.bat" "cache\brproject_cds.jsa" "..\libs\server.jar" "G1"
call "%~dp0cache\brproject-classpath.inc.bat" "..\libs"

%JAVA_CMD% %JVM_FLAGS% -cp "%BRPROJECT_CP%" ext.mods.loginserver.LoginServer

pause
