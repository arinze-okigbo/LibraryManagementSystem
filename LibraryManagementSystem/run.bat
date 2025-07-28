@echo off
echo Library Management System
echo ========================
echo.

REM Compile all Java files
echo Compiling Java files...
javac *.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo Starting Library Management System...
    echo Default login credentials:
    echo   Username: admin, Password: admin123
    echo   Username: john, Password: password123
    echo   Username: sarah, Password: password456
    echo.
    java LibraryGUI
) else (
    echo Compilation failed!
    pause
    exit /b 1
) 