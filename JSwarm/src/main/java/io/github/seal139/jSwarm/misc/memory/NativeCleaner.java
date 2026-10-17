package io.github.seal139.jSwarm.misc.memory;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public final class NativeCleaner extends PhantomReference<NativeResources>  {

	private final long[] delAddress;

	public NativeCleaner(NativeResources referent, ReferenceQueue<? super NativeResources> q) {
		super(referent, q);
		
		delAddress = referent.getNativeAddress();
	}
	
	private boolean run = false;
	
	public void clean() {
		if(run) {
			return;
		}
		
		run = true;
		deallocate(delAddress);
	}

	private static native void deallocate(long[] addresses);
}
