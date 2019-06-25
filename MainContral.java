package Config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

//0x...表示１６进制int型数据，(byte)强制转换，byte数据＆＆０xff可以获得byte数据无符号值
public class MainContral {
	public static void main(String[] args) throws Exception {

		String ip="192.168.37.207";
		int port=10000;
		int ladtrailer=0x99999999;
		//ladder(int sensorNum,int startNum,int ladHeader,int ladtrailer,String ip,int port)
		ladder lad1=new ladder(2, 1,0x01cccccc,ladtrailer,ip,port);
		ladder lad2=new ladder(2, 3,0x02cccccc,ladtrailer,ip,port+1);
		//ladder lad3=new ladder(10, 5,0x03cccccc,ladtrailer,ip,port+2);
		//ladder lad4=new ladder(2, 15,0x04cccccc,ladtrailer,ip,port+3);
		//ladder lad5=new ladder(2, 17,0x05cccccc,ladtrailer,ip,port+4);

		ladrun( lad1);
		ladrun( lad2);
		new Thread(new Build(new File("/home/wy/Desktop/javaStream/zz.dat"), lad1,lad2)).start();
	//	new Thread(new Build(new File("/home/wy/Desktop/javaStream/zz.dat"), lad2)).start();
		//ladrun( lad3);
		//ladrun( lad4);
		//ladrun( lad5);


	}
	public static void ladrun(ladder lad) throws Exception {
		// Loadconfig.Syscfg(2, lad,lad.getIp(),lad.getPort());
		recvdata rcvD=new recvdata();
		RingBuffer rbuf=new RingBuffer(1024*1000,30);
		analyse anly=new analyse();

		new Thread(new rwthread(rbuf,  false,rcvD,anly,lad)).start();//recv
		new Thread(new rwthread(rbuf,  true,rcvD,anly,lad)).start();//analyse
	}
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}
class Build implements Runnable{
	private int ladSize;
	private ladder[] args;
	private File fp;
	private int over=0;
	public Build(File fp,ladder... args) { 
		this.ladSize=args.length;
		this.args=args;
		this.fp=fp;
	}
	public void run() {
		int tri = 1;int pollTime;int millis=20;
		while (over<ladSize)//所有线程结束就停止
		{
			//查看trigger队列里面的值
			//			for (int i = 0; i < 100; i++) {
			//				int btr;
			//				try {
			//					btr = args[0].trigger.take();	
			//					System.out.printf("lad[0]---trigger[%d]:%d\n",i,btr);
			//				} catch (InterruptedException e) {
			//					// TODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//			
			//			}

			//查看buildQueue队列里面的值
			//			byte[] btr;	
			//			try {
			//				btr = args[0].buildQueue.take();
			//				for (int i = 0; i < btr.length; i++) {
			//					System.out.printf("lbuildQueue[%d]:%02x\n",i,btr[i]);
			//				}
			//			} catch (InterruptedException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}

			for (int i = 0; i < ladSize; i++) {
				if(args[i].controlN==millis) {
					continue;
				}
				pollTime=bulidTrigger(args[i],tri,millis, args[i].controlN);
				//System.out.printf("polltime:%d  ,  controlN:%d  \n",pollTime,args[i].controlN);
				if(pollTime==1) {
					args[i].controlN=millis;
					over++;//一个线程结束
					System.out.printf("data ladder %d is over;LastTrigger:%08x \n",i+1,tri-1);
					continue;
					
				}
				else {
					args[i].controlN=0;
				}
				bulidTrigger(args[i],tri,millis, args[i].controlN);
			}
			tri++;
		}
	}
	public int bulidTrigger(ladder lad,int tri,int millis,int controlN) {
		Integer fron=null;
		try (BufferedOutputStream bos = new BufferedOutputStream(new 
				FileOutputStream(fp,true));)
		{
//			fron = lad.trigger.take();
//			if(fron==tri) {
//				bos.write(lad.buildQueue.take());
//			}
//			else {
//				lad.trigger.add(fron);
//			}
//		     return 1;
			while(controlN<millis) {
				fron = lad.trigger.poll();
				if(fron==null) {
					Thread.sleep(1);
					//System.out.printf("null get:%d\n",controlN);
					controlN++;
				}
				else {
					//System.out.printf("count get:%d\n",controlN);
					break;
				}
			}
			if(controlN<millis) {
				if(fron==tri) {
					bos.write(lad.buildQueue.take());
				}
				else {
					lad.trigger.add(fron);
				}
				return 0;
			}
			return 1;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;

	}
}
class rwthread implements Runnable{

	private RingBuffer rbuf;
	private Boolean flag;
	private recvdata rcvD;
	private analyse  anly;
	private ladder lad;
	int getdtNum=5000000;
	int eventnumberperfile=5000000;
	public rwthread(RingBuffer rbuf,Boolean flag,recvdata rcvD,analyse  anly,ladder lad) {
		this.rbuf=rbuf;
		this.flag=flag;
		this.rcvD=rcvD;
		this.lad=lad;
		this.anly=anly;
	}
	public void run() {

		try {

			if(flag) {
				while(!anly.isClose()) {
					anly.alyDt(rbuf, "/home/wy/Desktop/javaStream", lad, getdtNum,eventnumberperfile);
				}
				//				for (int j = 0; j < lad.trigger.size(); j++) {
				//					int btr=lad.trigger.poll();
				//					System.out.printf("lad[0]---trigger[%d]:%d\n",j,btr);
				//				}
			}
			else {
				while(!rcvD.isClose()) {
					//Thread.sleep(20);
					String ip=lad.getIp();
					int port=lad.getPort();
					rcvD.recv(ip, port, rbuf);
				}
			}
			//Thread.sleep(100);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}