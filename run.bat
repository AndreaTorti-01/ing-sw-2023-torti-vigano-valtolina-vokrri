call mvn compile

start java -cp target/classes it.polimi.ingsw.ServerApp

set /p instances=Enter number of client instances to launch:

FOR /L %%i IN (1,1,%instances%) DO (
    start "Client %%i" cmd /c "java -cp target/classes it.polimi.ingsw.ClientApp"
)
