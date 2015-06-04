/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package npuzzle;

import java.util.*;

/**
 *
 * @author USER
 */
public class Board {
    // construct a board from an N-by-N
    //array of blocks
    // (where blocks[i][j] = block in
    //row i, column j)
    private int[][] blocks;
    private int N;
    private int blankRow,blankColumn;
    public Board(int[][] blocks) {
        this.blocks = blocks;
        N = blocks.length;
        setBlankPosition();

    }
    // board size N
    public int size(){
        return N;
    } 
    
    // number of blocks out of place
    public int hamming(){
        int outOfPlace=0,k=1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(blocks[i][j] != k++ && blocks[i][j] != 0)outOfPlace++;
            }
        }
        return outOfPlace;
    }
    public int tilesOutOfRowAndColumn(){
        int outOfRC=0,k=1,from,to;
        for (int i = 0; i < N; i++) {
            from=k;
            to = k+N-1;
            for (int j = 0; j < N; j++) {
                if(blocks[i][j] != 0){
                    if(blocks[i][j] < from || to < blocks[i][j])outOfRC++;
                    if(j != (blocks[i][j]-1)%N)outOfRC++;
                }
                k++;
            }
        }
        return outOfRC;
    }
    //public int manhattan() // sum of Manhattan distances between blocks and goal
    // is this board the goal board?
    public boolean isGoal(){
        return (hamming()==0) ? true : false ;
    }
    // is the board solvable?
    public boolean isSolvable(){
        int k=0,arrsize=N*N-1,blankRow=0;
        int[] arr=new int[arrsize];
        int[] temp=new int[arrsize];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(blocks[i][j] != 0)arr[k++]=blocks[i][j];
                else blankRow=i;
            }
        }

        int inversions=mergeSort(arr, temp, 0,arrsize-1);
        //System.out.println("Inversions = "+inversions+" BlankRow = "+blankRow);
        return ((N%2 != 0 && inversions % 2 == 0) || (N%2 == 0 && (inversions+blankRow) % 2 != 0)) ? true : false;
    } 
    // does this board equal y?
    @Override
    public boolean equals(Object y){
        Board z;
        if(y instanceof Board){
            z=(Board)y;
            return (this.toString().contentEquals(z.toString())) ? true : false;
        }
        else return false;
        /*if(this.N!=z.N)return false;
        else{
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if(this.blocks[i][j] != z.blocks[i][j])return false;
                }
            }
        }*/
    } 
    
    // all neighboring boards
    public Iterable<Board> neighbors(){
        List<Board> neighbrs=new ArrayList<Board>();
        
        if(blankRow >= 1){
            int[][] blks=new int[N][N];
            copyBlock(blks);
            blks[blankRow][blankColumn]=blks[blankRow-1][blankColumn];
            blks[blankRow-1][blankColumn]=0;
            neighbrs.add(new Board(blks));
        }
        
        if(blankRow < N-1){
            int[][] blks=new int[N][N];
            copyBlock(blks);
            blks[blankRow][blankColumn]=blks[blankRow+1][blankColumn];
            blks[blankRow+1][blankColumn]=0;
            neighbrs.add(new Board(blks));
        }
        
        if(blankColumn >= 1){
            int[][] blks=new int[N][N];
            copyBlock(blks);
            blks[blankRow][blankColumn]=blks[blankRow][blankColumn-1];
            blks[blankRow][blankColumn-1]=0;
            neighbrs.add(new Board(blks));
        }
        
        if(blankColumn < N-1){
            int[][] blks=new int[N][N];
            copyBlock(blks);
            blks[blankRow][blankColumn]=blks[blankRow][blankColumn+1];
            blks[blankRow][blankColumn+1]=0;
            neighbrs.add(new Board(blks));
        }
        
        return Collections.unmodifiableList(neighbrs);
    } 
    
    // string representation of the board (in the output format specified below)
    @Override
    public String toString(){
        String brdstr="";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                brdstr+=blocks[i][j]+"  ";
            }
            brdstr+="\n";
        }
       
        return brdstr;
    }
    // unit test           
    public static void main(String[] args){ 
        Scanner input=new Scanner(System.in);
        int N,brd[][];
        System.out.println("***Board Unit Test***");
        System.out.println("Enter Size of board:");
        N=input.nextInt();
        System.out.println("Enter the board:");
        brd=new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                brd[i][j]=input.nextInt();
            }
        }
        
        Board pzlbrd=new Board(brd);
        System.out.println(pzlbrd);
        Iterable<Board> nbrs = pzlbrd.neighbors();
        for (Board nbr : nbrs) {
            System.out.println(nbr);
        }

        System.out.println("Hamming:"+pzlbrd.hamming());
        System.out.println("TilesOutOfRowAndColumn:"+pzlbrd.tilesOutOfRowAndColumn());
        System.out.println("IsGoal:"+(pzlbrd.isGoal()? "True" : "False"));
        System.out.println("IsSolvable:"+(pzlbrd.isSolvable()? "True" : "False"));
        
        System.out.println("Enter another board of same size:");
        int[][] checkbrd=new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                checkbrd[i][j]=input.nextInt();
            }
        }
        Board anotherbrd=new Board(checkbrd);
        System.out.println("Equals:"+(pzlbrd.equals(anotherbrd)? "True" : "False"));        
    
    } 

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Arrays.deepHashCode(this.blocks);
        hash = 29 * hash + this.N;
        return hash;
    }

    
    /* An auxiliary recursive function that sorts the input array and
    returns the number of inversions in the array. */
    int mergeSort(int arr[], int temp[], int left, int right)
    {
        int mid, inv_count = 0;
        if (right > left)
        {
            /* Divide the array into two parts and call _mergeSortAndCountInv()
            for each of the parts */
            mid = (right + left)/2;

            /* Inversion count will be sum of inversions in left-part, right-part
            and number of inversions in merging */
            inv_count  = mergeSort(arr, temp, left, mid);
            inv_count += mergeSort(arr, temp, mid+1, right);

            /*Merge the two parts*/
            inv_count += merge(arr, temp, left, mid+1, right);
        }
        return inv_count;
    }

    /* This funt merges two sorted arrays and returns inversion count in
    the arrays.*/
    int merge(int arr[], int temp[], int left, int mid, int right)
    {
        int i, j, k;
        int inv_count = 0;

        i = left; /* i is index for left subarray*/
        j = mid;  /* i is index for right subarray*/
        k = left; /* i is index for resultant merged subarray*/
        while ((i <= mid - 1) && (j <= right))
        {
            if (arr[i] <= arr[j])
            {
            temp[k++] = arr[i++];
            }
            else
            {
            temp[k++] = arr[j++];
            inv_count = inv_count + (mid - i);
            }
        }

        /* Copy the remaining elements of left subarray
        (if there are any) to temp*/
        while (i <= mid - 1)
            temp[k++] = arr[i++];

        /* Copy the remaining elements of right subarray
        (if there are any) to temp*/
        while (j <= right)
            temp[k++] = arr[j++];

        /*Copy back the merged elements to original array*/
        for (i=left; i <= right; i++)
            arr[i] = temp[i];

        return inv_count;
    }
    
    private void setBlankPosition(){
        int found=0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(blocks[i][j] == 0){
                    found=1;
                    blankRow=i;
                    blankColumn=j;
                    //System.out.println("blocks["+i+"]["+j+"] = "+blocks[i][j]);
                    break;
                }
            }
            if(found==1)break;
        }        
    }
    
    private void copyBlock(int[][] blks){
        for(int i = 0; i < N; i++)
        {
            System.arraycopy(this.blocks[i], 0, blks[i], 0, N);
        }
    }
}
