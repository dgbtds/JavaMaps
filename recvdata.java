package Config;

import java.io.InputStream;
import java.net.Socket;

public class recvdata {
	public boolean rdCf;
	public int length=0;
	private static int buffersize=1024*3;
	public  int recv(String ip,int port,RingBuffer rbuf)  {
	
		try(Socket TSocket=new Socket(ip,port);
				InputStream in=TSocket.getInputStream();) {
			
			byte[]data=new byte[buffersize];
			int offset=0,FileSize=0;
			while(length!=-1) {
				offset=0;
				while(offset<buffersize) {

					length=in.read(data,offset,data.length-offset);
					//sop("length:"+length+" ; offset:"+offset);
					if(length!=-1)
					{
						offset=offset+length;
					}
					else {
						break;
					}
				}

				//sop(new String(data,0,	offset));
				int wsize=rbuf.write(data, 0, offset);
				if(wsize==-1)
				{
					length=-1;
					return length;
				}
				FileSize+=wsize;
				//System.out.println(" RECVDATA>>>>>>>space:"+(rbuf.canWrite()+wsize)+"----->"+rbuf.canWrite()+" ; write to rbuffer :"+wsize+" ;FileSize:"+FileSize);
			}
			TSocket.shutdownInput();
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		length=-1;
		return length;
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
