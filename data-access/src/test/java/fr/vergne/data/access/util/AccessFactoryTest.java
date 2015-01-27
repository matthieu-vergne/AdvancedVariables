package fr.vergne.data.access.util;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.ActiveWriteAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess.ValueListener;
import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;
import fr.vergne.data.access.impl.ReactiveControlledProperty;
import fr.vergne.data.access.impl.ReadableFlowController;
import fr.vergne.data.access.impl.SimplePassiveReadAccess;
import fr.vergne.data.access.impl.SimplePassiveWriteAccess;

public class AccessFactoryTest {

	@Test
	public void testCreateActiveReadFromPassiveRead() {
		ReactiveControlledProperty<Integer> active = new ReactiveControlledProperty<Integer>(
				1);

		ActiveReadAccess<Integer> passive = new AccessFactory()
				.createActiveReadFromPassiveRead(active, 2);
		assertEquals(2, (Object) passive.get());

		active.set(1);
		active.set(2);
		active.set(3);
		assertEquals(3, (Object) passive.get());

		active.set(4);
		active.set(5);
		active.set(-5641);
		assertEquals(-5641, (Object) passive.get());
	}

	@Test
	public void testCreateActiveWriteFromPassiveWrite() {
		ReadableFlowController<Integer> active = new ReadableFlowController<Integer>();
		ActiveWriteAccess<Integer> passive = new AccessFactory()
				.createActiveWriteFromPassiveWrite(active, 2);

		active.transfer();
		assertEquals(2, (Object) active.get());

		passive.set(5);
		assertEquals(2, (Object) active.get());
		active.transfer();
		assertEquals(5, (Object) active.get());

		passive.set(-654);
		assertEquals(5, (Object) active.get());
		active.transfer();
		assertEquals(-654, (Object) active.get());
	}

	@Test
	public void testCreateActiveReadFromPassiveWrite() {
		final Integer[] container = { 1 };
		PassiveWriteAccess<Integer> writer = new SimplePassiveWriteAccess<Integer>(
				new ValueGenerator<Integer>() {

					@Override
					public Integer generateValue() {
						return container[0];
					}
				});
		ActiveReadAccess<Integer> reader = new AccessFactory()
				.createActiveReadFromPassiveWrite(writer);

		assertEquals(1, (Object) reader.get());
		container[0] = 5;
		assertEquals(5, (Object) reader.get());
		container[0] = null;
		assertEquals(null, (Object) reader.get());
		container[0] = -65;
		assertEquals(-65, (Object) reader.get());
	}

	@Test
	public void testCreateActiveWriteFromPassiveRead() {
		final Integer[] container = { 1 };
		PassiveReadAccess<Integer> reader = new SimplePassiveReadAccess<Integer>();
		reader.addValueListener(new ValueListener<Integer>() {

			@Override
			public void valueGenerated(Integer value) {
				container[0] = value;
			}
		});
		ActiveWriteAccess<Integer> writer = new AccessFactory()
				.createActiveWriteFromPassiveRead(reader);

		writer.set(5);
		assertEquals(5, (Object) container[0]);
		writer.set(null);
		assertEquals(null, (Object) container[0]);
		writer.set(-65);
		assertEquals(-65, (Object) container[0]);
	}

}
