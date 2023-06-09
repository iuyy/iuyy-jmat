package net.iuyy.jmat.base;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:25
 * @description 算术运算
 */
public interface Arithmetic {

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
     * @return 矩阵
     */
    Matrix plus(Object obj);

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
     * @return 矩阵
     */
    Matrix minus(Object obj);

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
     * @return 矩阵
     */
    Matrix times(Object obj);

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
     * @return 矩阵
     */
    Matrix mTimes(Matrix matrix);

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
     * @return 矩阵
     */
    Matrix rDivide(Object obj);

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
     * @return 矩阵
     */
    Matrix lDivide(Object obj);

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
     * @return 矩阵
     */
    Matrix mrDivide();

}
