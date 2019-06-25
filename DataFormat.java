package Config;

import java.util.ArrayList;

class DataFormat{
	public ArrayList<Integer>formatByte=new ArrayList<Integer>();
	public ArrayList<Integer>formatinfo=new ArrayList<Integer>();
	public DataFormat append(int Bytenum,int info ) {
		formatByte.add( new Integer(Bytenum));
		formatinfo.add(  new Integer(info));
		return this;
	}
}