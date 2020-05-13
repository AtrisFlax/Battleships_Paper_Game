package com.liver_rus.Battleships.Network.NetworkEvent.Server.Events;

import com.liver_rus.Battleships.Network.NetworkEvent.NetworkCommandConstant;
import com.liver_rus.Battleships.Network.NetworkEvent.Server.Answer;
import com.liver_rus.Battleships.Network.NetworkEvent.Server.ServerNetworkEvent;
import com.liver_rus.Battleships.Network.Server.MetaInfo;

public class UnknownCommandServerNetworkEvent implements ServerNetworkEvent {
    private final String unknownMsg;

    public UnknownCommandServerNetworkEvent(String unknownMsg) {
        this.unknownMsg = unknownMsg;
    }

    @Override
    public Answer proceed(MetaInfo metaInfo) {
        return null;
    }

    @Override
    public String convertToString() {
        return NetworkCommandConstant.UNKNOWN_COMMAND + ":" +  unknownMsg;
    }
}