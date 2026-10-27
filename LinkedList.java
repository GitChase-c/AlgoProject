class LinkedList {
    Node head;

    static class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    public void insert(int data) {
        Node newNode = new Node(data);
        if (this.head == null) {
            this.head = newNode;
        }
        else {
            Node temp = this.head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }
    public void makeCycle(int data) {
        Node newNode = new Node(data);
        Node temp = this.head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
        newNode.next = getNodeAtI(2);
    }
    public Node getNodeAtI (int pointer){
        int i = 0;
        Node temp = this.head;
        while (i != pointer){
            temp = temp.next;
            i++;
        }
        return temp;
    }
    public Object checkForCycle(){
        Node slow = this.head;
        Node fast = this.head;
        while (fast != null & fast.next != null){
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


    public void printList() {
        Node current = this.head;
        System.out.print("List: ");
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
