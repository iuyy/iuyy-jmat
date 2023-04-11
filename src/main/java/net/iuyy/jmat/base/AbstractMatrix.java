package net.iuyy.jmat.base;

import net.iuyy.jmat.common.Pattern;
import net.iuyy.jmat.common.Symbol;
import net.iuyy.jmat.exception.TypeException;
import net.iuyy.jmat.matrix.MixMatrix;
import net.iuyy.jmat.matrix.NumberMatrix;
import net.iuyy.jmat.matrix.StringMatrix;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:22
 * @description
 */
public abstract class AbstractMatrix<E> implements Matrix<E> {

    protected Object[][] data;
    protected int rows;
    protected int columns;

    @Override
    public Object[][] getData() {
        return this.data;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int row, int column){
        return (E)(data[row][column]);
    }

    @Override
    public Double getDouble(int row, int column){
        Object cell = data[row][column];
        if (String.valueOf(cell).matches(Pattern.NUMBER)) {
            return Double.parseDouble(String.valueOf(cell));
        } else {
            throw new TypeException("该值不是数值类型或不能转换成数据类型！");
        }
    }

    @Override
    public void set(int row, int column, E data){
        this.data[row][column] = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append("\n");
        for (int i = 0; i < this.rows; i++) {
            sb.append("\t[");
            for (int j = 0; j < this.columns; j++) {
                sb.append("\t").append(get(i,j));
            }
            sb.append("]\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private Matrix getNumberMatrix(Matrix origin, Number obj, String symbol) {
        NumberMatrix result = new NumberMatrix(origin.getRows(), origin.getColumns());
        for (int i = 0; i < origin.getRows(); i++) {
            for (int j = 0; j < origin.getColumns(); j++) {
                Double newValue = null;
                if (symbol.equals(Symbol.ADDITION)) {
                    newValue = origin.getDouble(i, j) + obj.doubleValue();
                } else if (symbol.equals(Symbol.SUBTRACTION)) {
                    newValue = origin.getDouble(i, j) - obj.doubleValue();
                } else if (symbol.equals(Symbol.MULTIPLICATION)) {
                    newValue = origin.getDouble(i, j) * obj.doubleValue();
                } else if (symbol.equals(Symbol.DIVISION_RIGHT)) {
                    newValue = origin.getDouble(i, j) / obj.doubleValue();
                } else if (symbol.equals(Symbol.DIVISION_LEFT)) {
                    newValue = obj.doubleValue() / origin.getDouble(i, j);
                }
                result.set(i, j, newValue);
            }
        }
        return result;
    }

    private Matrix getStringMatrix(Matrix origin, String obj) {
        StringMatrix result = new StringMatrix(origin.getRows(), origin.getColumns());
        // 拼接字符串
        for (int i = 0; i < origin.getRows(); i++) {
            for (int j = 0; j < origin.getColumns(); j++) {
                String newValue = origin.get(i, j) + obj;
                result.set(i, j, newValue);
            }
        }
        return result;
    }

    private Matrix getMixMatrix(Matrix origin, Matrix target, String symbol) {
        int r = Math.max(origin.getRows(), target.getRows());
        int c = Math.max(origin.getColumns(), target.getColumns());
        Matrix<Object> result = new MixMatrix<>(r, c);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                int cIndex1 = j >= origin.getColumns() ? 0 : j;
                int rIndex1 = i >= origin.getRows() ? 0 : i;
                Double aValue = origin.getDouble(rIndex1, cIndex1);

                int rIndex2 = target.getRows() == 1 ? 0 : i;
                int cIndex2 = target.getColumns() == 1 ? 0 : j;
                Double bValue = target.getDouble(rIndex2, cIndex2);

                Double newValue = null;
                if (symbol.equals(Symbol.ADDITION)) {
                    newValue = aValue + bValue;
                } else if (symbol.equals(Symbol.SUBTRACTION)) {
                    newValue = aValue - bValue;
                } else if (symbol.equals(Symbol.MULTIPLICATION)) {
                    newValue = aValue * bValue;
                } else if (symbol.equals(Symbol.DIVISION_RIGHT)) {
                    newValue = aValue / bValue;
                } else if (symbol.equals(Symbol.DIVISION_LEFT)) {
                    newValue = bValue / aValue;
                }

                result.set(i, j, newValue);
            }
        }
        return result;
    }

