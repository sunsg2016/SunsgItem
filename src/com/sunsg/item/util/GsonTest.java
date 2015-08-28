package com.sunsg.item.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonTest {
	public GsonTest(){
		init();
	}
	
	private void init(){
		
		//对象转换 
		Student stu = new Student();
		stu.setAge(26);
		stu.setNmae("sunsg");
		Gson gson = new Gson();
		Log.i("test", "json = "+gson.toJson(stu));
		String str = "{\"name\":\"sunsg_sunsg\",\"age\":28}";
		Student stu2 = gson.fromJson(str, Student.class);
		Log.i("test", "name = "+stu2.getName() +" age = "+stu2.getAge());
		
		//list转换
		
		List<Student> list = new ArrayList<Student>();
		list.add(new Student("sunsg1", 25,"10000"));
		list.add(new Student("sunsg2", 26));
		list.add(new Student("sunsg3", 27));
		
		Type type =  new TypeToken<List<Student>>(){}.getType();
//		Log.i("test", "list json = "+gson.toJson(list, type));
//		
		String listJson = "[{\"name\":\"sunsg1\",\"age\":25},{\"name\":\"sunsg2\",\"age\":26},{\"name\":\"sunsg3\",\"age\":27}]";
		
		String  allJson = "{\"stus\":[{\"name\":\"sunsg1\",\"age\":25,\"apiCode\":\"10000\"},{\"name\":\"sunsg2\",\"age\":26},{\"name\":\"sunsg3\",\"age\":27}]," 
		+"\"student\":{\"name\":\"student1\",\"age\":23}," 
		+"\"map\":{\"user1\":{\"name\":\"student11\",\"age\":211},\"user2\":{\"name\":\"student33\",\"age\":233}}}";
		List<Student> ls = gson.fromJson(listJson, type);
		for (int i = 0; i < ls.size(); i++) {
			Log.i("test", "tostring = "+ls.get(i).getName());
		}
//		
		Teacher t = gson.fromJson(allJson, new TypeToken<Teacher>(){}.getType());
		Log.i("test", "t = "+t.toString());
		for (Student s: t.getList()) {
			Log.i("test", "list = "+s.tosString());
		}
		
		Log.i("test", "map user1 = "+t.getMap().get("user1").tosString()  +" user2 = "+t.getMap().get("user2").tosString());
	}
	
	public class All{
		
	}
	
	public class Student extends All{
		private String name;
		private int age;
		
		public Student(){
		}
		
		public Student(String name,int age){
			this.name = name;
			this.age = age;
		}
		
		public Student(String name,int age,String apicode){
			this.name = name;
			this.age = age;
		}
		
		public void setNmae(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
			
		}
		
		public void setAge(int age){
			this.age = age;
		}
		
		public int getAge(){
			return age;
		}
		
		public String tosString(){
			return getName() +" "+getAge() +" api = ";
		}
	}
	
	public class Teacher{
		private List<Student> stus;
		private Student student;
		private HashMap<String, Student> map;
		
		private List<Student> getList(){
			return stus;
		}
		
		public Student getStudent(){
			return student;
		}
		
		private HashMap<String , Student> getMap(){
			return map;
		}
		
		public  String toString(){
			return student.tosString() +" list = "+stus.toString() + getMap();
			
		}
		
	}
}
