package objectOrientedProgram.deckofcard;

public class MyDequeue<T> {
    MyLinkedList<T> myLinkedList;

    public MyDequeue() {
        myLinkedList = new MyLinkedList<T>();
    }

    public void addFront(T data) {
        myLinkedList.insert(0, data);
    }

    public void addRear(T data) {
        myLinkedList.add(data);
    }

    public T removeFront() {
        return myLinkedList.pop(0);
    }

    public T removeRear() {
        return myLinkedList.pop();
    }

    public boolean isEmpty() {
        return myLinkedList.isEmpty();
    }

    public int size() {
        return myLinkedList.size();
    }
}

