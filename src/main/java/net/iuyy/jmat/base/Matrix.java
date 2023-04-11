package net.iuyy.jmat.base;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:21
 * @description
 *
 */
public interface Matrix<E> extends Arithmetic {

    Object[][] getData();

    int getRows();

    int getColumns();

    E get(int row, int column);

    Double getDouble(int row, int column);

    String getStr(int i, int j);

    void set(int row, int column, E data);
}
