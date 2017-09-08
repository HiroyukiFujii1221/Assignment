public class MultiplicationTable
{
	  public static void main(String args[]) 
	  {
		    System.out.println("九九の表だよーん☆…です。");

		    int multiplicationtable[][] = new int[9][9]; 

		    for( int i = 0; i < 9; i++ ) {
		      for( int j = 0; j < 9; j++ ){
		    	  multiplicationtable[i][j] = (i+1) * (j+1);
		      }
		    }

		    for( int i = 0; i < 9; i++ ) {
		      for( int j = 0; j < 9; j++ ) { 
		    	  System.out.print(multiplicationtable[i][j] + " "); 
		      }
		      	  System.out.println(); 
		    }
	  }
}
