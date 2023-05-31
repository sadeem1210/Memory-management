/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package csc227.proj;

/**
 *
 * @author FAY
 */
public class Partition {
    
    private String status; // "allocated" or "free"
    private int size; // size of partition in KB
    private int startAddress; // starting address of partition in KB
    private int endAddress; // ending address of partition in KB
    private String processID; // ID of process currently allocated in partition, or "Null" if free, in form of PN => N=1,2,..etc
    private int internalFragmentation; // size of internal fragmentation in KB
    
    
    
    public Partition(String status, int size, int startAddress, int endAddress, String processID, int internalFragmentation) {
        this.status = status;
        this.size = size;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.processID = processID;
        this.internalFragmentation = internalFragmentation;
    }
    
    // getters and setters for each attribute

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public int getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(int endAddress) {
        this.endAddress = endAddress;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public int getInternalFragmentation() {
        return internalFragmentation;
    }

    public void setInternalFragmentation(int internalFragmentation) {
        this.internalFragmentation = internalFragmentation;
    }
  

    public void Pallocate(String processID,int processSize) {
        this.processID = processID; // assigns the given process to the partition
        this.status = "allocated"; // sets the partition as allocated
        int fragmentation = size - processSize;
        this.internalFragmentation=fragmentation;
    }
}
