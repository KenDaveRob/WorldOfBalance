package worldManager.gameEngine.species;

import metadata.Constants;

import model.PlantType;

/**
 *
 * @author KeithKong
 */
public class Plant extends Organism {

    private int noLightCount;

    public Plant(int plant_id) {
        organism_type = Constants.ORGANISM_TYPE_PLANT;
        organism_id = plant_id;
    }

    @Override
    public void onNoDailyWater() {
        noWaterCount++;
        setTargetBiomass(targetBiomass - speciesType.getWaterBiomassLoss());
    }

    public void manageDailyLight(float lightOutput) {
        float lightLoss = (((PlantType) speciesType).getLightNeedFrequency() - lightOutput);

        if (lightLoss > 0) {
            noLightCount++;
            setTargetBiomass(targetBiomass - (int) (lightLoss * (float) speciesType.getAvgBiomass()));
        }
    }

    public int getNoLightCount() {
        return noLightCount;
    }

    public int setNoLightCount(int noLightCount) {
        return this.noLightCount = noLightCount;
    }
}
