import java.util.*;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] items;
	private int count = 0;

	// construct an empty randomized queue
	public RandomizedQueue() {
		this.items = (Item[]) new Object[10];
	}

	// is the randomized queue empty?
	public boolean isEmpty() {
		return count == 0;
	}

	// return the number of items on the randomized queue
	public int size() {
		return count;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (count == items.length) {
			resize();
		}
		items[count++] = item;
	}

	// remove and return a random item
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException();
		int i = randomIndex();
		Item item = items[i];
		items[i] = items[--count];
		items[count] = null;
		compress();
		return item;
	}

	// return a random item (but do not remove it)
	public Item sample() {
		if (isEmpty())
			throw new NoSuchElementException();
		return items[randomIndex()];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		Deque<Item> d = new Deque<>();
		for (int i = 0; i < count; i++) {
			if (StdRandom.bernoulli()) {
				d.addFirst(items[i]);
			} else {
				d.addLast(items[i]);
			}
		}
		return d.iterator();
	}

	private void resize() {
		Item[] newItems = (Item[]) new Object[items.length * 2];
		for (int i = 0; i < items.length; i++) {
			newItems[i] = items[i];
		}
		items = newItems;
	}

	private void compress() {
		if (2 * count < items.length) {
			Item[] newItems = (Item[]) new Object[3 * items.length / 4];
			for (int i = 0; i < count; i++) {
				newItems[i] = items[i];
			}
			items = newItems;
		}
	}

	private int randomIndex() {
		return StdRandom.uniformInt(0, count);
	}
}
