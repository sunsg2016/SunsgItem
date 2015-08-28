package com.sunsg.item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class RandomAccessFileActivity extends TheBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout contentView = new RelativeLayout(this);
		setContentView(contentView);
		contentView.setBackgroundColor(0xffffffff);
		TextView tv = new TextView(this);
		tv.setText("RandomAcessFileTest");
		tv.setTextColor(0xffff00ff);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv.setLayoutParams(params);
		contentView.addView(tv);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				doWork();
				doThreadWork();
			}
		});
	}
	
	private void doWork(){
	 RandomAccessFile rf;
		try {
			File file = new File(mTheApplication.getTempPath(),"temp");
			rf = new RandomAccessFile(file, "rw");
			for (int i = 0; i < 10; i++) {  
	            //写入基本类型double数据  
	            rf.writeDouble(i * 1.414);  
	        }  
	        rf.close();  
	        rf = new RandomAccessFile(file, "rw");  
	        //直接将文件指针移到第5个double数据后面  
	        rf.seek(5 * 8);  
	        //覆盖第6个double数据  
	        rf.writeDouble(47.0001);  
	        rf.close();  
	        rf = new RandomAccessFile(file, "r");  
	        for (int i = 0; i < file.length(); i=i*10) {  
	            Log.i("test","Value " + i + ": " + rf.readDouble());  
	        }  
	        rf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Log.i("test","dowork randomaccessfile");   
	}
	
	private void doThreadWork(){
		 // 预分配文件所占的磁盘空间，磁盘中会创建一个指定大小的文件  
        RandomAccessFile raf;
		try {
			File file = new File(mTheApplication.getTempPath(),"temp");
			if(file.exists())file.delete();
			raf = new RandomAccessFile(file, "rw");
			raf.setLength(1024*1024); // 预分配 1M 的文件空间  
	        raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          
          
        // 所要写入的文件内容  
        String s1 = "第一个字符串";  
        String s2 = "第二个字符串";  
        String s3 = "第三个字符串";  
        String s4 = "第四个字符串";  
        String s5 = "第五个字符串";  
          
        // 利用多线程同时写入一个文件  
        new FileWriteThread(1024*1,s1.getBytes()).start(); // 从文件的1024字节之后开始写入数据  
        new FileWriteThread(1024*2,s2.getBytes()).start(); // 从文件的2048字节之后开始写入数据  
        new FileWriteThread(1024*3,s3.getBytes()).start(); // 从文件的3072字节之后开始写入数据  
        new FileWriteThread(1024*4,s4.getBytes()).start(); // 从文件的4096字节之后开始写入数据  
        new FileWriteThread(1024*5,s5.getBytes()).start(); // 从文件的5120字节之后开始写入数据  
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				doWork();
			}
		}, 1000);
        
	}
	
	// 利用线程在文件的指定位置写入指定数据  
     class FileWriteThread extends Thread{  
        private int skip;  
        private byte[] content;  
          
        public FileWriteThread(int skip,byte[] content){  
            this.skip = skip;  
            this.content = content;  
        }  
          
        public void run(){  
            RandomAccessFile raf = null;  
            try {  
                raf = new RandomAccessFile("D://abc.txt", "rw");  
                raf.seek(skip);  
                raf.write(content);  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {  
                try {  
                    raf.close();  
                } catch (Exception e) {  
                }  
            }  
        }  
    }  
	
	
}
