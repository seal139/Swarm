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

/**
 * Represent platform driver. This is the low level implementation that
 * communicate directly with runtime environment
 */
public interface Platform {

    /**
     * Get short name as identifier. This name should be unique.
     *
     * @return Identifier name.
     */
    String getName();

    /**
     * Get full driver name.
     *
     * @return Driver name.
     */
    String getFullName();

    /**
     * Mark vendor, device, or case -specific platform that can offer higher
     * performance. This method is used to differ preferable and fallback platform.
     * In case of Device is appear in multiple platform, an {@link Executor} object
     * for the same device from primary platform will showed up first.
     *
     * @return True when this driver is marked as 'preferable' platform
     */
    boolean isPrimary();

    /**
     * Get platform version. In case of multiple {@link Executor device} has
     * different supported version. This will return the oldest version to ensure
     * maximum compatibility possible
     *
     * @return Platform version
     */
    String getVersion();

    /**
     * Get devices {@link Executor} associated with this Platform
     *
     * @return
     */
    Executor[] getDevices();
}
