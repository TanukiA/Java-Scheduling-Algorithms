import java.util.*;

public class NonPreemptivePriority extends SchedulingAlgorithm{

    public static Process[] proc1 = new Process[11];
    public static Process[] proc2 = new Process[11];
    public static Process timechart = new Process();
    public static PriorityQueue<Process> queue = new PriorityQueue<Process>(new PriorityComparator());
    public static ArrayList<TimeChart> timeChart = new ArrayList<TimeChart>();
    public static float avgTurnArdTime, avgWaitTime;

    public NonPreemptivePriority(Scanner input){

        System.out.println("---------------------------");
        System.out.println("| Non Preemptive Priority |");
        System.out.println("---------------------------");
        
        for(int i=0; i<proc1.length; i++){
            proc1[i] = new Process();
            proc2[i] = new Process();
        }
   
        proc1[0].turnArdTime = 0;
        proc1[0].waitTime = 0;
        int n = getInput(input);
        
        simulation(n);
        turnWait(n);
        ganttChart(n, timeChart);
        displayTable(n);
    }

    public static int getInput(Scanner input){
        int n; 
        while(true){
            try{
    
                System.out.print("Enter number of process (3-10): ");
                n = input.nextInt();
                if(n < 3 || n > 10){
                    System.out.println("Number of process must within 3-10. Please try again.\n");
                    continue;
                }

                boolean flag = true;
                for(int i=1; i<=n; i++){
                    System.out.print("Enter process ID: ");
                    proc1[i].id = input.nextInt();
                    System.out.print("Enter arrival time for P" + proc1[i].id + ": ");
                    proc1[i].arrivalTime = input.nextInt();
                    if(proc1[i].arrivalTime < 0){
                        System.out.println("Minimum arrival time is 0. Please try again.\n");
                        flag = false;
                        break;
                    }
                    System.out.print("Enter burst time for P" + proc1[i].id + ": ");
                    proc1[i].burstTime = input.nextInt();
                    if(proc1[i].burstTime < 1){
                        System.out.println("Minimum burst time is 1. Please try again.\n");
                        flag = false;
                        break;
                    }
                    System.out.print("Enter priority for P" + proc1[i].id + ": ");
                    proc1[i].priority = input.nextInt();
                    if(proc1[i].priority < 1){
                        System.out.println("Minimum priority is 1. Please try again.\n");
                        flag = false;
                        break;
                    }
                }
                
                if(flag == false){
                    continue;
                }else{
                    
                    // Sort process by arrival time, priority then by burst time
                    Arrays.sort(proc1, 1, n+1, new Comparator<Process>(){
                        public int compare(Process p1, Process p2) {

                            Integer compr1 = Integer.compare(p1.arrivalTime, p2.arrivalTime);
                    
                            if (compr1 != 0) {
                                return compr1;
                            } 

                            Integer compr2 = Integer.compare(p1.priority, p2.priority);
                    
                            if (compr2 != 0) {
                                return compr2;
                            } 
                    
                            return Integer.compare(p1.burstTime, p2.burstTime);
                        }
                    });
                    
                    // deep copy the process array
                    for(int i=1; i<=n; i++){
                        proc2[i] = new Process(proc1[i]);
                    }
                    break;
                }
            
            }catch(InputMismatchException e){
                System.out.println("Invalid input. Please try again.\n");
                input.nextLine();
                continue;
            }
        }
   
        return n;
    }

    public static void turnWait(int n){

        for(int i=1; i<=n; i++){
            proc1[i].turnArdTime = proc1[i].finishTime - proc1[i].arrivalTime;
            proc1[i].waitTime = proc1[i].turnArdTime - proc1[i].burstTime;
            proc1[0].turnArdTime = proc1[0].turnArdTime + proc1[i].turnArdTime;
            proc1[0].waitTime = proc1[0].waitTime + proc1[i].waitTime;
        }   

        avgTurnArdTime = proc1[0].turnArdTime/n;
        avgWaitTime = proc1[0].waitTime/n;
    }

    public static void simulation(int n){

        int i = 1; 
        // set the first arrival time as next value
        int nextVal = proc2[1].arrivalTime; 
        timeChart.add(new TimeChart(proc2[1].id, nextVal));

        // add all process that arrived ealier or same time (sort automatically by priority then by arrival time)
        for(i=1; (i<=n) && (proc2[i].arrivalTime<=nextVal); i++){
            queue.offer(new Process(i, proc2[i].priority, proc2[i].arrivalTime));
        }
 
        // repeat for all process in queue
        while(!queue.isEmpty()){ 
            
            int f = queue.element().getIndex();   // get first process
            queue.remove();  // remove first process

            int temp = nextVal;

            nextVal = nextVal + proc2[f].burstTime;
            proc2[f].burstTime = 0; 
            proc1[f].finishTime = nextVal;   // set finish time for the process

            int chartBurst = nextVal - temp;
            timeChart.add(new TimeChart(proc2[f].id, chartBurst, nextVal));   // record time chart after a process

            // add all process that arrived ealier or same time (sort automatically by priority then by arrival time)
            while(i<=n && proc2[i].arrivalTime <= nextVal){
                queue.offer(new Process(i, proc2[i].priority, proc2[i].arrivalTime));
                i++;
            } 
        }
    } 

    public static void displayTable(int n){

        Arrays.sort(proc1, 1, n+1, new Comparator<Process>(){
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.id, p2.id);
            }
        });

        System.out.println("\n-------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-13s | %-11s | %-10s | %-15s | %-15s | %-13s |%n", "", "ARRIVAL TIME", "BURST TIME", 
            "PRIORITY", "FINISHING TIME", "TURNAROUND TIME", "WAITING TIME");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for(int i = 1; i <= n; i++){
            System.out.printf("| %-4s | %-13d | %-11d | %-10d | %-15d | %-15.0f | %-13.0f |%n", "P" + proc1[i].id, proc1[i].arrivalTime, 
            proc1[i].burstTime, proc1[i].priority, proc1[i].finishTime, proc1[i].turnArdTime, proc1[i].waitTime);
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
            
        // Round to 2 decimal places
        avgWaitTime = (float)(Math.round(avgWaitTime * 100.0) / 100.0);
        avgTurnArdTime = (float)(Math.round(avgTurnArdTime * 100.0) / 100.0);

        System.out.println("Total turnaround time: " + proc1[0].turnArdTime + " ms");
        System.out.println("Average turnaround time : " + avgTurnArdTime + " ms");
        System.out.println("Total waiting time: " + proc1[0].waitTime + " ms");
        System.out.println("Average waiting time : " + avgWaitTime + " ms");
        System.out.println();
    }

}

// For process inside the queue (already arrive), sort process by priority then by arrival time, then by burst time
class PriorityComparator implements Comparator<Process>{
    
    public int compare(Process p1, Process p2) {

        Integer compr1 = Integer.compare(p1.priority, p2.priority);
                
        if (compr1 != 0) {
            return compr1;
        } 

        Integer compr2 =  Integer.compare(p1.arrivalTime, p2.arrivalTime);

        if (compr2 != 0) {
            return compr2;
        } 

        return Integer.compare(p1.burstTime, p2.burstTime);
    }
}




