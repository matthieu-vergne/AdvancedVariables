package fr.vergne.access.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.vergne.access.Access;

public class SimpleAccessProviderTest {

	@Test
	public void testSetGetRemovePullAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getPullAccess(1));

		ReadWriteProperty<Integer> access1 = new ReadWriteProperty<Integer>();
		provider.setPullAccess(1, access1);
		assertEquals(access1, provider.getPullAccess(1));

		ReactiveReadWriteProperty<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		provider.setPullAccess(1, access2);
		assertEquals(access2, provider.getPullAccess(1));

		provider.removePullAccess(1);
		assertNull(provider.getPullAccess(1));
	}

	@Test
	public void testSetGetRemovePushAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getPushAccess(1));

		ReactiveReadWriteProperty<Integer> access1 = new ReactiveReadWriteProperty<Integer>();
		provider.setPushAccess(1, access1);
		assertEquals(access1, provider.getPushAccess(1));

		ReactiveReadWriteProperty<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		provider.setPushAccess(1, access2);
		assertEquals(access2, provider.getPushAccess(1));

		provider.removePushAccess(1);
		assertNull(provider.getPullAccess(1));
	}

	@Test
	public void testSetGetRemoveWriteAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getWriteAccess(1));

		ReadWriteProperty<Integer> access1 = new ReadWriteProperty<Integer>();
		provider.setWriteAccess(1, access1);
		assertEquals(access1, provider.getWriteAccess(1));

		ReactiveReadWriteProperty<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		provider.setWriteAccess(1, access2);
		assertEquals(access2, provider.getWriteAccess(1));

		provider.removeWriteAccess(1);
		assertNull(provider.getWriteAccess(1));
	}

	@Test
	public void testSetRemoveMultiAccesses() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getPullAccess(1));
		assertNull(provider.getPushAccess(1));
		assertNull(provider.getWriteAccess(1));
		assertNull(provider.getPullAccess(2));
		assertNull(provider.getPushAccess(2));
		assertNull(provider.getWriteAccess(2));

		ReadWriteProperty<Integer> access1 = new ReadWriteProperty<Integer>();
		provider.setMultiAccesses(1, access1);
		assertEquals(access1, provider.getPullAccess(1));
		assertEquals(null, provider.getPushAccess(1));
		assertEquals(access1, provider.getWriteAccess(1));
		assertNull(provider.getPullAccess(2));
		assertNull(provider.getPushAccess(2));
		assertNull(provider.getWriteAccess(2));

		ReactiveReadWriteProperty<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		provider.setMultiAccesses(1, access2);
		provider.setMultiAccesses(2, access1);
		assertEquals(access2, provider.getPullAccess(1));
		assertEquals(access2, provider.getPushAccess(1));
		assertEquals(access2, provider.getWriteAccess(1));
		assertEquals(access1, provider.getPullAccess(2));
		assertEquals(null, provider.getPushAccess(2));
		assertEquals(access1, provider.getWriteAccess(2));

		provider.removeMultiAccesses(1);
		assertNull(provider.getPullAccess(1));
		assertNull(provider.getPushAccess(1));
		assertNull(provider.getWriteAccess(1));
		assertEquals(access1, provider.getPullAccess(2));
		assertEquals(null, provider.getPushAccess(2));
		assertEquals(access1, provider.getWriteAccess(2));

		provider.removeMultiAccesses(2);
		assertNull(provider.getPullAccess(1));
		assertNull(provider.getPushAccess(1));
		assertNull(provider.getWriteAccess(1));
		assertNull(provider.getPullAccess(2));
		assertNull(provider.getPushAccess(2));
		assertNull(provider.getWriteAccess(2));
	}

	@Test
	public void testSetRemoveAllAccesses() {
		Access<Integer> access1 = new ReadWriteProperty<Integer>();
		Access<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		Access<Integer> access3 = new ReadWriteProperty<Integer>();

		Map<Object, Access<Integer>> accesses = new HashMap<Object, Access<Integer>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertTrue(provider.getIDs().isEmpty());

		provider.setAllAccesses(accesses);

		assertEquals(3, provider.getIDs().size());
		assertTrue(provider.getIDs().contains(1));
		assertTrue(provider.getIDs().contains(2));
		assertTrue(provider.getIDs().contains(3));

		assertEquals(access1, provider.getPullAccess(1));
		assertEquals(null, provider.getPushAccess(1));
		assertEquals(access1, provider.getWriteAccess(1));

		assertEquals(access2, provider.getPullAccess(2));
		assertEquals(access2, provider.getPushAccess(2));
		assertEquals(access2, provider.getWriteAccess(2));

		assertEquals(access3, provider.getPullAccess(3));
		assertEquals(null, provider.getPushAccess(3));
		assertEquals(access3, provider.getWriteAccess(3));
	}

	@Test
	public void testClear() {
		Access<Integer> access1 = new ReadWriteProperty<Integer>();
		Access<Integer> access2 = new ReactiveReadWriteProperty<Integer>();
		Access<Integer> access3 = new ReadWriteProperty<Integer>();

		Map<Object, Access<Integer>> accesses = new HashMap<Object, Access<Integer>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider provider = new SimpleAccessProvider();
		provider.setAllAccesses(accesses);
		provider.clear();

		assertTrue(provider.getIDs().isEmpty());

		assertNull(provider.getPullAccess(1));
		assertNull(provider.getPushAccess(1));
		assertNull(provider.getWriteAccess(1));

		assertNull(provider.getPullAccess(2));
		assertNull(provider.getPushAccess(2));
		assertNull(provider.getWriteAccess(2));

		assertNull(provider.getPullAccess(3));
		assertNull(provider.getPushAccess(3));
		assertNull(provider.getWriteAccess(3));
	}

	@Test
	public void testStartEmpty() {
		SimpleAccessProvider provider = new SimpleAccessProvider();
		assertTrue(provider.getIDs().isEmpty());
	}

	@Test
	public void testStartFull() {
		Access<Integer> access1 = new ReadWriteProperty<Integer>();
		Access<Integer> access2 = new ReadWriteProperty<Integer>();
		Access<Integer> access3 = new ReactiveReadWriteProperty<Integer>();

		Map<Object, Access<?>> accesses = new HashMap<Object, Access<?>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider providerRef = new SimpleAccessProvider();
		providerRef.setAllAccesses(accesses);

		SimpleAccessProvider provider = new SimpleAccessProvider(accesses);

		assertEquals(providerRef.getIDs().size(), provider.getIDs().size());
		assertTrue(providerRef.getIDs().containsAll(provider.getIDs()));
		assertTrue(provider.getIDs().containsAll(providerRef.getIDs()));
		for (Object id : provider.getIDs()) {
			assertEquals(providerRef.getPullAccess(id),
					provider.getPullAccess(id));
			assertEquals(providerRef.getPushAccess(id),
					provider.getPushAccess(id));
			assertEquals(providerRef.getWriteAccess(id),
					provider.getWriteAccess(id));
		}
	}
}
