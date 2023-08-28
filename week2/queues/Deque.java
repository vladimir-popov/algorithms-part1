import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	public static void main(String[] args) {
	}

	private class Node {
		public Item item;
		public Node next;
		public Node prev;

		public Node(Item item, Node prev, Node next) {
			this.item = item;
			this.prev = prev;
			this.next = next;
		}
	}

	private Node head = null;
	private Node last = null;
	private int count = 0;

	// construct an empty deque
	public Deque() {

	}

	// is the deque empty?
	public boolean isEmpty() {
		return head == null;
	}

	// return the number of items on the deque
	public int size() {
		return count;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (isEmpty()) {
			head = new Node(item, null, null);
			last = head;
		} else {
			head = new Node(item, null, head);
			head.next.prev = head;
		}
		count++;
	}

	// add the item to the back
	public void addLast(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (isEmpty()) {
			addFirst(item);
		} else {
			last = new Node(item, last, null);
			last.prev.next = last;
			count++;
		}
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
		Node oldHead = head;
		head = oldHead.next;
		if (head != null)
			head.prev = null;
		count--;
		return oldHead.item;
	}

	// remove and return the item from the back
	public Item removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		Node oldLast = last;
		last = oldLast.prev;
		if (last != null)
			last.next = null;
		else
			head = null;

		count--;
		return oldLast.item;
	}

	// return an iterator over items in order from front to back
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private Node cur = head;

			public boolean hasNext() {
				return cur != null;
			}

			public Item next() {
				if (cur == null)
					throw new NoSuchElementException();
				Item item = cur.item;
				cur = cur.next;
				return item;
			}
		};
	}
}
