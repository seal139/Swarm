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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * It indicates that an annotated method may be intrinsified by the platform
 * target. Providing more faster and efficient process. The implementation
 * provided will be used if code run in JVM. Mostly for debugging purpose.
 *
 * <br/>
 * <br/>
 * The standard contract that must be satisfied by using this annotation is the
 * method called must give exactly the same result in any platform and the Java
 * implementation itself.
 */

@Retention(RetentionPolicy.SOURCE)
public @interface FunctionIntrinsic {

}
