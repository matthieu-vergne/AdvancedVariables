package fr.vergne.measure.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleVariableTest {

	@Test
	public void testSetGetAlignment() {
		SimpleVariable<Integer> variable = new SimpleVariable<Integer>();

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
	public void testNullValueOnImplicitCreation() {
		SimpleVariable<Integer> variable = new SimpleVariable<Integer>();
		assertNull(variable.get());
	}

}
