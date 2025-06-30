package io.github.seal139.jSwarm.example;

import io.github.seal139.jSwarm.datatype.FloatVector;
import io.github.seal139.jSwarm.runtime.Program;

public final class ExampleKernel extends Program {

    private int getScalarIndex(int x, int y) {
        return((x * this.localRangeX()) + y);
    }

    public void matrixMultiplication(FloatVector matrix1, FloatVector matrix2, FloatVector result) {
        int y = this.currentLocalRangeY();
        int x = this.currentLocalRangeX();

        float temp = 0.0f;

        for (int i = 0; i < this.localRangeX(); i++) {
            float xi = get(matrix1, getScalarIndex(i, y));
            float yi = get(matrix2, getScalarIndex(x, i));

            // temp += (xi * yi);
            temp = fmafp32(xi, yi, temp);
        }

        set(result, getScalarIndex(x, y), temp);
    }

    public void fmaAccumulator(FloatVector v1, FloatVector v2, FloatVector output, int count) {

        float mul = 0;

        if ((currentLocalRangeX() % 5) == 0) {
            mul = 5 * (get(v1, currentRangeX()) + get(v2, currentRangeX()));
        }
        else if ((currentLocalRangeX() % 3) == 0) {
            mul = 3 * (get(v1, currentRangeX()) + get(v2, currentRangeX()));
        }
        else if ((currentLocalRangeX() % 2) == 0) {
            mul = 2 * (get(v1, currentRangeX()) + get(v2, currentRangeX()));
        }

        set(output, currentRangeX(), mul);

        synchronize();

        float accumulation = 0;
        if (currentLocalRangeX() == 0) {

            int start = (count * currentGlobalRangeX());
            int end   = start + 1024;

            for (int i = start; i < end; i++) {
                accumulation += get(output, i);
            }
        }

        synchronize();

        if (currentLocalRangeX() == 0) {
            set(output, currentGlobalRangeX(), accumulation);
        }
    }
}
