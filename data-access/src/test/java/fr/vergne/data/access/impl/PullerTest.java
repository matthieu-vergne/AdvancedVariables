package fr.vergne.data.access.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

public class PullerTest {

	@Test
	public void testGet() {
		final Integer[] source = { 1 };
		Puller<Integer> controller = new Puller<Integer>(
				new ValueGenerator<Integer>() {

					@Override
					public Integer generateValue() {
						return source[0];
					}
				});

		assertEquals(source[0], controller.get());
		source[0] = 58;
		assertEquals(source[0], controller.get());
		source[0] = -89;
		assertEquals(source[0], controller.get());
	}

}
