package it.polimi.ingsw.view.tui;

import static it.polimi.ingsw.utils.Constants.*;

public class TerminalPrintables {
    public static void printSeparee() {
        System.out.println(ANSI_YELLOW +
                "\n\n" +
                "\t\t\t\t╔══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╗\n" +
                "\t\t\t\t╚══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╝" +
                ANSI_RESET + "\n\n");
    }

    public static void printMyShelfie() {
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t\t███╗   ███╗" + ANSI_PURPLE + "██╗   ██╗  " + ANSI_GREEN + " ██████╗" + ANSI_CYAN + "██╗  ██╗" + ANSI_RED + "███████╗" + ANSI_YELLOW + "██╗     " + ANSI_PURPLE + "███████╗" + ANSI_GREEN + "██╗" + ANSI_CYAN + "███████╗  " + ANSI_RED + "██╗\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t████╗ ████║" + ANSI_PURPLE + "╚██╗ ██╔╝  " + ANSI_GREEN + "██╔════╝" + ANSI_CYAN + "██║  ██║" + ANSI_RED + "██╔════╝" + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "██╔════╝" + ANSI_GREEN + "██║" + ANSI_CYAN + "██╔════╝  " + ANSI_RED + "██║\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██╔████╔██║" + ANSI_PURPLE + " ╚████╔╝   " + ANSI_GREEN + "╚█████╗ " + ANSI_CYAN + "███████║" + ANSI_RED + "█████╗  " + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "█████╗  " + ANSI_GREEN + "██║" + ANSI_CYAN + "█████╗    " + ANSI_RED + "██║\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██║╚██╔╝██║" + ANSI_PURPLE + "  ╚██╔╝    " + ANSI_GREEN + " ╚═══██╗" + ANSI_CYAN + "██╔══██║" + ANSI_RED + "██╔══╝  " + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "██╔══╝  " + ANSI_GREEN + "██║" + ANSI_CYAN + "██╔══╝    " + ANSI_RED + "╚═╝\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██║ ╚═╝ ██║" + ANSI_PURPLE + "   ██║     " + ANSI_GREEN + "██████╔╝" + ANSI_CYAN + "██║  ██║" + ANSI_RED + "███████╗" + ANSI_YELLOW + "███████╗" + ANSI_PURPLE + "██║     " + ANSI_GREEN + "██║" + ANSI_CYAN + "███████╗  " + ANSI_RED + "██╗\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t╚═╝     ╚═╝" + ANSI_PURPLE + "   ╚═╝     " + ANSI_GREEN + "╚═════╝ " + ANSI_CYAN + "╚═╝  ╚═╝" + ANSI_RED + "╚══════╝" + ANSI_YELLOW + "╚══════╝" + ANSI_PURPLE + "╚═╝     " + ANSI_GREEN + "╚═╝" + ANSI_CYAN + "╚══════╝  " + ANSI_RED + "╚═╝" + ANSI_RESET);
    }

    public static void printCat() {
        System.out.print(" " + ANSI_GREEN_BACKGROUND + " C " + ANSI_RESET + " ║");
    }

    public static void printBook() {
        System.out.print(" " + ANSI_WHITE_BACKGROUND + " B " + ANSI_RESET + " ║");
    }

    public static void printGame() {
        System.out.print(" " + ANSI_YELLOW_BACKGROUND + " G " + ANSI_RESET + " ║");
    }

    public static void printPlant() {
        System.out.print(" " + ANSI_PURPLE_BACKGROUND + " P " + ANSI_RESET + " ║");
    }

    public static void printTrophies() {
        System.out.print(" " + ANSI_CYAN_BACKGROUND + " T " + ANSI_RESET + " ║");
    }

    public static void printFrame() {
        System.out.print(" " + ANSI_BLUE_BACKGROUND + " F " + ANSI_RESET + " ║");
    }

    public static void printEmpty() {
        System.out.print("     " + ANSI_RESET + "║");
    }

    public static void printInvalid() {
        System.out.print(ANSI_GREY + " ░░░ " + ANSI_RESET + "║");
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

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

    public static void printWelcomeScreen(){
        welcome();
        printMyShelfie();
        credits();
        System.out.println("\n");
    }

    public static void credits(){
        System.out.println("\n" + ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t\t\t\t\t\t\t\t\t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);
    }

    public static void welcome() {
        System.out.println("\n" + ANSI_YELLOW +
                "\t\t\t\t\t\t\t\t\t\t\t\t█   █ █▀▀ █   █▀▀ █▀▀█ █▀▄▀█ █▀▀ 　 ▀▀█▀▀ █▀▀█\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█ █ █ █▀▀ █   █   █  █ █ ▀ █ █▀▀ 　   █   █  █\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█▄▀▄█ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀▀ ▀   ▀ ▀▀▀ 　   █   ▀▀▀▀\n\n\n");
    }
}
