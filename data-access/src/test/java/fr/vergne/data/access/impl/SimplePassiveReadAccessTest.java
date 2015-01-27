package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveReadAccess.ValueListener;

public class SimplePassiveReadAccessTest {

	@Test
	public void testAddGetRemoveListeners() {
		SimplePassiveReadAccess<Integer> access = new SimplePassiveReadAccess<Integer>();
		assertEquals(0, access.getValueListeners().size());

		ValueListener<Integer> listener1 = new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				// do nothing
			}
		};
		access.addValueListener(listener1);
		assertEquals(1, access.getValueListeners().size());
		assertTrue(access.getValueListeners().contains(listener1));

		access.addValueListener(listener1);
		assertEquals(1, access.getValueListeners().size());
		assertTrue(access.getValueListeners().contains(listener1));

		ValueListener<Integer> listener2 = new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				// do nothing
			}
		};
		access.addValueListener(listener2);
		assertEquals(2, access.getValueListeners().size());
		assertTrue(access.getValueListeners().contains(listener1));
		assertTrue(access.getValueListeners().contains(listener2));

		access.addValueListener(listener2);
		assertEquals(2, access.getValueListeners().size());
		assertTrue(access.getValueListeners().contains(listener1));
		assertTrue(access.getValueListeners().contains(listener2));

		access.removeValueListener(listener1);
		assertEquals(1, access.getValueListeners().size());
		assertFalse(access.getValueListeners().contains(listener1));
		assertTrue(access.getValueListeners().contains(listener2));

		access.removeValueListener(listener2);
		assertEquals(0, access.getValueListeners().size());
		assertFalse(access.getValueListeners().contains(listener1));
		assertFalse(access.getValueListeners().contains(listener2));
	}

	@Test
	public void testAddNullListenerException() {
		SimplePassiveReadAccess<Integer> access = new SimplePassiveReadAccess<Integer>();
		try {
			access.addValueListener(null);
			fail("No exception thrown");
		} catch (NullPointerException e) {
		}
	}

}
