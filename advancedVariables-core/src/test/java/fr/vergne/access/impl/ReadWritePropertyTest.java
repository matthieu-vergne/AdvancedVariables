package fr.vergne.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReadWritePropertyTest {

	@Test
	public void testSetGetAlignment() {
		ReadWriteProperty<Integer> property = new ReadWriteProperty<Integer>();

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
		ReadWriteProperty<Integer> property = new ReadWriteProperty<Integer>();
		assertNull(property.get());
	}

}
