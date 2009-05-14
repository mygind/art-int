package gdi1sokoban.planning;

public class Estimate implements Comparable<Estimate>{

	private Board board;
	
	private int estimatedValue, stepValue;
	
	public Estimate(Board board, int estimatedValue, int stepValue) {
		super();
		this.board = board;
		this.estimatedValue = estimatedValue;
		this.stepValue = stepValue;
	}

	@Override
	public int compareTo(Estimate o) {
		return getTotalValue() - o.getTotalValue();
	}

	public Board getBoard() {
		return board;
	}
	
	public int getEstimatedValue() {
		return estimatedValue;
	}
	
	public void setStepValue(int stepValue) {
		this.stepValue = stepValue;
	}
	
	public int getStepValue() {
		return stepValue;
	}

	public int getTotalValue() {
		return stepValue + estimatedValue;
	}
	
	public boolean equals(Object o){
		if(o instanceof Board){
			return this.board.equals(o);
		}
		if(o instanceof Estimate){
			Estimate other = (Estimate)o;
			return this.board.equals(other.getBoard()) &&
					this.stepValue == other.getStepValue() &&
					this.estimatedValue == other.getEstimatedValue();
		}
		return false;
	}

}
