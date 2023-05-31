
package csc227.proj;

import java.io.*;
import java.util.*;

public class Csc227Proj {

	
    public static void main(String[] args) {
    	Scanner input= new Scanner(System.in);
    	int M;
    	int size;
    	
    	System.out.println("enter the number of partitions");
    	M=input.nextInt();
    	
    	Partition[] memory=new Partition[M];
    	int startAdd,endAdd;
    	for(int i=0;i<M; i++) {
    		
    		System.out.println("enter the size of partition#" +(i+1)+ "  in KB");
    		size=input.nextInt();
    		
    		if(i==0)
    			startAdd=0;
    		else
    			startAdd=memory[i-1].getEndAddress()+1;
    		
    		endAdd=startAdd+ size-1;// because we always start from 0
    		
    		memory[i]= new Partition("free",size,startAdd,endAdd,"Null",-1);
    	}
    	
    	System.out.println("Enter your prefered allocating strategy");
    	System.out.println("[First-fit (F), Best-fit (B), or Worst-fit (W)]");
    	String strategy=input.next();
    	
    	
    	boolean running=true;
    	while (running) {
    		
        	System.out.println("select from the menu\n1. Allocate a block of memory.\n"
        			+ "2. De-allocate a block of memory.\n"
        			+ "3. Report detailed information about regions of free and allocated memory blocks.\n"
        			+ "4. Exit the program.");
        	int option=input.nextInt();
        	
    	
    	switch ((int)option){
    	
    	case 1: // Allocate 
    	    System.out.println("enter the process ID and the size of allocation in the form P# ##");
            String processID = input.next();
            int Psize = input.nextInt();
    	    boolean check=true;
    	//check if the allocation insufficient- check duplicate
            for (Partition p : memory)
                   if (p.getProcessID().equals(processID)) {
                       System.out.println("there exist a process with same ID"); //duplicate
                        check=false;
                        		break; }
                    if(!check) 
                     	break;
            
            
               boolean allocateResult= allocate(Psize,processID,strategy,memory);
               if(!allocateResult)
               System.out.println("sorry, can not allocate this process");
    	    displayMemoryState(memory);
    	    break;
    		
    	case 2: //De-allocate
    		System.out.println("Enter the ProcessID to be deallocated");
    		String processDeallocate=input.next();
    		deallocate(processDeallocate,memory);
    		displayMemoryState(memory);
    		break;
    		
    	case 3: //Report 
    		displayDetailedInformation(memory);
    		writeToFile(memory);
    		break;
    		
    	case 4: //Exit 
            running=false;
            
    	break;
    	   }
    	}
    	
    }
    
    public static void deallocate(String processID, Partition[] memory) {
    	boolean ProcessExist=false;
    	
    	for(int i=0;i<memory.length;i++)
    	if (memory[i].getProcessID().equals(processID)) {
    		memory[i].setProcessID("Null");
    		memory[i].setStatus("free");
    		memory[i].setInternalFragmentation(-1);
    		ProcessExist=true;
    		System.out.println("the process "+processID+" has been deallocated");
    	}
    	 if (ProcessExist==false)
    		 System.out.println("the procces doesn't exist");
    	
    }
    
   public static void displayMemoryState(Partition[] memory) {
	   System.out.print("[");
	   for(int i=0;i<memory.length;i++) {
		   if(memory[i].getStatus().equalsIgnoreCase("allocated"))
			   System.out.print(memory[i].getProcessID()+"|");
		   else
			   System.out.print("H|");
		   
	   }
	   System.out.println("] \n");
   }
   
   public static void displayDetailedInformation(Partition[] memory) {
	   for(int i=0;i<memory.length;i++) {
               System.out.println("------------------------------------");
		   System.out.println("partition number " + (i+1) );
		   System.out.println("Partition status "+ memory[i].getStatus());
		   System.out.println("Partition size "+ memory[i].getSize());
		   System.out.println("Partition starting address "+ memory[i].getStartAddress());
		   System.out.println("Partition ending address "+ memory[i].getEndAddress());
		   System.out.println("Process ID "+ memory[i].getProcessID());
		   System.out.println("Internal fragmentation size "+ memory[i].getInternalFragmentation());
                System.out.println("------------------------------------");

		   
	   }
   }
   
   public static void writeToFile(Partition[] memory) {
	   try {
		    File myfile = new File("Report.txt");
		    FileWriter myWriter = new FileWriter(myfile);
		    for(int i=0;i<memory.length;i++) {
		      myWriter.write( //check printed pnum
				   "partition number " + (i+1) +"\n"+
				   "Partition status "+ memory[i].getStatus()+"\n"+
				   "Partition size "+ memory[i].getSize()+"\n"+
				   "Partition starting address "+ memory[i].getStartAddress()+"\n"+
				   "Partition ending address "+ memory[i].getEndAddress()+"\n"+
				   "Process ID "+ memory[i].getProcessID()+"\n"+
				   "Internal fragmentation size "+ memory[i].getInternalFragmentation()+"\n"+
               "-------------------------------\n"
				   
	   );}
		      myWriter.close();    
	   }
	   catch(Exception e) {
		   System.out.println("error occured");
	   }
   }
   
   public static boolean allocate( int processSize, String processID, String allocationStrategy, Partition[] memory){
        boolean success = false;

        if (allocationStrategy.equals("F")) {
            for (int i = 0; i < memory.length; i++) {
                if (memory[i].getStatus().equals("free") && memory[i].getSize() >= processSize) {
                    memory[i].Pallocate(processID, processSize); //add at first empty hole
                    success = true;
                    break;
                }
            }
        } else if (allocationStrategy.equals("B")) {
            int bestFitIndex = -1;
            int bestFitSize = Integer.MAX_VALUE;

            for (int i = 0; i < memory.length; i++) {
                if (memory[i].getStatus().equals("free")  && memory[i].getSize() >= processSize && memory[i].getSize() < bestFitSize) {
                    bestFitIndex = i;
                    bestFitSize = memory[i].getSize();
                }
            }

            if (bestFitIndex != -1) {
                memory[bestFitIndex].Pallocate(processID,processSize);
                success = true;
            }
        } else if (allocationStrategy.equals("W")) {
            int worstFitIndex = -1;
            int worstFitSize = -1;

            for (int i = 0; i < memory.length; i++) {
                if (memory[i].getStatus().equals("free")  && memory[i].getSize() >= processSize && memory[i].getSize() > worstFitSize) {
                    worstFitIndex = i;
                    worstFitSize = memory[i].getSize();
                }
            }

            if (worstFitIndex != -1) {
                memory[worstFitIndex].Pallocate(processID,processSize);
                success = true;
            }
        }
        return success;
     }
    
}
