package io.github.seal139.jSwarm.misc.memory;

import java.lang.ref.ReferenceQueue;

public class GarbageCollector {
	// In JNI, use @CriticalNative
	
	private static GarbageCollector instance = null;
	public static GarbageCollector getInstance() {
		if(instance == null) {
			instance = new GarbageCollector();
		}
		
		return instance;
	}

	private final ReferenceQueue<NativeResources> REF_QUEUE = new ReferenceQueue<>();
	
	private GarbageCollector() {
		final Thread cleanerDaemon = new Thread(() -> {
			while(true) {
				try {
					((NativeCleaner) REF_QUEUE.remove()).clean();
				} catch (InterruptedException e) {
					return;
				}
			}
		});
		
	
		cleanerDaemon.setPriority(Thread.MAX_PRIORITY);
		cleanerDaemon.setDaemon(true);
		
		// Start upon explicitly invoked;
		cleanerDaemon.start();	
	}
	
	public Runnable register(NativeResources referent) {
		return new NativeCleaner(referent, REF_QUEUE)::clean;
	}
	
	public void run() {
		System.gc();
	}
}
