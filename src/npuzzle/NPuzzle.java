/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package npuzzle;

import java.io.*;
import java.util.StringTokenizer;

/**
 *
 * @author USER
 */
public class NPuzzle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int N,brd[][];
        
        //Scanner input=new Scanner(System.in);
        try {
            File inFile = new File("input.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            String DataLine="";
            DataLine = br.readLine();  //N=input.nextInt();
            N = Integer.parseInt(DataLine);

            brd=new int[N][N];
            for (int i = 0; i < N; i++) {
            	DataLine = br.readLine();
            	StringTokenizer stToken1 = new StringTokenizer(DataLine," ");
                for (int j = 0; j < N; j++) {
                    brd[i][j] = Integer.parseInt(stToken1.nextToken());
                }
            }
        
            Board initial=new Board(brd);        
            // check if puzzle is solvable; if so, solve it and output solution
            if (initial.isSolvable()) {
                Solver solver = new Solver(initial);
                System.out.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                    System.out.println(board);
            }
            // if not, report unsolvable
            else {
                System.out.println("Unsolvable puzzle");
            }
        } 
        catch (FileNotFoundException ex)
        {
            System.out.println("FileNotFoundException HD");
        }
        catch (IOException ex)
        {
        	System.out.println("exception");
        }



    }
}