    /**
     * 加法
     * C = A + B
     * C = plus(A,B)
     *
     * C = A + B 通过对应元素相加将数组 A 和 B 相加。
     * 如果一个输入是字符串数组，则 plus 将对应的元素作为字符串追加。
     *
     * A 和 B 的大小必须相同或兼容。如果 A 和 B 的大小兼容，
     * 则这两个数组会隐式扩展以相互匹配。例如，如果 A 或 B 中的一个是标量，
     * 则该标量与另一个数组的每个元素相结合。
     * 此外，具有不同方向的向量（一个为行向量，另一个为列向量）会隐式扩展以形成矩阵。
     * @return
     */
    @Override
    public Matrix plus(Object obj){
        if (obj instanceof Number) {
            // 加数值
            return getNumberMatrix(this, (Number) obj, Symbol.ADDITION);

        } else if (obj instanceof String) {
            return getStringMatrix(this, (String) obj);

        } else if (obj instanceof Matrix) {
            return getMixMatrix(this, (Matrix) obj, Symbol.ADDITION);

        } else {
            throw new TypeException("该参数的数据类型不被支持！");
        }
    }

    /**
     * 减法
     * C = A - B
     * C = minus(A,B)
     *
     * C = A - B 从 A 数组中减去 B 数组，方法是将对应元素相减。
     * A 和 B 的大小必须相同或兼容。
     *
     * 如果 A 和 B 的大小兼容，则这两个数组会隐式扩展以相互匹配。
     * 例如，如果 A 或 B 是标量，则该标量与另一个数组的每个元素相结合。
     * 此外，具有不同方向的向量（一个为行向量，另一个为列向量）会隐式扩展以形成矩阵。
     * @return
     */
    @Override
    public Matrix minus(Object obj){
        if (obj instanceof Number) {
            return getNumberMatrix(this, (Number) obj, Symbol.SUBTRACTION);

        } else if (obj instanceof Matrix) {
            return getMixMatrix(this, (Matrix) obj, Symbol.SUBTRACTION);

        } else {
            throw new TypeException("该参数的数据类型不被支持！");
        }
    }

    /**
     * 乘法
     * C = A.*B
     * C = times(A,B)
     *
     * C = A.*B 通过将对应的元素相乘来将数组 A 和 B 相乘。
     * A 和 B 的大小必须相同或兼容。
     *
     * 如果 A 和 B 的大小兼容，则这两个数组会隐式扩展以相互匹配。
     * 例如，如果 A 或 B 中的一个是标量，则该标量与另一个数组的每个元素相结合。
     * 此外，具有不同方向的向量（一个为行向量，另一个为列向量）会隐式扩展以形成矩阵。
     * @return
     */
    @Override
    public Matrix times(Object obj){
        if (obj instanceof Number) {
            return getNumberMatrix(this, (Number) obj, Symbol.MULTIPLICATION);

        } else if (obj instanceof Matrix) {
            return getMixMatrix(this, (Matrix) obj, Symbol.MULTIPLICATION);

        } else {
            throw new TypeException("该参数的数据类型不被支持！");
        }
    }

