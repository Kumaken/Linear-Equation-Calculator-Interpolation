import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Program {
	/*----------NEW TYPE DECLARATION----------*/
	// untuk infinite solutions : typedef parametrik
	private double val;
    private String var;
	//private boolean origin;
	
	public Program( double val, String var){
		this.val = val;
		this.var = var;
		//this.origin = origin;
	}
	
    /*----------MATRIX METHODS----------*/
    // Matrix input from user
    public static void MatriksDariLayar (double[][] matriks, double[] aug, int m, int n, Scanner console) throws FileNotFoundException {
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j < n; j++) {
				matriks[i][j] = console.nextDouble();
            }
            aug[i] = console.nextDouble();
        }
    }

    // Matrix input from external file
    public static int CountRow (String source) throws IOException {
        FileReader file = new FileReader(source);
        BufferedReader filex = new BufferedReader(file);
        String text = new String();
        int counter = 0;

        text = filex.readLine();
        while (text != null) {          // while the currently read line in the external file is not empty
            counter++;                  // count the line
            text = filex.readLine();    // read a new line
        }
        filex.close();
        return counter;                 // return counter as size of rows
    }

    public static int CountColumn (String source) throws IOException {
        FileReader file = new FileReader(source);
        BufferedReader filex = new BufferedReader(file);
        String text = filex.readLine();         // read the first line
        String[] textArr = text.split(" ");     // split the line into an array of string marked by a space
        filex.close();
        return textArr.length;                  // return size of array as column size
    }

    public static void MatriksDariFile (double[][] matriks, double[] aug, int m, int n, String source) throws FileNotFoundException {
        // defining file and initiating reader
        File file = new File(source);
        Scanner filex = new Scanner(file);

        // reading from file
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j < n; j++) {
                matriks[i][j] = filex.nextDouble();
            }
            aug[i] = filex.nextDouble();
        }
        filex.close();
    }

    // Matrix output to screen
    public static void MatriksKeLayar (double[][] matriks, double[] aug, int m, int n) {
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j < n; j++) {
                System.out.print(matriks[i][j] + " ");
            }
            System.out.println(aug[i]);
        }
    }

    /*----------INTERPOLATION METHODS----------*/
    // Interpolation points input from user
    public static void InterpolasiDariLayar (double[][] interpolasi, int m, Scanner console) throws FileNotFoundException {
        // inputting interpolation points
        for (int i = 1; i <= m; i++) {
            interpolasi[i][1] = console.nextDouble();
            interpolasi[i][2] = console.nextDouble();
        }

        // input absis for ordinate solution
        System.out.print("Masukan absis: "); interpolasi[m+1][1] = console.nextDouble();
    }

    // Interpolation points input from eternal file (uses CountRow, perviously used as a matrix method)
    public static void InterpolasiDariFile (double[][] interpolasi, int m, String source) throws FileNotFoundException {
        // defining file and initiating a reader for file
        File file = new File(source);
        Scanner filex = new Scanner(file);

        // reading input points from file
        for (int i = 1; i <= m; i++) {
            interpolasi[i][1] = filex.nextDouble();
            interpolasi[i][2] = filex.nextDouble();
        }

        // reading absis for ordinate solution from file
        interpolasi[m+1][1] = filex.nextDouble();
        filex.close();
    }

    // Transforming interpolation points into a problem matrix
    public static void Matrikskan (double[][] interpolasi, double[][] matriks, double[] aug, int m, int n) {
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j < n; j++) {
                matriks[i][j] = Math.pow(interpolasi[i][1], j-1);
            }
            aug[i] = interpolasi[i][2];
        }
    }

    // Printing out an interpolation formula based on augmented matrix solution
    public static void FungsiInterpolasi (double[] persamaan, int l) {
        System.out.printf("f(x) = %.2f", persamaan[1]);                 // first term (no x)
        System.out.printf(" + %.2fx", persamaan[2]);                    // second term (x^1)
        for (int i = 2; i <= l; i++) {
            if (persamaan[i+1] != 0) {
                System.out.printf(" + %.2fx^%d", persamaan[i+1], i);    // next terms (x^i)
            }
        }
        System.out.println();
    }

    // Finding the ordinate to the given absis using interpolation equation/solution
    public static double SolusiInterpolasi (double[] persamaan, int l, double x) {
        double y;
        y = persamaan[1];                               // y = first term (no x)
        for (int i = 1; i <= l; i++) {
            y = y + (persamaan[i+1] * Math.pow(x, i));  // y += next terms (x^i)
        }
        return y;
    }


    /*----------GAUSSIAN AND GAUSS-JORDAN ELIMINATION----------*/
    // Finding pivot point for a certain column and below a certain row
    public static int FindPivotRow (double[][] matriks, int m, int row, int col) {
        //System.out.println("FindPivotRow");
        int foundPivot = 0;
        int i = row;
        while ((matriks[i][col] == 0) && (i <= m)) {
            i++;
        }
        if (i <= m) {
            foundPivot = i;
        }
        //System.out.println("Pivot is "+ i);
        return foundPivot;
    }

    // Swapping two rows in a matrix
    public static void SwapRow (double[][] matriks, double[] aug, int m, int n, int row1, int row2) {
        //System.out.println("SwapRow " + row1 + " " + row2);
        double[] temp = new double[n+1];
        for (int k = 1; k <= n; k++) {
            temp[k] = matriks[row1][k];
        }
        temp[n] = aug[row1];
        for (int k = 1; k <= n; k++) {
            matriks[row1][k] = matriks[row2][k];
        }
        aug[row1] = aug[row2];
        for(int k = 1; k <= n; k++) {
            matriks[row2][k] = temp[k];
        }
        aug[row2] = temp[n];
        //MatriksKeLayar(matriks, aug, m, n);
    }

    // Dividing pivot row so that pivot point will have the value of 1
    public static void DivideRow (double[][] matriks, double[] aug, int m, int n, int prow, int pcol) {
        //System.out.println("DivideRow "+ prow);
        double factor = matriks[prow][pcol];
        for (int k = 1; k <= n; k++) {
            if (matriks[prow][k] != 0 ) {
                matriks[prow][k] = matriks[prow][k] / factor;
            }
        }
        if (aug[prow] != 0 ) {
            aug[prow] = aug[prow] / factor;
        }
    
    }

    // Applying Gaussian elimination using a certain pivot point
    public static void Gauss (double[][] matriks, double[] aug, int m, int n, int prow, int pcol) {
        //System.out.println("Gauss [" + prow + "][" + pcol + "]");
        double factor;
        for (int i = prow + 1; i <= m; i++) {
            factor = matriks[i][pcol];
            for (int j = 1; j < n; j++) {
                matriks[i][j] = matriks[i][j] - (matriks[prow][j] * factor);
            }
            aug[i] = aug[i] - (aug[prow] * factor);
        }
        
    }

    // Applying Gauss-Jordan elimination using a certain pivot point
    public static void GaussJordan (double[][] matriks, double[] aug, int m, int n, int prow, int pcol) {
        //System.out.println("GaussJordan [" + prow + "][" + pcol + "]");
        double factor;
        for (int i = 1; i < prow; i++) {
            factor = matriks[i][pcol];
            for (int j = 1; j < n; j++) {
                matriks[i][j] = matriks[i][j] - (matriks[prow][j] * factor);
            }
            aug[i] = aug[i] - (aug[prow] * factor);
        }
        for (int i = prow + 1; i <= m; i++) {
            factor = matriks[i][pcol];
            for (int j = 1; j < n; j++) {
                matriks[i][j] = matriks[i][j] - (matriks[prow][j] * factor);
            }
            aug[i] = aug[i] - (aug[prow] * factor);
        }
        //MatriksKeLayar(matriks, aug, m, n);
        //System.out.println();
		
    }

	// Transforming an augmented matrix to its row echelon or reduced row echelon form
    public static double[] Solve (double[][] matriks, double[] aug, int n, int m, String type, Program[][] infsol ) {
        int i = 1;
        int j = 1;
        int prow;
        while ((i <= m) && (j < n)) {
            prow = FindPivotRow(matriks, m, i, j);
            if ((prow != 0) && (prow <= m)) {
                if (prow != i) {
                    SwapRow(matriks, aug, m, n, i, prow);
                    prow = i;
                }
                DivideRow(matriks, aug, m, n, prow, j);
				
				
                if (type == "gauss") {
                    Gauss(matriks, aug, m, n, prow, j);
                }
                if (type == "gauss-jordan") {
                    GaussJordan(matriks, aug, m, n, prow, j);
                }
                i++;
            }
            j++;
        }
        System.out.println();
        MatriksKeLayar(matriks, aug, m, n);
        System.out.println();
		
		//SOLUTION SECTION & ALSO PRINTS SOLUTION!!! 
		char solutionType;
		double[] solution = new double[100];
		Arrays.fill(solution, -999);
		solutionType = checksolution(m,n,matriks,aug);
        // Action on solution type
        if (solutionType == 'U'){
            System.out.println("The solution of this matrix is unique!");
			solution = getUniqueSolution(m,n,matriks,aug);
			reverseSolution(solution);
			printSolution(solution);
        } else if (solutionType == 'N') {
            System.out.println("This matrix is inconsistent! No solutions available!");
			solution[1] = -999; // MEANS INCONSISTENT
        } else if (solutionType == 'I'){
            System.out.println("The solution of this matrix is infinite!");
			solution[1] = -999;// MEANS INFINITE 
            //printInfSolution(m,n, matriks, aug);
			//Program[][] infsol = new Program[100][100];
			fillInfArray(infsol,m,n,matriks,aug);
			printInfSolArray(infsol);
        }   
		return solution;
    }

