package com.liver_rus.Battleships.Network.Server;

import com.liver_rus.Battleships.Network.Server.GamePrimitives.GameField;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO extract interface

//class binding connection info (SocketChannel) and game primitives (GameField, ready flag)
//after accepting ships message fields should be swapped
public class MetaInfo {

    private int numAcceptedConnections;
    private static final int FIRST_CONNECTED_PLAYER_ID = 0;
    private static final int SECOND_CONNECTED_PLAYER_ID = 1;
    private final static int MAX_CONNECTIONS = 2;

    List<Player> players;

    private TurnOrder initTurnOrder;

    private boolean isGameStarted;
    private Player activePlayer; //msg from this one
    private Player turnHolderPlayer; //wait action from this one

    GameField[] injectedGameFields;

    /**
     * @param gameFields injected game fields. Max size 2
     */
    private MetaInfo(GameField[] gameFields) {
        if (gameFields.length != 2) throw new IllegalArgumentException("Injected fields should be fields.length == 2");
        injectedGameFields = gameFields;
        numAcceptedConnections = 0;
        initTurnOrder = TurnOrder.RANDOM_TURN;
        players = new ArrayList<>(MAX_CONNECTIONS);
        activePlayer = null;
    }

    public static MetaInfo create(GameField[] injectedGameFields) {
        if (injectedGameFields == null) {
            GameField[] fields = new GameField[MAX_CONNECTIONS];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = new GameField();
            }
            return new MetaInfo(fields);
        } else {
            if (injectedGameFields.length != MAX_CONNECTIONS) {
                throw new IllegalArgumentException("Illegal length injected game fields. Legal length equal 2");
            }
            return new MetaInfo(injectedGameFields);
        }
    }

    private Player getFirstConnectedPlayerChannel() {
        return players.get(FIRST_CONNECTED_PLAYER_ID);
    }

    private Player getSecondConnectedPlayerChannel() {
        return players.get(SECOND_CONNECTED_PLAYER_ID);
    }

    private Player randomConnection() {
        int randID = new Random(System.currentTimeMillis()).nextInt(MAX_CONNECTIONS);
        return players.get(randID);
    }

    public static int getMaxConnections() {
        return MAX_CONNECTIONS;
    }

    public void setTurnHolder() {
        switch (initTurnOrder) {
            case FIRST_CONNECTED:
                turnHolderPlayer = getFirstConnectedPlayerChannel();
                break;
            case SECOND_CONNECTED:
                turnHolderPlayer = getSecondConnectedPlayerChannel();
                break;
            case RANDOM_TURN:
                turnHolderPlayer = randomConnection();
                break;
        }
    }

    public void addPlayer(SocketChannel channel) {
        if (numAcceptedConnections < MAX_CONNECTIONS) {
            Player newPlayer = new Player(channel, injectedGameFields[numAcceptedConnections]);
            players.add(newPlayer);
            numAcceptedConnections++;
        }
    }

    public void setInitTurnOrder(TurnOrder initTurnOrder) {
        this.initTurnOrder = initTurnOrder;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public void setActivePlayer(SocketChannel channel) {
        for (Player player : players) {
            if (player.getChannel() == channel) {
                this.activePlayer = player;
            }
        }
    }


    //active is whose message was received
    public Player getActivePlayer() {
        return activePlayer;
    }

    //passive is whose message was not received (an another player)
    public Player getPassivePlayer() {
        return getOtherPlayer(activePlayer);
    }

    public void setTurnHolderPlayer(Player turnHolderPlayer) {
        this.turnHolderPlayer = turnHolderPlayer;
    }

    //turnholder from whom the server is waiting for actions
    public Player getTurnHolderPlayer() {
        return turnHolderPlayer;
    }

    //notTurnholder from whom the server is NOT waiting for actions
    public Player getNotTurnHolderPlayer() {
        return getOtherPlayer(turnHolderPlayer);
    }

    private Player getOtherPlayer(Player turnHolderPlayer) {
        if (turnHolderPlayer.equals(players.get(FIRST_CONNECTED_PLAYER_ID))) {
            return players.get(SECOND_CONNECTED_PLAYER_ID);
        } else {
            return players.get(FIRST_CONNECTED_PLAYER_ID);
        }
    }

    public int getNumAcceptedConnections() {
        return numAcceptedConnections;
    }


    public GameField[] getGameFields() {
        return injectedGameFields;
    }

    public void setGameFields(GameField[] injectedGameFields) {
        this.injectedGameFields = injectedGameFields;
    }

    public boolean isPlayersReadyForDeployment() {
        if (players.size() != MAX_CONNECTIONS) {
            return false;
        }
        for (Player player : players) {
            if (!player.isReadyForDeployment()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPlayersReadyForGame() {
        for (Player player : players) {
            if (!player.isReadyForGame()) {
                return false;
            }
        }
        return true;
    }

    public void setGameEnded() {
        for (Player player : players) {
            player.setReadyForGame(false);
        }

    }

    public boolean isGameEnded() {
        for (Player player : players) {
            if (player.isReadyForGame()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPlayersWantReamatch() {
        for (Player player : players) {
            if (!player.isWantRematch()) {
                return false;
            }
        }
        return true;
    }

    public void resetForRematch() {
        for (GameField field : injectedGameFields) {
            field.reset();
        }
        initTurnOrder = TurnOrder.RANDOM_TURN;
    }
}