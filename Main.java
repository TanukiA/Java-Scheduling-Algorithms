import java.util.*;

public class Main{
	public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        boolean cont = true;
        int choice = 0;
	    do{

            try{
                System.out.println("Please choose a CPU scheduling algorithm: ");
                System.out.println("1. Round Robin\n2. Non Preemptive Priority\n3. Non Preemptive SJF\n4. Quit");
                System.out.print("Your choice: ");
                if(input.hasNext()){
                    choice = input.nextInt();
                }
                if(choice<1 || choice>4){
                    System.out.println("Please enter a number within 1-4.\n");
                    continue;
                }

            }catch(InputMismatchException e){
                System.out.println("Invalid input. Please enter a number.\n");
                input.nextLine();
                continue;
            }

            switch(choice){
                case 1: new RoundRobin(input); break;
                case 2: new NonPreemptivePriority(input); break;
                case 3: new NonPreemptiveSJF(input); break; 
                case 4: cont = false; break;
            }
            
        }while(cont);

        input.close();

	}
}