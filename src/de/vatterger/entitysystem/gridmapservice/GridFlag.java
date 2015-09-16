package de.vatterger.entitysystem.gridmapservice;

public class GridFlag {
	public static final int NETWORKED	= 	1,
							COLLISION	= 	2,
							STATIC		= 	4,
							AI			=	8,
							INACTIVE		=	16;
	
	private int flag;
	
	public GridFlag() {
		flag = 0;
	}
	
	public GridFlag(int initialFlag) {
		flag = initialFlag;
	}
	
	public int flag() {
		return flag;
	}

	public GridFlag addFlag(int f) {
		flag = flag | f;
		return this;
	}
	
	public GridFlag removeFlag(int f) {
		flag = flag & (~f);
		return this;
	}
	
	public boolean hasAllFlagsOf(int f) {
		return (flag & f) == f;
	}
	
	public boolean hasSameFlagsAs(int f) {
		return (flag & f) == flag;
	}
	
	public GridFlag setFlag(int f) {
		flag = f;
		return this;
	}

	public boolean isFlagSet(int pos) {
	   return (flag & (1 << pos)) != 0;
	}
	
	@Override
	public String toString() {
		return ""+Integer.toBinaryString(flag);
	}
}
