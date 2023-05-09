@echo off

call mvn compile

start java -cp target/classes it.polimi.ingsw.ServerApp

set /a "i=0"

start "Client %i%" cmd /c "java -cp target/classes it.polimi.ingsw.ClientApp"
set /a "i+=1"

:loop
echo Press Enter to start a new client. Type "quit" to exit.
set /p input=
if "%input%"=="" (
    set /a "i+=1"
    start "Client %i%" cmd /c "java -cp target/classes it.polimi.ingsw.ClientApp"
    goto loop
) else if /i "%input%"=="quit" (
    exit /b
) else (
    goto loop
)