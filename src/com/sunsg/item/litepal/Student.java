package com.sunsg.item.litepal;

import java.util.Date;

import org.litepal.crud.DataSupport;

public class Student extends DataSupport {

	private int id;

	private String title;

	private String content;

	private Date publishDate;

	private int commentCount;
	
	private String subTitle;
	
	private Teacher teacher;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(teacher == null)teacher = new Teacher();
		String teacherToString = "";
		teacherToString = teacher.toString();
		return "id = " + id + " title " + title + " content " + content + " publishDate " + publishDate + " commentCount " + commentCount +"subtitle = "+subTitle +"teacher = "+teacherToString;
	}

}
