package fr.vergne.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.access.PushReadAccess.PushListener;

public class ReactiveReadWritePropertyTest {

	@Test
	public void testSetGetAlignment() {
		ReactiveReadWriteProperty<Integer> property = new ReactiveReadWriteProperty<Integer>();

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
		PushListener<Integer> listener = new PushListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				isRun[0] = true;
			}
		};

		ReactiveReadWriteProperty<Integer> property = new ReactiveReadWriteProperty<Integer>();
		property.addPushListener(listener);

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

		property.removePushListener(listener);
		isRun[0] = false;

		property.set(-10);
		assertFalse(isRun[0]);

		property.set(654132);
		assertFalse(isRun[0]);

		property.set(null);
		assertFalse(isRun[0]);

		property.set(65);
		assertFalse(isRun[0]);

		property.addPushListener(listener);

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
		ReactiveReadWriteProperty<Integer> property = new ReactiveReadWriteProperty<Integer>();
		assertNull(property.get());
	}

}
