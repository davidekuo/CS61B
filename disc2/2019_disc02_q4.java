// Discussion 2 Question 4

public static IntList square(IntList L) {
	
	if (L == null) {
		return L;
	}

	return new IntList(L.first * L.first, square(L.rest));

}

public static IntList squareDestructive(IntList L) {
	
	if (L == null) {
		return L;
	}

	L.first = L.first * L.first;
	squareDestructive(L.rest);
		
	return L;
}