// FIXED & UPDATED!!!
	public static double[] reverseSolution(double[] sol){
		//Get first index from the back of array
		int firstIdx = -999;
		for (int i = sol.length-1 ; i>=1 ; i--){
			if ( sol[i] != -999){
				firstIdx = i;
				break;
			}
		}
		
		//Reverse process
		
		if (firstIdx != -999){
			int j = firstIdx;
			double temp;
			int i = 1;
			while (!(i>=j)){
				temp = sol[i];
				sol[i] = sol[j];
				sol[j] = temp;
				j--;
				i++;
			}
		}
		else
			System.out.println("ERROR, Array is EMPTY!");
		
		return sol;
	}

	public static double[] getUniqueSolution(int m,int n,double[][]matriks, double[]aug){
		//Get the solution of an echelon matrix.
		double[] solution= new double[100];
        Arrays.fill(solution, -999);
		
		//step1 : search for leading 1 in every row
      
		int solutionIndex = 0;
		double rowsum = 0.0;
		
		for(int i = m; i>=1; i--){
			
			//check for rowsum
			for(int y = 1; y < n; y++){
				rowsum += matriks[i][y];
			}
			
			int counter = 0;
			if (rowsum == 0)
				//All zero row... 
				continue;
			else{
				//Solution found!
				solutionIndex++;
				solution[solutionIndex] = aug[i];
				//System.out.println(solutionIndex+" : "+ aug[i]);
				for(int j = n-1; j>=1;j--){
					counter++;
					if (j == i) 
						continue; //MEANS IT'S THE LEADING ONE ELEMENT, just skip it
					else if (j > i){
						//System.out.println(solution[solutionIndex]+" - "+matriks[i][j]);
						solution[solutionIndex] -= (matriks[i][j] * solution[counter]);
						//System.out.println("result "+solution[solutionIndex]);
					}
					/*else
						solution[solutionIndex] -= matriks[i][j]; // isi dari belakang
						System.out.println("result2 : "+solution[solutionIndex]);*/
				}
			}
		}
		return solution;
		
	}

	public static void printSolution(double[] sol)
    {
        System.out.println("\nHere comes the solution: ");
		//REVERSED VERSION :
			/*for (int i = sol.length-1; i >=1; i--) 
				if (!(sol[i] == -999)){
					System.out.printf("%.2f ", sol[i]);
					System.out.println();}   			
			System.out.println(); */    
		
		String tempstring;
		for (int i = 1; i < sol.length; i++) 
				if (!(sol[i] == -999)){
					tempstring = Double.toString(sol[i]);
					System.out.printf("x%d = %s ",i, tempstring);
					System.out.println();}   			
			System.out.println();
    }    

    public static char checksolution (int m, int n, double[][] matrix, double[] aug){
        char solutionType = 'U';                       // Unique solution assumed by default
        
        // No solution check
        double rowsum;
		boolean exist;
        int allzerorows = 0;
        for(int i = 1; i <= m; i++){
			exist = false;
            rowsum = 0.0;
            for(int j = 1; j < n; j++){
                rowsum += matrix[i][j];
				if (matrix[i][j]!=0){
					exist = true;
				}
            }
                
            if (rowsum + aug[i] == 0.0) {
                allzerorows ++;
            }
			
			// Inconsistent Matrix Check
			if ((!exist) && (aug[i] != 0.0)) {
                solutionType = 'N'; // NO SOLUTION
				break;
            }

        }
		
		 // Infinite solutions check
        if ((n-1) > (m-allzerorows) && solutionType != 'N' ) {
            solutionType = 'I'; //number of variables is more than the number of nonzero rows in the the matrix
        }
		

        
        return solutionType;
    }

    public static boolean IsInArray(int x,int length, int array[]){
		for(int idx = 1; idx<=length;idx++){
			if (array[idx] == x)
				return true;
		}
		return false;
    }
    
	// OLDDD ONE - DEPRECATED //
    public static void printInfSolution(int m, int n, double[][] matrix, double[] aug){
		
		// Search for free variables :
		boolean leadingOneFound;
		int freevariable [] = new int[99];
		//Arrays.fill(freevariable, -999);
		//int freevariableRow [] = new int[99];
		//Arrays.fill(freevariableRow, -999);
		int variableRow [] = new int[99];
		//Arrays.fill(variableRow, -999);
		
		int idx1 = 1; // for freevariable array
		int idx2 = 1; // for variable row array
		int increment = 0;
		//int idx3 = 0;
		for(int col = 1; col < n; col++){
			leadingOneFound = false;
			for(int row = 1+ increment; row <= m; row++){
					if (matrix[row][col] == 1.0 ){
						leadingOneFound = true;
						increment ++;
						variableRow[idx2] = row;
						//System.out.println("VARIABLE ROW gets : "+row);
						break;
					}
			}
			if (!(leadingOneFound)){
				//System.out.println("freevariable gets : "+col);
				freevariable[idx1] = col;
				//variableRow[idx3] = row;
				idx1++;
			}
			idx2++;
			//idx3++;
		}
		
		char[] parameters = {'z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p'}; //PARAMETERS INITIALIZATION
		//printing parametrical equations :
		double temp;
		boolean emptyString = true; // check biar value 0 gak return empty string
		
		for(int IdxVar = 1; IdxVar< n; IdxVar++){
			if (IsInArray (IdxVar,idx1,freevariable)){ // it's a free variable
				System.out.printf("X%d = %c", IdxVar, parameters[IdxVar]);
				System.out.println();
			}
			else{
				System.out.printf("X%d = ", IdxVar);
				
				if ( aug[variableRow[IdxVar]] != 0){
					System.out.printf("%f ",  aug[variableRow[IdxVar]] );}
					
				for(int col = 1 ;col< n; col++){
					if (col == IdxVar)
						continue;
					else{
						if (matrix[variableRow[IdxVar]][col] != 0.0){
							emptyString = false; // not empty string
							
							temp = matrix[variableRow[IdxVar]][col] *(-1.0);
							if (temp >=0) { // positive value
									if (IsInArray (col,idx1,freevariable)){ //check if it's a free variable
										//temp = matrix[freevariableRow[IdxVar-1]][col] *(-1.0);
										System.out.printf("+ %f%c", temp,parameters[col]);
									}
									else 
										//temp = matrix[variableRow[IdxVar-1]][col] *(-1.0);
										System.out.printf("+ %fX%d", temp, col);
							}	
							else { // negative value
								if (IsInArray (col,idx1,freevariable)){ //check if it's a free variable
									//temp = matrix[freevariableRow[IdxVar-1]][col] *(-1.0);
									System.out.printf("%f%c  ", temp,parameters[col]);
									}
								else { //NOT A FREE VARIABLE - HAVE TO BE AT MOST SIMPLIFIED FORM!!!
									//temp = matrix[variableRow[IdxVar-1]][col] *(-1.0);
									
									// simplification :
									
									System.out.printf("%fX%d  ", temp, col);
									}
							}
						}	
					}	
				}
			if (emptyString){ // empty string case
					System.out.printf("%f ", IdxVar, aug[variableRow[IdxVar]] );
			}
					
			System.out.println();
			}	
		}
    }

	//---------------------------NEW BATCH-------------------------- //

	public static void printInfSolArray(Program[][] solArray){
		int i = 1;
		int j = 1;
		simplifySol(solArray);
		//System.out.println("SIMPLIFY DONE! "+solArray[i][j].var);
		while (solArray[i][0].var != "-999" && i<=100) {
			System.out.printf("X%d = ", i);
			//print constant
			if (solArray[i][0].val != 0 || solArray[i][1].var.equals("-999"))
				System.out.printf("%.3f ", solArray[i][0].val);
			j=1; //reset j
			while (solArray[i][j].var != "-999" && j<=100) {
				if (solArray[i][j].val != 0){
					if (solArray[i][j].val >= 0 ){
						if (solArray[i][j].val != 1.00)
							System.out.printf("+%.3f", solArray[i][j].val);
						System.out.printf("%s", solArray[i][j].var);
					}
					else{
						if (solArray[i][j].val != 1.00)
							System.out.printf("%.3f", solArray[i][j].val);
						System.out.printf("%s", solArray[i][j].var);
					}
				}
				j++;
			}
		i++;
		System.out.println();
		}
	}
	
	public static void simplifySol ( Program[][] solArray){
		int i = 1;
		int k,y;
		double varconst = 0;
		int idxvar=0;
		char temp,temp2;
		boolean found;
		//System.out.println("TESTX3 "+solArray[3][1].var);
		while (solArray[i][0].var != "-999" && i<100) {
			//System.out.println("WHILE "+i);
			for(int j = 1; j < 100 && solArray[i][j].var != "-999" ; j++){
				// CHECK IF IT NEEDS TO BE SIMPLIFIED : HAVE AN UNFREE VARIABLE
				temp = 	(solArray[i][j].var).charAt(0);
				if(temp == 'X') {
						//System.out.println("VAR Needs to be simplified yang ke : "+j);
					//REPLACE & SHIFT LEFT!
					//save variable's const to varconst 
					varconst = solArray[i][j].val;
					
					//save x-index to idxvar
					k = j;
					idxvar=0; // reset idxvar
					for(int z =1 ; z< solArray[i][j].var.length(); z++){
						//System.out.println("IDXVAR FOR "+z);
						//System.out.println("batas atas "+solArray[i][j].var.length());
						temp2 = (solArray[i][j].var).charAt(z);
						//System.out.println("nilai charAt "+(solArray[i][j].var).charAt(z));
						idxvar = (idxvar * 10 ) + Character.getNumericValue(temp2);
						//System.out.println("Idxvar =  "+idxvar);
					}
					//GESER KIRI
					while( solArray[i][k].var != "-999"){
						//System.out.println("GESER KE "+k);
						solArray[i][k].val = solArray[i][k+1].val;
						solArray[i][k].var = solArray[i][k+1].var;
						k++;
					}
					
					//Determine if there's the same var in equation FOR EVERY ELEMENT IN X-idxvar
					
					// 1. for constants, it is mandatory!
					//System.out.println("OPERASI CONSTANTA :  "+solArray[i][0].val+" + "+solArray[idxvar][0].val*varconst);
					solArray[i][0].val += (solArray[idxvar][0].val) * varconst ;
					//2. for variables
					//cari neff reference array :
					int neffr = 0;
					for(int z = 1; z <(solArray[idxvar]).length && solArray[idxvar][z].var != "-999" ;z++){
						neffr++;
					}
					
					//FOR EVERY REFERENCE ARRAY ELEMENTS : 
					for(int z =1 ; z<= neffr; z++){
						//SEARCHING 
						
						//cari neff solArray dlu
						int neff = 0;
						for(int p = 1; p <(solArray[i]).length && solArray[i][p].var != "-999" ;p++){
							neff++;
						}
						
						//System.out.println("neff : " +neff);
						//SEARCHING DI ARRAY UTAMA
						found = false;
						int idxtarget = 0;
						for(int p = 1; p <=neff ;p++){
							//System.out.println("nilai current var : " +solArray[i][p].var + " matched with " + solArray[idxvar][z].var+"!");
							if( (solArray[i][p].var).trim().equals((solArray[idxvar][z].var).trim())) {
								found = true;
								//System.out.println(found);
								idxtarget = p;
								break;
							}
						}
						
						//DECISION
						if (found){ //IF FOUND
							//System.out.println("OPERASI FOUND :  "+solArray[i][idxtarget].val+" + "+solArray[idxvar][z].val);
							solArray[i][idxtarget].val += solArray[idxvar][z].val *varconst;
						}
						else{ // NOT FOUND = ADD AS LAST ELEMENT
							// find last idx :
							y = 1;
							while (solArray[i][y].var != "-999") {
								y++;
							}
							//System.out.println("Add " +solArray[idxvar][z].var+" as last element: " +y);
							solArray[i][y].val = solArray[idxvar][z].val*varconst;
							solArray[i][y].var = solArray[idxvar][z].var;
						}
					}	
					
				}
			}
		i++;
		}
	}
	public static void fillInfArray ( Program[][] solArray, int m, int n, double[][] matrix, double[] aug ){
		// fill array of parametrics for ALL var
		//initializing sol Array 
		for(int i = 1; i < 100; i++){
			for(int j = 0; j < 100; j++){
				solArray[i][j] = new Program(0,"-999"); //UNDEF VALUE
			}
		}
		
		// Search for free variables :
		boolean leadingOneFound;
		int freevariable [] = new int[99];
		int variableRow [] = new int[99];
		
		int idx1 = 1; // for freevariable array
		int idx2 = 1; // for variable row array
		int increment = 0;
		
		for(int col = 1; col < n; col++){
			leadingOneFound = false;
			for(int row = 1+ increment; row <= m; row++){
					if (matrix[row][col] == 1.0 ){
						leadingOneFound = true;
						increment ++;
						variableRow[idx2] = row;
						break;
					}
			}
			if (!(leadingOneFound)){
				freevariable[idx1] = col;
				idx1++;
			}
			idx2++;
		}
		
		char[] parameters = {'z','a','b','q','d','e','f','g','h','i','j','k','l','m','n','o','p','r','s','t','y','u'}; //PARAMETERS INITIALIZATION
		//printing parametrical equations :
		double temp;
		
		for(int IdxVar = 1; IdxVar< n; IdxVar++){
			solArray[IdxVar][0].var = "c";
			solArray[IdxVar][0].val = 0;
			
			if (IsInArray (IdxVar,idx1,freevariable)){ // it's a free variable
				solArray[IdxVar][1].var = String.valueOf(parameters[IdxVar]);
				solArray[IdxVar][1].val = 1.00;
				//System.out.println("CHECK IF1 "+IdxVar+" : "+solArray[IdxVar][1].var +" "+  solArray[IdxVar][1].val);
			}
			else{
				//constanta section
				solArray[IdxVar][0].var = "c";
				solArray[IdxVar][0].val = aug[variableRow[IdxVar]];
				//System.out.println("CHECK constanta "+IdxVar+" : "+solArray[IdxVar][0].var +" "+ solArray[IdxVar][0].val);
				
				// variables section
				int arridx = 1;
				for(int col = 1 ;col< n; col++){
					//System.out.println("COL : "+col);
					if (col == IdxVar)
						continue;
					else{
						temp = matrix[variableRow[IdxVar]][col] *(-1.0);
						if (temp != 0) {
							if (IsInArray (col,idx1,freevariable)){ //check if it's a free variable
								solArray[IdxVar][arridx].var = String.valueOf(parameters[col]);
								solArray[IdxVar][arridx].val = temp;
								//System.out.println("CHECK if2 free var "+IdxVar+" : "+solArray[IdxVar][arridx].var +" "+ solArray[IdxVar][arridx].val);
								}
							else // NOT FREE VARIABLE!
								{
								solArray[IdxVar][arridx].var = ('X'+String.valueOf(col));
								solArray[IdxVar][arridx].val = temp;
								//System.out.println("CHECK if2 not free "+IdxVar+" : "+solArray[IdxVar][arridx].var +" "+ solArray[IdxVar][arridx].val);
								}
							arridx++;
							}
						}
					}
				}
			}
		}

    /******************** SAVING SOLUTION TO EXTERNAL FILE ********************/
    public static void HasilKeFile (double[][] matriks, double[] aug, int m, int n, double[] solusi, double x, double y, String destination, Program[][] infsol) throws IOException {
        DecimalFormat df = new DecimalFormat("##.##");
        // defining target file and initiating writer for file
        File file = new File(destination);
        if (file.exists() == false) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        
        // Writing into file
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j < n; j++) {
                writer.write(String.valueOf(matriks[i][j]) + " ");
            }
            writer.write(aug[i] + "\n");
        }
		
		//check soltype : 
		char solutionType;
		solutionType = checksolution(m,n,matriks,aug);
		
        if (solutionType == 'U') {
            writer.write("\nSolusi:\n");
            for (int i = 1; i < n; i++) {
                writer.write("x" + i + " = " + df.format(solusi[i]) + "\n");
            }
            if ((x != -999) && (y != -999)) {
                if (solusi[1] != 0) {
                    writer.write("\nf(x) = " + df.format(solusi[1]));               // first term (no x)
                }
                if (solusi[2] != 0) {
                    writer.write(" + " + df.format(solusi[2]) + "x");                   // second term (x^1)
                }
                for (int i = 2; i < m; i++) {
                    if (solusi[i+1] != 0) {
                        writer.write(" + " + df.format(solusi[i+1]) + "x^" + i);    // next terms (x^i)
                    }
                }
                writer.write("\n\n");
                writer.write("f(" + df.format(x) + ") = " + df.format(y) + "\n");
            }
        } 
		else if (solutionType == 'I'){
			int i = 1;
			int j = 1;
            writer.write("\nSolusi:\n");
			while (infsol[i][0].var != "-999" && i<=100) {
				writer.write("X"+i+" = ");
				//print constant
				if (infsol[i][0].val != 0 || infsol[i][1].var.equals("-999"))
					writer.write(df.format(infsol[i][0].val));
				j=1; //reset j
				while (infsol[i][j].var != "-999" && j<=100) {
					if (infsol[i][j].val != 0){
						if (infsol[i][j].val >= 0 ){
							if (infsol[i][j].val != 1.00)
								writer.write(df.format(infsol[i][j].val));
							writer.write((infsol[i][j].var));
						}
						else{
							if (infsol[i][j].val != 1.00)
								writer.write(df.format(infsol[i][j].val));
							writer.write((infsol[i][j].var));
						}
					}
					j++;
				}
			i++;
			writer.write("\n");
			}
        }
		else {
            writer.write("\nMatriks ini tidak memiliki solusi karena merupakan matriks inconsistent!.\n\n");
        }
        writer.close();
    }













    public static void main (String[] args) throws IOException, FileNotFoundException {
        double[][] matriks = new double[100][100];      // main matrix for linear equation and interpolation
        double[] aug = new double[100];                 // augmented column of matrix
        double[][] interpolasi = new double[100][3];    // placeholder for interpolation points
        double[] solusi = new double[100];              // solution array for linear equation
        int m = 0;      // "matriks" row
        int n = 0;      // "matriks" column
        int l = 0;      // interpolation function degree (m - 1)
        double x = -999;   // absis to put in interpolation function
        double y = -999;   // ordinate from absis and interpolation function
        String filename = new String();             // file name for external file
        Scanner console = new Scanner(System.in);   // input from console reader
        int opsi1, opsi2, opsi3, opsi4;             // menu input variables
		Program[][] infsol = new Program[100][100]; // special case for infsolution

        // MENU
        System.out.println("MENU");
        System.out.println("1. Sistem Persamaan Linier");
        System.out.println("2. Interpolasi Polinom");
        System.out.println("3. Keluar");
        System.out.print("Pilihan Anda (1-3): "); opsi1 = console.nextInt();
        while ((opsi1 < 1) && (opsi1 > 3)) {
            System.out.print("Opsi hanya 1 atau 2. Input lagi: "); opsi1 = console.nextInt();
        }

        // 3. KELUAR
        if (opsi1 == 3) {
            System.exit(0);
        }

        System.out.println();
        System.out.println("Jenis input:");
        System.out.println("1. Dari pengguna");
        System.out.println("2. Dari file eksternal");
        System.out.print("Pilihan jenis input (1/2): "); opsi2 = console.nextInt();
        while ((opsi2 < 1) && (opsi2 > 2)) {
            System.out.print("Opsi hanya 1 atau 2. Input lagi: "); opsi2 = console.nextInt();
        }
        
        // 1. SISTEM PERSAMAAN LINIER
        if (opsi1 == 1) {
            // 1. Input dari pengguna
            if (opsi2 == 1) {
                System.out.print("Jumlah baris: "); m = console.nextInt();
                System.out.print("Jumlah kolom: "); n = console.nextInt() + 1;
                MatriksDariLayar(matriks, aug, m, n, console);
            }
            // 2. Input dari file eksternal
            if (opsi2 == 2) {
                System.out.print("Nama file eksternal (dan extension-nya): "); filename = console.next();
                m = CountRow(filename);
                n = CountColumn(filename);
                MatriksDariFile(matriks, aug, m, n, filename);
            }
            System.out.println();
            MatriksKeLayar(matriks, aug, m, n);
        }

        // 2. INTERPOLASI
        if (opsi1 == 2) {
            // 1. Input dari pengguna
            if (opsi2 == 1) {
                System.out.print("Derajat fungsi: "); l = console.nextInt();
                m = l + 1;
                InterpolasiDariLayar(interpolasi, m, console);
            }
            // 2. Input dari file eksternal
            if (opsi2 == 2) {
                System.out.print("Nama file eksternal (dan extension-nya): "); filename = console.next();
                m = CountRow(filename) - 1;
                l = m - 1;
                InterpolasiDariFile(interpolasi, m, filename);
            }
            n = m + 1;
            x = interpolasi[m+1][1];
            Matrikskan(interpolasi, matriks, aug, m, n);
            System.out.println();
            MatriksKeLayar(matriks, aug, m, n);
        }

        System.out.println();
        System.out.println("Metode penyelesaian:");
        System.out.println("1. Gauss");
        System.out.println("2. Gauss-Jordan");
        System.out.print("Pilihan metode penyelesaian (1/2): "); opsi3 = console.nextInt();
        while ((opsi3 < 1) && (opsi3 > 2)) {
            System.out.print("Opsi hanya 1 atau 2. Input lagi: "); opsi3 = console.nextInt();
        }

        if (opsi3 == 1) {
			solusi = Solve(matriks, aug, n, m, "gauss", infsol);
        }
        if (opsi3 == 2) {
			solusi = Solve(matriks, aug, n, m, "gauss-jordan",infsol);
        }
		
        if (opsi1 == 2) {
            FungsiInterpolasi(solusi, l);
            y = SolusiInterpolasi(solusi, l, x);
            System.out.printf("f(%.2f) = %.2f", x, y);
        }

        System.out.println();
        System.out.println("Simpan hasil ke file eksternal?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");
        System.out.print("Pilihan Anda (1/2): "); opsi4 = console.nextInt();
        while ((opsi4 < 1) && (opsi4 > 2)) {
            System.out.print("Opsi hanya 1 atau 2. Input lagi: "); opsi4 = console.nextInt();
        }
        
        if (opsi4 == 1) {
            System.out.print("Nama file eksternal (dan extension-nya): "); filename = console.next();
            HasilKeFile(matriks, aug, m, n, solusi, x, y, filename,infsol);
        }
    }
}