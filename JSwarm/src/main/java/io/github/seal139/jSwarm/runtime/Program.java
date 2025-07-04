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

package io.github.seal139.jSwarm.runtime;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

import io.github.seal139.jSwarm.datatype.Vector;
import io.github.seal139.jSwarm.misc.FunctionIntrinsic;

/**
 * Represent GPGPU Program that contains kernel function
 */
public abstract class Program {

    private int globalRangeX, globalRangeY, globalRangeZ, //
            localRangeX, localRangeY, localRangeZ;

    private int currentGlobalRangeX, currentGlobalRangeY, currentGlobalRangeZ, //
            currentLocalRangeX, currentLocalRangeY, currentLocalRangeZ, //
            currentRangeX, currentRangeY, currentRangeZ; //

    private long totalRangeX, totalRangeY, totalRangeZ;

    private CyclicBarrier synchronizer;

    protected <T extends Number> void set(Vector<T> vec, long index, T value) {
        vec.set(index, value);
    }

    protected <T extends Number> T get(Vector<T> vec, long index) {
        return vec.get(index);
    }

    int count = 0;

    /**
     * Await all thread to complete execution at this point before continue <br/>
     * <br/>
     *
     * @throws BrokenBarrierException
     * @throws InterruptedException
     */
    @FunctionIntrinsic
    protected void synchronize() {
        try {
            this.synchronizer.await();
        }
        catch (InterruptedException | BrokenBarrierException e) {
        }
    }

    /**
     * Calculate the maximum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final int max(int a, int b) {
        return FastMath.max(a, b);
    }

    /**
     * Calculate the maximum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final long max(long a, long b) {
        return FastMath.max(a, b);
    }

    /**
     * Calculate the maximum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float maxfp32(float a, float b) {
        return FastMath.max(a, b);
    }

    /**
     * Calculate the maximum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double maxfp64(double a, double b) {
        return FastMath.max(a, b);
    }

    /**
     * Calculate the minimum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final int min(int a, int b) {
        return FastMath.min(a, b);
    }

    /**
     * Calculate the minimum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final long min(long a, long b) {
        return FastMath.min(a, b);
    }

    /**
     * Calculate the minimum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float minfp32(float a, float b) {
        return FastMath.min(a, b);
    }

    /**
     * Calculate the minimum value of the input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double minfp64(double a, double b) {
        return FastMath.min(a, b);
    }

    /**
     * Calculate the absolute value of the input argument
     *
     * @param i
     * @return
     */
    @FunctionIntrinsic
    protected final float abs(int i) {
        return FastMath.abs(i);
    }

    /**
     * Calculate the absolute value of the input argument
     *
     * @param i
     * @return
     */
    @FunctionIntrinsic
    protected final float abs(long i) {
        return FastMath.abs(i);
    }

    /**
     * Calculate the absolute value of the input argument
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float absfp32(float f) {
        return FastMath.abs(f);
    }

    /**
     * Calculate the absolute value of the input argument
     *
     * @param d
     * @return
     */
    @FunctionIntrinsic
    protected final double absfp64(double d) {
        return FastMath.abs(d);
    }

