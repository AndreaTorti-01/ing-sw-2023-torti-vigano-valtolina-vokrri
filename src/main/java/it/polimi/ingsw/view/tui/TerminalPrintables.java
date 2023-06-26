package it.polimi.ingsw.view.tui;

import static it.polimi.ingsw.utils.Constants.*;

public class TerminalPrintables {
    /**
     * Prints to the terminal a separator.
     */
    public static void printSeparee() {
        System.out.println(ANSI_YELLOW +
                "\n\n" +
                "\t\t\t\t╔══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╗\n" +
                "\t\t\t\t╚══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╝" +
                ANSI_RESET + "\n\n");
    }

    /**
     * Prints to the terminal the title of the game.
     */
    public static void printMyShelfie() {
        System.out.printf(
                """
                        %s\t\t\t\t\t\t\t\t███╗   ███╗%s██╗   ██╗  %s ██████╗%s██╗  ██╗%s███████╗%s██╗     %s███████╗%s██╗%s███████╗  %s██╗
                        %s\t\t\t\t\t\t\t\t████╗ ████║%s╚██╗ ██╔╝  %s██╔════╝%s██║  ██║%s██╔════╝%s██║     %s██╔════╝%s██║%s██╔════╝  %s██║
                        %s\t\t\t\t\t\t\t\t██╔████╔██║%s ╚████╔╝   %s╚█████╗ %s███████║%s█████╗  %s██║     %s█████╗  %s██║%s█████╗    %s██║
                        %s\t\t\t\t\t\t\t\t██║╚██╔╝██║%s  ╚██╔╝    %s ╚═══██╗%s██╔══██║%s██╔══╝  %s██║     %s██╔══╝  %s██║%s██╔══╝    %s╚═╝
                        %s\t\t\t\t\t\t\t\t██║ ╚═╝ ██║%s   ██║     %s██████╔╝%s██║  ██║%s███████╗%s███████╗%s██║     %s██║%s███████╗  %s██╗
                        %s\t\t\t\t\t\t\t\t╚═╝     ╚═╝%s   ╚═╝     %s╚═════╝ %s╚═╝  ╚═╝%s╚══════╝%s╚══════╝%s╚═╝     %s╚═╝%s╚══════╝  %s╚═╝%s%n""",
                ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_YELLOW, ANSI_PURPLE, ANSI_GREEN, ANSI_CYAN, ANSI_RED, ANSI_RESET);
    }

