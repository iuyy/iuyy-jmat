package net.iuyy.jmat.base;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:22
 * @description
 */
public abstract class AbstractMatrix implements Matrix {

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

}
