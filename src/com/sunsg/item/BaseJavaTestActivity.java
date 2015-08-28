package com.sunsg.item;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

public class BaseJavaTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_java_test);
		bindEvent();
	}

	private void bindEvent() {
		/** 泛型 */
		findViewById(R.id.Btn_fanxing).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startPoint();
			}
		});

		/** 日期 */
		findViewById(R.id.Btn_data_format).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSimpleDataFormat();
			}
		});
		/** 比较 */
		findViewById(R.id.btn_comparable).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// startSimpleDataFormat();
				startComparable();
			}
		});
		/** 观察 */
		findViewById(R.id.btn_observal).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				staratObserable();
			}
		});
		/** 正则 */

		findViewById(R.id.btn_pattern).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pattern();
			}
		});
		/** hashmap */
		findViewById(R.id.btn_hashmap).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(0, 0.75f, true);
				map.put("1", "11");
				map.put("2", "22");
				map.put("4", "44");
				map.put("5", "55");
				map.put("6", "66");
				map.put("7", "77");
				map.put("8", "88");
				map.put("3", "33");
				map.put("9", "99");
				map.get("1");
				map.get("5");
				Iterator<String> ite = map.keySet().iterator();

				while (ite.hasNext()) {
					String key = ite.next();
					Logger.e("test", "key = " + key + " value = ");

				}
			}
		});
		/** 线程池 */

		findViewById(R.id.btn_threadPoolExecutor).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				threadPoolExecutor();
			}
		});
		
		/**FutureTask*/
		findViewById(R.id.btn_future_task).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				futureTask();
			}
		});
		
		/** view 加载方式*/
		findViewById(R.id.btn_view_attache_type).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attacheView();
			}
		});
		
		

	}

	// ==============================================================================================
	// 泛型1
	class Point<T> {
		private T x;
		private T y;

		public Point() {

		}

		public Point(T x, T y) {
			this.x = x;
			this.y = y;
		}

		public T getX() {
			return x;
		}

		public void setX(T x) {
			this.x = x;
		}

		public T getY() {
			return y;
		}

		public void setY(T y) {
			this.y = y;
		}
	}

	class TestPoint {
		private String name;

		public TestPoint() {

		}

		public TestPoint(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private void startPoint() {
		Point<Integer> p = new Point<Integer>(2, 3);
		// p.setX(2);
		// p.setY(3);
		Logger.e("test", "startPoint  x = " + p.getX() + "y= " + p.getY());

		Point<Integer> p2 = new Point<Integer>(2, 3);
		// testFanxing2(p2);

		Point<TestPoint> tp = new Point<BaseJavaTestActivity.TestPoint>();
		TestPoint tpp = new TestPoint("sunsg");
		tp.setX(tpp);
		testFanxing2(tp);
		Logger.e("test", "泛型传方法 int " + testFnxing3(234));
		Logger.e("test", "泛型传方法 String " + testFnxing3("sun shuguang"));

		MyRuleImpl my = testFanxing4();
		Logger.e("test", "泛型传方法 MyRule " + my.name);

	}

	/**
	 * 泛型 传参数
	 * 
	 * @param point
	 */
	private void testFanxing2(Point<?> point) {
		if (point.getX() instanceof TestPoint) {
			TestPoint tp = (TestPoint) point.getX();
			Logger.e("test", "testFanxing2   x = " + tp.getName() + " y= " + point.getY());
		}
	}

	/**
	 * 泛型方法 可以接受任意类型的数据
	 */
	private <T> T testFnxing3(T t) {
		return t;
	}

	private <T extends MyRule> T testFanxing4() {
		MyRule my = new MyRuleImpl("susng");
		return (T) my;
	}

	interface MyRule {
		// void myRule();
	}

	class MyRuleImpl implements MyRule {
		private String name;

		public MyRuleImpl() {

		}

		public MyRuleImpl(String name) {
			this.name = name;
		}
	}

	// ==============================================================================================

	// ==============================================================================================
	@SuppressLint("SimpleDateFormat")
	private void startSimpleDataFormat() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒");
		Logger.e("test", "data = " + format.format(new Date()));

		// 随即产生7以内的数
		Random ran = new Random();
		for (int i = 0; i < 10; i++) {
			Logger.e("test", "random = " + ran.nextInt(7));
		}
	}

	// ==============================================================================================

	// ==============================================================================================
	private void startComparable() {
		Student s1 = new Student("ww", 20, 78);
		Student s2 = new Student("ss", 24, 79);
		Student s3 = new Student("dd", 34, 85);
		Student s4 = new Student("ll", 29, 73);
		Student s5 = new Student("rr", 28, 75);
		Student s6 = new Student("tt", 45, 98);
		Student s7 = new Student("oo", 19, 90);
		Student s8 = new Student("bb", 25, 76);
		Student s9 = new Student("pp", 32, 87);
		Student s10 = new Student("qq", 23, 77);
		// List<Student> list = new ArrayList<BaseJavaTestActivity.Student>();
		Set<Student> list = new TreeSet<Student>();
		list.add(s1);
		list.add(s1);
		list.add(s2);
		list.add(s3);
		list.add(s4);
		list.add(s5);
		list.add(s6);
		list.add(s7);
		list.add(s8);
		list.add(s9);
		list.add(s10);
		Iterator<Student> it = list.iterator();
		while (it.hasNext()) {
			Logger.e("test", "student = " + it.next());
		}
		// for (int i = 0; i < list.size(); i++) {
		// Logger.e("test", "student = "+list.g);
		// }
	}

	class Student implements Comparable<Student> {
		private String name;
		private int age;
		private int score;

		public Student() {

		}

		public Student(String name, int age, int score) {
			this.name = name;
			this.age = age;
			this.score = score;
		}

		@Override
		public int compareTo(Student another) {
			if (another.score > this.score) {
				return 1;
			} else if (another.score < this.score) {
				return -1;
			}

			return 0;
		}

		@Override
		public String toString() {
			return "name = " + name + " age = " + age + " score =" + score;
		}

	}

	// ==============================================================================================
	// ==============================================================================================
	// 观察者

	private void staratObserable() {
		House h = new House(3000);
		HoustPriceObserver p1 = new HoustPriceObserver("A");
		HoustPriceObserver p2 = new HoustPriceObserver("B");
		HoustPriceObserver p3 = new HoustPriceObserver("C");
		h.addObserver(p1);
		h.addObserver(p2);
		h.addObserver(p3);

		Logger.e("test", "h = " + h);
		h.setPrice(5000);
		h.setPrice(8000);

	}

	class House extends Observable {
		private float price;

		public House(float price) {
			this.price = price;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			super.setChanged();
			super.notifyObservers(price);
			this.price = price;
		}

		@Override
		public String toString() {
			return "房子的价格为： " + price;
		}
	}

	class HoustPriceObserver implements Observer {
		private String name;

		public HoustPriceObserver(String name) {
			this.name = name;
		}

		@Override
		public void update(Observable observable, Object data) {
			if (data instanceof Float) {
				Logger.e("test", "update " + this.name + "观察到的价格为：" + data);
			}
		}

	}

	// ==============================================================================================

	// ==============================================================================================
	// 正则

	private void pattern() {
		String str = "1234567890";
		String pat = "[0-9]+";
		marcher(str, pat, "全是数字");

		String str1 = "1983-05-15";
		String pat1 = "\\d{4}-\\d{2}-\\d{2}";
		marcher(str1, pat1, "日期格式");

		String str2 = "1983-05-15s";
		String pat2 = "\\d{4}-\\d{2}-\\d{2}";
		marcher(str1, pat1, "日期格式");

		String str3 = "A123B222C343243D385798EF3479387F749837G";
		String pat3 = "\\d+";
		marcherSplite(str3, pat3, "拆分");

	}

	private void marcher(String str, String pat, String token) {
		Pattern p = Pattern.compile(pat);
		Matcher mat = p.matcher(str);
		if (mat.matches()) {
			Logger.e("test", token);
		} else {
			Logger.e("test", "不是" + token);
		}
	}

	private void marcherSplite(String str, String pat, String token) {
		Pattern p = Pattern.compile(pat);
		String s[] = p.split(str);
		for (int i = 0; i < s.length; i++) {
			Logger.e("test", token + s[i]);
		}

	}

	// ==============================================================================================
	// ==============================================================================================
	// 线程池
	private void threadPoolExecutor() {
		  int produceTaskSleepTime = 2;
	      int produceTaskMaxNumber = 9;
		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1),
				new ThreadPoolExecutor.CallerRunsPolicy());
		
		for (int i = 1; i <= produceTaskMaxNumber; i++) {
			try {
				// 产生一个任务，并将其加入到线程池
				String task = "task@ " + i;
				System.out.println("put " + task);
				threadPool.execute(new ThreadPoolTask(task));

				// 便于观察，等待一段时间
				Thread.sleep(produceTaskSleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.e("test", "count = "+threadPool.getActiveCount());
//		for (int i = 1; i <= produceTaskMaxNumber; i++) {
//			try {
//				// 产生一个任务，并将其加入到线程池
//				String task = "task@ " + i;
//				System.out.println("put " + task);
//				threadPool.execute(new ThreadPoolTask(task));
//
//				// 便于观察，等待一段时间
//				Thread.sleep(produceTaskSleepTime);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		Logger.e("test", "count = "+threadPool.getActiveCount());
	}

	/**
	 * 线程池执行的任务
	 */
	static class ThreadPoolTask implements Runnable, Serializable {
		private static final long serialVersionUID = 0;
		private static int consumeTaskSleepTime = 2000;
		// 保存任务所需要的数据
		private Object threadPoolTaskData;

		ThreadPoolTask(Object tasks) {
			this.threadPoolTaskData = tasks;
		}

		public void run() {
			// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
			System.out.println(Thread.currentThread().getName());
			System.out.println("start .." + threadPoolTaskData);

			try {
				// //便于观察，等待一段时间
				Thread.sleep(consumeTaskSleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			threadPoolTaskData = null;
		}

		public Object getTask() {
			return this.threadPoolTaskData;
		}
	}
	// ==============================================================================================
	//Future Task
	@SuppressWarnings("all")  
	private void futureTask(){
		 // 初始化一个Callable对象和FutureTask对象  
        Callable pAccount = new PrivateAccount();  
        FutureTask futureTask = new FutureTask(pAccount);  
        // 使用futureTask创建一个线程  
        Thread pAccountThread = new Thread(futureTask);  
        System.out.println("futureTask线程现在开始启动，启动时间为：" + System.nanoTime());  
        pAccountThread.start();  
        System.out.println("主线程开始执行其他任务");  
        // 从其他账户获取总金额  
        int totalMoney = new Random().nextInt(100000);  
        System.out.println("现在你在其他账户中的总金额为" + totalMoney);  
        System.out.println("等待私有账户总金额统计完毕...");  
        // 测试后台的计算线程是否完成，如果未完成则等待  
        while (!futureTask.isDone()) {  
            try {  
                Thread.sleep(500);  
                System.out.println("私有账户计算未完成继续等待... ");  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } 
        }  
        System.out.println("futureTask线程计算完毕，此时时间为" + System.nanoTime());  
        String privateAccountMoney = null;  
        try {  
            privateAccountMoney = (String) futureTask.get();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (ExecutionException e) {  
            e.printStackTrace();  
        }  
        System.out.println("您现在的总金额为：" + totalMoney + " "+privateAccountMoney);  
    }  
	
	
	@SuppressWarnings("all")
	class PrivateAccount implements Callable {  
	    String totalMoney;  
	  
	    @Override  
	    public Object call() throws Exception {  
	        Thread.sleep(5000);  
	        totalMoney = 1000+"";  
	        System.out.println("您当前有 " + totalMoney + "在您的私有账户中");  
	        return totalMoney;  
	    }  
	  
	} 
	// ==============================================================================================
	
	// ==============================================================================================
	//view的加载方式
	private void attacheView(){
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
		root.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		Button view =  (Button) inflater.inflate(R.layout.attache_view_type, root,false);
		root.addView(view);
	}
	
	// ==============================================================================================
}
