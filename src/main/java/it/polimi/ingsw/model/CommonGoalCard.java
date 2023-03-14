package main.java.it.polimi.ingsw.model;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;


enum CommonGoalCardType {
    SIXPAIRS,
    DIAGONALFIVE,
    FOURQUARTETS,
    FOURLINESMAXTHREETYPES,
    EQUALCORNERS,
    TWORAINBOWCOLUMNS,
    TWOSQUARES,
    TWORAINBOWLINES,
    THREECOLUMNSMAXTHREETYPES,
    CROSS,
    EIGHTEQUAL,
    STAIR;

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



