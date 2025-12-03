@echo off
cd /d "%~dp0"
echo Starting Music Player Server...
java -jar "target\music-player-server-1.0.0.jar"
pause
