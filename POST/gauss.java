public static void fillInfArray ( program[][] solArray, ){
		// fill array of parametrics for just one variable
		
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
		
		char[] parameters = {'z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p'}; //PARAMETERS INITIALIZATION
		//printing parametrical equations :
		double temp;
		
		for(int IdxVar = 1; IdxVar< n; IdxVar++){
			if (IsInArray (IdxVar,idx1,freevariable)){ // it's a free variable
				solArray[IdxVar][1].var = 'X'+parameters[IdxVar];
				solArray[IdxVar][1].val = 1.00;
			}
			else{
				
				if ( aug[variableRow[IdxVar]] != 0){
					//constanta section
					solArray[IdxVar][0].var = 'c'
					solArray[IdxVar][0].val = aug[variableRow[IdxVar]];
				
				// variables section
				for(int col = 1 ;col< n; col++){
					if (col == IdxVar)
						continue;
					else{
						if (matrix[variableRow[IdxVar]][col] != 0.0){
							emptyString = false; // not empty string
							
							temp = matrix[variableRow[IdxVar]][col] *(-1.0);
							
							if (IsInArray (col,idx1,freevariable)){ //check if it's a free variable
								solArray[IdxVar][col].var = parameters[col]
								solArray[IdxVar][col].val = temp;
								}
							else // NOT FREE VARIABLE!
								{
								solArray[IdxVar][col].var = parameters[col]
								solArray[IdxVar][col].val = temp;
								}
							}
						}
					}
				}
			}
		}
}