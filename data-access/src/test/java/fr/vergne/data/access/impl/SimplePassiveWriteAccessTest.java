package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

public class SimplePassiveWriteAccessTest {

	@Test
	public void testSetGetGenerator() {
		ValueGenerator<Integer> generator1 = new ValueGenerator<Integer>() {

			@Override
			public Integer generateValue() {
				return null;
			}
		};
		SimplePassiveWriteAccess<Integer> access = new SimplePassiveWriteAccess<Integer>(
				generator1);
		assertEquals(generator1, access.getValueGenerator());

		ValueGenerator<Integer> generator2 = new ValueGenerator<Integer>() {

			@Override
			public Integer generateValue() {
				return null;
			}
		};
		access.setValueGenerator(generator2);
		assertEquals(generator2, access.getValueGenerator());

		access.setValueGenerator(generator1);
		assertEquals(generator1, access.getValueGenerator());
	}

}
