public class FloydCycleDetection {

    //method operates similar to the one in linked list
    //should detect a cycle ina functional graph using a fast and slow pointer

    /*     public Integer checkForCycle(){
    Node slow = this.head;
    Node fast = this.head;
    while (fast != null && fast.next != null){
        slow = slow.next;
        fast = fast.next.next;
        if (fast == slow){
            slow = this.head;
            while (slow != fast){
                slow = slow.next;
                fast = fast.next;
            }
            return slow.data;
        }
    }
    return null;
}
*/
    public static int findCycleStart(FunctionalGraph graph, int startVertex) {

        // Initialize both pointers at the starting vertex
        int slow = startVertex;
        int fast = startVertex;

        // Phase 1: Detect whether a cycle exists
        while (fast != FunctionalGraph.NO_SUCCESSOR) {


            slow = graph.getSuccessor(slow);


            int preFast = graph.getSuccessor(fast);
            // Check if fast has reached the end
            if (preFast == FunctionalGraph.NO_SUCCESSOR) {
                return FunctionalGraph.NO_SUCCESSOR;
            }
            fast = graph.getSuccessor(preFast);

            // Check whether the pointers have met
            if (slow == fast) {
                slow = startVertex;
                while (slow != fast) {
                    slow = graph.getSuccessor(slow);
                    fast = graph.getSuccessor(fast);
                }
                // Return the vertex where the cycle begins
                return slow;
            }
        }
        // No cycle was detected
        return FunctionalGraph.NO_SUCCESSOR;
    }
}

