package io.github.seal139.jSwarm.runtime;

import io.github.seal139.jSwarm.backend.Executor;

/**
 * Define work size for kernel
 */
public final class NdRange {

    private final int x, y, z;
    private final int lx, ly, lz;

    private NdRange(int x, int lx, int y, int ly, int z, int lz) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
    }

    /**
     * Set work size as one dimensional.
     *
     * @param maxGlobalRange Define maximum global range. This value must not
     *                       exceeded {@link Executor#getMaxGlobalSize()} at index 0
     *                       (Dimension -X)
     * @param maxLocalRange  Define maximum local range. This value must not
     *                       exceeded {@link Executor#getMaxLocalSize()} at index 0
     *                       (Dimension -X) and also must not exceeded
     *                       {@link Executor#getMaxLocalThread()}
     * @return New 1D {@link NdRange} Object
     */
    public static NdRange oneDimensional(int maxGlobalRange, int maxLocalRange) {
        return new NdRange(maxGlobalRange, maxLocalRange, 1, 1, 1, 1);
    }

    /**
     * Set work size as two dimensional.
     *
     * @param maxGlobalRangeX Define maximum global range. This value must not
     *                        exceeded {@link Executor#getMaxGlobalSize()} at index
     *                        0 (Dimension -X)
     * @param maxGlobalRangeY Define maximum global range. This value must not
     *                        exceeded {@link Executor#getMaxGlobalSize()} at index
     *                        1 (Dimension -Y)
     * @param maxLocalRangeX  Define maximum local range. This value must not
     *                        exceeded {@link Executor#getMaxLocalSize()} at index 0
     *                        (Dimension -X). When multiplied with maxLocalRangeY,
     *                        the value must not exceeded
     *                        {@link Executor#getMaxLocalThread()}
     * @param maxLocalRangeY  Define maximum local range. This value must not
     *                        exceeded {@link Executor#getMaxLocalSize()} at index 1
     *                        (Dimension -Y). When multiplied with maxLocalRangeX,
     *                        the value must not exceeded
     *                        {@link Executor#getMaxLocalThread()}
     * @return New 2D {@link NdRange} Object
     */
    public static NdRange twoDimensional(int maxGlobalRangeX, int maxGlobalRangeY, //
                                         int maxLocalRangeX, int maxLocalRangeY) {

        return new NdRange(maxGlobalRangeX, maxLocalRangeX, maxGlobalRangeY, maxLocalRangeY, 1, 1);
    }

    /**
     * Set work size as three dimensional.
     *
     * @param maxGlobalRangeX Define maximum global range. This value must not
     *                        exceeded {@link Executor#getMaxGlobalSize()} at index
     *                        0 (Dimension -X)
     * @param maxGlobalRangeY Define maximum global range. This value must not
     *                        exceeded {@link Executor#getMaxGlobalSize()} at index
     *                        1 (Dimension -Y)
     * @param maxGlobalRangeZ Define maximum global range. This value must not
     *                        exceeded {@link Executor#getMaxGlobalSize()} at index
     *                        2 (Dimension -Z)
     * @param maxLocalRangeX  Define maximum local range. This value must not
     *                        exceeded {@link Executor#getMaxLocalSize()} at index 0
     *                        (Dimension -X). When multiplied with maxLocalRangeY
     *                        and maxLocalRangeZ, the value must not exceeded
     *                        {@link Executor#getMaxLocalThread()}
     * @param maxLocalRangeY  Define maximum local range. This value must not
     *                        exceeded {@link Executor#getMaxLocalSize()} at index 1
     *                        (Dimension -Y). When multiplied with maxLocalRangeX
     *                        and maxLocalRangeZ, the value must not exceeded
     *                        {@link Executor#getMaxLocalThread()}
     * @param maxLocalRangeZ  Define maximum local range. This value must not
     *                        exceeded {@link Executor#getMaxLocalSize()} at index 2
     *                        (Dimension -Z). When multiplied with maxLocalRangeX
     *                        and maxLocalRangeY, the value must not exceeded
     *                        {@link Executor#getMaxLocalThread()}
     * @return New 3D {@link NdRange} Object
     */
    public static NdRange threeDimensional(int maxGlobalRangeX, int maxGlobalRangeY, int maxGlobalRangeZ, //
                                           int maxLocalRangeX, int maxLocalRangeY, int maxLocalRangeZ) {

        return new NdRange(maxGlobalRangeX, maxLocalRangeX, maxGlobalRangeY, maxLocalRangeY, maxGlobalRangeZ, maxLocalRangeZ);
    }

    /**
     * Global work size for dimension -X
     *
     * @return Work size
     */
    public int getXGlobal() { return this.x; }

    /**
     * Global work size for dimension -Y
     *
     * @return Work size
     */
    public int getYGlobal() { return this.y; }

    /**
     * Global work size for dimension -Z
     *
     * @return Work size
     */
    public int getZGlobal() { return this.z; }

    /**
     * Local work size for dimension -X
     *
     * @return Work size
     */
    public int getXLocal() { return this.lx; }

    /**
     * Local work size for dimension -Y
     *
     * @return Work size
     */
    public int getYLocal() { return this.ly; }

    /**
     * Local work size for dimension -Z
     *
     * @return Work size
     */
    public int getZLocal() { return this.lz; }

    /**
     * Total work size for dimension -X. Based on {@link #getXGlobal()} *
     * {@link #getXLocal()}.
     *
     * @return Total work size
     */
    public long getTotalX() { return getXGlobal() * getXLocal(); }

    /**
     * Total work size for dimension -Y. Based on {@link #getYGlobal()} *
     * {@link #getYLocal()}.
     *
     * @return Total work size
     */
    public long getTotalY() { return getYGlobal() * getYLocal(); }

    /**
     * Total work size for dimension -Z. Based on {@link #getZGlobal()} *
     * {@link #getZLocal()}.
     *
     * @return Total work size
     */
    public long getTotalZ() { return getZGlobal() * getZLocal(); }

    /**
     * Total work size for all dimensions. Based on {@link #getTotalX()} *
     * {@link #getTotalY()} * {@link #getTotalZ()}
     *
     * @return Grand total work size
     */
    public long getTotalWorkSize() { return getTotalX() * getTotalY() * getTotalZ(); }
}
