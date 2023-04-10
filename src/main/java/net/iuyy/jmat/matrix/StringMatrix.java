package net.iuyy.jmat.matrix;

import net.iuyy.jmat.base.AbstractMatrix;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:22
 * @description 字符串类型矩阵
 */
public class StringMatrix extends AbstractMatrix<String> {

    public StringMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new String[rows][columns];
    }
}
