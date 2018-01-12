class ReadWriteExample {
	 int A = 0;
	 boolean B = false;
	 
	 //CPU1 (thread1) runs this method
	 void writer() {
		A = 10;   //stores 10 to memory location A
		B = true; //stores true to memory location B
	 }
	 
	 //CPU2 (thread2) runs this method
	 void reader() {
		while (!B) continue; //loads data from memory location B
		// I do care about A and B store order in method writer()
		assert A == 10; //loads data from memory location A
	}
}
List 1


class ReadWriteExample {
	 int A = 0;
	 boolean B = false;
	 
	 //CPU1 (thread1) runs this method
	 void writer () {
		 A = 10; //stores 10 to memory location A
		 membar; //pseudo memory barrier to make sure                              
		 //line 7 is executed before the next one
		 B = true; //stores true to memory location B
	 }
	 
	 //CPU2 (thread2) runs this method
	 void reader () {
		 while (!B) continue; //loads from memory location B
		 // I do care about the A and B store order in method writer()
		 membar; //pseudo memory barrier to make sure
		 //line 15 is executed before the next one
		 assert A == 10; //loads from memory location A
	 }                                                             
}
	 List 2
	 
	 
	 
	 
	 
	 
	 
	 
A = 10;
B = A * 2; //B’s value dependents on its previous A’s value.
List 3







A = 10;                                                           
B = A + 10; //It has to waits for the previous statement to finish  
C = 20;     //it has no dependency on its previous 2 statements
List 4



//suppose A and B are some objects; r variables are all local
//thread1 runs this method
void foo() {
	r1 = A;
	r2 = r1.x;
	r3 = B;
	r4 = r3.x;
	r5 = r1.x;
	r6 = r1.y;
}

//thread2 runs this method
void bar() {
	r7   = A;
	r7.x = 3;
	r7.y = 5;
}
List 5





class ReadWriteExample {
    int A = 0;
	
	//thread1 runs this method
	void writer () {
		lock monitor1;   //a new value will be stored
		A = 10;          //stores 10 to memory location A
		unlock monitor1; //a new value is ready for reader to read
    }
	
	//thread2 runs this method
	void reader () {
		lock monitor1;  //a new value will be read
		assert A == 10; //loads from memory location A
		unlock monitor1;//a new value was just read
	}
}
List 6
	

class ReadWriteExample {
    int A = 0;  //normal field
	volatile boolean B = false; //a data ready flag
	
	//thread1 runs this method
	void writer () {
		A = 10;   //stores 10 to memory location A
		B = true; //stores true to memory location B
	}
	
	//thread2 runs this method
	void reader () {
		//we use a while statement to spin CPU for demo purpose;
		// using wait and notify may be more efficient.
		while (!B) continue; //loads from memory location B
		assert A == 10; //loads from memory location A
	}
}
List 7

class FinalFieldExample {
    final int x;
	int y;
	static FinalFieldExample f;
	
	public FinalFieldExample() {
		x = 3;
		y = 4;
		// f = this; //don’t do this
	}
	
	//one thread executes this method
	static void writer() {
		f = new FinalFieldExample();
	}
	
	//another thread executes this method
	static void reader() {
		if (f != null) {
			int i = f.x;   //guaranteed to see 3
			int j = f.y;   //could see 0
		}
	}
}
List 8
	
