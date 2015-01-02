package core;

// Java Imports
import java.sql.SQLException;

// Other Imports
import dataAccessLayer.AvatarDAO;
import dataAccessLayer.WorldDAO;
import metadata.Constants;
import model.Avatar;
import model.Player;
import model.World;
import networking.response.ResponseUpdateLevel;
import networking.response.ResponseUpdateResources;
import utility.ExpTable;
import utility.Log;

public class GameResources {

    private GameResources() {
    }

    public static void updateCredits(World world, int amount) {
        world.setCredits(Math.min(Constants.MAX_CREDITS, world.getCredits() + amount));

        try {
            WorldDAO.updateCredits(world.getID(), world.getCredits());

            ResponseUpdateResources updateResponse = new ResponseUpdateResources();
            updateResponse.setType(Constants.RESOURCE_CREDITS);
            updateResponse.setAmount(amount);
            updateResponse.setTarget(world.getCredits());
            NetworkManager.addResponseForWorld(world.getID(), updateResponse);
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }
    }

    /**
     * Adjusts a player's money by the given amount.
     *
     * @param player references instance of the player
     * @param amount contains the amount of money given
     */
    public static void updateCash(Player player, int amount) {
        Avatar avatar = player.getAvatar();
        avatar.setCurrency(Math.min(Constants.MAX_COINS, avatar.getCurrency() + amount));

        try {
            AvatarDAO.updateCurrency(avatar);

            ResponseUpdateResources updateResponse = new ResponseUpdateResources();
            updateResponse.setAmount(amount);
            updateResponse.setTarget(avatar.getCurrency());

            NetworkManager.addResponseForUser(player.getID(), updateResponse);
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }
    }

    /**
     * Adjusts a player's experience by the given amount. If the experience
     * reaches a certain amount, their level is adjusted as well.
     *
     * @param player references instance of the player
     * @param amount contains the amount of experience given
     */
    public static void updateExperience(Player player, int amount) {
        amount *= Constants.MULTIPLIER_EXP;

        Avatar avatar = player.getAvatar();
        avatar.setExperience(Math.min(ExpTable.getExp(Constants.MAX_LEVEL - 1), avatar.getExperience() + amount));

        try {
            AvatarDAO.updateExperience(avatar);
        } catch (SQLException ex) {
            Log.println_e(ex.getMessage());
        }

        int oldLevel = avatar.getLevel(), newLevel = ExpTable.getLevel(avatar.getExperience());

        if (newLevel > oldLevel) {
            avatar.setLevel(newLevel);

            try {
                AvatarDAO.updateLevel(avatar);
            } catch (SQLException ex) {
                Log.println_e(ex.getMessage());
            }

            ResponseUpdateLevel updateLevelResponse = new ResponseUpdateLevel();
            updateLevelResponse.setAmount(newLevel - oldLevel);
            updateLevelResponse.setLevel(newLevel);

            String range = String.valueOf(ExpTable.getExpToAdvance(oldLevel + 1));
            for (int i = oldLevel + 2; i <= newLevel; i++) {
                range += "," + ExpTable.getExpToAdvance(i);
            }
            updateLevelResponse.setRange(range);

            NetworkManager.addResponseForUser(player.getID(), updateLevelResponse);

            updateCash(player, amount);
        }

        ResponseUpdateResources updateResponse = new ResponseUpdateResources();
        updateResponse.setType(Constants.RESOURCE_XP);
        updateResponse.setAmount(amount);
        updateResponse.setTarget(avatar.getExperience());

        NetworkManager.addResponseForUser(player.getID(), updateResponse);
    }
}
