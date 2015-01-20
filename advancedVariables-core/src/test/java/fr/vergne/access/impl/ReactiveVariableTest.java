package fr.vergne.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.access.PushReadable.ValueListener;

public class ReactiveVariableTest {

	@Test
	public void testSetGetAlignment() {
		ReactiveVariable<Integer> variable = new ReactiveVariable<Integer>();

		variable.set(3);
		assertEquals(3, (Object) variable.get());

		variable.set(-60);
		assertEquals(-60, (Object) variable.get());

		variable.set(null);
		assertEquals(null, (Object) variable.get());

		variable.set(0);
		assertEquals(0, (Object) variable.get());
	}

	@Test
	public void testSetNotifications() {
		final boolean[] isRun = { false };
		ValueListener<Integer> listener = new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				isRun[0] = true;
			}
		};

		ReactiveVariable<Integer> variable = new ReactiveVariable<Integer>();
		variable.addValueListener(listener);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(3);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(-10);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(null);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(1);
		assertTrue(isRun[0]);

		variable.removeValueListener(listener);
		isRun[0] = false;

		variable.set(-10);
		assertFalse(isRun[0]);

		variable.set(654132);
		assertFalse(isRun[0]);

		variable.set(null);
		assertFalse(isRun[0]);

		variable.set(65);
		assertFalse(isRun[0]);

		variable.addValueListener(listener);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(2);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		variable.set(98);
		assertTrue(isRun[0]);
	}

	@Test
	public void testNullValueOnImplicitCreation() {
		ReactiveVariable<Integer> variable = new ReactiveVariable<Integer>();
		assertNull(variable.get());
	}

}
