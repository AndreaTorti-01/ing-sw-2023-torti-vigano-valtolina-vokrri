package it.polimi.ingsw.view;

import org.junit.jupiter.api.Test;

class TuiTest {

    Tui tui = new Tui();

    @Test
    void runTest() {
        tui.run();
    }

    @Test
    void printLoadingScreenTest() {
        tui.printLoadingScreen();
    }

    @Test
    void printEndScreenTest() {
        tui.printEndScreen("DiegosDigos");
    }

    @Test
    void printErrorTest() {
        tui.printError("Error");
    }



}