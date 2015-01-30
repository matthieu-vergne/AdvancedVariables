package fr.vergne.data.access.impl.advanced;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveReadAccess.ValueListener;
import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

public class ReadableFlowControllerTest {

	@Test
	public void testGet() {
		final Integer[] source = { 1 };
		ReadableFlowController<Integer> controller = new ReadableFlowController<Integer>(
				new ValueGenerator<Integer>() {

					@Override
					public Integer generateValue() {
						return source[0];
					}
				});
		final Integer[] target = { null };
		controller.addValueListener(new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				target[0] = value;
			}
		});

		assertEquals(null, controller.get());
		controller.transfer();
		assertEquals(source[0], controller.get());
		Integer old = source[0];
		source[0] = 58;
		assertEquals(old, controller.get());
		controller.transfer();
		assertEquals(source[0], controller.get());
	}

}
