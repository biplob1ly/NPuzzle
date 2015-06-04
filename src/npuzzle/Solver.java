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
public class Solver {
    
    private PriorityQueue<Node> leaves;
    private HashSet <String> explored;
    private List<Board> boardSequence;
    private Node source;
    private int numberOfMoves;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        leaves = new PriorityQueue<Node>(10, new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                return (node1.getF() < node2.getF()) ? -1 : 1;
            }
        });
        
        explored = new HashSet <String>();
        boardSequence=new ArrayList<Board>();
        this.source=new Node(initial, 0, null);
        
        leaves.add(this.source);
        explored.add(this.source.getBoard().toString());
        
        while(!leaves.isEmpty()){
            Node current=leaves.poll();
            if(current == null)break;
            if(current.getBoard().isGoal()){
                getBoardSequence(current);
                numberOfMoves = current.getG();
                break;
            }
            

            Iterable<Board> nbrs = current.getBoard().neighbors();
            for (Board board : nbrs) {
                if(!explored.contains(board.toString())){
                    leaves.add(new Node(board, current.getG()+1, current));
                    explored.add(board.toString());
                }
                else board=null;
            }
        }
    } 
            
    // min number of moves to solve initial board
    public int moves(){
        return numberOfMoves;
    } 
    
    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
        return Collections.unmodifiableList(boardSequence);
    }
    
    private void getBoardSequence(Node goalNode){
        if(goalNode.getParent() != null){
            getBoardSequence(goalNode.getParent());
        }
        boardSequence.add(goalNode.getBoard());
    }
  
    public class Node {
        private int N,h,g,f;
        private Node parent;
        private Board board;

        public Node(Board brd, int g, Node parent){
            this.board=brd;
            this.g=g;
            this.parent=parent;
            this.N=brd.size();
            this.h=brd.tilesOutOfRowAndColumn(); //brd.tilesOutOfRowAndColumn()  brd.hamming();
            this.f=this.g+this.h;
        }

        public Board getBoard() {
            return board;
        }

        public int getF() {
            return f;
        }

        public Node getParent() {
            return parent;
        }

        public int getG() {
            return g;
        }
    }    
    
    // solve a slider puzzle
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        int N,brd[][];
        System.out.println("***Solver Unit Test***");
        System.out.println("Enter Size of board:");
        N=input.nextInt();
        System.out.println("Enter the board:");
        brd=new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                brd[i][j] = input.nextInt();
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

}
