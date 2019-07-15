package com.vission.mf.base.engine.graph.bo;

import java.util.List;

public class Graph {
	private List<GraphTableNode> tables;
	private List<GraphLine> lines;
	private List<GraphNode> nodes;
	private List<GraphRelation> relations;
	public List<GraphTableNode> getTables() {
		return tables;
	}
	public void setTables(List<GraphTableNode> tables) {
		this.tables = tables;
	}
	public List<GraphLine> getLines() {
		return lines;
	}
	public void setLines(List<GraphLine> lines) {
		this.lines = lines;
	}
	public List<GraphNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<GraphNode> nodes) {
		this.nodes = nodes;
	}
	public List<GraphRelation> getRelations() {
		return relations;
	}
	public void setRelations(List<GraphRelation> relations) {
		this.relations = relations;
	}
	
}
