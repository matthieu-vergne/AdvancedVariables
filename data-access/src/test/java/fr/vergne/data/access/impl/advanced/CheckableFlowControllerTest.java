package fr.vergne.data.access.impl.advanced;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

public class CheckableFlowControllerTest {

	@Test
	public void testGet() {
		final Integer[] source = { 1 };
		CheckableFlowController<Integer> controller = new CheckableFlowController<Integer>(
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
