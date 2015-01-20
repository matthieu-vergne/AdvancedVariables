package fr.vergne.access.util;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.access.PullReadAccess;
import fr.vergne.access.impl.ReactiveReadWriteProperty;

public class AccessFactoryTest {

	@Test
	public void testCreatePullForPush() {
		ReactiveReadWriteProperty<Integer> push = new ReactiveReadWriteProperty<Integer>(
				1);

		PullReadAccess<Integer> pull = new AccessFactory().createPullForPush(
				push, 2);
		assertEquals(2, (Object) pull.get());

		push.set(1);
		push.set(2);
		push.set(3);
		assertEquals(3, (Object) pull.get());

		push.set(4);
		push.set(5);
		push.set(-5641);
		assertEquals(-5641, (Object) pull.get());
	}

}
