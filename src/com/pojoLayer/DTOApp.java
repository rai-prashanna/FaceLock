package com.pojoLayer;

public class DTOApp {
	String name = null;
	 boolean selected = false;
	 String pkgName=null;
	 public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
//	public DTOApp(String name,boolean bool) {
//		this.name=name;
//		this.selected=bool;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	 
}
