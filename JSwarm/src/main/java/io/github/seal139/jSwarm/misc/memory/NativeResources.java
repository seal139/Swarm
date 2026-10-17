package io.github.seal139.jSwarm.misc.memory;

public interface NativeResources extends AutoCloseable {

	long[] getNativeAddress();
	
}
