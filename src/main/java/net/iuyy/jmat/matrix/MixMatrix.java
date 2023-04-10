package net.iuyy.jmat.matrix;

import net.iuyy.jmat.base.AbstractMatrix;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:22
 * @description M * N 的任意数据类型矩阵
 */
public class MixMatrix<E> extends AbstractMatrix<E> {

    public MixMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Object[rows][columns];
    }
}
