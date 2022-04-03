package com.company;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);
    public static Random random = new Random();
    public static char[][] enemyField = new char[10][10]; // '0'
    public static char[][] myField = new char[10][10];
    public static final String MY_FIELD_TITLE = "MY FIELD";
    public static final String ENEMIES_FIELD_TITLE = "ENEMIES FIELD";
    //public static final int[] SHIPS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    public static final int[] SHIPS = {4, 2, 1};
    public static int[][] checkedCells;
    public static int successRowIndex = -1, successColumnIndex = -1;
    public static boolean isMyChance = true, wasKilled = false;

    public static void main(String[] args) {
        showField(myField, MY_FIELD_TITLE, false);
        showField(enemyField, ENEMIES_FIELD_TITLE, false);
        hideAllShips(false);
        hideAllShips(true);
        showField(enemyField, ENEMIES_FIELD_TITLE, false);
        while (true) {
            if (isMyChance) {
                isMyChance = false;
                while (shootAgain(true)) {
                    System.out.println("One more shoot");
                    showField(enemyField, ENEMIES_FIELD_TITLE, false);
                }
                showField(enemyField, ENEMIES_FIELD_TITLE, false);
                if (isGameFinished(true)) {
                    break;
                }
            } else {
                isMyChance = true;
                while (shootAgain(false)) {
                    showField(myField, MY_FIELD_TITLE, false);
                }
                showField(myField, MY_FIELD_TITLE, false);
                if (isGameFinished(false)) {
                    break;
                }
            }
        }
    }

    public static boolean isGameFinished(boolean isUser) {
        char[][] field = myField;
        if (isUser) {
            field = enemyField;
        }
        boolean isAnyShips = false;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 's') {
                    isAnyShips = true;
                    break;
                }
            }
        }
        if (!isAnyShips) {
            if (isUser) {
                System.out.println("You won!!!");
            } else {
                System.out.println("Enemy won!!!");
            }
            return true;
        }
        return false;
    }

    public static boolean shootAgain(boolean isUser) {
        int row, column;
        if (isUser) {
            System.out.println("Enter cell row: ");
            row = sc.nextInt();
            System.out.println("Enter cell column: ");
            column = sc.nextInt();
        } else {
            row = random.nextInt(10);
            column = random.nextInt(10);
        }
        char[][] field = myField;
        if (isUser) {
            field = enemyField;
        }
        if (field[row][column] == 0) {
            field[row][column] = 'l';
            System.out.println("You missed!!!");
            return false;
        } else if (field[row][column] == 's') {
            field[row][column] = 'f';
            checkedCells = new int[4][2];
            fillArray(checkedCells);
            insertInCheckedArray(row, column);
            if (isShipKilled(row, column, isUser)) {
                System.out.println("You killed the ship!!!");
                successRowIndex = row;
                successColumnIndex = column;
                wasKilled = true;
                checkedCells = new int[4][2];
                fillArray(checkedCells);
                insertInCheckedArray(row, column);
                drownShip(row, column, isUser);
            } else {
                System.out.println("You fired the ship!!!");
                successRowIndex = row;
                successColumnIndex = column;
                wasKilled = false;
            }
            return true;
        } else {
            System.out.println("Wrong shoot!!!");
            return false;
        }
    }

    public static boolean drownShip(int row, int column, boolean isUser) {
        char[][] field = myField;
        if (isUser) {
            field = enemyField;
        }
        if (field[getTrueIndex(row, -1)][column] == 'f'
                || field[getTrueIndex(row, -1)][column] == 'k') {
            field[getTrueIndex(row, -1)][column] = 'k';
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == getTrueIndex(row, -1) && checkedCells[i][1] == column) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(getTrueIndex(row, -1), column);
                if (drownShip(getTrueIndex(row, -1), column, isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[getTrueIndex(row, 1)][column] == 'f'
                || field[getTrueIndex(row, 1)][column] == 'k') {
            field[getTrueIndex(row, 1)][column] = 'k';
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == getTrueIndex(row, 1) && checkedCells[i][1] == column) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(getTrueIndex(row, 1), column);
                if (drownShip(getTrueIndex(row, 1), column, isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[row][getTrueIndex(column, -1)] == 'f'
                || field[row][getTrueIndex(column, -1)] == 'k') {
            field[row][getTrueIndex(column, -1)] = 'k';
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == row && checkedCells[i][1] == getTrueIndex(column, -1)) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(row, getTrueIndex(column, -1));
                if (drownShip(row, getTrueIndex(column, -1), isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[row][getTrueIndex(column, 1)] == 'f'
                || field[row][getTrueIndex(column, 1)] == 'k') {
            field[row][getTrueIndex(column, 1)] = 'k';
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == row && checkedCells[i][1] == getTrueIndex(column, 1)) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(row, getTrueIndex(column, 1));
                if (drownShip(row, getTrueIndex(column, 1), isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isShipKilled(int row, int column, boolean isUser) {
        char[][] field = myField;
        if (isUser) {
            field = enemyField;
        }
        if (field[getTrueIndex(row, -1)][column] == 's'
                || field[getTrueIndex(row, 1)][column] == 's'
                || field[row][getTrueIndex(column, -1)] == 's'
                || field[row][getTrueIndex(column, 1)] == 's') {
            return false;
        }
        if (field[getTrueIndex(row, -1)][column] == 'f') {
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == getTrueIndex(row, -1) && checkedCells[i][1] == column) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(getTrueIndex(row, -1), column);
                if (isShipKilled(getTrueIndex(row, -1), column, isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[getTrueIndex(row, 1)][column] == 'f') {
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == getTrueIndex(row, 1) && checkedCells[i][1] == column) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(getTrueIndex(row, 1), column);
                if (isShipKilled(getTrueIndex(row, 1), column, isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[row][getTrueIndex(column, -1)] == 'f') {
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == row && checkedCells[i][1] == getTrueIndex(column, -1)) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(row, getTrueIndex(column, -1));
                if (isShipKilled(row, getTrueIndex(column, -1), isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (field[row][getTrueIndex(column, 1)] == 'f') {
            boolean willCheck = true;
            for (int i = 0; i < checkedCells.length && checkedCells[i][0] != -1; i++) {
                if (checkedCells[i][0] == row && checkedCells[i][1] == getTrueIndex(column, 1)) {
                    willCheck = false;
                    break;
                }
            }
            if (willCheck) {
                insertInCheckedArray(row, getTrueIndex(column, 1));
                if (isShipKilled(row, getTrueIndex(column, 1), isUser)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static void hideAllShips(boolean isUser) {
        for (int i = 0; i < SHIPS.length; i++) {
            while (!hideShip(SHIPS[i], isUser)) {
            }
            if (isUser) {
                showField(myField, MY_FIELD_TITLE, false);
            } else {
                showField(enemyField, ENEMIES_FIELD_TITLE, false); // debug mode so enemies field is not hidden
            }
        }
    }

    public static void insertInCheckedArray(int row, int column) {
        for (int i = 0; i < checkedCells.length; i++) {
            if (checkedCells[i][0] == -1) {
                checkedCells[i][0] = row;
                checkedCells[i][1] = column;
                break;
            }
        }
    }

    public static int[][] fillArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i][0] = -1;
            arr[i][1] = -1;
        }
        return arr;
    }

    public static boolean hideShip(int cells, boolean isUser) {
        if (isUser) {
            System.out.println("\nPlease, hide the " + cells + " cells ship: ");
        }
        int[][] temp = new int[cells][2];
        temp = fillArray(temp);
        for (int i = 0; i < cells; i++) {
            int row, column;
            if (isUser) {
                System.out.println("Enter cell row: ");
                row = sc.nextInt();
                System.out.println("Enter cell column: ");
                column = sc.nextInt();
            } else {
                row = random.nextInt(10);
                column = random.nextInt(10);
            }
            if (temp[0][0] == -1 && validateAdjacent(row, column, isUser)) {
                temp[i][0] = row;
                temp[i][1] = column;
            } else if (validateShipsStructure(temp, row, column, isUser)
                    && validateAdjacent(row, column, isUser)) {
                temp[i][0] = row;
                temp[i][1] = column;
            } else {
                return false;
            }
        }
        System.out.println(Arrays.deepToString(temp));
        for (int i = 0; i < temp.length; i++) {
            if (isUser) {
                myField[temp[i][0]][temp[i][1]] = 's';
            } else {
                enemyField[temp[i][0]][temp[i][1]] = 's';
            }
        }
        return true;
    }

    public static boolean validateShipsStructure(int arr[][], int row, int column, boolean isUser) {
        boolean isStructured = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] == row && arr[i][1] == column) {
                if (isUser) {
                    System.out.println("\nYou already used this cell, try again please!");
                }
                return false;
            }
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] == getTrueIndex(row, -1) && arr[i][1] == column
                    || arr[i][0] == getTrueIndex(row, 1) && arr[i][1] == column
                    || arr[i][1] == getTrueIndex(column, -1) && arr[i][0] == row
                    || arr[i][1] == getTrueIndex(column, 1) && arr[i][0] == row) {
                isStructured = true;
                break;
            } else if (arr[i][0] == -1) {
                if (!isStructured) {
                    if (isUser) {
                        System.out.println("\nYour ship must be structured well, try again please!");
                    }
                }
                return isStructured;
            }
        }
        if (!isStructured && isUser) {
            System.out.println("\nYour ship must be structured well, try again please!");
        }
        return isStructured;
    }

    public static int getTrueIndex(int index, int change) {
        if (index + change < 0) {
            return 0;
        } else if (index + change > 9) {
            return 9;
        } else {
            return index + change;
        }
    }

    public static boolean validateAdjacent(int row, int column, boolean isUser) {
        char[][] field = enemyField;
        if (isUser) {
            field = myField;
        }
        if (field[row][column] == 's') {
            if (isUser) {
                System.out.println("\nCell is not free, try again please!");
            }
            return false;
        }
        if (field[getTrueIndex(row, -1)][column] == 's'
                || field[getTrueIndex(row, -1)][getTrueIndex(column, -1)] == 's'
                || field[getTrueIndex(row, -1)][getTrueIndex(column, 1)] == 's'
                || field[row][getTrueIndex(column, -1)] == 's'
                || field[row][getTrueIndex(column, 1)] == 's'
                || field[getTrueIndex(row, 1)][column] == 's'
                || field[getTrueIndex(row, 1)][getTrueIndex(column, -1)] == 's'
                || field[getTrueIndex(row, 1)][getTrueIndex(column, 1)] == 's') {
            if (isUser) {
                System.out.println("\nYour ship can not be adjacent with placed, try again please!");
            }
            return false;
        }
        return true;
    }

    public static void showField(char[][] field, String title, boolean isHidden) {
        System.out.println("\t\t" + title);
        System.out.print(" ");
        for (int i = 0; i < field.length; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.print("\n");
        for (int i = 0; i < field.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == 0) { // '0' == 0
                    System.out.print("*  ");
                } else if (isHidden && field[i][j] == 's') {
                    System.out.print("*  ");
                } else {
                    System.out.print(field[i][j] + "  ");
                }
            }
            System.out.println();
        }
    }
}