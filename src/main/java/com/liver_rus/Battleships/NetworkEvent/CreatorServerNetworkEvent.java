package com.liver_rus.Battleships.NetworkEvent;

import com.liver_rus.Battleships.NetworkEvent.Server.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.liver_rus.Battleships.NetworkEvent.NetworkCommandConstant.*;

//Common deserialize class
public class CreatorServerNetworkEvent {
    private final Pattern myNamePattern;
    private final Pattern tryDeployShipPattern;
    private final Pattern shotPattern;
    private final Pattern disconnectPattern;
    private final Pattern noTryRematchPattern;
    private final Pattern tryRematchPattern;
    private final Pattern resetFleetWhileDeployPattern;

    public CreatorServerNetworkEvent() {
        myNamePattern = Pattern.compile("^" + MY_NAME + "(.+)");
        tryDeployShipPattern = Pattern.compile("^" + TRY_DEPLOY_SHIP + "(\\d)(\\d)(\\d)([VH])" + "$");
        shotPattern = Pattern.compile("^" + SHOT + "(\\d)(\\d)" + "$");
        disconnectPattern = Pattern.compile("^" + DISCONNECT + "$");
        noTryRematchPattern = Pattern.compile("^" + NO_REMATCH + "$");
        tryRematchPattern = Pattern.compile("^" + TRY_REMATCH + "$");
        resetFleetWhileDeployPattern = Pattern.compile("^" + RESET_FLEET_WHILE_DEPLOY + "$");
    }

    public NetworkServerEvent deserializeMessage(String msg) {
        Matcher matcher = myNamePattern.matcher(msg);
        if (matcher.find()) {
            return new NetworkMyNameEvent(matcher.group(1));
        }

        matcher = tryDeployShipPattern.matcher(msg);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int type = Integer.parseInt(matcher.group(3));
            boolean isHorizontal = matcher.group(4).equals("H");
            return new NetworkTryDeployShipEvent(x, y, type, isHorizontal);
        }

        matcher = shotPattern.matcher(msg);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            return new NetworkShotEvent(x, y);
        }

        matcher = disconnectPattern.matcher(msg);
        if (matcher.find()) {
            return new NetworkDisconnectEvent();
        }

        matcher = noTryRematchPattern.matcher(msg);
        if (matcher.find()) {
            return new NetworkNoRematchEvent();
        }

        matcher = tryRematchPattern.matcher(msg);
        if (matcher.find()) {
            return new NetworkTryRematchEvent();
        }

        matcher = resetFleetWhileDeployPattern.matcher(msg);
        if (matcher.find()) {
            return new NetworkResetFleetWhileDeployEvent();
        }
        return new NetworkUnknownCommandServerEvent(msg);
    }
}
