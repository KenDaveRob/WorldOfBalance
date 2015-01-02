package networking.request;

// Java Imports
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Other Imports
import dataAccessLayer.EnvironmentDAO;
import model.World;
import networking.response.ResponseHighScore;
import utility.DataReader;

public class RequestHighScore extends GameRequest {

    // Data
    private short type;
    // Responses
    private ResponseHighScore responseHighScore;

    public RequestHighScore() {
        responses.add(responseHighScore = new ResponseHighScore());
    }

    @Override
    public void parse() throws IOException {
        type = DataReader.readShort(dataInput);
    }

    @Override
    public void doBusiness() throws Exception {
        responseHighScore.setType(type);

        if (type == 0) {
            World world = client.getActiveObject(World.class);

            List<String[]> scoreList = new ArrayList<String[]>();
            scoreList.add(new String[]{client.getPlayer().getUsername(), String.valueOf(world.getEnvironment().getHighEnvScore())});
            responseHighScore.setEnvScore(scoreList);

            List<String[]> totalScoreList = new ArrayList<String[]>();
            totalScoreList.add(new String[]{client.getPlayer().getUsername(), String.valueOf(world.getEnvironment().getAccumulatedEnvScore())});
            responseHighScore.setTotalEnvScore(totalScoreList);

            List<String[]> currentScoreList = new ArrayList<String[]>();
            currentScoreList.add(new String[]{client.getPlayer().getUsername(), String.valueOf(world.getEnvironment().getEnvironmentScore())});
            responseHighScore.setCurrentEnvScore(currentScoreList);
        } else {
            try {
                List<String> patternList = new ArrayList<String>();
                String username = client.getPlayer().getUsername();

                if (username.startsWith("wbtester")) {
                    patternList.add("wbtester");
                } else if (username.startsWith("wbuser")) {
                    patternList.add("wbuser");
                }

                responseHighScore.setEnvScore(EnvironmentDAO.getBestEnvScore(0, 3, patternList));
                responseHighScore.setTotalEnvScore(EnvironmentDAO.getBestTotalEnvScore(0, 3, patternList));
                responseHighScore.setCurrentEnvScore(EnvironmentDAO.getBestCurrentEnvScore(0, 3, patternList));
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
