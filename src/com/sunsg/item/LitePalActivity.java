package com.sunsg.item;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import com.breadtrip.R;
import com.sunsg.item.litepal.Student;
import com.sunsg.item.litepal.Teacher;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.StorageUtils;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class LitePalActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_litepal);
		SQLiteDatabase db = Connector.getDatabase(); 
		Logger.e("test", "db path = "+db.getPath());
		File file = new File(db.getPath());
		if(file.exists()){
			Teacher t = new Teacher();
			t.setName("susng");
			t.setAge("27");
			t.setTitle("班主任");
			t.save();
			
			Student s = new Student();
			s.setId(100);
			s.setCommentCount(10);
			s.setContent("haohaohoahoahoa");
			s.setPublishDate(new Date());
			s.setTitle("新闻");
			s.setToDefault("fuang");
//			s.setSubTitle("");
			s.setTeacher(t);
			s.save();
			
			
			
			MainApplicaion app = (MainApplicaion) getApplication();
			StorageUtils.copyFile(file, app.getDbPath() +"/guang5");
		}
		
		//找到表studeng中所有的数据
		List<Student> students = DataSupport.findAll(Student.class,true);  
		Student ss = null;
		for (int i = 0; i < students.size(); i++) {
			ss = students.get(i);
			Logger.e("test", "tostring "+students.get(i).toString());
			List<Teacher> teachers = DataSupport.where("student_id=?",String.valueOf(ss.getId())).find(Teacher.class);
			Logger.e("test", "teacher size "+teachers.size());
			for (int j = 0; j < teachers.size(); j++) {
				Logger.e("test", "teacher ======="+teachers.get(j));
			}
		}
		
		List<Teacher> ts = DataSupport.findAll(Teacher.class);
		for (int i = 0; i < ts.size(); i++) {
			Logger.e("test", "tostring "+ts.get(i).toString());
		}
	}
}
