package model;

/**
 *
 * @author Gary
 */
public class ZoneType {

    private int zone_type_id;
    private boolean contains_water;

    public ZoneType(int zone_type_id, boolean contains_water) {
        this.zone_type_id = zone_type_id;
        this.contains_water = contains_water;
    }

    public int getID() {
        return zone_type_id;
    }

    public int setID(int zone_type_id) {
        return this.zone_type_id = zone_type_id;
    }

    public boolean containsWater() {
        return contains_water;
    }
}
