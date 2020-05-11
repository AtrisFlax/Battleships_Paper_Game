package com.liver_rus.Battleships.Network.Server;

import com.liver_rus.Battleships.Network.Server.GamePrimitives.GameField;

import java.nio.channels.SocketChannel;

public class Player {
    private SocketChannel channel;
    private final GameField gameField;
    private boolean readyForDeployment;
    private boolean readyForGame;
    private String name;
    private boolean saveShooting;

    //tri state enum or Boolean possible
    private boolean wantRematch;
    private boolean rematchHasSet;

    public Player(SocketChannel channel, GameField gameField) {
        this.channel = channel;
        this.gameField = gameField;
        this.name = "Player";
        this.readyForDeployment = false;
        this.readyForGame = false;
        this.saveShooting = false;
        this.wantRematch = false;
        this.rematchHasSet = false;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public GameField getGameField() {
        return gameField;
    }

    public boolean isReadyForDeployment() {
        return readyForDeployment;
    }

    public void setReadyForDeployment(boolean readyForDeployment) {
        this.readyForDeployment = readyForDeployment;
    }

    public boolean isReadyForGame() {
        return readyForGame;
    }

    public void setReadyForGame(boolean readyForGame) {
        if (readyForGame) {
            readyForDeployment = false;
        }
        this.readyForGame = readyForGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWantRematch() {
        return wantRematch && rematchHasSet;
    }

    public void setWantRematch(boolean wantRematch) {
        this.wantRematch = wantRematch;
        setRematchHasSet(true);
    }

    public boolean isRematchHasSet() {
        return rematchHasSet;
    }

    public void setRematchHasSet(boolean rematchHasSet) {
        this.rematchHasSet = rematchHasSet;
    }

    public boolean isSaveShooting() {
        return saveShooting;
    }

    public void setSaveShooting(boolean saveShooting) {
        this.saveShooting = saveShooting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (readyForGame != player.readyForGame) return false;
        if (!channel.equals(player.channel)) return false;
        if (!gameField.equals(player.gameField)) return false;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        int result = channel.hashCode();
        result = 31 * result + gameField.hashCode();
        result = 31 * result + (readyForGame ? 1 : 0);
        result = 31 * result + name.hashCode();
        return result;
    }
}