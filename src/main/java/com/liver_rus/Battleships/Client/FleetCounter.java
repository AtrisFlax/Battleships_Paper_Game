package com.liver_rus.Battleships.Client;

import java.util.Arrays;

/**
 * Класс FleetCounter отвечающий за подсчет количества оставшихся кораблей
 */

class FleetCounter {
    static final int NUM_MAX_SHIPS = Arrays.stream(shipsBuilder()).sum();
    private int[] ships;
    private int left;

    FleetCounter() {
        ships = shipsBuilder();
        left = NUM_MAX_SHIPS;
    }

    private static int[] shipsBuilder(){
        return new int[]{2, 2, 1, 1, 1};
    }

    int getShipsLeft() {
        return left;
    }

    //return -1 if have no more ships type
    int popShip(int type) {
        if (ships[type] > 0) {
            --left;
            ships[type] = ships[type] - 1;
            return ships[type];
        } else {
            return -1;
        }
    }

    int popShip(Ship.Type shipType) {
        int type = Ship.Type.shipTypeToInt(shipType);
        return popShip(type);
    }
    
}