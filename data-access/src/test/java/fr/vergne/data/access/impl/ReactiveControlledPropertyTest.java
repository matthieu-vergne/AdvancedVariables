package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveReadAccess.ValueListener;

public class ReactiveControlledPropertyTest {

	@Test
	public void testSetGetAlignment() {
		ReactiveControlledProperty<Integer> property = new ReactiveControlledProperty<Integer>();

		property.set(3);
		assertEquals(3, (Object) property.get());

		property.set(-60);
		assertEquals(-60, (Object) property.get());

		property.set(null);
		assertEquals(null, (Object) property.get());

		property.set(0);
		assertEquals(0, (Object) property.get());
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

		ReactiveControlledProperty<Integer> property = new ReactiveControlledProperty<Integer>();
		property.addValueListener(listener);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(3);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(-10);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(null);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(1);
		assertTrue(isRun[0]);

		property.removeValueListener(listener);
		isRun[0] = false;

		property.set(-10);
		assertFalse(isRun[0]);

		property.set(654132);
		assertFalse(isRun[0]);

		property.set(null);
		assertFalse(isRun[0]);

		property.set(65);
		assertFalse(isRun[0]);

		property.addValueListener(listener);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(2);
		assertTrue(isRun[0]);

		isRun[0] = false;
		assertFalse(isRun[0]);
		property.set(98);
		assertTrue(isRun[0]);
	}

	@Test
	public void testNullValueOnImplicitCreation() {
		ReactiveControlledProperty<Integer> property = new ReactiveControlledProperty<Integer>();
		assertNull(property.get());
	}

}
