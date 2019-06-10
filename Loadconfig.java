package Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Loadconfig {
	private static int SenId=0x3;
	private static int Rudpdatalength=193;
	private static StringBuilder[] udpconfig=new StringBuilder[Rudpdatalength];
	private static byte[] command=new byte[1024*10];
	private static byte[] oxcfg=new byte[Rudpdatalength];

	public Loadconfig() {

	}
	public static void load(double threshold,ladder lad,int i) throws IOException {
		File udpconfigFile=new File("/home/wy/eclipse-workspace/Maps/ConfigInfo/"+threshold+"/"+(lad.startNum+i)+".dat");
		FileInputStream fis=new FileInputStream(udpconfigFile);
		int a=0,b=0,c=0; 
		while((a=fis.read())!=-1) {
			if(((char)a!=' ')&&(a!=10)) {
				b=fis.read();
				if(c<Rudpdatalength) {
					String str=(char)a+""+(char)b+"";
					udpconfig[c]=new StringBuilder(str);
					oxcfg[c]=(byte) Integer.parseUnsignedInt(str, 16);
//					System.out.println("udp"+udpconfig[c]);
//					System.out.println("ox"+oxcfg[c]);//按照１６进制转为２进制但是转为１０进制时由于符号位会和１６进制值不同
//					System.out.println("");
					c++; 
				}
				else {
					fis.close();
					throw new RuntimeException("configFile is error");
				}
			}
		}
		//		int count=0;
		//		for(StringBuilder bi:udpconfig) {
		//			System.out.println(count+":"+bi);
		//			count++;
		//		}
		fis.close();
	}
	public static void Syscfg(double threshold,ladder lad,String ip,int port) throws Exception {
		/*配置流程：reset,config,configEnable,sensorStatus,sensorSelect
		 * sitcp=udpheader+sitcphesder(8byte)+data
		 * */
		command[0]=(byte) 0xff;
		command[1]=(byte) 0x80;//模式80写，c0读写
		command[2]=0x00;

		command[4]=0x00;
		command[5]=0x00;
		command[6]=0x00;

		//reset 
		sop("reset" );
		command[3]=0x01;//发送数据长度 
		command[7]=(byte) 0xf0;//发送地址
		command[8]=0x01;//数据内容
		SendAcheck(ip,port,command,command[3]);
		Thread.sleep(10);

		command[3]=0x01;//发送数据长度 
		command[7]=(byte) 0xf0;//发送地址
		command[8]=0x00;//数据内容
		SendAcheck(ip,port,command,command[3]);
		Thread.sleep(10);

		//config,configEnable
		
		for(int i=0;i<lad.sensorNum;i++) {
			load( threshold, lad, i);
			//config
			sop("config" );
			command[3]=113;//发送数据长度 
			command[7]=(byte) 0xf0;//发送地址
	        for(int j=8;j<8+Rudpdatalength;j++)
	        {
	            command[j]=oxcfg[j-8];
	           if ( (j-8<20)&&(j-8>14) )
	           {
	        	   System.out.printf(" j=%d; command[]:%d  %02x\n",j-8,command[j]&0xff,command[j]);
	           }
	        }
			SendAcheck(ip,port,command,command[3]);
			Thread.sleep(10);

			
			//configEnable
			sop("configEnable" );
			command[3]=0x01;//发送数据长度 
			command[7]=(byte) 0xf1;//发送地址
			command[8]=0x01;//数据内容
			SendAcheck(ip,port,command,command[3]);
			Thread.sleep(10);

			command[3]=0x01;//发送数据长度 
			command[7]=(byte) 0xf1;//发送地址
			command[8]=0x00;//数据内容
			SendAcheck(ip,port,command,command[3]);	
			Thread.sleep(10);


		}
		
		//sensorStatus
		sop("sensorStatus" );
		command[1]=(byte) 0xc0;
		command[3]=0x01;//发送数据长度 
		command[7]=(byte) 0xf2;//发送地址
		command[8]=0x00;//数据内容
		byte[]Rdata=SendAcheck(ip,port,command,command[3]);
//		if(Rdata[8]!=1) {
//			System.out.printf("0xf2 return data  ERROR!:%02x\n", Rdata[8]);
//			throw new RuntimeException("sensor status error;");
//		}
		
		//sensorSelect 
		sop("sensorSelect ");
		byte Wdata[]=new byte[2];
		Wdata[0]=(byte)(SenId&0xff);
	    Wdata[1]=(byte)((SenId&0xff00)>>8);
	    System.out.printf("sen8=%x\n",Wdata[0]);
	    System.out.printf("sen9=%x\n",Wdata[1]);
	    
		command[1]=(byte) 0x80;
		command[3]=0x02;//发送数据长度 
		command[7]=(byte) 0xf3;//发送地址
		command[8]=Wdata[0];command[9]= Wdata[1];//数据内容
		SendAcheck(ip,port,command,command[3]);
		 
	}

	public static byte[] SendAcheck(String ip,int port,byte[]data,int length) throws SocketException, IOException{
		DatagramSocket udpSocket=new DatagramSocket();
		DatagramPacket sendPacket=new DatagramPacket
				(data, 8+length,InetAddress.getByName(ip),port);
		udpSocket.send(sendPacket);

		byte[]Rdata=new byte[data.length];
		DatagramPacket RtnPacket=new DatagramPacket(Rdata, 8+length);
		udpSocket.receive(RtnPacket);
		for (int i = 0; i < 8+length; i++) {
			if(data[i]!=Rdata[i])
			{
				System.out.printf("%d Ack wrong:send(%x)!=recv(%x)\n",i,data[i],Rdata[i]);
			}
		}
		udpSocket.close();
		return Rdata;
	}
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}
class ladder{
	public int sensorNum;
	public int startNum;//芯片配置起始文件号
	public ladder(int sensorNum,int startNum) {
		this.sensorNum=sensorNum;
		this.startNum=startNum;
	}
}



