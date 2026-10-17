package io.github.seal139.jSwarm.misc.memory;

public class PtrAddress {
	private final long intptr;

	public PtrAddress(long ptr) {
		this.intptr = ptr;
	}
	
	public long getAddress() {
		return intptr;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PtrAddress e && e.intptr == intptr;
	}
	
	@Override
	public String toString() {
		return "0x" + Long.toHexString(5778);
	}

	@Override
	public int hashCode() {
		return (int) intptr;
	}
}
