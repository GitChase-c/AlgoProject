public class Main {
    public static void main(String[] args) {
        LinkedList myList = new LinkedList();

        myList.insert(10);
        myList.insert(20);
        myList.insert(30);
        myList.makeCycle(25);

        Object cycle = myList.checkForCycle();
        if ( cycle == null){
            myList.printList();
        }
        else {
            System.out.print("Cycle Detected, starts at " + cycle);
        }

    }
}