    /**
     * Determine whether argument is infinite.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isinf(float f) {
        return Float.isInfinite(f) ? 1 : 0;
    }

    /**
     * Determine whether argument is infinite.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isinf(double f) {
        return Double.isInfinite(f) ? 1 : 0;
    }

    /**
     * Determine whether argument is finite.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isfinite(float f) {
        return Float.isFinite(f) ? 1 : 0;
    }

    /**
     * Determine whether argument is finite.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isfinite(double f) {
        return Double.isFinite(f) ? 1 : 0;
    }

    /**
     * Determine whether argument is NaN.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isnan(float f) {
        return Float.isNaN(f) ? 1 : 0;
    }

    /**
     * Determine whether argument is NaN.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int isnan(double f) {
        return Double.isNaN(f) ? 1 : 0;
    }

    /**
     * Calculate the arc cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float acosfp32(float f) {
        return (float) FastMath.acos(f);
    }

    /**
     * Calculate the arc cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double acosfp64(double f) {
        return FastMath.acos(f);
    }

    /**
     * Calculate the nonnegative inverse hyperbolic cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float acoshfp32(float f) {
        return (float) FastMath.acosh(f);
    }

    /**
     * Calculate the nonnegative inverse hyperbolic cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double acoshfp64(double f) {
        return FastMath.acosh(f);
    }

    /**
     * Calculate the arc sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float asinfp32(float f) {
        return (float) FastMath.asin(f);
    }

    /**
     * Calculate the arc sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double asinfp64(double f) {
        return FastMath.asin(f);
    }

    /**
     * Calculate the inverse hyperbolic sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float asinhfp32(float f) {
        return (float) FastMath.asinh(f);
    }

    /**
     * Calculate the inverse hyperbolic sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double asinhfp64(double f) {
        return FastMath.asinh(f);
    }

    /**
     * Calculate the arc tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float atanfp32(float f) {
        return (float) FastMath.atan(f);
    }

    /**
     * Calculate the arc tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double atanfp64(double f) {
        return FastMath.atan(f);
    }

    /**
     * Calculate the arc tangent of the ratio of first and second input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float atanfp32(float a, float b) {
        return (float) FastMath.atan2(a, b);
    }

    /**
     * Calculate the arc tangent of the ratio of first and second input arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double atanfp64(double a, double b) {
        return FastMath.atan2(a, b);
    }

    /**
     * Calculate the inverse hyperbolic tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float atanhfp32(float f) {
        return (float) FastMath.atanh(f);
    }

    /**
     * Calculate the inverse hyperbolic tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double atanhfp64(double f) {
        return FastMath.atanh(f);
    }

    /**
     * Calculate the cube root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float cbrtfp32(float f) {
        return (float) FastMath.cbrt(f);
    }

    /**
     * Calculate the cube root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double cbrtfp64(double f) {
        return FastMath.cbrt(f);
    }

    /**
     * Calculate ceiling of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float ceilfp32(float f) {
        return (float) FastMath.ceil(f);
    }

    /**
     * Calculate ceiling of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double ceilfp64(double f) {
        return FastMath.ceil(f);
    }

    /**
     * Create value with given magnitude, copying sign of second value.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float copysignfp32(float a, float b) {
        return FastMath.copySign(a, b);
    }

    /**
     * Create value with given magnitude, copying sign of second value.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double copysignfp64(double a, double b) {
        return FastMath.copySign(a, b);
    }

    /**
     * Calculate the cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float cosfp32(float f) {
        return (float) FastMath.cos(f);
    }

    /**
     * Calculate the cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double cosfp64(double f) {
        return FastMath.cos(f);
    }

    /**
     * Calculate the hyperbolic cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float coshfp32(float f) {
        return (float) FastMath.cosh(f);
    }

    /**
     * Calculate the hyperbolic cosine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double coshfp64(double f) {
        return FastMath.cosh(f);
    }

    /**
     * Calculate the cosine of the input argument * pi
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float cospifp32(float f) {
        return (float) (FastMath.PI * FastMath.cosh(f));
    }

    /**
     * Calculate the cosine of the input argument * pi
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double cospifp64(double f) {
        return FastMath.PI * FastMath.cos(f);
    }

    /**
     * Calculate the complementary error function of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float erfcfp32(float f) {
        return (float) Erf.erfc(f);
    }

    /**
     * Calculate the complementary error function of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double erfcfp64(double f) {
        return Erf.erfc(f);
    }

    /**
     * Calculate the error function of the input argument.
     *
     * @param f
     * @return
     */

    @FunctionIntrinsic
    protected final float erffp32(float f) {
        return (float) Erf.erf(f);
    }

