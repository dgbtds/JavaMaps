package Config;

import java.util.concurrent.LinkedBlockingQueue;

public class ladder {
	public LinkedBlockingQueue<byte[]> buildQueue=new LinkedBlockingQueue<byte[]>();
	public LinkedBlockingQueue<Integer> trigger=new LinkedBlockingQueue<Integer>();
	private String ip;
	private int port;
	private int sensorNum;
	private int ladHeader;
	private int ladtrailer;
	public int startNum;//芯片配置起始文件号
	public int controlN=0;//组装超时控制号
	public ladder(int sensorNum,int startNum,int ladHeader,int ladtrailer,String ip,int port) {
		this.sensorNum=sensorNum;
		this.startNum=startNum;
		this.ladHeader=ladHeader;
		this.ladtrailer=ladtrailer;
		this.ip=ip;
		this.port=port;
	}
	public int  getSensorNum() {
		return  sensorNum;
	}
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public int getHeader() {
		return ladHeader;
	}
	public int getTrailer() {
		return ladtrailer;
	}

}
