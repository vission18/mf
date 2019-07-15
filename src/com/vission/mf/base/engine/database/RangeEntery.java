package com.vission.mf.base.engine.database;

public class RangeEntery {
	public final String columnName;
	public final String[] args;
	public final boolean isLike;

	public RangeEntery(String columnName, String... args) {
		this(columnName, false, args);
	}

	public RangeEntery(String columnName, boolean isLike, String... args) {
		this.columnName = columnName;
		this.isLike = isLike;
		this.args = (args == null || args.length == 0) ? null : args;

		if (this.isLike && this.args != null) {
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].toLowerCase();
			}
		}
	}

	public boolean filter(Object value) {
		if (args == null) {
			return true;
		}

		String text = String.valueOf(value);

		if (isLike) {
			for (String s : args) {
				if (text.contains(s)) {
					return true;
				}
			}
		} else {
			for (String s : args) {
				if (text.equals(s)) {
					return true;
				}
			}
		}

		return false;
	}
}
