package fr.vergne.data.storage.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import fr.vergne.data.storage.ReactiveStorage.OperationListener;

public class SimpleStorageTest {

	@Test
	public void testSetGet() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();

		assertEquals(null, storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));

		storage.set(1, "a");
		assertEquals("a", storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));

		storage.set(2, "b");
		assertEquals("a", storage.get(1));
		assertEquals("b", storage.get(2));
		assertEquals(null, storage.get(3));

		storage.set(3, "c");
		assertEquals("a", storage.get(1));
		assertEquals("b", storage.get(2));
		assertEquals("c", storage.get(3));

		storage.set(2, "d");
		assertEquals("a", storage.get(1));
		assertEquals("d", storage.get(2));
		assertEquals("c", storage.get(3));

		storage.set(1, null);
		assertEquals(null, storage.get(1));
		assertEquals("d", storage.get(2));
		assertEquals("c", storage.get(3));
	}

	@Test
	public void testRemove() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();

		storage.set(1, "a");
		storage.set(2, "b");
		storage.set(3, "c");

		assertEquals("a", storage.get(1));
		assertEquals("b", storage.get(2));
		assertEquals("c", storage.get(3));

		storage.remove(2);
		assertEquals("a", storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals("c", storage.get(3));

		storage.remove(3);
		assertEquals("a", storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));

		storage.remove(2);
		assertEquals("a", storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));

		storage.remove(1);
		assertEquals(null, storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));

		storage.remove(4);
		assertEquals(null, storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals(null, storage.get(3));
	}

	@Test
	public void testGetKeys() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();

		assertEquals(0, storage.getKeys().size());

		storage.set(1, "a");
		assertEquals(1, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));

		storage.set(2, "b");
		assertEquals(2, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertTrue(storage.getKeys().contains(2));

		storage.set(3, "c");
		assertEquals(3, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertTrue(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));

		storage.remove(2);
		assertEquals(2, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertFalse(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));

		storage.set(1, null);
		assertEquals(1, storage.getKeys().size());
		assertFalse(storage.getKeys().contains(1));
		assertFalse(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));
	}

	@Test
	public void testSetAll() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();

		Map<Integer, String> map1 = new HashMap<Integer, String>();
		map1.put(1, "a");
		map1.put(2, "b");
		map1.put(3, "c");

		storage.setAll(map1.entrySet());
		assertEquals(3, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertTrue(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));
		assertEquals("a", storage.get(1));
		assertEquals("b", storage.get(2));
		assertEquals("c", storage.get(3));

		Map<Integer, String> map2 = new HashMap<Integer, String>();
		map2.put(1, "d");
		map2.put(5, "e");
		map2.put(10, "f");

		storage.setAll(map2.entrySet());
		assertEquals(5, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertTrue(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));
		assertTrue(storage.getKeys().contains(5));
		assertTrue(storage.getKeys().contains(10));
		assertEquals("d", storage.get(1));
		assertEquals("b", storage.get(2));
		assertEquals("c", storage.get(3));
		assertEquals("e", storage.get(5));
		assertEquals("f", storage.get(10));
	}

	@Test
	public void testGetAll() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();
		storage.set(1, "a");
		storage.set(2, "b");
		storage.set(3, "c");
		storage.set(5, "e");
		storage.set(10, "f");

		List<Object> list = storage.getAll(Arrays.asList(5, 10, 2));
		assertEquals("e", list.get(0));
		assertEquals("f", list.get(1));
		assertEquals("b", list.get(2));
	}

	@Test
	public void testRemoveAll() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();
		storage.set(1, "a");
		storage.set(2, "b");
		storage.set(3, "c");
		storage.set(5, "e");
		storage.set(10, "f");

		storage.removeAll(Arrays.asList(5, 10, 2));
		assertEquals(2, storage.getKeys().size());
		assertTrue(storage.getKeys().contains(1));
		assertFalse(storage.getKeys().contains(2));
		assertTrue(storage.getKeys().contains(3));
		assertFalse(storage.getKeys().contains(5));
		assertFalse(storage.getKeys().contains(10));
		assertEquals("a", storage.get(1));
		assertEquals(null, storage.get(2));
		assertEquals("c", storage.get(3));
		assertEquals(null, storage.get(5));
		assertEquals(null, storage.get(10));
	}

	@Test
	public void testIterator() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();
		storage.set(1, "a");
		storage.set(2, "b");
		storage.set(3, "c");
		storage.set(5, "e");
		storage.set(10, "f");

		Iterator<Entry<Integer, Object>> iterator = storage.iterator();
		List<Integer> keys = new LinkedList<Integer>();
		List<Object> values = new LinkedList<Object>();
		while (iterator.hasNext()) {
			Entry<Integer, Object> entry = iterator.next();
			keys.add(entry.getKey());
			values.add(entry.getValue());
		}

		assertTrue(storage.getKeys().containsAll(keys));
		assertTrue(keys.containsAll(storage.getKeys()));
		assertEquals("" + values, keys.size(), values.size());
		for (int i = 0; i < keys.size(); i++) {
			Integer key = keys.get(i);
			Object value = values.get(i);
			assertEquals(storage.get(key), value);
		}
	}

	@Test
	public void testClear() {
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();
		storage.set(1, "a");
		storage.set(2, "b");
		storage.set(3, "c");
		storage.set(5, "e");
		storage.set(10, "f");

		storage.clear();
		assertEquals(0, storage.getKeys().size());
		assertNull(storage.get(1));
		assertNull(storage.get(2));
		assertNull(storage.get(3));
		assertNull(storage.get(5));
		assertNull(storage.get(10));
	}

	@Test
	public void testOperationListeners() {
		final List<Object[]> params = new LinkedList<Object[]>();
		Comparator<Object[]> paramSorter = new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				Integer i1 = (Integer) o1[0];
				Integer i2 = (Integer) o2[0];
				return i1.compareTo(i2);
			}
		};
		SimpleStorage<Integer> storage = new SimpleStorage<Integer>();
		storage.addOperationListener(new OperationListener<Integer>() {

			@Override
			public void entrySet(Integer key, Object oldValue, Object newValue) {
				params.add(new Object[] { key, oldValue, newValue });
			}
		});

		params.clear();
		storage.set(1, "a");
		assertEquals(1, params.size());
		assertEquals(1, params.get(0)[0]);
		assertEquals(null, params.get(0)[1]);
		assertEquals("a", params.get(0)[2]);

		params.clear();
		storage.set(2, "b");
		assertEquals(1, params.size());
		assertEquals(2, params.get(0)[0]);
		assertEquals(null, params.get(0)[1]);
		assertEquals("b", params.get(0)[2]);

		params.clear();
		storage.set(2, "c");
		assertEquals(1, params.size());
		assertEquals(2, params.get(0)[0]);
		assertEquals("b", params.get(0)[1]);
		assertEquals("c", params.get(0)[2]);

		params.clear();
		storage.remove(2);
		assertEquals(1, params.size());
		assertEquals(2, params.get(0)[0]);
		assertEquals("c", params.get(0)[1]);
		assertEquals(null, params.get(0)[2]);

		params.clear();
		storage.get(2);
		assertEquals(0, params.size());

		params.clear();
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "1");
		map.put(2, "2");
		map.put(3, "3");
		storage.setAll(map.entrySet());
		assertEquals(3, params.size());
		Collections.sort(params, paramSorter);
		assertEquals(1, params.get(0)[0]);
		assertEquals("a", params.get(0)[1]);
		assertEquals("1", params.get(0)[2]);
		assertEquals(2, params.get(1)[0]);
		assertEquals(null, params.get(1)[1]);
		assertEquals("2", params.get(1)[2]);
		assertEquals(3, params.get(2)[0]);
		assertEquals(null, params.get(2)[1]);
		assertEquals("3", params.get(2)[2]);

		params.clear();
		storage.removeAll(Arrays.asList(1, 2));
		assertEquals(2, params.size());
		Collections.sort(params, paramSorter);
		assertEquals(1, params.get(0)[0]);
		assertEquals("1", params.get(0)[1]);
		assertEquals(null, params.get(0)[2]);
		assertEquals(2, params.get(1)[0]);
		assertEquals("2", params.get(1)[1]);
		assertEquals(null, params.get(1)[2]);

		params.clear();
		storage.getAll(Arrays.asList(1, 3));
		assertEquals(0, params.size());
	}
}
