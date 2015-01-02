package core;

// Java Imports
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Other Imports
import dataAccessLayer.BadgeDataDAO;
import model.Player;
import model.World;
import utility.Log;

public class LobbyManager {

    // Singleton Instance
    public static LobbyManager manager;
    // Reference Tables
    private Map<String, Lobby> lobbyList = new HashMap<String, Lobby>();

    public LobbyManager() {
    }

    public static LobbyManager getInstance() {
        if (manager == null) {
            manager = new LobbyManager();
        }

        return manager;
    }

    public Lobby add(Lobby lobby) {
        return lobbyList.put(lobby.getID(), lobby);
    }

    public Lobby get(String lobby_id) {
        return lobbyList.get(lobby_id);
    }

    public Lobby remove(Lobby lobby) {
        lobby.destroy();
        return lobbyList.remove(lobby.getID());
    }

    public List<GameClient> getClients(String lobby_id) {
        List<GameClient> clients = new ArrayList<GameClient>();

        for (GameClient client : GameServer.getInstance().getActiveThreads().values()) {
            if (client.getActiveObject(Lobby.class) != null) {
                if (client.getActiveObject(Lobby.class).getID().equals(lobby_id)) {
                    clients.add(client);
                }
            }
        }

        return clients;
    }

    public Lobby getLobby(World world) {
        Lobby lobby = new Lobby(null);
        lobby.setWorld(world);

        Comparator<Lobby> comparator = new Comparator<Lobby>() {
            @Override
            public int compare(Lobby l1, Lobby l2) {
                return Integer.valueOf(l1.getWorld() != null ? l1.getWorld().getID() : 0).compareTo(l2.getWorld() != null ? l2.getWorld().getID() : 0);
            }
        };

        List<Lobby> lobbys = new ArrayList<Lobby>(lobbyList.values());
        Collections.sort(lobbys, comparator);

        int index = Collections.binarySearch(lobbys, lobby, comparator);
        return index >= 0 ? lobbys.get(index) : null;
    }

    public Lobby createLobby(Player player) {
        Lobby lobby = new Lobby(GameServer.createUniqueID());
        lobby.add(player);
        lobby.setHostID(player.getID());

        add(lobby);

        return lobby;
    }

    public void closeLobby(Lobby lobby) {
        List<Player> userList = new ArrayList<Player>(lobby.getUserList().values());

        for (Player user : userList) {
            lobby.remove(user.getID());
        }

        remove(lobby);
    }

    public boolean addUser(Lobby lobby, Player user) {
        return lobby.add(user);
    }

    public void removeUser(Lobby lobby, int user_id) {
        lobby.remove(user_id);

        World world = lobby.getWorld();
        if (world != null) {
            world.removePlayer(user_id);
        }

        List<Player> userList = new ArrayList<Player>(lobby.getUserList().values());

        if (!userList.isEmpty()) {
            if (user_id == lobby.getHostID()) {
                int index = new Random().nextInt(userList.size());
                lobby.setHostID(userList.get(index).getID());
            }
        } else {
            remove(lobby);
        }
    }
}
