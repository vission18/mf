package com.vission.mf.base.engine.graph.bo;

public class GraphRelation {
	private int sourceRow;
	private int targetRow;
	private String id;
	private GraphLine line;
	
	public int getSourceRow() {
		return sourceRow;
	}
	public void setSourceRow(int sourceRow) {
		this.sourceRow = sourceRow;
	}
	public int getTargetRow() {
		return targetRow;
	}
	public void setTargetRow(int targetRow) {
		this.targetRow = targetRow;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public GraphLine getLine() {
		return line;
	}
	public void setLine(GraphLine line) {
		this.line = line;
	}
	
}