    /**
     * Calculate the error function of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double erffp64(double f) {
        return Erf.erf(f);
    }

    /**
     * Calculate the base e exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float expfp32(double f) {
        return (float) FastMath.exp(f);
    }

    /**
     * Calculate the base e exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double expfp64(double f) {
        return FastMath.exp(f);
    }

    /**
     * Calculate the base 2 exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float exp2fp32(float f) {
        return (float) FastMath.pow(2, f);
    }

    /**
     * Calculate the base 2 exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double exp2fp64(double f) {
        return FastMath.pow(2, f);
    }

    /**
     * Calculate the base 10 exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float exp10fp32(float f) {
        return (float) FastMath.pow(10, f);
    }

    /**
     * Calculate the base 10 exponential of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double exp10fp64(double f) {
        return FastMath.pow(10, f);
    }

    /**
     * Calculate the base e exponential of the input argument, minus 1.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float expm1fp32(float f) {
        return (float) FastMath.expm1(f);
    }

    /**
     * Calculate the base e exponential of the input argument, minus 1.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double expm1fp64(double f) {
        return FastMath.expm1(f);
    }

    /**
     * Compute the positive difference between a and b.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float fdimfp32(float a, float b) {
        return absfp32(a - b);
    }

    /**
     * Compute the positive difference between a and b.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double fdimfp64(float a, float b) {
        return absfp64(a - b);
    }

    /**
     * Calculate the largest integer less than or equal to x.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float floorfp32(float f) {
        return (float) FastMath.floor(f);
    }

    /**
     * Calculate the largest integer less than or equal to x.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double floorfp64(double f) {
        return FastMath.floor(f);
    }

    /**
     * Compute a * b + c as a single operation.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    @FunctionIntrinsic
    protected final float fmafp32(float a, float b, float c) {
        return Math.fma(a, b, c);
    }

    /**
     * Compute a * b + c as a single operation.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    @FunctionIntrinsic
    protected final double fmafp64(double a, double b, double c) {
        return Math.fma(a, b, c);
    }

    /**
     * Calculate the single-precision floating-point remainder of a / b.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float modfp32(float a, float b) {
        return a - ((int) (a / b) * b);
    }

    /**
     * Calculate the double-precision floating-point remainder of a / b.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double modfp64(double a, double b) {
        return a - ((int) (a / b) * b);
    }

    /**
     * Extract mantissa and exponent of a floating-point value.
     *
     * @param f
     * @param exp
     * @return
     */
    @FunctionIntrinsic
    protected final float frexpfp32(float f, int exp[]) {
        if (f == 0.0) {
            exp[0] = 0;
            return 0.0f;
        }

        int   e = Math.getExponent(f);                  // base-2 exponent
        float m = (float) (f / FastMath.scalb(1.0, e)); // same as x / 2^e

        // Normalize mantissa to [0.5, 1.0)
        if (absfp32(m) < 0.5) {
            m *= 2;
            e -= 1;
        }

        exp[0] = e;
        return m;
    }

    /**
     * Extract mantissa and exponent of a floating-point value.
     *
     * @param f
     * @param exp
     * @return
     */
    @FunctionIntrinsic
    protected final double frexpfp64(double f, int exp[]) {
        if (f == 0.0) {
            exp[0] = 0;
            return 0.0;
        }

        int    e = Math.getExponent(f);        // base-2 exponent
        double m = f / FastMath.scalb(1.0, e); // same as x / 2^e

        // Normalize mantissa to [0.5, 1.0)
        if (absfp64(m) < 0.5) {
            m *= 2;
            e -= 1;
        }

        exp[0] = e;
        return m;
    }

    /**
     * Calculate the square root of the sum of squares of two arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float hypotfp32(float a, float b) {
        return (float) FastMath.hypot(a, b);
    }

    /**
     * Calculate the square root of the sum of squares of two arguments.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double hypotfp64(double a, double b) {
        return FastMath.hypot(a, b);
    }

    /**
     * Compute the unbiased integer exponent of the argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int ilogbfp32(float f) {
        return FastMath.getExponent(f);
    }

    /**
     * Compute the unbiased integer exponent of the argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final int ilogbfp64(double f) {
        return FastMath.getExponent(f);
    }

    /**
     * Calculate the value of f * 2 ^ n
     *
     * @param f
     * @param n
     * @return
     */
    @FunctionIntrinsic
    protected final float ldexpfp32(float f, int n) {
        return FastMath.scalb(f, n);
    }

