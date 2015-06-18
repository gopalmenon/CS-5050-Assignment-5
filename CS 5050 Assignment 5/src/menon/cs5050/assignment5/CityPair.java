package menon.cs5050.assignment5;

public class CityPair {
	
	private City city1;
	private City city2;
	
	public CityPair(City city1, City city2) {
		this.city1 = city1;
		this.city2 = city2;		
	}

	@Override
	public boolean equals(Object otherCityPair) {
		
		if (otherCityPair instanceof CityPair) {
			if (this.city1.equals(((CityPair) otherCityPair).city1) && this.city2.equals(((CityPair) otherCityPair).city2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf((this.city1.hashCode() * 100 + this.city2.hashCode()) * 100).intValue();
	}
	
	
	@Override
	public String toString() {
		
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("[");
		returnValue.append(this.city1.toString());
		returnValue.append(", ");
		returnValue.append(this.city2.toString());
		returnValue.append("]");
		return returnValue.toString();
		
	}

}
