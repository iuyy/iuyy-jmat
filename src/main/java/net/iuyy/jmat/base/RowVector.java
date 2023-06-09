package net.iuyy.jmat.base;

/**
 * @author iuyy
 * @version v1.0
 * @corporation Copyright by iuyy.net
 * @date 2023-04-07 16:57
 * @description
 */
public abstract class RowVector<E> extends AbstractMatrix<E> implements Vector<E> {

    @Override
    public boolean isVector(){
        return getRows() == 1;
    }

}