    /**
     * 矩阵乘法
     * C = A*B
     * C = mtimes(A,B)
     *
     * C = A*B 是 A 和 B 的矩阵乘积。如果 A 是 m×p 矩阵，B 是 p×n 矩阵，
     * 则 C 是通过以下公式定义的 m×n 矩阵：
     *           p
     * C(i,j) =  ∑ A(i,k)B(k,j)
     *          k=1
     *该定义说明 C(i,j) 是 A 第 i 行与 B 第 j 列的内积。您可以使用 MATLAB® 冒号运算符来书写该定义，如下所示
     *
     * C(i,j) = A(i,:)*B(:,j)
     * 对于非标量 A 和 B，A 的列数必须等于 B 的行数。对于非标量输入，矩阵乘法不一定能互换位置。
     * 也就是说，A*B 不一定等于 B*A。至少一方输入为标量时，A*B 等于 A.*B，这时可以互换位置。
     *
     * 新矩阵的row = A.row,column = b.column
     * A = {a b c
     *      d e f}
     * B = {1 2
     *      3 4
     *      5 6}
     * A * B = {[a1+b2+c3] [a2+b4+C6]
     *          [d1+e2+f3] [d2+e4+f6]}
     * @return
     */
    @Override
    public Matrix mTimes(Matrix matrix){
        NumberMatrix newMatrix = new NumberMatrix(this.rows, matrix.getColumns());
        for (int rowIndex = 0; rowIndex < this.rows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < matrix.getColumns(); columnIndex++) {
                double product = 0;
                for (int i = 0; i < matrix.getRows(); i++) {
                    double aCell = this.getDouble(rowIndex, i);
                    double bCell = matrix.getDouble(i, columnIndex);
                    product += (aCell * bCell);
                }
                newMatrix.set(rowIndex, columnIndex, product);
            }
        }
        return newMatrix;
    }

    /**
     * 数组右除
     * x = A./B
     * x = rdivide(A,B)
     *
     * x = A./B 用 A 的每个元素除以 B 的对应元素。A 和 B 的大小必须相同或兼容。
     *
     * 如果 A 和 B 的大小兼容，则这两个数组会隐式扩展以相互匹配。
     * 例如，如果 A 或 B 中的一个是标量，则该标量与另一个数组的每个元素相结合。
     * 此外，具有不同方向的向量（一个为行向量，另一个为列向量）会隐式扩展以形成矩阵。
     * @return
     */
    @Override
    public Matrix rDivide(Object obj){
        if (obj instanceof Number) {
            return getNumberMatrix(this, (Number) obj, Symbol.DIVISION_RIGHT);

        } else if (obj instanceof Matrix) {
            return getMixMatrix(this, (Matrix) obj, Symbol.DIVISION_RIGHT);

        } else {
            throw new TypeException("该参数的数据类型不被支持！");
        }
    }

    /**
     * 数组左除
     * x = B.\A
     * x = ldivide(B,A)
     *
     * x = B.\A 用 A 的每个元素除以 B 的对应元素。A 和 B 的大小必须相同或兼容。
     *
     * 如果 A 和 B 的大小兼容，则这两个数组会隐式扩展以相互匹配。
     * 例如，如果 A 或 B 中的一个是标量，则该标量与另一个数组的每个元素相结合。
     * 此外，具有不同方向的向量（一个为行向量，另一个为列向量）会隐式扩展以形成矩阵。
     * @return
     */
    @Override
    public Matrix lDivide(Object obj){
        if (obj instanceof Number) {
            return getNumberMatrix(this, (Number) obj, Symbol.DIVISION_LEFT);

        } else if (obj instanceof Matrix) {
            return getMixMatrix(this, (Matrix) obj, Symbol.DIVISION_LEFT);

        } else {
            throw new TypeException("该参数的数据类型不被支持！");
        }
    }

    /**
     * 求解关于 x 的线性方程组 xA = B
     * x = B/A
     * x = mrdivide(B,A)
     *
     * x = B/A 对线性方程组 x*A = B 求解 x。矩阵 A 和 B 必须具有相同的列数。
     * 如果 A 未正确缩放或接近奇异值，MATLAB® 将会显示警告信息，但还是会执行计算。
     *
     * 1. 如果 A 是标量，那么 B/A 等于 B./A。
     * 2. 如果 A 是 n×n 方阵，B 是 n 列矩阵，那么 x = B/A 是方程 x*A = B 的解（如果存在解的话）。
     * 3. 如果 A 是矩形 m×n 矩阵，且 m ~= n，B 是 n 列矩阵，那么 x=B/A 返回方程组 x*A = B 的最小二乘解。
     * @return
     */
    @Override
    public Matrix mrDivide(){
        return null;
    }

}
