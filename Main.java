public class Main {
    public static void main(String[] args) {
        LinkedList myList = new LinkedList();

        myList.insert(10);
        myList.insert(20);
        myList.insert(30);
        myList.insert(40);
        myList.insert(50);

        myList.makeCycle(2);

        Integer cycle = myList.checkForCycle();

        if (cycle == null) {
            System.out.println("No cycle detected");
        } else {
            System.out.println(
                    "Cycle detected, starts at " + cycle
            );
        }
    }
}