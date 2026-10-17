package io.github.seal139.jSwarm.misc.memory;

public class Xmpl implements NativeResources{

	final Runnable deallocator;
	public Xmpl() {
		deallocator = GarbageCollector.getInstance().register(this);
	}
	
	@Override
	public void close() throws Exception {
		deallocator.run();		
	}

	@Override
	public long[] getNativeAddress() {
		return null;
	}

}
