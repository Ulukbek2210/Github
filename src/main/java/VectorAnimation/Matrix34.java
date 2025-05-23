package VectorAnimation;// Matrix3.java
import java.util.Arrays;

public class Matrix34 {
    public double[][] data = new double[3][3];

    // Constructor for identity matrix
    public Matrix34() {
        data[0][0] = 1; data[0][1] = 0; data[0][2] = 0;
        data[1][0] = 0; data[1][1] = 1; data[1][2] = 0;
        data[2][0] = 0; data[2][1] = 0; data[2][2] = 1;
    }

    // Constructor from an array
    public Matrix34(double[][] data) {
        if (data.length != 3 || data[0].length != 3) {
            throw new IllegalArgumentException("Matrix must be 3x3");
        }
        this.data = new double[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, 3);
        }
    }

    // Static factory methods for common transformations
    public static Matrix34 createTranslation(double tx, double ty) {
        Matrix34 m = new Matrix34();
        m.data[0][2] = tx;
        m.data[1][2] = ty;
        return m;
    }

    public static Matrix34 createRotation(double angleRadians) {
        Matrix34 m = new Matrix34();
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        m.data[0][0] = cos; m.data[0][1] = -sin;
        m.data[1][0] = sin; m.data[1][1] = cos;
        return m;
    }

    public static Matrix34 createScaling(double sx, double sy) {
        Matrix34 m = new Matrix34();
        m.data[0][0] = sx;
        m.data[1][1] = sy;
        return m;
    }

    /**
     * Performs standard matrix multiplication (this * other).
     * @param other The matrix to multiply by.
     * @return The resulting matrix.
     */
    public Matrix34 multiply(Matrix34 other) {
        Matrix34 result = new Matrix34();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.data[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result.data[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }

    // Transform a 2D point (x, y) as [x, y, 1] column vector
    public double[] transformPoint(double x, double y) {
        double[] point = {x, y, 1};
        double[] transformed = new double[3];
        for (int i = 0; i < 3; i++) {
            transformed[i] = 0;
            for (int j = 0; j < 3; j++) {
                transformed[i] += data[i][j] * point[j];
            }
        }
        return new double[]{transformed[0], transformed[1]}; // Return x, y
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(Arrays.toString(data[i])).append("\n");
        }
        return sb.toString();
    }
}