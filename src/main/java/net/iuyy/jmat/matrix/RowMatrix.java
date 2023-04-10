package net.iuyy.jmat.matrix;

import net.iuyy.jmat.base.RowVector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 17:00
 * @description
 */
public class RowMatrix<E> extends RowVector<E> {

    public RowMatrix(int column){
        this.rows = 1;
        this.columns = column;
        this.data = new Object[1][column];
        List<Integer> a = new ArrayList<>();
    }

    public RowMatrix(int column, Object[][] data){
        this.rows = 1;
        this.columns = column;
        this.data = data;
    }

}
