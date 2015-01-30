package fr.vergne.data.access.impl.advanced;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.PassiveReadAccess.ValueListener;
import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

public class FlowControllerTest {

	@Test
	public void testTransfer() {
		final Integer[] source = { 1 };
		FlowController<Integer> controller = new FlowController<Integer>(
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

		assertFalse(source[0] == target[0]);
		controller.transfer();
		assertTrue(source[0] == target[0]);
		source[0] = 58;
		assertFalse(source[0] == target[0]);
		controller.transfer();
		assertTrue(source[0] == target[0]);
	}

}
