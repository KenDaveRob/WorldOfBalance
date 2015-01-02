using System.Collections;

public class ComparisonTypes {
	
	public static int SortByTrophicLevels(SpeciesData x, SpeciesData y) {
		return y.trophic_level.CompareTo(x.trophic_level);
	}
}
