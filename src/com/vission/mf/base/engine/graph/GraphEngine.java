package com.vission.mf.base.engine.graph;

import com.vission.mf.base.engine.graph.bo.Graph;
import com.vission.mf.base.engine.graph.bo.GraphFieldNode;
import com.vission.mf.base.engine.graph.bo.GraphLine;
import com.vission.mf.base.engine.graph.bo.GraphNode;
import com.vission.mf.base.engine.graph.bo.GraphTableNode;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class GraphEngine {
	
	/**
	 * 生成表关系图形的xml
	 * @param graph
	 * @return
	 */
	public static String generateTableRelXML(Graph graph) {
		Document document = DocumentHelper.createDocument();
		Element mxGraphModel = document.addElement("mxGraphModel");
	    Element root = mxGraphModel.addElement("root");
	    //生成默认的两个cell
	    root.addElement("mxCell").addAttribute("id", GraphConstants.ROOT_NODE_ID);
	    root.addElement("mxCell").addAttribute("id", GraphConstants.MAIN_NODE_ID)
	    	.addAttribute("parent", GraphConstants.ROOT_NODE_ID);
	    //表
	    if(graph.getTables()!=null){
		    for(GraphTableNode table : graph.getTables()){
		    	Element tableNode = root.addElement("mxCell");
		    	tableNode.addAttribute("id", table.getId());
		    	tableNode.addAttribute("parent", table.getParent());
		    	tableNode.addAttribute("style", GraphConstants.CELL_TYPE_TABLE);
		    	tableNode.addAttribute("vertex", table.isVertex()?"1":"0");
		    	tableNode.addElement("Table").addAttribute("name", table.getName()).addAttribute("as", table.getType());
		    	Element mxGeometryNode = tableNode.addElement("mxGeometry");
		    	mxGeometryNode.addAttribute("x", String.valueOf(table.getShape().getX()));
		    	mxGeometryNode.addAttribute("y", String.valueOf(table.getShape().getY()));
		    	mxGeometryNode.addAttribute("width", String.valueOf(table.getShape().getWidth()));
		    	mxGeometryNode.addAttribute("height", String.valueOf(table.getShape().getHeight()));
		    	mxGeometryNode.addAttribute("as", table.getShape().getType());
		    	mxGeometryNode.addElement("mxRectangle").addAttribute("width", String.valueOf(table.getRect().getWidth()))
		    		.addAttribute("height", String.valueOf(table.getRect().getHeight())).addAttribute("as", table.getRect().getType());
		    	for(GraphFieldNode field : table.getFields()){
		    		Element fieldNode = root.addElement("mxCell");
		    		fieldNode.addAttribute("id", field.getId());
		    		fieldNode.addAttribute("parent", field.getParent());
		    		fieldNode.addAttribute("vertex", field.isVertex()?"1":"0");
		    		fieldNode.addAttribute("connectable", field.isConnectable()?"1":"0");
		    		fieldNode.addAttribute("style", GraphConstants.CELL_TYPE_FIELD);
		    		fieldNode.addElement("Column").addAttribute("type", field.getFieldType())
		    			.addAttribute("name", field.getName()).addAttribute("as", field.getType())
		    			.addAttribute("primaryKey", field.isPrimaryKey()?"1":"0")
		    			.addAttribute("autoIncrement", field.isAutoIncrement()?"1":"0");
		    		fieldNode.addElement("mxGeometry").addAttribute("y", String.valueOf(field.getShape().getY()))
		    			.addAttribute("width", String.valueOf(field.getShape().getWidth()))
		    			.addAttribute("height", String.valueOf(field.getShape().getHeight()))
		    			.addAttribute("as", field.getShape().getType());
		    	}
		    }
	    }
	    //节点
	    if(graph.getNodes()!=null){
		    for(GraphNode node : graph.getNodes()){
		    	Element nodeNode = root.addElement("mxCell");
		    	nodeNode.addAttribute("id", node.getId());
		    	nodeNode.addAttribute("parent", node.getParent());
		    	nodeNode.addAttribute("style", node.getStyle());
		    	nodeNode.addAttribute("vertex", node.isVertex()?"1":"0");
		    	nodeNode.addAttribute("style", GraphConstants.CELL_TYPE_NODE);
		    	Element mxGeometryNode = nodeNode.addElement("mxGeometry");
		    	mxGeometryNode.addAttribute("x", String.valueOf(node.getShape().getX()));
		    	mxGeometryNode.addAttribute("y", String.valueOf(node.getShape().getY()));
		    	mxGeometryNode.addAttribute("width", String.valueOf(node.getShape().getWidth()));
		    	mxGeometryNode.addAttribute("height", String.valueOf(node.getShape().getHeight()));
		    	mxGeometryNode.addAttribute("as", node.getShape().getType());
		    }
	    }
	    //线
	    if(graph.getLines()!=null){
		    for(GraphLine line : graph.getLines()){
		    	Element lineNode = root.addElement("mxCell");
		    	lineNode.addAttribute("id", line.getId());
		    	lineNode.addAttribute("parent", line.getParent());
		    	lineNode.addAttribute("source", line.getSourceId());
		    	lineNode.addAttribute("target", line.getTargetId());
		    	lineNode.addAttribute("edge", line.isEdge()?"1":"0");
		    	lineNode.addAttribute("style", GraphConstants.CELL_TYPE_LINE);
		    	Element mxGeometryNode = lineNode.addElement("mxGeometry");
		    	mxGeometryNode.addAttribute("relative", String.valueOf(line.getRelative()));
		    	mxGeometryNode.addAttribute("as", line.getType());
		    }
	    }
	    System.out.println(document.asXML());
	    return document.asXML();
	}
	
	
	/**
	 * 生成表关系图形的xml
	 * @param graph
	 * @return
	 */
	public static String generateBlankXML() {
		Document document = DocumentHelper.createDocument();
		Element mxGraphModel = document.addElement("mxGraphModel");
	    Element root = mxGraphModel.addElement("root");
	    //生成默认的两个cell
	    root.addElement("mxCell").addAttribute("id", GraphConstants.ROOT_NODE_ID);
	    root.addElement("mxCell").addAttribute("id", GraphConstants.MAIN_NODE_ID)
	    	.addAttribute("parent", GraphConstants.ROOT_NODE_ID);
	    return document.asXML();
	}

}
