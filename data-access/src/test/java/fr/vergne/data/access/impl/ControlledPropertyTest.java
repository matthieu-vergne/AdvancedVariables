package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlledPropertyTest {

	@Test
	public void testSetGetAlignment() {
		ControlledProperty<Integer> property = new ControlledProperty<Integer>();

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
	public void testNullValueOnImplicitCreation() {
		ControlledProperty<Integer> property = new ControlledProperty<Integer>();
		assertNull(property.get());
	}

}
