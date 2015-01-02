package utility;

// Java Imports
import java.util.Comparator;
import model.SpeciesType;

// Other Imports
import worldManager.gameEngine.species.Organism;

/**
 * The SpeciesComparator class provides comparators to sort organisms,
 * specifically by group size.
 */
public class SpeciesComparator {

//    public static Comparator<Organism> GroupSizeComparatorASC = new Comparator<Organism>() {
//        @Override
//        public int compare(Organism o1, Organism o2) {
//            return Integer.valueOf(o1.getGroupSize()).compareTo(Integer.valueOf(o2.getGroupSize()));
//        }
//    };
//
//    public static Comparator<Organism> GroupSizeComparatorDESC = new Comparator<Organism>() {
//        @Override
//        public int compare(Organism o1, Organism o2) {
//            return Integer.valueOf(o2.getGroupSize()).compareTo(Integer.valueOf(o1.getGroupSize()));
//        }
//    };

    public static Comparator<SpeciesType> SpeciesNameComparator = new Comparator<SpeciesType>() {
        @Override
        public int compare(SpeciesType o1, SpeciesType o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };
}
