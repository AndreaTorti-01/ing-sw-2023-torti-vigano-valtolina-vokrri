package it.polimi.ingsw.model;


import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonalGoalCardTest {
    @Test
    void testPattern() {
        for (int i = 0; i < Constants.numberOfPersonalGoalCardTypes; i++) {
            PersonalGoalCard currentPersonalGoalCard = new PersonalGoalCard(i);
            ItemType[][] pattern = currentPersonalGoalCard.getPattern();

            try {
                // gets the pattern file corresponding to the given index
                InputStream inputStream = getClass().getResourceAsStream(
                        String.format("/personalGoalCards/PGC%d.txt", i)
                );

                // allows to read data from the obtained file
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                int row = 0;
                String line = reader.readLine();
                while (line != null) {
                    for (int column = 0; column < line.length(); column++) {
                        char currentChar = line.charAt(column);
                        assertEquals(pattern[row][column], ItemType.getItemTypeFromAbbreviation(currentChar));
                    }

                    // goes to next line
                    line = reader.readLine();
                    row++;
                }
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void testGetPattern0() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(0);

        assertEquals(personalGoalCard.getPattern()[0][2], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[3][3], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[4][4], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[5][0], ItemType.CATS);
    }

    @Test
    void testGetPattern1() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);

        assertEquals(personalGoalCard.getPattern()[0][0], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[0][2], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[1][4], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[2][3], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[3][1], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[5][2], ItemType.TROPHIES);
    }

    @Test
    void testGetPattern2() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(2);

        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[2][0], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[3][4], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[4][3], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[5][4], ItemType.FRAMES);
    }

    @Test
    void testGetPattern3() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(3);

        assertEquals(personalGoalCard.getPattern()[1][0], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[1][3], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[3][1], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[3][4], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[5][0], ItemType.BOOKS);
    }

    @Test
    void testGetPattern4() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(4);

        assertEquals(personalGoalCard.getPattern()[0][4], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[2][0], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[3][3], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[4][1], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[4][2], ItemType.CATS);
    }

    @Test
    void testGetPattern5() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(5);

        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[3][1], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[3][2], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[4][4], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[5][0], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[5][3], ItemType.CATS);
    }

    @Test
    void testGetPattern6() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(6);

        assertEquals(personalGoalCard.getPattern()[0][2], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[0][4], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[2][3], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[4][1], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[4][3], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[5][0], ItemType.PLANTS);
    }

    @Test
    void testGetPattern7() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(7);

        assertEquals(personalGoalCard.getPattern()[0][0], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[1][3], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[2][1], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[3][0], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[4][4], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[5][2], ItemType.BOOKS);
    }

    @Test
    void testGetPattern8() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(8);

        assertEquals(personalGoalCard.getPattern()[0][4], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[3][0], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[4][3], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[5][3], ItemType.GAMES);
    }

    @Test
    void testGetPattern9() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(9);

        assertEquals(personalGoalCard.getPattern()[0][2], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[2][2], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[3][4], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[4][1], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[4][4], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[5][0], ItemType.FRAMES);
    }

    @Test
    void testGetPattern10() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(10);

        assertEquals(personalGoalCard.getPattern()[0][4], ItemType.TROPHIES);
        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[2][0], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[3][3], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[4][1], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[5][3], ItemType.PLANTS);
    }

    @Test
    void testGetPattern11() {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(11);

        assertEquals(personalGoalCard.getPattern()[0][2], ItemType.PLANTS);
        assertEquals(personalGoalCard.getPattern()[1][1], ItemType.BOOKS);
        assertEquals(personalGoalCard.getPattern()[2][0], ItemType.GAMES);
        assertEquals(personalGoalCard.getPattern()[3][2], ItemType.FRAMES);
        assertEquals(personalGoalCard.getPattern()[4][4], ItemType.CATS);
        assertEquals(personalGoalCard.getPattern()[5][3], ItemType.TROPHIES);
    }

    @Test
    void testIllegalIndex() {
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> new PersonalGoalCard(-1)
        );

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> new PersonalGoalCard(12)
        );
    }
}