    /**
     * Calculate the value of f * 2 ^ n
     *
     * @param f
     * @param n
     * @return
     */
    @FunctionIntrinsic
    protected final double ldexpfp64(double f, int n) {
        return FastMath.scalb(f, n);
    }

    /**
     * Calculate the natural logarithm of the absolute value of the gamma function
     * of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float lgammafp32(float f) {
        return (float) Gamma.logGamma(f);
    }

    /**
     * Calculate the natural logarithm of the absolute value of the gamma function
     * of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double lgammafp64(double f) {
        return Gamma.logGamma(f);
    }

    /**
     * Calculate the base e logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float logfp32(float f) {
        return (float) FastMath.log(f);
    }

    /**
     * Calculate the base e logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double logfp64(double f) {
        return FastMath.log(f);
    }

    /**
     * Calculate the base 2 logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float log2fp32(float f) {
        return (float) FastMath.log(2, f);
    }

    /**
     * Calculate the base 2 logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double log2fp64(double f) {
        return FastMath.log(2, f);
    }

    /**
     * Calculate the base 10g logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float log10fp32(float f) {
        return (float) FastMath.log(10, f);
    }

    /**
     * Calculate the base 10 logarithm of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double log10fp64(double f) {
        return FastMath.log(10, f);
    }

    /**
     * Calculate the value of log e (1 + f);
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float log1pfp32(float f) {
        return (float) FastMath.log1p(f);
    }

    /**
     * Calculate the value of log e (1 + f);
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double log1pfp64(double f) {
        return FastMath.log1p(f);
    }

    /**
     * Calculate the floating-point representation of the exponent of the input
     * argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float logbfp32(float f) {
        if (f == 0.0) {
            return Float.NEGATIVE_INFINITY;
        }
        if (Double.isInfinite(f)) {
            return Float.POSITIVE_INFINITY;
        }
        if (Double.isNaN(f)) {
            return Float.NaN;
        }

        return Math.getExponent(f);
    }

    /**
     * Calculate the floating-point representation of the exponent of the input
     * argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double logbfp64(double f) {
        if (f == 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (Double.isInfinite(f)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(f)) {
            return Double.NaN;
        }

        return Math.getExponent(f);
    }

    /**
     * Return next representable double-precision floating-point value after
     * argument a in the direction of b
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float nextafterfp32(float a, float b) {
        return FastMath.nextAfter(a, b);
    }

    /**
     * Return next representable double-precision floating-point value after
     * argument a in the direction of b
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double nextafterfp64(double a, double b) {
        return FastMath.nextAfter(a, b);
    }

    /**
     * Calculate the value of first argument to the power of second argument.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float powfp32(float a, float b) {
        return (float) FastMath.pow(a, b);
    }

    /**
     * Calculate the value of first argument to the power of second argument.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double powfp32(double a, double b) {
        return FastMath.pow(a, b);
    }

    /**
     * Compute single-precision floating-point remainder.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final float remainderfp32(float a, float b) {
        return a - (FastMath.round(a / b) * b);
    }

    /**
     * Compute double-precision floating-point remainder.
     *
     * @param a
     * @param b
     * @return
     */
    @FunctionIntrinsic
    protected final double remainderfp64(double a, double b) {
        return a - (FastMath.round(a / b) * b);
    }

    /**
     * Compute single-precision floating-point remainder and part of quotient.
     *
     * @param a
     * @param b
     * @param quo
     * @return
     */
    protected final float remquofp32(float a, float b, int quo[]) {
        double quotient = a / b;
        long   q        = Math.round(quotient); // IEEE-style rounding
        float  rem      = a - (q * b);

        // Extract lowest 3 bits (like C/CUDA typically do)
        quo[0] = (int) (q & 0x7); // adjust as needed (0x7 = 3 bits)

        return rem;
    }

