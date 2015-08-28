package com.sunsg.item;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TestThreadActivity extends Activity {
	private Semaphore semp = new Semaphore(5);  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_thread);
		bindEvent();
	}

	private void bindEvent() {
		//线程的join
		findViewById(R.id.Btn_force_work).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startJoinThread();
			}
		});
		//线程的interrupt
		findViewById(R.id.Btn_interrupt).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startInterruptThread();
			}
		});
		//信号量
		findViewById(R.id.Btn_semaphor).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				StatSemaphor();
			}
		});
		//线程的生产者合消费者
		findViewById(R.id.Btn_product).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startProduct();
			}
		});
		
	}

	// =================================================================================================
	//线程的强制执行
	private void startJoinThread() {
		MyJoinThread thread = new MyJoinThread();
		Thread t = new Thread(thread, "join线程");
		t.start();
		for (int i = 0; i < 200; i++) {
			if (i > 50) {
				try {
					t.join();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			Logger.e("test", "main线程运行 --> " + i);
		}
	}

	class MyJoinThread implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 200; i++) {
				Logger.e("test", Thread.currentThread().getName() + "运行 --> " + i);
			}
		}

	}
	// =================================================================================================

	
	// =================================================================================================
	//线程的中断
	private void startInterruptThread() {
		MyIntterruptThread thread = new MyIntterruptThread();
		Thread t = new Thread(thread, "intterrupt线程");
		t.start();
		try {
			Thread.sleep(1100);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		Logger.e("test", "=====================================main 完成休眠");
		t.interrupt();
		
	}

	class MyIntterruptThread implements Runnable {

		@Override
		public void run() {
			Logger.e("test", "1.进入 run 方法");
			try {
				Logger.e("test", "2.休眠1s");
				Thread.sleep(1000);
				Logger.e("test", "3.已经完成休眠");
				for (int i = 0; i < 5000; i++) {
					Logger.e("test", Thread.currentThread().getName() + "运行 --> " + i);
				}
			} catch (Exception e) {
				Logger.e("test", "4.休眠被终止");
				return;
			}
			Logger.e("test", "5.方法正常结束");
			
		}

	}
	// =================================================================================================

	// =================================================================================================
	//信号许可
	
	private void StatSemaphor(){
		 // 线程池 
        ExecutorService exec = Executors.newCachedThreadPool();  
        // 只能5个线程同时访问 
        
        // 模拟20个客户端访问 
        for (int index = 0; index < 20; index++) {
            final int NO = index;  
            Runnable run = new Runnable() {  
                public void run() {  
                    try {  
                        // 获取许可 
                        semp.acquire();  
                        Logger.e("test", "Accessing: " + NO);
//                        System.out.println("Accessing: " + NO);  
                        Thread.sleep((long) (Math.random() * 5000));  
                        // 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞
                        if(NO == 4)
                        semp.release();  
                    } catch (InterruptedException e) {  
                    }  
                }  
            };  
//            Logger.e("test", "线程开始 " + NO);
            exec.execute(run);  
        }  
        // 退出线程池 
        exec.shutdown();  
        Logger.e("test", "退出线程");
	}
	// =================================================================================================
	
	// =================================================================================================
	//生产者／消费者
	
	private void startProduct(){
		Car car = new Car();
		new Thread(new Product(car)).start();
		new Thread(new Consumer(car)).start();
	}
	
	
	class Car{
		private String name;
		private String color;
		boolean flag = true;
		public Car(){
			
		}
		
		public synchronized void set(String name,String color){
			if(!flag){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			setName(name);
			setColor(color);
			flag = false;
			this.notify();
			
		}
		
		public synchronized void get(){
			if(flag){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Logger.e("test","car -->"+this.getName() +" "+this.getColor());
			flag = true;
			this.notify();
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
	}
	
	class Product implements Runnable{
		private Car car = null;
		
		public Product(Car car) {
			this.car = car;
		}

		@Override
		public void run() {
			boolean flag = false;
			for (int i = 0; i < 50; i++) {
				if(flag){
					flag = false;
					this.car.set("奔驰", "白色");
				}else{
					flag = true;
					this.car.set("宝马", "黑色");
				}
			}
		}
		
	}
	
	class Consumer implements Runnable{
		private Car car = null;
		public Consumer(Car car){
			this.car = car;
		}

		@Override
		public void run() {
			for (int i = 0; i < 50; i++) {
				this.car.get();
			}
		}
		
	}
	// =================================================================================================

}
