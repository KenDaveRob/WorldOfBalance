package networking.request;

// Java Imports
import core.Lobby;
import core.LobbyManager;
import core.WorldManager;
import dataAccessLayer.EnvironmentDAO;
import dataAccessLayer.WorldDAO;
import java.io.IOException;

// Other Imports
import metadata.Constants;
import model.Environment;
import model.World;
import networking.response.ResponseWorldMenuAction;
import simulationEngine.SimulationEngine;
import utility.DataReader;
import worldManager.gameEngine.Zone;

public class RequestWorldMenuAction extends GameRequest {
    // Data
    private World world;
    private short action;
    private int world_id;

    public RequestWorldMenuAction() {
        world = new World(-1);
    }

    @Override
    public void parse() throws IOException {
        action = DataReader.readShort(dataInput);

        switch (action) {
            case 1: // Create
                parseCreateAction();
                break;
            case 2: // Join
                parseJoinAction();
                break;
            case 3: // Delete
                parseDeleteAction();
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness() throws Exception {
        switch (action) {
            case 1: // Create
                doBusinessCreateAction();
                break;
            case 2: // Join
                doBusinessJoinAction();
                break;
            case 3: // Delete
                doBusinessDeleteAction();
                break;
            default:
                break;
        }
    }

    private void parseCreateAction() throws IOException {
        world.setGameMode(DataReader.readShort(dataInput));

        String name = DataReader.readString(dataInput);
        if (name.isEmpty()) {
            name = client.getPlayer().getUsername() + "'s World " + System.currentTimeMillis() % 100;
        }
        world.setGameName(name);

        world.setMaxPlayers(DataReader.readShort(dataInput));
        world.setEnvType(DataReader.readString(dataInput));
        world.setAccessType(DataReader.readShort(dataInput));

        if (world.getAccessType() == Constants.PRIVACY_TYPE_PRIVATE) {
            world.setPassword(DataReader.readString(dataInput));
        }

        world.setCreatorID(client.getPlayer().getID());
    }

    private void doBusinessCreateAction() throws Exception {
        if (world.getGameMode() == 0) {
            world.setCredits(Constants.INITIAL_CREDITS);
        } else {
            world.setCredits(Constants.INITIAL_CREDITS);
        }

        WorldManager.createWorld(world);
        client.putActiveObject(World.class, world);

        ResponseWorldMenuAction responseWorldMenuAction = new ResponseWorldMenuAction();
        responseWorldMenuAction.setAction((short) 1);
        responseWorldMenuAction.setStatus((short) 0);
        responseWorldMenuAction.setWorld(world);
        responses.add(responseWorldMenuAction);
    }

    private void parseJoinAction() throws IOException {
        world_id = DataReader.readInt(dataInput);
    }

    private void doBusinessJoinAction() throws Exception {
        Lobby lobby;
        // Check if World is already running
        world = WorldManager.getInstance().get(world_id);

        if (world != null) {
            lobby = LobbyManager.getInstance().getLobby(world);

            if (lobby != null) {
                // Remove user from existing Lobby
                Lobby userLobby = client.getActiveObject(Lobby.class);
                if (userLobby != null && !userLobby.getID().equals(lobby.getID())) {
                    LobbyManager.getInstance().removeUser(userLobby, client.getPlayer().getID());
                    client.removeActiveObject(Lobby.class);
                } else {
                    // Same Lobby
                    return;
                }
            }
        } else {
            world = WorldManager.getInstance().createExistingWorld(world_id);

            if (world != null) {
                lobby = LobbyManager.getInstance().createLobby(client.getPlayer());
                lobby.setWorld(world);
            } else {
                // World Unavailable
                return;
            }
        }

        if (lobby != null) {
            if (lobby.getSize() < lobby.getCapacity()) {
                LobbyManager.getInstance().addUser(lobby, client.getPlayer());
                client.putActiveObject(Lobby.class, lobby);

                WorldManager.enterWorld(world, client);
                WorldManager.initializeEnvironment(world, client);

                lobby.getObjectiveManager().getNewObjective();
            } else {
                ResponseWorldMenuAction responseWorldMenuAction = new ResponseWorldMenuAction();
                responseWorldMenuAction.setAction((short) 2);
                responseWorldMenuAction.setStatus((short) 1); // Lobby Full
                responses.add(responseWorldMenuAction);
            }
        }
    }

    private void parseDeleteAction() throws IOException {
        world_id = DataReader.readInt(dataInput);
    }

    private void doBusinessDeleteAction() throws Exception {
        Environment env = EnvironmentDAO.getEnvironmentByWorldID(world_id);

        if (env != null) {
            SimulationEngine simulationEngine = new SimulationEngine();

            for (Zone zone : env.getZones()) {
                simulationEngine.deleteManipulation(zone.getManipulationID());
            }

            WorldDAO.removeWorld(world_id);

            ResponseWorldMenuAction responseWorldMenuAction = new ResponseWorldMenuAction();
            responseWorldMenuAction.setAction((short) 3);
            responseWorldMenuAction.setStatus((short) 0);
            responseWorldMenuAction.setWorldID(world_id);
            responses.add(responseWorldMenuAction);
        } else {
            ResponseWorldMenuAction responseWorldMenuAction = new ResponseWorldMenuAction();
            responseWorldMenuAction.setAction((short) 3);
            responseWorldMenuAction.setStatus((short) 1);
            responses.add(responseWorldMenuAction);
        }
    }
}
