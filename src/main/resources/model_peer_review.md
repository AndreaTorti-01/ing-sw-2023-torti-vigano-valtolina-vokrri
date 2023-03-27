# Project Description

Our model is composed of the following classes:

- `ItemCard` (6 types): the card representing the items the player can pick from the board and insert in the shelf;
- `PersonalGoalCard` (12 types): the cards containing the pattern the player should recreate on his shelf to gain points;
- `Board`: containing all the item cards the player can pick;
- `Bag`: containing all the available item cards;
- `Shelf`: where the player inserts the item cards taken from the board

All those classes are a subtype of the `GameObject` abstract class, which is an abstraction for all the objects used during a game. This class may be removed in the future if found useless, but for now is a good abstraction.

There's also a `Game` class, whose job is to instantiate and take care of all objects used during the game, but also to communicate with the game controller. It's like a middleware between the model itself and the controller: the controller will give instructions to the `Game` object, which will change its state to reflect the current game state.

We decided not to implement a `Player` class because the `Game` class receives from the constructor an array of strings representing the names of the players: the length of this array will be the number of players playing the current game. The game objects the player "interacts" with, like the shelf or the personal goal card, but also other information, like the scores or the names, will be retrieved using the index of the current player.

*For example*: the name of the first player will be the string in the position 0 of the `players` array, the score is the integer in position 0 of the `scores` array, the corresponding shelf will be the `Shelf` object in position 0 of the array `shelves` etc...
