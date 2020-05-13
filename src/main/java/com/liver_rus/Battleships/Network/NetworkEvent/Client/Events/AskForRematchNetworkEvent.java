package com.liver_rus.Battleships.Network.NetworkEvent.Client.Events;

import com.liver_rus.Battleships.Client.GUI.GUIActions;
import com.liver_rus.Battleships.Network.NetworkEvent.Client.ClientNetworkEvent;
import com.liver_rus.Battleships.Network.NetworkEvent.NetworkCommandConstant;

public class AskForRematchNetworkEvent implements ClientNetworkEvent {

    @Override
    public String proceed(GUIActions action) {
        action.askRematch();
        return null;
    }

    @Override
    public String convertToString() {
        return NetworkCommandConstant.ASK_REMATCH;
    }
}