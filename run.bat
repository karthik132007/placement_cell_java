@echo off
echo ==============================================
echo   Placement Cell Management - Run Script
echo ==============================================

echo [1/3] Finding Java files...
dir /s /B *.java > sources.txt

echo [2/3] Compiling Project...
javac -cp "lib/*;." @sources.txt
del sources.txt

echo [3/3] Starting Application...
java -cp "lib/*;." Main
