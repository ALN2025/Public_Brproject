@echo off
REM Classpath fixo e ordenado para AppCDS (evita "shared class paths mismatch" no Windows).
REM Uso: call "%~dp0cache\brproject-classpath.inc.bat" "..\libs"
setlocal EnableDelayedExpansion
set "LIBS=%~1"
if "%LIBS%"=="" set "LIBS=..\libs"
set "CP=!LIBS!\server.jar"
for /f "delims=" %%f in ('dir /b /on "!LIBS!\*.jar" 2^>nul') do (
  if /i not "%%f"=="server.jar" (
    echo %%f | findstr /i /c:".encrypted" /c:"kotlin-stdlib-2.0.0.jar" /c:"kotlin-reflect-2.0.0.jar" /c:"kotlinx-coroutines-core-jvm-1.8.1.jar" >nul
    if errorlevel 1 set "CP=!CP!;!LIBS!\%%f"
  )
)
for %%A in ("!CP!") do endlocal & set "BRPROJECT_CP=%%~A"
