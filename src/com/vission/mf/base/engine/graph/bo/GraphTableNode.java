package com.vission.mf.base.engine.graph.bo;

import java.util.List;

/**
* 功能/模块 ：js画图引擎
 */
public class GraphTableNode extends GraphNode {

	private List<GraphFieldNode> fields;
	private GraphShape rect;
	
	public List<GraphFieldNode> getFields() {
		return fields;
	}
	public void setFields(List<GraphFieldNode> fields) {
		this.fields = fields;
	}
	public GraphShape getRect() {
		return rect;
	}
	public void setRect(GraphShape rect) {
		this.rect = rect;
	}
	
}
