package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveReadAccess.ValueListener;

public class PusherTest {

	@Test
	public void testSetNotifications() {
		final Integer[] isRun = { null };
		ValueListener<Integer> listener = new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				isRun[0] = value;
			}
		};

		Pusher<Integer> pusher = new Pusher<Integer>();
		pusher.addValueListener(listener);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(3);
		assertEquals(3, (Object) isRun[0]);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(-10);
		assertEquals(-10, (Object) isRun[0]);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(null);
		assertEquals(null, (Object) isRun[0]);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(1);
		assertEquals(1, (Object) isRun[0]);

		pusher.removeValueListener(listener);
		isRun[0] = null;

		pusher.set(-10);
		assertNull(isRun[0]);

		pusher.set(654132);
		assertNull(isRun[0]);

		pusher.set(null);
		assertNull(isRun[0]);

		pusher.set(65);
		assertNull(isRun[0]);

		pusher.addValueListener(listener);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(2);
		assertEquals(2, (Object) isRun[0]);

		isRun[0] = null;
		assertNull(isRun[0]);
		pusher.set(98);
		assertEquals(98, (Object) isRun[0]);
	}

}
