
public class LinkedList<E> {
  int size;
  Node<E> first;
  Node<E> last;

  public LinkedList() {
  }

  public ListIterator<E> get_iterator() {
    return new ListIterator<E>(this);
  }
}

class Node<E> {
  E item;
  Node<E> next;
  Node<E> prev;

  Node(Node<E> prev, E element, Node<E> next) {
    this.item = element;
    this.next = next;
    this.prev = prev;
  }
}

class ListIterator<E> {

  private LinkedList<E> collection;
  private Node<E> lastReturned;
  private Node<E> next;
  private int nextIndex;
  private int size;

  ListIterator(LinkedList<E> collection) {
    this.next = collection.first;
    this.nextIndex = 0;
    this.collection = collection;
    this.size = collection.size;
  }

  public boolean has_next() {
    return nextIndex < size;
  }

  public E get_next() {
    if (!has_next()) {
    }

    lastReturned = next;
    next = next.next;
    nextIndex += 1;
    return lastReturned.item;
  }

}