package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.PropertyAccess;

public class SimpleAccessProviderTest {

	@Test
	public void testSetGetRemovePassiveReadAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getActiveReadAccess(1));

		ControlledProperty<Integer> access1 = new ControlledProperty<Integer>();
		provider.setActiveReadAccess(1, access1);
		assertEquals(access1, provider.getActiveReadAccess(1));

		ReactiveControlledProperty<Integer> access2 = new ReactiveControlledProperty<Integer>();
		provider.setActiveReadAccess(1, access2);
		assertEquals(access2, provider.getActiveReadAccess(1));

		provider.removeActiveReadAccess(1);
		assertNull(provider.getActiveReadAccess(1));
	}

	@Test
	public void testSetGetRemoveActiveReadAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getPassiveReadAccess(1));

		ReactiveControlledProperty<Integer> access1 = new ReactiveControlledProperty<Integer>();
		provider.setPassiveReadAccess(1, access1);
		assertEquals(access1, provider.getPassiveReadAccess(1));

		ReactiveControlledProperty<Integer> access2 = new ReactiveControlledProperty<Integer>();
		provider.setPassiveReadAccess(1, access2);
		assertEquals(access2, provider.getPassiveReadAccess(1));

		provider.removePassiveReadAccess(1);
		assertNull(provider.getActiveReadAccess(1));
	}

	@Test
	public void testSetGetRemovePassiveWriteAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getActiveWriteAccess(1));

		ControlledProperty<Integer> access1 = new ControlledProperty<Integer>();
		provider.setActiveWriteAccess(1, access1);
		assertEquals(access1, provider.getActiveWriteAccess(1));

		ReactiveControlledProperty<Integer> access2 = new ReactiveControlledProperty<Integer>();
		provider.setActiveWriteAccess(1, access2);
		assertEquals(access2, provider.getActiveWriteAccess(1));

		provider.removeActiveWriteAccess(1);
		assertNull(provider.getActiveWriteAccess(1));
	}

	@Test
	public void testSetGetRemoveActiveWriteAccess() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getPassiveWriteAccess(1));

		PassiveWriteAccess<Integer> access1 = new FlowController<Integer>();
		provider.setPassiveWriteAccess(1, access1);
		assertEquals(access1, provider.getPassiveWriteAccess(1));

		provider.removePassiveWriteAccess(1);
		assertNull(provider.getPassiveWriteAccess(1));
	}

	@Test
	public void testSetRemoveMultiAccesses() {
		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertNull(provider.getActiveReadAccess(1));
		assertNull(provider.getActiveWriteAccess(1));
		assertNull(provider.getPassiveReadAccess(1));
		assertNull(provider.getPassiveWriteAccess(1));
		assertNull(provider.getActiveReadAccess(2));
		assertNull(provider.getActiveWriteAccess(2));
		assertNull(provider.getPassiveReadAccess(2));
		assertNull(provider.getPassiveWriteAccess(2));

		ControlledProperty<Integer> access1 = new ControlledProperty<Integer>();
		provider.setMultiAccesses(1, access1);
		assertEquals(access1, provider.getActiveReadAccess(1));
		assertEquals(access1, provider.getActiveWriteAccess(1));
		assertEquals(null, provider.getPassiveReadAccess(1));
		assertEquals(null, provider.getPassiveWriteAccess(1));
		assertNull(provider.getActiveReadAccess(2));
		assertNull(provider.getActiveWriteAccess(2));
		assertNull(provider.getPassiveReadAccess(2));
		assertNull(provider.getPassiveWriteAccess(2));

		ReactiveControlledProperty<Integer> access2 = new ReactiveControlledProperty<Integer>();
		provider.setMultiAccesses(1, access2);
		provider.setMultiAccesses(2, access1);
		assertEquals(access2, provider.getActiveReadAccess(1));
		assertEquals(access2, provider.getActiveWriteAccess(1));
		assertEquals(access2, provider.getPassiveReadAccess(1));
		assertEquals(null, provider.getPassiveWriteAccess(1));
		assertEquals(access1, provider.getActiveReadAccess(2));
		assertEquals(access1, provider.getActiveWriteAccess(2));
		assertEquals(null, provider.getPassiveReadAccess(2));
		assertEquals(null, provider.getPassiveWriteAccess(2));

		provider.removeMultiAccesses(1);
		assertNull(provider.getActiveReadAccess(1));
		assertNull(provider.getActiveWriteAccess(1));
		assertNull(provider.getPassiveReadAccess(1));
		assertNull(provider.getPassiveWriteAccess(1));
		assertEquals(access1, provider.getActiveReadAccess(2));
		assertEquals(access1, provider.getActiveWriteAccess(2));
		assertEquals(null, provider.getPassiveReadAccess(2));
		assertEquals(null, provider.getPassiveWriteAccess(2));

		provider.removeMultiAccesses(2);
		assertNull(provider.getActiveReadAccess(1));
		assertNull(provider.getActiveWriteAccess(1));
		assertNull(provider.getPassiveReadAccess(1));
		assertNull(provider.getPassiveWriteAccess(1));
		assertNull(provider.getActiveReadAccess(2));
		assertNull(provider.getActiveWriteAccess(2));
		assertNull(provider.getPassiveReadAccess(2));
		assertNull(provider.getPassiveWriteAccess(2));
	}

	@Test
	public void testSetRemoveAllAccesses() {
		PropertyAccess<Integer> access1 = new ControlledProperty<Integer>();
		PropertyAccess<Integer> access2 = new ReactiveControlledProperty<Integer>();
		PropertyAccess<Integer> access3 = new ControlledProperty<Integer>();

		Map<Object, PropertyAccess<Integer>> accesses = new HashMap<Object, PropertyAccess<Integer>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider provider = new SimpleAccessProvider();

		assertTrue(provider.getKeys().isEmpty());

		provider.setAllAccesses(accesses);

		assertEquals(3, provider.getKeys().size());
		assertTrue(provider.getKeys().contains(1));
		assertTrue(provider.getKeys().contains(2));
		assertTrue(provider.getKeys().contains(3));

		assertEquals(access1, provider.getActiveReadAccess(1));
		assertEquals(null, provider.getPassiveReadAccess(1));
		assertEquals(access1, provider.getActiveWriteAccess(1));

		assertEquals(access2, provider.getActiveReadAccess(2));
		assertEquals(access2, provider.getPassiveReadAccess(2));
		assertEquals(access2, provider.getActiveWriteAccess(2));

		assertEquals(access3, provider.getActiveReadAccess(3));
		assertEquals(null, provider.getPassiveReadAccess(3));
		assertEquals(access3, provider.getActiveWriteAccess(3));
	}

	@Test
	public void testClear() {
		PropertyAccess<Integer> access1 = new ControlledProperty<Integer>();
		PropertyAccess<Integer> access2 = new ReactiveControlledProperty<Integer>();
		PropertyAccess<Integer> access3 = new ControlledProperty<Integer>();

		Map<Object, PropertyAccess<Integer>> accesses = new HashMap<Object, PropertyAccess<Integer>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider provider = new SimpleAccessProvider();
		provider.setAllAccesses(accesses);
		provider.clear();

		assertTrue(provider.getKeys().isEmpty());

		assertNull(provider.getActiveReadAccess(1));
		assertNull(provider.getPassiveReadAccess(1));
		assertNull(provider.getActiveWriteAccess(1));

		assertNull(provider.getActiveReadAccess(2));
		assertNull(provider.getPassiveReadAccess(2));
		assertNull(provider.getActiveWriteAccess(2));

		assertNull(provider.getActiveReadAccess(3));
		assertNull(provider.getPassiveReadAccess(3));
		assertNull(provider.getActiveWriteAccess(3));
	}

	@Test
	public void testStartEmpty() {
		SimpleAccessProvider provider = new SimpleAccessProvider();
		assertTrue(provider.getKeys().isEmpty());
	}

	@Test
	public void testStartFull() {
		PropertyAccess<Integer> access1 = new ControlledProperty<Integer>();
		PropertyAccess<Integer> access2 = new ControlledProperty<Integer>();
		PropertyAccess<Integer> access3 = new ReactiveControlledProperty<Integer>();

		Map<Object, PropertyAccess<?>> accesses = new HashMap<Object, PropertyAccess<?>>();
		accesses.put(1, access1);
		accesses.put(2, access2);
		accesses.put(3, access3);

		SimpleAccessProvider providerRef = new SimpleAccessProvider();
		providerRef.setAllAccesses(accesses);

		SimpleAccessProvider provider = new SimpleAccessProvider(accesses);

		assertEquals(providerRef.getKeys().size(), provider.getKeys().size());
		assertTrue(providerRef.getKeys().containsAll(provider.getKeys()));
		assertTrue(provider.getKeys().containsAll(providerRef.getKeys()));
		for (Object key : provider.getKeys()) {
			assertEquals(providerRef.getActiveReadAccess(key),
					provider.getActiveReadAccess(key));
			assertEquals(providerRef.getPassiveReadAccess(key),
					provider.getPassiveReadAccess(key));
			assertEquals(providerRef.getActiveWriteAccess(key),
					provider.getActiveWriteAccess(key));
		}
	}
}
