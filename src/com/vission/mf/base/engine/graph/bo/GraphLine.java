package com.vission.mf.base.engine.graph.bo;

/**
* 功能/模块 ：js画图引擎
 */
public class GraphLine {
	private String id;
	private String parent;
	private boolean edge;
	private String sourceId;
	private String targetId;
	private int relative;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public boolean isEdge() {
		return edge;
	}
	public void setEdge(boolean edge) {
		this.edge = edge;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public int getRelative() {
		return relative;
	}
	public void setRelative(int relative) {
		this.relative = relative;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
