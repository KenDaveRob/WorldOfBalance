package core;

// Java Imports
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

// Other Imports
import dataAccessLayer.BadgeDataDAO;
import dataAccessLayer.EnvironmentDAO;
import model.World;
import networking.response.ResponseBadgeList;
import networking.response.ResponseBadgeUpdate;
import utility.GameTimer;
import utility.Log;

public class BadgeManager {

    // Environment Score Max Threshold
    public static int maxScoreThreshold;
    public static GameTimer badgeScoreTimer = new GameTimer();
    // Other
    private int player_id;
    private EventHandler eventHandler;
    private Map<Integer, Badge> badgeList = new HashMap<Integer, Badge>();
    private int currentScoreThreshold;

    public BadgeManager(int player_id, EventHandler eventHandler) {
        this.player_id = player_id;
        this.eventHandler = eventHandler;
        this.currentScoreThreshold = maxScoreThreshold;

        load();
    }

    public static void setBadgeScores() {
        int threshold = 0;

        try {
            List<Integer> scoreList = EnvironmentDAO.getEnvironmentScores();

            if (scoreList.size() >= 10) {
                // Create 10 Bins
                int numBins = 10;
                List<Integer[]> binList = new ArrayList<Integer[]>(numBins);
                int interval = scoreList.get(0) / numBins;
                // Initialize Bins
                for (int i = 0; i < numBins; i++) {
                    binList.add(new Integer[]{i * interval, 0});
                }
                // Determine Frequencies
                for (int score : scoreList) {
                    binList.get(score / (interval + 1))[1]++;
                }
                // Sort Frequecies by Descending Order
                Collections.sort(binList, new Comparator<Integer[]>() {
                    @Override
                    public int compare(Integer[] t, Integer[] t1) {
                        int value = t1[1].compareTo(t[1]);
                        return value == 0 ? t1[0].compareTo(t[0]) : value;
                    }
                });
                // Get Most Frequent Score
                threshold = binList.get(0)[0] + interval;
                // Update Every 30 Minutes
                badgeScoreTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setBadgeScores();
                    }
                }, 1800000, 1800000);
            }
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }

        maxScoreThreshold = Math.max(50000, threshold);
    }

    public final void load() {
        float threshold = currentScoreThreshold;
        int months;

        Badge badge;
        int badge_id = 1;
        Objective objective;

        badge = new Badge(badge_id++, "Eagle");
        threshold *= 0.95f;
        months = 1;
        objective = create(badge, badge.getID(), "Maintain at least " + (int) Math.round(threshold) + " Env Score for " + months + " months in a row", (int) Math.round(threshold), months);
        badge.setObjective(objective);
        this.add(badge);

        badge = new Badge(badge_id++, "Elephant");
        threshold *= 0.85f;
        months = 4;
        objective = create(badge, badge.getID(), "Maintain at least " + (int) Math.round(threshold) + " Env Score for " + months + " months in a row", (int) Math.round(threshold), months);
        badge.setObjective(objective);
        this.add(badge);
    }

    public final void initialize(List<Badge> badgeList) {
        for (Badge badge : badgeList) {
            Badge temp = this.badgeList.get(badge.getID());
            temp.setAmount(badge.getAmount());
            temp.setProgress(badge.getProgess());
        }
    }

    public Map<Integer, Badge> getBadgeList() {
        return badgeList;
    }

    public void add(Badge badge) {
        if (eventHandler != null) {
            eventHandler.add(EventTypes.NEW_MONTH, badge.getObjective().getEventListener());
        }

        badgeList.put(badge.getID(), badge);
    }

    public Objective create(final Badge badge, int objective_id, String name, int threshold, int target) {
        final Objective objective = new Objective(objective_id, name, target, "");
        objective.setValue("Threshold", threshold);

        EventListener eventListener = new EventListener() {
            @Override
            public void run(Object... args) {
                World world = (World) args[0];

                if (world.getEnvironment().getEnvironmentScore() >= objective.getValue("Threshold", Integer.class)) {
                    objective.setAmount(objective.getAmount() + 1);
                } else {
                    objective.setAmount(0);
                }

                update(badge, objective);
            }
        };

        objective.setEventListener(eventListener);

        return objective;
    }

    public void update(Badge badge, Objective objective) {
        // Sync Badge Progress
        ResponseBadgeUpdate response = new ResponseBadgeUpdate();
        response.setBadgeID(badge.getID());
        response.setProgress(objective.getAmount());
        response.setDone(objective.isDone());
        // Badge Complete
        if (objective.getAmount() == objective.getTarget()) {
            badge.setAmount(badge.getAmount() + 1);
            objective.setAmount(0);
            // Update Threshold
            currentScoreThreshold = maxScoreThreshold;

            float threshold = 0;

            if (badge.getName().equals("Eagle")) {
                threshold = currentScoreThreshold * 0.95f;
            } else if (badge.getName().equals("Elephant")) {
                threshold = currentScoreThreshold * 0.95f * 0.85f;
            }

            if ((int) Math.round(threshold) != objective.getValue("Threshold", Integer.class)) {
                objective.setName("Maintain at least " + (int) Math.round(threshold) + " Env Score for " + objective.getTarget() + " months in a row");
                objective.setValue("Threshold", (int) Math.round(threshold));

                ResponseBadgeList badgeListResponse = new ResponseBadgeList();
                badgeListResponse.setBadgeList(new ArrayList<Badge>(Arrays.asList(new Badge[]{badge})));
                NetworkManager.addResponseForUser(player_id, badgeListResponse);
            }
        }

        response.setAmount(badge.getAmount());
        NetworkManager.addResponseForUser(player_id, response);
        // Save Badge Progress
        try {
            BadgeDataDAO.createEntry(player_id, badge.getID(), badge.getAmount(), badge.getProgess());
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }
    }
}
