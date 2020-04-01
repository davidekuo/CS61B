// CS61B 2018 Discussion 3 Part 2: Arrays

public class Exercise2_Arrays {
	
	public static void print(int[] arr) {
		for(int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

	/** Exercise 2.1
	Create a method that inserts (item) into array arr at the given position and returns the resulting array.
	For example, if x = [5, 9, 14, 15], item = 6, and position = 2, then the method should return [5, 9, 6, 14, 15].
	If position is past the end of the array, insert item at the end of the array.
	*/
	public static int[] insert(int[] arr, int item, int position) {
		int[] newArr = new int[arr.length+1];
		position = Math.min(position, arr.length);
		if (position >= arr.length) {
			System.arraycopy(arr, 0, newArr, 0, arr.length);
			newArr[position] = item;
		} else {
			System.arraycopy(arr, 0, newArr, 0, position);
			newArr[position] = item;
			System.arraycopy(arr, position, newArr, position+1, arr.length-position);
		}
		return newArr;	
	}

	/** Exercise 2.2
		Create a method that destructively reverses the items in arr. 
		For example calling reverse on an array [1, 2, 3] should change the array to be [3, 2, 1].
		What is the fewest number of iteration steps you need? 
			arr.length/2
		What is the fewest number of additional variables you need?
			1
	*/
		public static void reverse(int[] arr) {
			for (int i = 0; i < arr.length/2; i++) {
				int temp = arr[i];
				arr[i] = arr[arr.length-1-i];
				arr[arr.length-1-i] = temp;
			}
		}

	/** Exercise 2.3
		Write a non-destructive method replicate(int[] arr) that replaces the number at index i with arr[i] copies of itself.
		For example, replicate ([3,2,1]) returns [3,3,3,2,2,1].
	*/
		public static int[] replicate(int[] arr) {
			int newLength = 0;
			for (int i : arr) {
				newLength += i;
			}
			int[] newArr = new int[newLength];
			int counter = 0;
			for (int item : arr) {
				for(int k = 0; k < item; k++) {
					newArr[counter] = item;
					counter++;
				}
			}
			return newArr;
		}

		public static void main(String[] args) {
			int[] test1 = {1};
			int[] test2 = {1,2};
			int[] test3 = {1,2,3};
			
			reverse(test1); 
			reverse(test2); 
			reverse(test3); 

			print(test1); // 1
			print(test2); // 2 1
			print(test3); // 3 2 1

			int[] test4 = insert(test3, 4, 7);
			print(test4); // 3 2 1 4
			test4 = insert(test4, 5, 2);
			print(test4); // 3 2 5 1 4

			print(replicate(test1)); // 1
			print(replicate(test2)); // 2 2 1
			print(replicate(test3)); // 3 3 3 2 2 1


		}
}

