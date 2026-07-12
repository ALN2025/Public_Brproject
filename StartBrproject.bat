@echo off
title Brproject - License Init
color 0B
cd /d "%~dp0"

REM --- Habilita cores ANSI no console (cmd.exe) antes de tudo ---
call "%~dp0cache\brproject-ansi.inc.bat"

REM --- Localiza dinamicamente o Java (sem caminhos hardcoded) ---
call "%~dp0cache\brproject-java.inc.bat"

REM --- Flags para VPS/servidor: evita crash de driver grafico (awt.dll) ---
REM -Dbrproject.safe.graphics=true = molduras e paineis com cores solidas (sem gradiente)
"%JAVA_CMD%" -Xms256m -Xmx512m -Dsun.java2d.opengl=false -Dsun.java2d.d3d=false -Dsun.java2d.pmoffscreen=false -Dbrproject.safe.graphics=true -cp "libs/*" ext.mods.security.LicenseInit
pause