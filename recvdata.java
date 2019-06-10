package Config;

import java.io.InputStream;
import java.net.Socket;

public class recvdata {
	public boolean rdCf;
	public int length=0;
	private static int buffersize=1024;
	public  void recv(String ip,int port,RingBuffer rbuf) throws Exception {
		Socket s=new Socket(ip,port);

		InputStream in=s.getInputStream();
		byte[]data=new byte[buffersize];
		int offset=0;
		while(length!=-1) {
			offset=0;
			while(offset<buffersize) {
				
					length=in.read(data,offset,data.length-offset);
					sop(offset);
				if(length!=-1)
				{
					offset=offset+length;
				}
				else {
					break;
				}
			}
			//sop(new String(data,0,	offset));
			System.out.println("canwrite:"+rbuf.canWrite());
			System.out.println("write frome wdata :"+rbuf.write(data, 0, offset));
		}
		s.shutdownInput();

		in.close();
		s.close();
	}
	public boolean isClose() {
		if(length==-1)
			return true;
		else
			return false;
	}
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}
