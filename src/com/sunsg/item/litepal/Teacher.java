package com.sunsg.item.litepal;

import org.litepal.crud.DataSupport;

public class Teacher extends DataSupport{
	private String name;
	private String age;
	private String title;
	public String getName() {
		return name;
	}
	public String getAge() {
		return age;
	}
	public String getTitle() {
		return title;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "name = "+name +" age= "+age +" title= "+title;
	}
	
	
}