    /**
     * Compute double-precision floating-point remainder and part of quotient.
     *
     * @param a
     * @param b
     * @param quo
     * @return
     */
    protected final double remquofp64(double a, double b, int quo[]) {
        double quotient = a / b;
        long   q        = Math.round(quotient); // IEEE-style rounding
        double rem      = a - (q * b);

        // Extract lowest 3 bits (like C/CUDA typically do)
        quo[0] = (int) (q & 0x7); // adjust as needed (0x7 = 3 bits)

        return rem;
    }

    /**
     * Round to nearest integer value in floating-point
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float rintfp32(float f) {
        return (float) FastMath.rint(f);
    }

    /**
     * Round to nearest integer value in floating-point
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double rintfp64(double f) {
        return FastMath.rint(f);
    }

    /**
     * Scale floating-point input by integer power of two.
     *
     * @param f
     * @param n
     * @return
     */
    @FunctionIntrinsic
    protected final float scalbfp32(float f, int n) {
        return FastMath.scalb(f, n);
    }

    /**
     * Scale floating-point input by integer power of two.
     *
     * @param f
     * @param n
     * @return
     */
    @FunctionIntrinsic
    protected final double scalbfp64(double f, int n) {
        return FastMath.scalb(f, n);
    }

    /**
     * Round to nearest integer value in floating-point.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float roundfp32(float f) {
        return FastMath.round(f);
    }

    /**
     * Round to nearest integer value in floating-point.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float roundfp64(float f) {
        return FastMath.round(f);
    }

    /**
     * Calculate the reciprocal of the square root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float rsqrtfp32(float f) {
        return (float) (1.0f / FastMath.sqrt(f));
    }

    /**
     * Calculate the reciprocal of the square root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float rsqrtfp64(float f) {
        return (float) (1.0f / FastMath.sqrt(f));
    }

    /**
     * Calculate the sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float sinfp32(float f) {
        return (float) FastMath.sin(f);
    }

    /**
     * Calculate the sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double sinfp64(double f) {
        return FastMath.sin(f);
    }

    /**
     * Calculate the hyperbolic sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float sinhfp32(float f) {
        return (float) FastMath.sinh(f);
    }

    /**
     * Calculate the hyperbolic sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double sinhfp64(double f) {
        return FastMath.sinh(f);
    }

    /**
     * Calculate the sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float sinpifp32(float f) {
        return (float) (FastMath.PI * FastMath.sin(f));
    }

    /**
     * Calculate the sine of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double sinpifp64(double f) {
        return FastMath.PI * FastMath.sin(f);
    }

    /**
     * Calculate the reciprocal of the square root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float sqrtfp32(float f) {
        return (float) FastMath.sqrt(f);
    }

    /**
     * Calculate the reciprocal of the square root of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double sqrtfp32(double f) {
        return FastMath.sqrt(f);
    }

    /**
     * Calculate the tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float tanfp32(float f) {
        return (float) FastMath.tan(f);
    }

    /**
     * Calculate the tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double tanfp64(double f) {
        return FastMath.tan(f);
    }

    /**
     * Calculate the hyperbolic tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float tanhfp32(float f) {
        return (float) FastMath.tanh(f);
    }

    /**
     * Calculate the hyperbolic tangent of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double tanhfp64(double f) {
        return FastMath.tanh(f);
    }

    /**
     * Calculate the gamma function of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float tgammafp32(float f) {
        return (float) Gamma.gamma(f);
    }

    /**
     * Calculate the gamma function of the input argument.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double tgammafp64(double f) {
        return Gamma.gamma(f);
    }

    /**
     * Truncate input argument to the integral part.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final float truncfp32(float f) {
        return (int) f;
    }

    /**
     * Truncate input argument to the integral part.
     *
     * @param f
     * @return
     */
    @FunctionIntrinsic
    protected final double truncfp64(double f) {
        return (long) f;
    }

    // ========= Setter =========
    @FunctionIntrinsic
    public final int globalRangeX() {
        return this.globalRangeX;
    }

