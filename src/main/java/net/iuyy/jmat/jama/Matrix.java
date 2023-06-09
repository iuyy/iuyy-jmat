package net.iuyy.jmat.jama;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StreamTokenizer;
import net.iuyy.jmat.jama.util.*;

/**
   Jama = Java Matrix class.
<P>
   The Java Matrix Class provides the fundamental operations of numerical
   linear algebra.  Various constructors create Matrices from two dimensional
   arrays of double precision floating point numbers.  Various "gets" and
   "sets" provide access to submatrices and matrix elements.  Several methods 
   implement basic matrix arithmetic, including matrix addition and
   multiplication, matrix norms, and element-by-element array operations.
   Methods for reading and printing matrices are also included.  All the
   operations in this version of the Matrix Class involve real matrices.
   Complex matrices may be handled in a future version.
<P>
   Five fundamental matrix decompositions, which consist of pairs or triples
   of matrices, permutation vectors, and the like, produce results in five
   decomposition classes.  These decompositions are accessed by the Matrix
   class to compute solutions of simultaneous linear equations, determinants,
   inverses and other matrix functions.  The five decompositions are:
<P><UL>
   <LI>Cholesky Decomposition of symmetric, positive definite matrices.
   <LI>LU Decomposition of rectangular matrices.
   <LI>QR Decomposition of rectangular matrices.
   <LI>Singular Value Decomposition of rectangular matrices.
   <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square matrices.
</UL>
<DL>
<DT><B>Example of use:</B></DT>
<P>
<DD>Solve a linear system A x = b and compute the residual norm, ||b - A x||.
<P><PRE>
      double[][] vals = {{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};
      Matrix A = new Matrix(vals);
      Matrix b = Matrix.random(3,1);
      Matrix x = A.solve(b);
      Matrix r = A.times(x).minus(b);
      double rnorm = r.normInf();
</PRE></DD>
</DL>

@author The MathWorks, Inc. and the National Institute of Standards and Technology.
@version 5 August 1998
*/

public class Matrix implements Cloneable, java.io.Serializable {

/* ------------------------
   Class variables
 * ------------------------ */

   /** Array for internal storage of elements.
   @serial internal array storage.
   */
   private double[][] data;

   /** Row and column dimensions.
   @serial row dimension.
   @serial column dimension.
   */
   private int rows, columns;

/* ------------------------
   Constructors
 * ------------------------ */

   /** Construct an m-by-n matrix of zeros. 
   @param rows    Number of rows.
   @param columns    Number of colums.
   */

   public Matrix (int rows, int columns) {
      this.rows = rows;
      this.columns = columns;
      this.data = new double[rows][columns];
   }

   /** Construct an m-by-n constant matrix.
   @param rows    Number of rows.
   @param columns    Number of colums.
   @param s    Fill the matrix with this scalar value.
   */

   public Matrix (int rows, int columns, double s) {
      this.rows = rows;
      this.columns = columns;
      data = new double[rows][columns];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = s;
         }
      }
   }

   /** Construct a matrix from a 2-D array.
   @param data    Two-dimensional array of doubles.
   @exception  IllegalArgumentException All rows must have the same length
   @see        #constructWithCopy
   */

   public Matrix (double[][] data) {
      rows = data.length;
      columns = data[0].length;
      for (int i = 0; i < rows; i++) {
         if (data[i].length != columns) {
            throw new IllegalArgumentException("All rows must have the same length.");
         }
      }
      this.data = data;
   }

   /** Construct a matrix quickly without checking arguments.
   @param data    Two-dimensional array of doubles.
   @param rows    Number of rows.
   @param columns    Number of colums.
   */

   public Matrix (double[][] data, int rows, int columns) {
      this.data = data;
      this.rows = rows;
      this.columns = columns;
   }

   /** Construct a matrix from a one-dimensional packed array
   @param vals One-dimensional array of doubles, packed by columns (ala Fortran).
   @param rows    Number of rows.
   @exception  IllegalArgumentException Array length must be a multiple of m.
   */

   public Matrix (double[] vals, int rows) {
      this.rows = rows;
      columns = (rows != 0 ? vals.length/ rows : 0);
      if (rows * columns != vals.length) {
         throw new IllegalArgumentException("Array length must be a multiple of m.");
      }
      data = new double[rows][columns];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = vals[i+j* rows];
         }
      }
   }

