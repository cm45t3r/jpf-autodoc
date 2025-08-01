@echo off
REM jpfautodoc.bat - Windows batch script for JPF AutoDoc

set SCRIPT_DIR=%~dp0
set JAR_FILE=%SCRIPT_DIR%..\build\libs\jpf-autodoc-1.0.0-SNAPSHOT-executable.jar

if not exist "%JAR_FILE%" (
    echo Error: JAR file not found. Please build the project first.
    echo Run: gradlew.bat build
    exit /b 1
)

java -jar "%JAR_FILE%" %* 