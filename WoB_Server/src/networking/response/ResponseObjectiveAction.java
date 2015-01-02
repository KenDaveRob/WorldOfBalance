package networking.response;

// Other Imports
import core.Objective;
import metadata.Constants;
import utility.GamePacket;

public class ResponseObjectiveAction extends GameResponse {

    private short action;
    private Objective objective;

    public ResponseObjectiveAction() {
        responseCode = Constants.SMSG_OBJECTIVE_ACTION;
    }

    @Override
    public byte[] constructResponseInBytes() {
        GamePacket packet = new GamePacket(responseCode);
        packet.addShort16(action);
        packet.addInt32(objective.getID());

        if (action == 0) { // Create
            packet.addString(objective.getName());
            packet.addInt32((Integer) objective.getTarget());
            packet.addString(objective.getUnit());
        } else if (action == 1) { // Update
            packet.addInt32((Integer) objective.getAmount());
            packet.addBoolean(objective.isDone());
        }

        return packet.getBytes();
    }

    public void setAction(short action) {
        this.action = action;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }
}
