package pl.memexurer.limbobimbo.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class AuthenticationData {
    private final ArrayList<String> authenticatedPlayers = new ArrayList<>();

    public void authenticatePlayer(String playerName) {
        this.authenticatedPlayers.add(playerName);
    }

    public void deauthenticate(Player player) {
        authenticatedPlayers.add(player.getName());
    }

    public boolean isAuthenticated(Player player) {
        return authenticatedPlayers.contains(player.getName());
    }
}
