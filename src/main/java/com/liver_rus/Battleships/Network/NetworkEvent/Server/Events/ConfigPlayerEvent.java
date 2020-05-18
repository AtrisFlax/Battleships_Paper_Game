package com.liver_rus.Battleships.Network.NetworkEvent.Server.Events;

import com.liver_rus.Battleships.Network.NetworkEvent.Client.Events.*;
import com.liver_rus.Battleships.Network.NetworkEvent.NetworkCommandConstant;
import com.liver_rus.Battleships.Network.NetworkEvent.PlayerType;
import com.liver_rus.Battleships.Network.NetworkEvent.Server.Answer;
import com.liver_rus.Battleships.Network.NetworkEvent.Server.ServerNetworkEvent;
import com.liver_rus.Battleships.Network.Server.GamePrimitives.GameField;
import com.liver_rus.Battleships.Network.Server.GamePrimitives.Ship;
import com.liver_rus.Battleships.Network.Server.GamePrimitives.TryingAddTooManyShipsOnFieldException;
import com.liver_rus.Battleships.Network.Server.MetaInfo;
import com.liver_rus.Battleships.Network.Server.Player;
import com.liver_rus.Battleships.Network.Server.TurnOrder;

import static com.liver_rus.Battleships.Network.NetworkEvent.NetworkCommandConstant.*;
import static com.liver_rus.Battleships.utils.Debug.DEBUG_AUTO_DEPLOY;


public class ConfigPlayerEvent implements ServerNetworkEvent {
    private final boolean saveShooting;
    private final String name;

    public ConfigPlayerEvent(boolean saveShooting, String name) {
        this.saveShooting = saveShooting;
        this.name = name;
    }

    @Override
    public Answer proceed(MetaInfo metaInfo) {
        Answer answer = new Answer();
        Player activePlayer = metaInfo.getActivePlayer();
        if (!metaInfo.isPlayersInGame()) {
            activePlayer.setName(name);
            activePlayer.setSaveShooting(saveShooting);
            activePlayer.setReadyForDeployment(true);
        }
        if (metaInfo.isPlayersReadyForDeployment()) {
            //TODO delete debug
            if (DEBUG_AUTO_DEPLOY) {
                Player passivePlayer = metaInfo.getPassivePlayer();
                answer.add(activePlayer, new SetEnemyNameNetworkEvent(passivePlayer.getName()));
                answer.add(passivePlayer, new SetEnemyNameNetworkEvent(activePlayer.getName()));
                GameField activePlayerField = activePlayer.getGameField();
                GameField passivePlayerField = passivePlayer.getGameField();
                try {
                    addShipsPreset1(activePlayerField);
                    addShipsPreset2(passivePlayerField);
                } catch (TryingAddTooManyShipsOnFieldException e) {
                    e.printStackTrace();
                }
                for (Ship ship : activePlayerField.getShips()) {
                    answer.add(activePlayer, new DrawShipNetworkEvent(
                            ship.getX(), ship.getY(), ship.getType(), ship.isHorizontal(), PlayerType.YOU)
                    );
                }
                for (Ship ship : passivePlayerField.getShips()) {
                    answer.add(passivePlayer, new DrawShipNetworkEvent(
                            ship.getX(), ship.getY(), ship.getType(), ship.isHorizontal(), PlayerType.YOU)
                    );
                }
                activePlayer.setReadyForGame(true);
                passivePlayer.setReadyForGame(true);
                activePlayer.setReadyForDeployment(false);
                passivePlayer.setReadyForDeployment(false);
                metaInfo.setInitTurnOrder(TurnOrder.FIRST_CONNECTED);
                metaInfo.setTurnHolder();
                answer.add(metaInfo.getTurnHolderPlayer(), new CanShootNetworkEvent());
            } else {
                Player passivePlayer = metaInfo.getPassivePlayer();
                answer.add(activePlayer, new SetEnemyNameNetworkEvent(passivePlayer.getName()));
                answer.add(passivePlayer, new SetEnemyNameNetworkEvent(activePlayer.getName()));
                GameField activePlayerField = activePlayer.getGameField();
                GameField passivePlayerField = passivePlayer.getGameField();
                answer.add(activePlayer, new DeployNetworkEvent(activePlayerField.getShipsLeftByTypeForDeploy()));
                answer.add(passivePlayer, new DeployNetworkEvent(passivePlayerField.getShipsLeftByTypeForDeploy()));
            }
        } else {
            answer.add(activePlayer, new WaitingSecondPlayerNetworkEvent("Deployment"));
        }
        return answer;
    }

    public String convertToString() {
        return NetworkCommandConstant.CONFIG_PLAYER + (saveShooting ? ON : OFF) + NAME + name;
    }

    private void addShipsPreset1(GameField field) throws TryingAddTooManyShipsOnFieldException {
        field.addShip(1, 8, 0, true);
        field.addShip(3, 2, 0, true);
        field.addShip(1, 1, 1, false);
        field.addShip(3, 4, 1, true);
        field.addShip(2, 6, 2, true);
        field.addShip(7, 4, 3, false);
        field.addShip(9, 1, 4, false);
    }

    private void addShipsPreset2(GameField field) throws TryingAddTooManyShipsOnFieldException {
        field.addShip(6, 2, 3, false);
        field.addShip(2, 3, 2, false);
        field.addShip(1, 0, 4, true);
        field.addShip(4, 7, 1, true);
        field.addShip(8, 4, 1, false);
        field.addShip(1, 8, 0, false);
        field.addShip(7, 8, 0, false);
    }
}
