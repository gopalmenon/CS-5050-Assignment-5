package menon.cs5050.assignment5;

public class City {
	
	private double xCoordinate;
	private double yCoordinate;
	
	public City (double xCoordinate, double yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	
	public double getxCoordinate() {
		return xCoordinate;
	}
	
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	
	public double getyCoordinate() {
		return yCoordinate;
	}
	
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	/**
	 * @param otherCity
	 * @return the distance to another city using Pythagoras' theorem
	 */
	public double distanceTo(City otherCity) {
		
		return Math.pow(Math.pow(this.xCoordinate - otherCity.getxCoordinate(), 2) + Math.pow(this.yCoordinate - otherCity.getyCoordinate(), 2), 0.5);
		
	}
	
	@Override
	public boolean equals(Object otherCity) {
		
		if (otherCity instanceof City) {
			if (this.xCoordinate == ((City) otherCity).getxCoordinate() && this.yCoordinate == ((City) otherCity).getyCoordinate()) {
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
		return Double.valueOf((this.xCoordinate * 100 + this.yCoordinate) * 100).intValue();
	}
	
	
	@Override
	public String toString() {
		
		StringBuffer returnValue = new StringBuffer();
		returnValue.append("(");
		returnValue.append(this.xCoordinate);
		returnValue.append(", ");
		returnValue.append(this.yCoordinate);
		returnValue.append(")");
		return returnValue.toString();
		
	}
	
}