    /**
     * Prints to the terminal the cat Item Card type.
     */
    public static void printCat() {
        System.out.print(" " + ANSI_GREEN_BACKGROUND + " C " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal the book Item Card type.
     */
    public static void printBook() {
        System.out.print(" " + ANSI_WHITE_BACKGROUND + " B " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal the game Item Card type.
     */
    public static void printGame() {
        System.out.print(" " + ANSI_YELLOW_BACKGROUND + " G " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal the plant Item Card type.
     */
    public static void printPlant() {
        System.out.print(" " + ANSI_PURPLE_BACKGROUND + " P " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal the trophy Item Card type.
     */
    public static void printTrophies() {
        System.out.print(" " + ANSI_CYAN_BACKGROUND + " T " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal the frame Item Card type.
     */
    public static void printFrame() {
        System.out.print(" " + ANSI_BLUE_BACKGROUND + " F " + ANSI_RESET + " ║");
    }

    /**
     * Prints to the terminal empty space.
     */
    public static void printEmpty() {
        System.out.print("     " + ANSI_RESET + "║");
    }

    /**
     * Prints to the terminal invalid message.
     */
    public static void printInvalid() {
        System.out.print(ANSI_GREY + " ░░░ " + ANSI_RESET + "║");
    }

    /**
     * Clears the terminal screen.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Prints to the terminal the "waiting for players" message.
     */
    public static void printWaitingForPlayers() {
        //clear the console screen
        clearConsole();
        welcome();
        printMyShelfie();
        credits();
        System.out.print("""
                \t\t\t\t█   █ █▀▀█ ▀ ▀▀█▀▀ ▀ █▀▀▄ █▀▀▀ 　 █▀▀ █▀▀█ █▀▀█ 　 █▀▀█ ▀▀█▀▀ █  █ █▀▀ █▀▀█ 　 █▀▀█ █   █▀▀█ █  █ █▀▀ █▀▀█ █▀▀
                \t\t\t\t█ █ █ █▄▄█ █   █   █ █  █ █ ▀█ 　 █▀▀ █  █ █▄▄▀ 　 █  █   █   █▀▀█ █▀▀ █▄▄▀ 　 █  █ █   █▄▄█ █▄▄█ █▀▀ █▄▄▀ ▀▀█
                \t\t\t\t█▄▀▄█ ▀  ▀ ▀   ▀   ▀ ▀  ▀ ▀▀▀▀ 　 ▀   ▀▀▀▀ ▀ ▀▀ 　 ▀▀▀▀   ▀   ▀  ▀ ▀▀▀ ▀ ▀▀ 　 █▀▀▀ ▀▀▀ ▀  ▀ ▄▄▄█ ▀▀▀ ▀ ▀▀ ▀▀▀""");
    }

    /**
     * Prints to the terminal the welcome screen.
     */
    public static void printWelcomeScreen() {
        welcome();
        printMyShelfie();
        credits();
        System.out.println("\n");
    }

    /**
     * Prints to the terminal the credits screen.
     */
    public static void credits() {
        System.out.println("\n" + ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t\t\t\t\t\t\t\t\t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);
    }

    /**
     * Prints to the terminal the welcome message.
     */
    public static void welcome() {
        System.out.println("\n" + ANSI_YELLOW +
                "\t\t\t\t\t\t\t\t\t\t\t\t█   █ █▀▀ █   █▀▀ █▀▀█ █▀▄▀█ █▀▀ 　 ▀▀█▀▀ █▀▀█\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█ █ █ █▀▀ █   █   █  █ █ ▀ █ █▀▀ 　   █   █  █\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█▄▀▄█ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀▀ ▀   ▀ ▀▀▀ 　   █   ▀▀▀▀\n\n\n");
    }

    /**
     * Prints to the terminal the winner message.
     */
    public static void printWon() {
        System.out.println(ANSI_GREEN +
                "██╗░░░██╗░█████╗░██╗░░░██╗  ░██╗░░░░░░░██╗░█████╗░███╗░░██╗██╗\n" +
                "╚██╗░██╔╝██╔══██╗██║░░░██║  ░██║░░██╗░░██║██╔══██╗████╗░██║██║\n" +
                "░╚████╔╝░██║░░██║██║░░░██║  ░╚██╗████╗██╔╝██║░░██║██╔██╗██║██║\n" +
                "░░╚██╔╝░░██║░░██║██║░░░██║  ░░████╔═████║░██║░░██║██║╚████║╚═╝\n" +
                "░░░██║░░░╚█████╔╝╚██████╔╝  ░░╚██╔╝░╚██╔╝░╚█████╔╝██║░╚███║██╗\n" +
                "░░░╚═╝░░░░╚════╝░░╚═════╝░  ░░░╚═╝░░░╚═╝░░░╚════╝░╚═╝░░╚══╝╚═╝\n" + ANSI_RESET);
    }

    /**
     * Prints to the terminal the loser message.
     */
    public static void printLost() {
        System.out.println(ANSI_RED +

                "██╗░░░██╗░█████╗░██╗░░░██╗  ██╗░░░░░░█████╗░░██████╗████████╗░░░░░░░░░██╗\n" +
                "╚██╗░██╔╝██╔══██╗██║░░░██║  ██║░░░░░██╔══██╗██╔════╝╚══██╔══╝░░░░░░░░░██║\n" +
                "░╚████╔╝░██║░░██║██║░░░██║  ██║░░░░░██║░░██║╚█████╗░░░░██║░░░░░░░░░░░░██║\n" +
                "░░╚██╔╝░░██║░░██║██║░░░██║  ██║░░░░░██║░░██║░╚═══██╗░░░██║░░░░░░░░░░░░╚═╝\n" +
                "░░░██║░░░╚█████╔╝╚██████╔╝  ███████╗╚█████╔╝██████╔╝░░░██║░░░██╗██╗██╗██╗\n" +
                "░░░╚═╝░░░░╚════╝░░╚═════╝░  ╚══════╝░╚════╝░╚═════╝░░░░╚═╝░░░╚═╝╚═╝╚═╝╚═╝\n" + ANSI_RESET);
    }
}
