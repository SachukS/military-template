@echo off
REM Maven Wrapper for Windows
REM Uses Maven from C:\tools\apache-maven-3.9.5
setlocal enabledelayedexpansion

set MAVEN_HOME=C:\tools\apache-maven-3.9.5

if not defined JAVA_HOME (
    REM Try to find Java
    for /D %%i in ("C:\Program Files\Eclipse Adoptium\jdk-*") do (
        set JAVA_HOME=%%i
        goto :found
    )
    echo ERROR: JAVA_HOME not set and no JDK found
    exit /b 1
)
:found

"%MAVEN_HOME%\bin\mvn.cmd" %*
