package net.iuyy.jmat.matrix;

import net.iuyy.jmat.base.AbstractMatrix;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:22
 * @description 数值类型矩阵
 */
public class NumberMatrix extends AbstractMatrix<Number> {

    private Number[][] data;

    public NumberMatrix() {
    }

    public NumberMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Number[rows][columns];
    }

    public NumberMatrix(Number[][] data) {
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = data;
    }

    @Override
    public Number[][] getData() {
        return this.data;
    }

    @Override
    public Number get(int row, int column){
        return data[row][column];
    }

    @Override
    public Double getDouble(int row, int column){
        Number cell = data[row][column];
        return cell != null ? cell.doubleValue() : 0d;
    }

    @Override
    public void set(int row, int column, Number data){
        this.data[row][column] = data;
    }

}