/* ------------------------
   Public Methods
 * ------------------------ */

   /** Construct a matrix from a copy of a 2-D array.
   @param A    Two-dimensional array of doubles.
   @exception  IllegalArgumentException All rows must have the same length
   */

   public static Matrix constructWithCopy(double[][] A) {
      int m = A.length;
      int n = A[0].length;
      Matrix X = new Matrix(m,n);
      double[][] C = X.getArray();
      for (int i = 0; i < m; i++) {
         if (A[i].length != n) {
            throw new IllegalArgumentException
               ("All rows must have the same length.");
         }
         for (int j = 0; j < n; j++) {
            C[i][j] = A[i][j];
         }
      }
      return X;
   }

   /** Make a deep copy of a matrix
   */

   public Matrix deepCopy() {
      Matrix result = new Matrix(rows, columns);
      double[][] data = result.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = this.data[i][j];
         }
      }
      return result;
   }

   /** Clone the Matrix object.
   */
   @Override
   public Object clone() {
      return this.deepCopy();
   }

   /** Access the internal two-dimensional array.
   @return     Pointer to the two-dimensional array of matrix elements.
   */

   public double[][] getArray () {
      return data;
   }

   /** Copy the internal two-dimensional array.
   @return     Two-dimensional array copy of matrix elements.
   */

   public double[][] getArrayCopy () {
      double[][] C = new double[rows][columns];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = data[i][j];
         }
      }
      return C;
   }

   /** Make a one-dimensional column packed copy of the internal array.
   @return     Matrix elements packed in a one-dimensional array by columns.
   */

   public double[] getColumnPackedCopy () {
      double[] vals = new double[rows * columns];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            vals[i+j* rows] = data[i][j];
         }
      }
      return vals;
   }

   /** Make a one-dimensional row packed copy of the internal array.
   @return     Matrix elements packed in a one-dimensional array by rows.
   */

   public double[] getRowPackedCopy () {
      double[] vals = new double[rows * columns];
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            vals[i* columns +j] = data[i][j];
         }
      }
      return vals;
   }

   /** Get row dimension.
   @return     m, the number of rows.
   */

   public int getRowDimension () {
      return rows;
   }

   /** Get column dimension.
   @return     n, the number of columns.
   */

   public int getColumnDimension () {
      return columns;
   }

   /** Get a single element.
   @param i    Row index.
   @param j    Column index.
   @return     A(i,j)
   @exception  ArrayIndexOutOfBoundsException
   */

   public double get (int i, int j) {
      return data[i][j];
   }

   /** Get a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param j0   Initial column index
   @param j1   Final column index
   @return     A(i0:i1,j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

   public Matrix getMatrix (int i0, int i1, int j0, int j1) {
      Matrix X = new Matrix(i1-i0+1,j1-j0+1);
      double[][] B = X.getArray();
      try {
         for (int i = i0; i <= i1; i++) {
            for (int j = j0; j <= j1; j++) {
               B[i-i0][j-j0] = data[i][j];
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
      return X;
   }

   /** Get a submatrix.
   @param r    Array of row indices.
   @param c    Array of column indices.
   @return     A(r(:),c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

   public Matrix getMatrix (int[] r, int[] c) {
      Matrix X = new Matrix(r.length,c.length);
      double[][] B = X.getArray();
      try {
         for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < c.length; j++) {
               B[i][j] = data[r[i]][c[j]];
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
      return X;
   }

   /** Get a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param c    Array of column indices.
   @return     A(i0:i1,c(:))
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

   public Matrix getMatrix (int i0, int i1, int[] c) {
      Matrix X = new Matrix(i1-i0+1,c.length);
      double[][] B = X.getArray();
      try {
         for (int i = i0; i <= i1; i++) {
            for (int j = 0; j < c.length; j++) {
               B[i-i0][j] = data[i][c[j]];
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
      return X;
   }

   /** Get a submatrix.
   @param r    Array of row indices.
   @param j0   Initial column index
   @param j1   Final column index
   @return     A(r(:),j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

   public Matrix getMatrix (int[] r, int j0, int j1) {
      Matrix X = new Matrix(r.length,j1-j0+1);
      double[][] B = X.getArray();
      try {
         for (int i = 0; i < r.length; i++) {
            for (int j = j0; j <= j1; j++) {
               B[i][j-j0] = data[r[i]][j];
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
      return X;
   }

   /** Set a single element.
   @param i    Row index.
   @param j    Column index.
   @param s    A(i,j).
   @exception  ArrayIndexOutOfBoundsException
   */

   public void set (int i, int j, double s) {
      data[i][j] = s;
   }

   /** Set a submatrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param j0   Initial column index
   @param j1   Final column index
   @param X    A(i0:i1,j0:j1)
   @exception  ArrayIndexOutOfBoundsException Submatrix indices
   */

   public void setMatrix (int i0, int i1, int j0, int j1, Matrix X) {
      try {
         for (int i = i0; i <= i1; i++) {
            for (int j = j0; j <= j1; j++) {
               data[i][j] = X.get(i-i0,j-j0);
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
   }

   /** Set a sub matrix.
   @param r    Array of row indices.
   @param c    Array of column indices.
   @param X    A(r(:),c(:))
   @exception  ArrayIndexOutOfBoundsException Sub matrix indices
   */

   public void setMatrix (int[] r, int[] c, Matrix X) {
      try {
         for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < c.length; j++) {
               data[r[i]][c[j]] = X.get(i,j);
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Sub matrix indices");
      }
   }

   /** Set a sub matrix.
   @param r    Array of row indices.
   @param j0   Initial column index
   @param j1   Final column index
   @param X    A(r(:),j0:j1)
   @exception  ArrayIndexOutOfBoundsException Sub matrix indices
   */

   public void setMatrix (int[] r, int j0, int j1, Matrix X) {
      try {
         for (int i = 0; i < r.length; i++) {
            for (int j = j0; j <= j1; j++) {
               data[r[i]][j] = X.get(i,j-j0);
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Sub matrix indices");
      }
   }

   /** Set a sub matrix.
   @param i0   Initial row index
   @param i1   Final row index
   @param c    Array of column indices.
   @param X    A(i0:i1,c(:))
   @exception  ArrayIndexOutOfBoundsException Sub matrix indices
   */

   public void setMatrix (int i0, int i1, int[] c, Matrix X) {
      try {
         for (int i = i0; i <= i1; i++) {
            for (int j = 0; j < c.length; j++) {
               data[i][c[j]] = X.get(i-i0,j);
            }
         }
      } catch(ArrayIndexOutOfBoundsException e) {
         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
      }
   }

   /** Matrix transpose.
   @return    A'
   */

   public Matrix transpose () {
      Matrix X = new Matrix(columns, rows);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[j][i] = data[i][j];
         }
      }
      return X;
   }

   /** One norm
   @return    maximum column sum.
   */

   public double norm1 () {
      double f = 0;
      for (int j = 0; j < columns; j++) {
         double s = 0;
         for (int i = 0; i < rows; i++) {
            s += Math.abs(data[i][j]);
         }
         f = Math.max(f,s);
      }
      return f;
   }

   /** Two norm
   @return    maximum singular value.
   */

   public double norm2 () {
      return (new SingularValueDecomposition(this).norm2());
   }

   /** Infinity norm
   @return    maximum row sum.
   */

   public double normInf () {
      double f = 0;
      for (int i = 0; i < rows; i++) {
         double s = 0;
         for (int j = 0; j < columns; j++) {
            s += Math.abs(data[i][j]);
         }
         f = Math.max(f,s);
      }
      return f;
   }

   /** Frobenius norm
   @return    sqrt of sum of squares of all elements.
   */

   public double normF () {
      double f = 0;
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            f = Maths.hypot(f, data[i][j]);
         }
      }
      return f;
   }

   /**  Unary minus
   @return    -A
   */

   public Matrix uminus () {
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = -data[i][j];
         }
      }
      return X;
   }

   /** C = A + B
   @param B    another matrix
   @return     A + B
   */

   public Matrix plus (Matrix B) {
      checkMatrixDimensions(B);
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = data[i][j] + B.data[i][j];
         }
      }
      return X;
   }

   /** A = A + B
   @param B    another matrix
   @return     A + B
   */

   public Matrix plusEquals (Matrix B) {
      checkMatrixDimensions(B);
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = data[i][j] + B.data[i][j];
         }
      }
      return this;
   }

   /** C = A - B
   @param B    another matrix
   @return     A - B
   */

   public Matrix minus (Matrix B) {
      checkMatrixDimensions(B);
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = data[i][j] - B.data[i][j];
         }
      }
      return X;
   }

   /** A = A - B
   @param B    another matrix
   @return     A - B
   */

   public Matrix minusEquals (Matrix B) {
      checkMatrixDimensions(B);
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = data[i][j] - B.data[i][j];
         }
      }
      return this;
   }

   /** Element-by-element multiplication, C = A.*B
   @param B    another matrix
   @return     A.*B
   */

   public Matrix arrayTimes (Matrix B) {
      checkMatrixDimensions(B);
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = data[i][j] * B.data[i][j];
         }
      }
      return X;
   }

   /** Element-by-element multiplication in place, A = A.*B
   @param B    another matrix
   @return     A.*B
   */

   public Matrix arrayTimesEquals (Matrix B) {
      checkMatrixDimensions(B);
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = data[i][j] * B.data[i][j];
         }
      }
      return this;
   }

   /** Element-by-element right division, C = A./B
   @param B    another matrix
   @return     A./B
   */

   public Matrix arrayRightDivide (Matrix B) {
      checkMatrixDimensions(B);
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = data[i][j] / B.data[i][j];
         }
      }
      return X;
   }

   /** Element-by-element right division in place, A = A./B
   @param B    another matrix
   @return     A./B
   */

   public Matrix arrayRightDivideEquals (Matrix B) {
      checkMatrixDimensions(B);
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = data[i][j] / B.data[i][j];
         }
      }
      return this;
   }

   /** Element-by-element left division, C = A.\B
   @param B    another matrix
   @return     A.\B
   */

   public Matrix arrayLeftDivide (Matrix B) {
      checkMatrixDimensions(B);
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = B.data[i][j] / data[i][j];
         }
      }
      return X;
   }

   /** Element-by-element left division in place, A = A.\B
   @param B    another matrix
   @return     A.\B
   */

   public Matrix arrayLeftDivideEquals (Matrix B) {
      checkMatrixDimensions(B);
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = B.data[i][j] / data[i][j];
         }
      }
      return this;
   }

   /** Multiply a matrix by a scalar, C = s*A
   @param s    scalar
   @return     s*A
   */

   public Matrix times (double s) {
      Matrix X = new Matrix(rows, columns);
      double[][] C = X.getArray();
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            C[i][j] = s* data[i][j];
         }
      }
      return X;
   }

   /** Multiply a matrix by a scalar in place, A = s*A
   @param s    scalar
   @return     replace A by s*A
   */

   public Matrix timesEquals (double s) {
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            data[i][j] = s* data[i][j];
         }
      }
      return this;
   }

   /** Linear algebraic matrix multiplication, A * B
   @param B    another matrix
   @return     Matrix product, A * B
   @exception  IllegalArgumentException Matrix inner dimensions must agree.
   */

   public Matrix times (Matrix B) {
      if (B.rows != columns) {
         throw new IllegalArgumentException("Matrix inner dimensions must agree.");
      }
      Matrix X = new Matrix(rows,B.columns);
      double[][] C = X.getArray();
      double[] Bcolj = new double[columns];
      for (int j = 0; j < B.columns; j++) {
         for (int k = 0; k < columns; k++) {
            Bcolj[k] = B.data[k][j];
         }
         for (int i = 0; i < rows; i++) {
            double[] Arowi = data[i];
            double s = 0;
            for (int k = 0; k < columns; k++) {
               s += Arowi[k]*Bcolj[k];
            }
            C[i][j] = s;
         }
      }
      return X;
   }

   /** LU Decomposition
   @return     LUDecomposition
   @see LUDecomposition
   */

   public LUDecomposition lu () {
      return new LUDecomposition(this);
   }

   /** QR Decomposition
   @return     QRDecomposition
   @see QRDecomposition
   */

   public QRDecomposition qr () {
      return new QRDecomposition(this);
   }

   /** Cholesky Decomposition
   @return     CholeskyDecomposition
   @see CholeskyDecomposition
   */

   public CholeskyDecomposition chol () {
      return new CholeskyDecomposition(this);
   }

   /** Singular Value Decomposition
   @return     SingularValueDecomposition
   @see SingularValueDecomposition
   */

   public SingularValueDecomposition svd () {
      return new SingularValueDecomposition(this);
   }

   /** Eigenvalue Decomposition
   @return     EigenvalueDecomposition
   @see EigenvalueDecomposition
   */

   public EigenvalueDecomposition eig () {
      return new EigenvalueDecomposition(this);
   }

   /** Solve A*X = B
   @param B    right hand side
   @return     solution if A is square, least squares solution otherwise
   */

   public Matrix solve (Matrix B) {
      return (rows == columns ? (new LUDecomposition(this)).solve(B) :
                       (new QRDecomposition(this)).solve(B));
   }

   /** Solve X*A = B, which is also A'*X' = B'
   @param B    right hand side
   @return     solution if A is square, least squares solution otherwise.
   */

   public Matrix solveTranspose (Matrix B) {
      return transpose().solve(B.transpose());
   }

   /** Matrix inverse or pseudoinverse
   @return     inverse(A) if A is square, pseudoinverse otherwise.
   */

   public Matrix inverse () {
      return solve(identity(rows, rows));
   }

   /** Matrix determinant
   @return     determinant
   */

   public double det () {
      return new LUDecomposition(this).det();
   }

   /** Matrix rank
   @return     effective numerical rank, obtained from SVD.
   */

   public int rank () {
      return new SingularValueDecomposition(this).rank();
   }

   /** Matrix condition (2 norm)
   @return     ratio of largest to smallest singular value.
   */

   public double cond () {
      return new SingularValueDecomposition(this).cond();
   }

   /** Matrix trace.
   @return     sum of the diagonal elements.
   */

   public double trace () {
      double t = 0;
      for (int i = 0; i < Math.min(rows, columns); i++) {
         t += data[i][i];
      }
      return t;
   }

   /** Generate matrix with random elements
   @param m    Number of rows.
   @param n    Number of columns.
   @return     An m-by-n matrix with uniformly distributed random elements.
   */

   public static Matrix random (int m, int n) {
      Matrix A = new Matrix(m,n);
      double[][] X = A.getArray();
      for (int i = 0; i < m; i++) {
         for (int j = 0; j < n; j++) {
            X[i][j] = Math.random();
         }
      }
      return A;
   }

   /** Generate identity matrix
   @param m    Number of rows.
   @param n    Number of colums.
   @return     An m-by-n matrix with ones on the diagonal and zeros elsewhere.
   */

   public static Matrix identity (int m, int n) {
      Matrix A = new Matrix(m,n);
      double[][] X = A.getArray();
      for (int i = 0; i < m; i++) {
         for (int j = 0; j < n; j++) {
            X[i][j] = (i == j ? 1.0 : 0.0);
         }
      }
      return A;
   }


   /** Print the matrix to stdout.   Line the elements up in columns
     * with a Fortran-like 'Fw.d' style format.
   @param w    Column width.
   @param d    Number of digits after the decimal.
   */

   public void print (int w, int d) {
      print(new PrintWriter(System.out,true),w,d); }

   /** Print the matrix to the output stream.   Line the elements up in
     * columns with a Fortran-like 'Fw.d' style format.
   @param output Output stream.
   @param w      Column width.
   @param d      Number of digits after the decimal.
   */

   public void print (PrintWriter output, int w, int d) {
      DecimalFormat format = new DecimalFormat();
      format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
      format.setMinimumIntegerDigits(1);
      format.setMaximumFractionDigits(d);
      format.setMinimumFractionDigits(d);
      format.setGroupingUsed(false);
      print(output,format,w+2);
   }

   /** Print the matrix to stdout.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
   @param format A  Formatting object for individual elements.
   @param width     Field width for each column.
   @see DecimalFormat#setDecimalFormatSymbols
   */

   public void print (NumberFormat format, int width) {
      print(new PrintWriter(System.out,true),format,width); }

   // DecimalFormat is a little disappointing coming from Fortran or C's printf.
   // Since it doesn't pad on the left, the elements will come out different
   // widths.  Consequently, we'll pass the desired column width in as an
   // argument and do the extra padding ourselves.

   /** Print the matrix to the output stream.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
   @param output the output stream.
   @param format A formatting object to format the matrix elements 
   @param width  Column width.
   @see DecimalFormat#setDecimalFormatSymbols
   */

   public void print (PrintWriter output, NumberFormat format, int width) {
      output.println();  // start on new line.
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            String s = format.format(data[i][j]); // format the number
            int padding = Math.max(1,width-s.length()); // At _least_ 1 space
            for (int k = 0; k < padding; k++) {
               output.print(' ');
            }
            output.print(s);
         }
         output.println();
      }
      output.println();   // end with blank line.
   }

   /** Read a matrix from a stream.  The format is the same the print method,
     * so printed matrices can be read back in (provided they were printed using
     * US Locale).  Elements are separated by
     * whitespace, all the elements for each row appear on a single line,
     * the last row is followed by a blank line.
   @param input the input stream.
   */

   public static Matrix read (BufferedReader input) throws java.io.IOException {
      StreamTokenizer tokenizer= new StreamTokenizer(input);

      // Although StreamTokenizer will parse numbers, it doesn't recognize
      // scientific notation (E or D); however, Double.valueOf does.
      // The strategy here is to disable StreamTokenizer's number parsing.
      // We'll only get whitespace delimited words, EOL's and EOF's.
      // These words should all be numbers, for Double.valueOf to parse.

      tokenizer.resetSyntax();
      tokenizer.wordChars(0,255);
      tokenizer.whitespaceChars(0, ' ');
      tokenizer.eolIsSignificant(true);
      java.util.Vector<Double> vD = new java.util.Vector<Double>();

      // Ignore initial empty lines
      while (tokenizer.nextToken() == StreamTokenizer.TT_EOL);
      if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
         throw new java.io.IOException("Unexpected EOF on matrix read.");
      }
      do {
         vD.addElement(Double.valueOf(tokenizer.sval)); // Read & store 1st row.
      } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

      int n = vD.size();  // Now we've got the number of columns!
      double row[] = new double[n];
      for (int j=0; j<n; j++) {// extract the elements of the 1st row.
         row[j] = vD.elementAt(j).doubleValue();
      }
      java.util.Vector<double[]> v = new java.util.Vector<double[]>();
      v.addElement(row);  // Start storing rows instead of columns.
      while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
         // While non-empty lines
         v.addElement(row = new double[n]);
         int j = 0;
         do {
            if (j >= n) {
               throw new java.io.IOException("Row " + v.size() + " is too long.");
            }
            row[j++] = Double.valueOf(tokenizer.sval).doubleValue();
         } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
         if (j < n) {
            throw new java.io.IOException("Row " + v.size() + " is too short.");
         }
      }
      int m = v.size();  // Now we've got the number of rows.
      double[][] A = new double[m][];
      v.copyInto(A);  // copy the rows out of the vector
      return new Matrix(A);
   }


/* ------------------------
   Private Methods
 * ------------------------ */

   /** Check if size(A) == size(B) **/

   private void checkMatrixDimensions (Matrix B) {
      if (B.rows != rows || B.columns != columns) {
         throw new IllegalArgumentException("Matrix dimensions must agree.");
      }
   }

  private static final long serialVersionUID = 1;
}
