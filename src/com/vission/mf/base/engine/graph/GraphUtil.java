package com.vission.mf.base.engine.graph;

import com.vission.mf.base.engine.graph.bo.GraphShape;

public class GraphUtil {
	public static GraphShape tableShape(){
		GraphShape shape = new GraphShape();
		shape.setX(100);
		shape.setY(100);
		shape.setWidth(200);
		shape.setHeight(100);
		shape.setType(GraphConstants.AS_TYPE_GEOMETRY);
		return shape;
	}
	
	public static GraphShape tableRect(){
		GraphShape shape = new GraphShape();
		shape.setWidth(200);
		shape.setHeight(28);
		shape.setType(GraphConstants.AS_TYPE_BOUNDS);
		return shape;
	}
	
	public static GraphShape fieldShape(){
		GraphShape shape = new GraphShape();
		shape.setY(28);
		shape.setWidth(200);
		shape.setHeight(26);
		shape.setType(GraphConstants.AS_TYPE_GEOMETRY);
		return shape;
	}
}
