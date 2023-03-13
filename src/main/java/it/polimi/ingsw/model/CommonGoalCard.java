package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;


enum CommonGoalCardType {
    SixPairs,
    DiagonalFive,
    FourQuartets,
    FourLinesMaxThreeTypes,
    EqualCorners,
    TwoRainbowColumns,
    TwoSquares,
    TwoRainbowLines,
    ThreeColumnsMaxThreeTypes,
    Cross,
    EightEqual,
    Stair;

    private static final List<CommonGoalCardType> values = Arrays.asList(CommonGoalCardType.values());

    private static final Random random = new Random();
    private static final int size = values.size();

    public static CommonGoalCardType getRandomType() {
        return values.get(random.nextInt(size));
    }

}

public class CommonGoalCard extends GameObject {
    private final Stack<Integer> assignedPoints;
    private final CommonGoalCardType type;

    public CommonGoalCard(int numberOfPlayers) {
        assignedPoints = new Stack<>();

        switch (numberOfPlayers) {
            case 2 -> {
                assignedPoints.push(4);
                assignedPoints.push(8);
            }
            case 3 -> {
                assignedPoints.push(4);
                assignedPoints.push(6);
                assignedPoints.push(8);
            }
            case 4 -> {
                assignedPoints.push(2);
                assignedPoints.push(4);
                assignedPoints.push(6);
                assignedPoints.push(8);
            }
        }

        // TODO: da rivedere l'implementazione per evitare di assegnare più di 12 carte dello stesso tipo
        type = CommonGoalCardType.getRandomType();

    }

    public CommonGoalCardType getType() {
        return type;
    }

    public int peekPoints() {
        return assignedPoints.peek();
    }

    public int popPoints() {
        return assignedPoints.pop();
    }
}



