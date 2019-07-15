package com.vission.mf.base.engine.graph.bo;

/**
* 功能/模块 ：js画图引擎
 */
public class GraphNode {
	private String id;
	private String parent;
	private String style;
	private String name;
	private boolean vertex;
	private String type;
	private GraphShape shape;
	
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isVertex() {
		return vertex;
	}
	public void setVertex(boolean vertex) {
		this.vertex = vertex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GraphShape getShape() {
		return shape;
	}
	public void setShape(GraphShape shape) {
		this.shape = shape;
	}
	
}
