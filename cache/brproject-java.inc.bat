@echo off
REM Java 25 — edite JAVA_HOME se o JDK estiver em outro caminho (pasta do JDK, nao bin).
set "JAVA_HOME=D:\core jdk 25"
set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"

if not exist "%JAVA_CMD%" if exist "C:\Program Files\Java\jdk-25\bin\java.exe" (
  set "JAVA_HOME=C:\Program Files\Java\jdk-25"
  set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
)
if not exist "%JAVA_CMD%" if exist "C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot\bin\java.exe" (
  set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
  set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
)
if not exist "%JAVA_CMD%" if exist "%~dp0..\gradle\jdk-25\bin\java.exe" (
  set "JAVA_HOME=%~dp0..\gradle\jdk-25"
  set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
)

if not exist "%JAVA_CMD%" (
  echo.
  echo ERRO: Java 25 nao encontrado.
  echo Edite: Brproject_Distribution\cache\brproject-java.inc.bat
  echo   set "JAVA_HOME=D:\core jdk 25"
  pause
  exit /b 1
)
