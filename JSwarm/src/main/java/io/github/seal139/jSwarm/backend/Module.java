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

package io.github.seal139.jSwarm.backend;

import io.github.seal139.jSwarm.misc.NativeCleaner.DeallocatedException;
import io.github.seal139.jSwarm.misc.NativeCleaner.NativeResources;

/**
 * Represent GPU module or programs that holds {@link Kernel}
 */
public interface Module extends NativeResources {

    /**
     * Get kernel function.
     *
     * @param kernelName Name of the {@link Kernel}.
     * @return {@link Kernel}.
     */
    Kernel getKernel(String kernelName) throws BackendException, DeallocatedException;
}
