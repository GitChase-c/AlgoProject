class LinkedList {
    Node head;
    Node tail;
    boolean hasCreatedCycle = false;


    public void insert(int data) {
        if (hasCreatedCycle) {
            throw new IllegalStateException(
                    "Cannot insert into a cyclic list"
            );
        }

        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public void makeCycle(int index) {
        Node cycleStart = getNodeAtI(index);
        tail.next = cycleStart;
        hasCreatedCycle = true;
    }

    public Node getNodeAtI (int index){
        Node temp = head;
        for (int i = 0; i < index; i++) {
            if (temp == null) {
                throw new IndexOutOfBoundsException();
            }
            temp = temp.next;
        }

        if (temp == null) {
            throw new IndexOutOfBoundsException();
        }

        return temp;
    }
    public Integer checkForCycle(){
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
