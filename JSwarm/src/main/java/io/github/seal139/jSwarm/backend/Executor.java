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

import io.github.seal139.jSwarm.runtime.DeviceType;

/**
 * Represent device hardware.
 */
public interface Executor {

    /**
     * Get default execution context.
     *
     * @return Default managed {@link Context}.
     */
    Context getDefaultContext() throws BackendException;

    /**
     * Get the type of the hardware device used by this Executor.
     *
     * @return The {@link DeviceType}.
     */
    DeviceType getType();

    /**
     * Device UUID. This value is unique per physical hardware
     *
     * @return UUID
     */
    String getUuid();

    /**
     * Get the name of the device.
     *
     * @return Name of the device. This value is not unique and cannot be used to
     *         identify physical hardware.
     */
    String getName();

    /**
     * Get logical processor in the device. This value may not includes logical
     * cores per compute unit as it usually hidden by the vendor.
     *
     * @return The number of logical processor core.
     */
    long getComputeUnit();

    /**
     * Get total memory in bytes that can be used by the device.
     *
     * @return Total memory in bytes.
     */
    long getTotalMemory();

    /**
     * Return maximum ND-Range supported by the device. ND-Range represent total
     * dimension count that can be used to process data. <br/>
     * <br/>
     * For example, this function will return 1 if the device only accept single
     * dimension in X axis. return 2 for two-dimensional of X and Y axis. And 3 for
     * three-dimensional of X, Y, and Z axis. <br/>
     * <br/>
     * In most device, this function will return 3 that represent three-dimensional
     * of X, Y, and Z axis.
     *
     * @return Max dimension supported.
     */
    int getMaxNDRange();

    /**
     * Get maximum upper-bound value possible for each dimension
     *
     * @return Maximum global size bound per dimension
     */
    long[] getMaxGlobalSize();

    /**
     * Get maximum local upper-bound value possible for each dimension. This is an
     * extension of {@link #getMaxGlobalSize()} where total dimension is
     * {@link #getMaxGlobalSize() Global Size} multiplied by Local Size
     *
     * @return Maximum local size bound per dimension
     */
    long[] getMaxLocalSize();

    /**
     * Get maximum local thread that can be initialized to be run together. However,
     * the actual thread that run simultaneously may be differ and smaller than this
     * value and the count is hardware and vendor -specific. <br/>
     * <br/>
     *
     * This value is related to {@link #getMaxLocalSize()}. When launching
     * {@link Kernel}, the local size for each dimension multiplied must not
     * exceeded this value.
     *
     * @return Maximum local thread
     */
    long getMaxLocalThread();

    /**
     * Get device maximum theoretical floating point performance. By default, This
     * value is based on {@link #getComputeUnit()} and it's core count. As the core
     * count usually is not visible to public by the API, the computed FLOPS may not
     * be accurate. However, this value is sufficient and good enough to be used to
     * determine the computation power per device (for sorting purpose).
     *
     * @return FLOPS number in GFlops.
     */
    long getFlops();
}
