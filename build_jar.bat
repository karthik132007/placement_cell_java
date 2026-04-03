@echo off
echo ==============================================
echo   Placement Cell Management - Build JAR
echo ==============================================

if not exist bin mkdir bin

echo [1/4] Finding Java files...
dir /s /B *.java > sources.txt

echo [2/4] Compiling Project into 'bin' folder...
javac -d bin -cp "lib/*;." @sources.txt
del sources.txt

echo [3/4] Creating Manifest...
echo Main-Class: Main> manifest.txt
echo Class-Path: lib/jcalendar-1.4.jar lib/mysql-connector-j-9.5.0.jar>> manifest.txt

echo [4/4] Building JAR file...
cd bin
jar cvfm ..\PlacementCell.jar ..\manifest.txt *
cd ..

echo Cleaning up temporary files...
rmdir /s /q bin
del manifest.txt

echo ==============================================
echo Build Completed! 
echo You can now double-click PlacementCell.jar to run the app.
echo Requirements: Keep the 'lib' folder next to the jar file.
echo ==============================================
pause