    @FunctionIntrinsic
    public final int globalRangeY() {
        return this.globalRangeY;
    }

    @FunctionIntrinsic
    public final int globalRangeZ() {
        return this.globalRangeZ;
    }

    @FunctionIntrinsic
    public final int localRangeX() {
        return this.localRangeX;
    }

    @FunctionIntrinsic
    public final int localRangeY() {
        return this.localRangeY;
    }

    @FunctionIntrinsic
    public final int localRangeZ() {
        return this.localRangeZ;
    }

    @FunctionIntrinsic
    public long totalRangeX() {
        return this.totalRangeX;
    }

    @FunctionIntrinsic
    public long totalRangeY() {
        return this.totalRangeY;
    }

    @FunctionIntrinsic
    public long totalRangeZ() {
        return this.totalRangeZ;
    }

    @FunctionIntrinsic
    public final int currentGlobalRangeX() {
        return this.currentGlobalRangeX;
    }

    @FunctionIntrinsic
    public final int currentGlobalRangeY() {
        return this.currentGlobalRangeY;
    }

    @FunctionIntrinsic
    public final int currentGlobalRangeZ() {
        return this.currentGlobalRangeZ;
    }

    @FunctionIntrinsic
    public final int currentLocalRangeX() {
        return this.currentLocalRangeX;
    }

    @FunctionIntrinsic
    public final int currentLocalRangeY() {
        return this.currentLocalRangeY;
    }

    @FunctionIntrinsic
    public final int currentLocalRangeZ() {
        return this.currentLocalRangeZ;
    }

    @FunctionIntrinsic
    public final int currentRangeX() {
        return this.currentRangeX;
    }

    @FunctionIntrinsic
    public final int currentRangeY() {
        return this.currentRangeY;
    }

    @FunctionIntrinsic
    public final int currentRangeZ() {
        return this.currentRangeZ;
    }

    // ==

    final void setNdRange(int lx, int gx, long tx, int ly, int gy, long ty, int lz, int gz, long tz) {
        this.localRangeX  = lx;
        this.globalRangeX = gx;
        this.totalRangeX  = tx;

        this.localRangeY  = ly;
        this.globalRangeY = gy;
        this.totalRangeY  = ty;

        this.localRangeZ  = lz;
        this.globalRangeZ = gz;
        this.totalRangeZ  = tz;

        this.currentRangeX = 0;
        this.currentRangeY = 0;
        this.currentRangeZ = 0;

        this.currentLocalRangeX = 0;
        this.currentLocalRangeY = 0;
        this.currentLocalRangeZ = 0;

        this.currentGlobalRangeX = 0;
        this.currentGlobalRangeY = 0;
        this.currentGlobalRangeZ = 0;
    }

    final void setLocalRange(int x, int y, int z) {
        this.currentLocalRangeX = x;
        this.currentLocalRangeY = y;
        this.currentLocalRangeZ = z;
    }

    final void setCurrentRange(int x, int y, int z) {
        this.currentRangeX = x;
        this.currentRangeY = y;
        this.currentRangeZ = z;
    }

    final void setCurrentGlobalRangeX(int currentGlobalRangeX, int currentRangeX) {
        this.currentGlobalRangeX = currentGlobalRangeX;
        this.currentRangeX       = currentRangeX + this.currentLocalRangeX;
    }

    final void setCurrentGlobalRangeY(int currentGlobalRangeY, int currentRangeY) {
        this.currentGlobalRangeY = currentGlobalRangeY;
        this.currentRangeY       = currentRangeY + this.currentLocalRangeY;
    }

    final void setCurrentGlobalRangeZ(int currentGlobalRangeZ, int currentRangeZ) {
        this.currentGlobalRangeZ = currentGlobalRangeZ;
        this.currentRangeZ       = currentRangeZ + this.currentLocalRangeZ;
    }

    final void setSynchronizer(CyclicBarrier synchronizer) { this.synchronizer = synchronizer; }
}