import java.util.ArrayList;

public class SchedulingAlgorithm {
    
    public static void ganttChart(int n, ArrayList<TimeChart> timeChart){
        int i, j;
        System.out.println("\nGantt Chart:");

        // print top bar
        System.out.print("-");
        for(i=0; i<timeChart.size(); i++) {
            for(j=0; j<timeChart.get(i).getBurstTime(); j++) 
                System.out.print("--");
            System.out.print("-");
        }
        System.out.print("\n|");
        
        // print process ID
        for(i=1; i<timeChart.size(); i++) {
            for(j=0; j<timeChart.get(i).getBurstTime()-1; j++){
                System.out.print(" ");
            }
            System.out.printf("P%d", timeChart.get(i).getProcess());
            for(j=0; j<timeChart.get(i).getBurstTime()-1; j++){
                System.out.print(" ");
            }
            System.out.print("|");
        }
        System.out.println();
        
        // print bottom bar
        for(i=0; i<timeChart.size(); i++) {
            for(j=0; j<timeChart.get(i).getBurstTime(); j++) 
                System.out.print("--");
            System.out.print("-");
        }
        System.out.println();
        
        // print time
        for(i=0; i<timeChart.size(); i++) {
            for(j=0; j<timeChart.get(i).getBurstTime(); j++){
                if((timeChart.get(i).getTime() > 9) && (j == timeChart.get(i).getBurstTime()-1)){
                    System.out.print(" ");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.print(timeChart.get(i).getTime());
        }
        System.out.println();
    }

}

class Process{
    int id, index, arrivalTime, burstTime, finishTime, priority;
    float waitTime, turnArdTime;

    Process() {}

    // a copy contructor
    Process(Process cloneProcess){
        this.id = cloneProcess.id;
        this.arrivalTime = cloneProcess.arrivalTime;
        this.burstTime = cloneProcess.burstTime;
        this.waitTime = cloneProcess.waitTime;
        this.turnArdTime = cloneProcess.turnArdTime;
        if(Integer.valueOf(cloneProcess.priority) != null)
            this.priority = cloneProcess.priority;
    }

    // used for Non Preemptive Priority
    Process(int index, int priority, int arrivalTime){
        this.index = index;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
    }

    // used for Non Preemptive SJF
    Process(boolean sjf, int index, int burstTime, int arrivalTime){
        this.index = index;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }

    int getIndex(){
        return index;
    }
}

class TimeChart{
    private int process, burstTime, time;

    public TimeChart(int process, int time){
        this.process = process;
        this.time = time;
    }

    public TimeChart(int process, int burstTime, int time){
        this.process = process;
        this.burstTime = burstTime;
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public int getProcess(){
        return process;
    }

    public int getBurstTime(){
        return burstTime;
    }
}    
