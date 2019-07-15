package com.vission.mf.base.model.bo;

public class DataGridColumn {
	public static final String ALIGN_LEFT = "left";
	public static final String ALIGN_RIGHT = "right";
	public static final String ALIGN_CENTER = "center";
	
	private String title;//列的标题文字
	private String field;//列的字段名
	private int width;//列的宽度
	private int rowspan;//指一个单元格占据多少行
	private int colspan;//指一个单元格占据多少列
	private String align;//指如何对齐此列的数据，可以用 'left'、'right'、'center'
	private boolean sortable;//True 就允许此列被排序
	private boolean resizable;//True 就允许此列被调整尺寸
	private boolean hidden;//True 就隐藏此列
	private boolean checkbox;//True 就显示 checkbox
	private String formatter;//单元格的格式化函数，需要三个参数： 	value： 字段的值。rowData： 行的记录数据。rowIndex： 行的索引。
	private String styler;//单元格的样式函数，返回样式字符串来自定义此单元格的样式，例如 'background:red' 。此函数需要三个参数：	value： 字段的值。	rowData： 行的记录数据。	rowIndex： 行的索引。
	private String sorter;//自定义字段的排序函数，需要两个参数：a： 第一个字段值。b： 第二个字段值。
	private String editor;//指编辑类型
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	public boolean isResizable() {
		return resizable;
	}
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isCheckbox() {
		return checkbox;
	}
	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public String getStyler() {
		return styler;
	}
	public void setStyler(String styler) {
		this.styler = styler;
	}
	public String getSorter() {
		return sorter;
	}
	public void setSorter(String sorter) {
		this.sorter = sorter;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}

}
