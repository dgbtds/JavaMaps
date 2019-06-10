package Config;

import java.util.Arrays;

public class RingBuffer {

	private final static int DEFAULT_SIZE  = 1024;
	private byte[] buffer;
	private int rHead = 0;
	private int wTail = 1;
	private int bufferSize;
	private int rbCapacity;

	public RingBuffer(){
		this.bufferSize = DEFAULT_SIZE;
		this.buffer = new byte[bufferSize];
		rbCapacity = bufferSize;
	}
	public RingBuffer(int initSize){
		this.bufferSize = initSize;
		this.buffer = new byte[bufferSize];
		rbCapacity = bufferSize;
	}
	public void clear(){
		Arrays.fill(buffer,(byte)0xff);
		this.rHead = 0;
		this.wTail = 1;
	}
	public int canRead()
	{
		if (rHead < wTail)
		{
			return wTail - rHead-1;
		}
		return rbCapacity - (rHead - wTail);
	}
	public int canWrite()
	{
		return rbCapacity - canRead()-1;
	}
	int fla=1;
	public int read(byte[] rdata,int srcpos ,int count) throws InterruptedException
	{
		int copySz = 0;  int tailAvailSz = 0;
		if(count<0) {
			throw new RuntimeException("count must more than zero!");
		}
		while (count > canRead() )
		{
			sop("count>canread()");
			Thread.sleep(10);
		}
		if (rHead < wTail)
		{
			copySz = count;
			//memcpy(data, rHead, copySz);
			System.arraycopy(buffer, rHead, rdata, srcpos, count);
			rHead += copySz;
			return copySz;
		}
		else
		{
			tailAvailSz = rbCapacity - rHead ;
			if (count <= tailAvailSz)
			{
				//memcpy(data, rHead, count);
				System.arraycopy(buffer, rHead, rdata, srcpos, count);
				rHead += count;
				if (rHead == rbCapacity)
				{
					rHead= 0;
				}
				return count;
			}
			else
			{
				//memcpy(data, rHead, tailAvailSz);
				System.arraycopy(buffer, rHead, rdata, srcpos, tailAvailSz);
				rHead= 0;
				return tailAvailSz+read(rdata, srcpos+tailAvailSz, count-tailAvailSz);
			}
		}
	}
	public int write(byte[]data, int srcpos,int count) throws InterruptedException
	{
	    int tailAvailSz = 0;
	    while (count > canWrite() )
	    {
	    	sop("count > canWrite() ");
			Thread.sleep(10);
	    }
	    if (rHead <wTail)
	    {
	        tailAvailSz = rbCapacity - wTail;
	        if (count <= tailAvailSz)
	        {
	            //memcpy(wTail, data, count);
	        	System.arraycopy(data,srcpos,buffer ,wTail, count);
	            wTail += count;
	            if (wTail == rbCapacity)
	            {
	                wTail = 0;
	            }
	            return count;
	        }
	        else
	        {
	           // memcpy(wTail, data, tailAvailSz);
	        	System.arraycopy(data,srcpos,buffer ,wTail, tailAvailSz);
	            wTail = 0;

	            return tailAvailSz + write(data,tailAvailSz+srcpos ,count-tailAvailSz);
	        }
	    }
	    else
	    {
	       // memcpy(wTail, data, count);
	    	System.arraycopy(data,srcpos,buffer ,wTail, count);
	        wTail += count;
	        return count;
	    }
	}
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}

