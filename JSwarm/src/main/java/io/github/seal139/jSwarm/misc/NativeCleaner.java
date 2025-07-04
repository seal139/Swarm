/*
* Swarm - An Extensible and Modular GPGPU framework
* Copyright (C) 2025  Septian Pramana / Mercu Buana University
*
* This file is part of Swarm.
*
* Swarm is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2.
*
* Swarm is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; If not, see <http://www.gnu.org/licenses/>.
*/

package io.github.seal139.jSwarm.misc;

import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;

import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources.Deallocator;

/**
 * GC daemon to delete resources when object is garbage collected
 */
public final class NativeCleaner {

    /**
     * While this interface allows try-with-resources statements, it's not necessary
     * to use that. This interface is bind with Java Garbage Collector to perform
     * cleanup when object is phantom reachable.
     */
    public interface NativeResources extends AutoCloseable {

        /**
         * When object is phantom reachable, it means the data is no longer exist. In
         * other words, it's also means the pointer and any other native reference is
         * lost. This special interface is a workaround which is responsible to perform
         * cleanup for native resources for that object.
         */
        public interface Deallocator {

            /**
             * Perform native resource cleanup
             */
            void clean();
        }

        /**
         * Get object deallocator
         *
         * @return {@link Deallocator} for this object
         */
        Deallocator getDeallocator();

        boolean isClosed();
    }

    public static class DeallocatedException extends Exception {

        private static final long serialVersionUID = 5227062002232052580L;

        public DeallocatedException() {
            super("Resources is closed");
        }

    }

    private static final Cleaner CLEANER = Cleaner.create();

    /**
     * Register object to be monitored with this cleaner.
     *
     * @param obj Object to be monitored
     * @return {@link Cleaner} for manual cleanup
     */
    public static Cleanable register(NativeResources obj) {
        final Deallocator del = obj.getDeallocator();

        return CLEANER.register(obj, () -> del.clean());
    }
}
