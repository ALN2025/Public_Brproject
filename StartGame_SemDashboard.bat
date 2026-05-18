@echo off
title L2JBR - GameServer
color 0B

set JAVA_CMD=java

REM ===== Inicializador sem dashboard elaborado By Eduardo.SilvaL2J =====
set L2_EMAIL=brprojeto@l2jbrasil.com

REM Gera uma key simples baseada no horario
set KEY=%RANDOM%%RANDOM%%RANDOM%

REM ===== JVM FLAGS: G1GC + AppCDS + reclaim periodico (similar ao ZGC) =====
call "%~dp0cache\brproject-g1-reclaim.inc.bat"
set JVM_FLAGS=-Xms3g -Xmx3g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+UseCompactObjectHeaders -XX:+TieredCompilation -XX:TieredStopAtLevel=4 %G1_RECLAIM_FLAGS% -XX:+AutoCreateSharedArchive -XX:SharedArchiveFile=cache/brproject_cds.jsa -Xlog:cds=error

echo.
echo =====================================
echo        INICIANDO GAME SERVER
echo =====================================
echo.
echo Key usada: %KEY%
echo Email usado: %L2_EMAIL%
echo.

cd /d "%~dp0game"

if not exist cache mkdir cache

call "%~dp0cache\brproject-cds-check.inc.bat" "cache\brproject_cds.jsa" "..\libs\server.jar" "G1"
call "%~dp0cache\brproject-classpath.inc.bat" "..\libs"

%JAVA_CMD% %JVM_FLAGS% -cp "%BRPROJECT_CP%" ext.mods.gameserver.GameServer %KEY% %L2_EMAIL%

pause
