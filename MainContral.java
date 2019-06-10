package Config;

import java.util.Arrays;

public class MainContral {
	public static void main(String[] args) throws Exception {
		//		ladder lad=new ladder(10, 5);
		//		String ip="192.168.37.207";
		//		int port=10000;
		//Loadconfig.Syscfg(2, lad,"192.168.37.207",10000);
		//new recvdata().recv("192.168.37.207", 10000);
		recvdata rcvD=new recvdata();
		RingBuffer rbuf=new RingBuffer(1024*10);

		byte rdata[]=new byte[1024];byte wdata[]=new byte[1024];
		//new Thread(new rwthread(rbuf, wdata, false,rcvD)).start();
		//new Thread(new rwthread(rbuf, rdata, true,rcvD)).start();
		
		rdata[0]=(byte) 0xff;
		System.out.printf("%d", 0xff);

		/*	
		//test ringbuffer
		RingBuffer rbuf=new RingBuffer(1024*10);
		byte rdata[]=new byte[1024];
		byte wdata[]=new byte[1024];
		Arrays.fill(wdata,(byte)0xff);
		new Thread(new rwthread(rbuf, wdata, false)).start();
		new Thread(new rwthread(rbuf, rdata, true)).start();
		 */
	}

}
class rwthread implements Runnable{

	private RingBuffer rbuf;
	private byte[] data; 
	private Boolean flag;
	private recvdata rcvD;
	public rwthread(RingBuffer rbuf,byte[] data,Boolean flag,recvdata rcvD) {
		this.rbuf=rbuf;
		this.data=data;
		this.flag=flag;
		this.rcvD=rcvD;
	}
	public void run() {

		String ip="192.168.37.207";
		int port=10000;
		try {

			if(flag) {
				Thread.sleep(100);
				while(rbuf.canRead()!=0) {
					Thread.sleep(100);
					System.out.println("canread:"+rbuf.canRead());
					System.out.println("read to rdata :"+rbuf.read(data, 0, 1024));
					System.out.println("data :/n/n"+new String(data));
				}
			}
			else {
				while(!rcvD.isClose()) {
					rcvD.recv(ip, port, rbuf);
					//System.out.println("canwrite:"+rbuf.canWrite());
					//System.out.println("write frome wdata :"+rbuf.write(data, 0, 1024));
				}
			}
			//Thread.sleep(100);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}