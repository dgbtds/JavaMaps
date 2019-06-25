package Config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class analyse {
	private int BUFFER_SIZE=1024*10;
	public int length =0;
	public  int alyDt(RingBuffer rbuffer,String File_name,ladder lad,int getdtNum,int eventnumberperfile) throws Exception {
		String ip=lad.getIp();
		File ipfile=new File((File_name+"/"+ip+"-"+lad.getPort()));
		sop(ipfile.getPath());
		if(!ipfile.exists())
		  ipfile.mkdirs();
		else {
			sop("file exists!!");
		}
		
		DataFormat Mapsformat=new DataFormat();
		Mapsformat.append(4, lad.getHeader()).append(4, lad.getTrailer());
	
		
		byte[] buffer=new byte[BUFFER_SIZE];
		int packnum=0;
		int  wrongHeader=0,wrongtail=0,canwrite=0;
		int datalength=0;
		int eventcount=0;
		int dataBH=0,ByteCount=0;
		while( packnum<getdtNum)
		{ 
			eventcount=0;
			dataBH++;
			File fp=new File(File_name+"/"+ip+"-"+lad.getPort()+"/"+dataBH+".dat");
			sop(fp.getPath());

					while( (eventcount<eventnumberperfile)&&(packnum<getdtNum) )
					{
						length=rbuffer.read(buffer, 0, 4);
					
						if(length==-1)
							return length;

						//lad.ladHeader=0x01cccccc;
						if(this.BtI(buffer,0, 4,true) !=Mapsformat.formatinfo.get(0)) {
							for (int i = 0; i < 4; i++) {
								System.out.printf("buffer[%d]:%02x\n",i,buffer[i]);
							}
							sop("\n!!!!!header wrong");
							//wrongHeader=4;
						}
						//System.out.printf("buffer(0123):%02x %02x %02x %02x",(buffer[0]),(buffer[1]),(buffer[2]),buffer[3]);
						
						while ( this.BtI(buffer,0, 4,true) !=Mapsformat.formatinfo.get(0))  //　校验头部，注意括号　移位并
						{
							buffer[0]=buffer[1];
							buffer[1]= buffer[2];
							buffer[2]= buffer[3];
							length= rbuffer.read(buffer,3,1);
							
							System.out.printf("buffer[%d]:%02x \n",wrongHeader+4,(buffer[3]));
							wrongHeader++;
						//	if(wrongHeader>1000) {
							//	length=-1;
							//	return length;
							//}
							if(length==-1)
								return length;
						}
						
						//System.out.printf("wrongHeader:%dByte \n",wrongHeader);

						//校验８字节
						length= rbuffer.read(buffer,4,4);
						if(length==-1)
							return length;
						length= rbuffer.read(buffer,8,8);
						if(length==-1)
							return length;

						if(length==8)
						{
							datalength=this.BtI(buffer, 12, 4, false)*2;
						}
						length= rbuffer.read(buffer,16, datalength+4);
						if(length==-1)
							return length;
						if(length==datalength+4)
						{
							if( this.BtI(buffer, 16+datalength, 4, true) ==Mapsformat.formatinfo.get(1))
							{
								packnum++;
								eventcount++;
								
//								BufferedOutputStream bos=new BufferedOutputStream(new 
//										FileOutputStream(fp,true));
//								bos.write(buffer, 0, 20+datalength);
//								bos.close();
								ByteCount+=20+datalength;
								
								byte[]ZZbuffer=Arrays.copyOf(buffer, 20+datalength);
								lad.buildQueue.put(ZZbuffer);
								lad.trigger.put(this.BtI(buffer, 4, 4, false));
							   // System.out.printf("lad[0]---trigger:%d\n",lad.trigger.poll());
							   // lad.trigger.put(this.BtI(buffer, 4, 4, false));
							    
								if(packnum<0)
								//if((packnum>19900)|(packnum<100))
								{
									System.out.printf("\n\n%s : ",ip);
									System.out.printf("the %d pck is correct;\n",packnum);
									System.out.printf("datalength:%d\n",datalength);
									canwrite=rbuffer.canWrite();
									System.out.printf("read %dbytes<<<<--space--: %d ;OutSize:%d ; wrongDSize:%d\n ", datalength+20,canwrite,ByteCount,wrongHeader+wrongtail);
									System.out.printf("****frameCount :%08x \n", this.BtI(buffer, 8, 4, true) );
									System.out.printf("****trigger :%d \n\n", this.BtI(buffer, 4, 4, false) );
								}
							}
							else
							{
								wrongtail+=datalength+20;
								System.out.printf("\n\n%s : \n",ip);
								System.out.printf("datalength:%d\n",datalength);
								System.out.printf("trailer of the %d package is error: ",packnum);
								for(int i=0;i<16;i++) 
								{
									System.out.printf(" %02x",buffer[i]);
								}
								for(int i=0;i<datalength;i++) 
								{
									if(i%10==0)
									System.out.printf("\n ");
									System.out.printf(" %02x",buffer[16+i]);
								}
								System.out.printf("\n ");
								for(int i=0;i<4;i++) 
								{
									System.out.printf(" %02x",buffer[i+datalength+16]);
								}
								System.out.printf("\n ");
							}
						}
					}
		}
		return ByteCount/1000;
	}
	public int BtI(byte[]data,int off,int n,boolean nixu) {
		if(nixu)
		{
			switch (n) {
			case 1:
				return (int) data[off];
			case 2:
				return (int) (data[off+1] & 0xff) | (data[off] << 8 & 0xff00);
			case 3:
				return (int) (data[off+2] & 0xff) | (data[off+1] << 8 & 0xff00) | (data[off] << 16 & 0xff0000);
			case 4:
				return (int) (data[off+3] & 0xff) | (data[off+2] << 8 & 0xff00) | (data[off+1] << 16 & 0xff0000)
						| (data[off] << 24 & 0xff000000);
			default:
				return 0;
			}
		}
		else {
			switch (n) {
			case 1:
				return (int) data[off];
			case 2:
				return (int) (data[off] & 0xff) | (data[off+1] << 8 & 0xff00);
			case 3:
				return (int) (data[off] & 0xff) | (data[off+1] << 8 & 0xff00) | (data[off+2] << 16 & 0xff0000);
			case 4:
				return (int) (data[off] & 0xff) | (data[off+1] << 8 & 0xff00) | (data[off+2] << 16 & 0xff0000)
						| (data[off+3] << 24 & 0xff000000);
			default:
				return 0;
			}
		}
	}
	public static void sop(Object obj) {
		System.out.println(obj);
	}
	public boolean isClose() {
		if(length==-1)
			return true;
		else
			return false;
	}
}



















