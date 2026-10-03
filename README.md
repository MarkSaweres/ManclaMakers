# Mancala Makers

A two-player Mancala game for the desktop, written in Java with Swing. Team project for CS 151 (Object-Oriented Design) at San Jose State University, fall 2021, built with Thinh Vo and Steven Stansberry.

![Stone icon](stone.png)

## Features
- Start with 3 or 4 stones per pit.
- Full rules: landing in your own mancala earns an extra turn, and landing in an empty pit on your side captures the stones across from it.
- Each player gets up to 3 undos per turn.
- The game detects when one side is empty and announces the winner or a tie.

## Design
- **Model-view-controller.** `BoardModel` holds the board state and the previous state for undo. `MancalaView` draws the board and listens for changes. Clicks on pits go through `Pit`, a mouse handler.
- **Observer pattern.** The view registers with the model as a `ChangeListener`, and the model notifies every listener after each move or undo.
- **Strategy pattern.** The board's look sits behind the `BoardDesign` interface, so a new style can be swapped in without touching the game logic.
- **Custom icons.** `Stone` implements Swing's `Icon` to draw the stones.

## Tech stack
Java, Swing, AWT

## Running it
```bash
javac *.java
java MancalaTest
```
