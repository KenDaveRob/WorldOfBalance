package core;

// Java Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

// Other Imports
import metadata.Constants;
import networking.response.ResponseObjectiveAction;
import utility.GameTimer;

public class ObjectiveManager {

    private Lobby lobby;
    private EventHandler eventHandler;
    private Map<Integer, Objective> objectiveTypes;
    private List<Objective> activeObjectives;
    private GameTimer nextObjectiveTimer;

    public ObjectiveManager(Lobby lobby, EventHandler eventHandler) {
        this.lobby = lobby;
        this.eventHandler = eventHandler;

        objectiveTypes = new HashMap<Integer, Objective>();
        activeObjectives = new ArrayList<Objective>();
        nextObjectiveTimer = new GameTimer();

        initialize();
    }

    public final void initialize() {
        Objective objective;
        int objective_id = 1;

        objective = create(objective_id++, "Spend 100 Credits", 100, "Credits");
        objective.setReward(Constants.RESOURCE_CREDITS, 150);
        objective.setEvent(EventTypes.CREDITS_SPENT);
        this.add(objective);

        objective = create(objective_id++, "Spend 250 Credits", 250, "Credits");
        objective.setReward(Constants.RESOURCE_CREDITS, 300);
        objective.setEvent(EventTypes.CREDITS_SPENT);
        this.add(objective);

        objective = create(objective_id++, "Spend 500 Credits", 500, "Credits");
        objective.setReward(Constants.RESOURCE_CREDITS, 600);
        objective.setEvent(EventTypes.CREDITS_SPENT);
        this.add(objective);

        objective = create(objective_id++, "Spend 750 Credits", 750, "Credits");
        objective.setReward(Constants.RESOURCE_CREDITS, 1000);
        objective.setEvent(EventTypes.CREDITS_SPENT);
        this.add(objective);

        objective = create(objective_id++, "Spend 1000 Credits", 1000, "Credits");
        objective.setReward(Constants.RESOURCE_CREDITS, 1500);
        objective.setEvent(EventTypes.CREDITS_SPENT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 1 Species", 1, "Species");
        objective.setReward(Constants.RESOURCE_CREDITS, 50);
        objective.setEvent(EventTypes.SPECIES_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 3 Species", 3, "Species");
        objective.setReward(Constants.RESOURCE_CREDITS, 100);
        objective.setEvent(EventTypes.SPECIES_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 5 Species", 5, "Species");
        objective.setReward(Constants.RESOURCE_CREDITS, 200);
        objective.setEvent(EventTypes.SPECIES_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 10 Species", 10, "Species");
        objective.setReward(Constants.RESOURCE_CREDITS, 500);
        objective.setEvent(EventTypes.SPECIES_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 500 Biomass", 500, "Biomass");
        objective.setReward(Constants.RESOURCE_CREDITS, 50);
        objective.setEvent(EventTypes.BIOMASS_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 1000 Biomass", 1000, "Biomass");
        objective.setReward(Constants.RESOURCE_CREDITS, 125);
        objective.setEvent(EventTypes.BIOMASS_BOUGHT);
        this.add(objective);

        objective = create(objective_id++, "Purchase 2500 Biomass", 2500, "Biomass");
        objective.setReward(Constants.RESOURCE_CREDITS, 300);
        objective.setEvent(EventTypes.BIOMASS_BOUGHT);
        this.add(objective);
    }

    public Objective create(int objective_id, String name, int target) {
        return create(objective_id, name, target, "", null);
    }

    public Objective create(int objective_id, String name, int target, String unit) {
        return create(objective_id, name, target, unit, null);
    }

    public Objective create(int objective_id, String name, int target, String unit, EventListener eventListener) {
        final Objective objective = new Objective(objective_id, name, target, unit);

        if (eventListener == null) {
            eventListener = new EventListener() {
                @Override
                public void run(Object... args) {
                    objective.setAmount(Math.min(objective.getTarget(), objective.getAmount() + (Integer) args[0]));
                    update(objective);
                }
            };
        }

        objective.setEventListener(eventListener);

        return objective;
    }

    public void update(Objective objective) {
        ResponseObjectiveAction response = new ResponseObjectiveAction();
        response.setAction((short) 1);
        response.setObjective(objective);
        NetworkManager.addResponseForLobby(lobby.getID(), response);

        if (objective.getAmount() == objective.getTarget()) {
            isComplete(objective);
        }
    }

    public Objective add(Objective objective) {
        objectiveTypes.put(objective.getID(), objective);
        return objective;
    }

    public Objective getRandomObjective() {
        List<Objective> objectives = new ArrayList<Objective>(objectiveTypes.values());
        return objectives.get(new Random().nextInt(objectives.size()));
    }

    public void getNewObjective() {
        Objective objective = getRandomObjective();
        EventListener eventListener = objective.getEventListener();

        for (EventTypes event : objective.getEvents()) {
            eventHandler.add(event, eventListener);
        }

        activeObjectives.add(objective);

        ResponseObjectiveAction response = new ResponseObjectiveAction();
        response.setAction((short) 0);
        response.setObjective(objective);
        NetworkManager.addResponseForLobby(lobby.getID(), response);
    }

    public void isComplete(Objective objective) {
        activeObjectives.remove(objective);

        for (EventTypes event : objective.getEvents()) {
            eventHandler.remove(event, objective.getEventListener());
        }

        for (Entry<Short, Integer> entry : objective.getRewards().entrySet()) {
            switch (entry.getKey()) {
                case Constants.RESOURCE_CREDITS:
                    GameResources.updateCredits(lobby.getWorld(), entry.getValue());
                    break;
            }
        }

        startNextObjectiveTimer();
    }

    public void startNextObjectiveTimer() {
        nextObjectiveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getNewObjective();
            }
        }, 60000);
    }

    public boolean stopNextObjectiveTimer() {
        return nextObjectiveTimer.end();
    }
}
