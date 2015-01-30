package fr.vergne.data.access.impl.advanced;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;
import fr.vergne.data.access.PropertyAccess;
import fr.vergne.data.access.impl.SimplePassiveWriteAccess;
import fr.vergne.data.access.impl.advanced.LastInstantComputer.Computer;

public class LastInstantComputerTest {

	private class Counter extends Puller<Integer> {

		private int count;

		public Counter(int firstValue) {
			super(null);
			count = firstValue;
			setValueGenerator(new ValueGenerator<Integer>() {

				@Override
				public Integer generateValue() {
					return count++;
				}
			});
		}

	}

	@Test
	public void testCounter() {
		{
			Counter counter = new Counter(0);
			assertEquals(0, (Object) counter.get());
			assertEquals(1, (Object) counter.get());
			assertEquals(2, (Object) counter.get());
		}
		{
			Counter counter = new Counter(-345);
			assertEquals(-345, (Object) counter.get());
			assertEquals(-344, (Object) counter.get());
			assertEquals(-343, (Object) counter.get());
		}
		{
			Counter counter = new Counter(76);
			assertEquals(76, (Object) counter.get());
			assertEquals(77, (Object) counter.get());
			assertEquals(78, (Object) counter.get());
		}
	}

	@Test
	public void testEmptyInstantiation() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		assertNull(access.getComputer());
		assertEquals(0, access.getAllSources().size());
	}

	@Test
	public void testComputerOnlyInstantiation() {
		Computer<Integer> computer = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				return null;
			}
		};
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>(
				computer);
		assertEquals(computer, access.getComputer());
		assertEquals(0, access.getAllSources().size());
	}

	@Test
	public void testComputerAndInputsInstantiation() {
		Computer<Integer> computer = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				return null;
			}
		};
		Counter source1 = new Counter(0);
		Pusher<Integer> source2 = new Pusher<Integer>();
		final int[] source3Value = { 0 };
		SimplePassiveWriteAccess<Integer> source3 = new SimplePassiveWriteAccess<Integer>(
				new ValueGenerator<Integer>() {

					@Override
					public Integer generateValue() {
						return source3Value[0];
					}
				});

		Map<Object, PropertyAccess<?>> sources = new HashMap<Object, PropertyAccess<?>>();
		sources.put(1, source1);
		sources.put(2, source2);
		sources.put(3, source3);
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>(
				computer, sources.entrySet());
		assertEquals(computer, access.getComputer());
		assertEquals(3, access.getAllSources().size());

		assertEquals(0, access.getSource(1).get());
		assertEquals(1, access.getSource(1).get());
		assertEquals(2, access.getSource(1).get());

		source2.set(8);
		assertEquals(8, access.getSource(2).get());
		assertEquals(8, access.getSource(2).get());
		source2.set(-4);
		assertEquals(-4, access.getSource(2).get());

		source3Value[0] = 5;
		assertEquals(5, access.getSource(3).get());
		source3Value[0] = -2;
		assertEquals(-2, access.getSource(3).get());
		source3Value[0] = 44;
		assertEquals(44, access.getSource(3).get());
	}

	@Test
	public void testSetGetActiveReadSource() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ActiveReadAccess<?> source1 = (ActiveReadAccess<?>) new Counter(0);
		ActiveReadAccess<?> source2 = (ActiveReadAccess<?>) new Counter(0);
		ActiveReadAccess<?> source3 = (ActiveReadAccess<?>) new Counter(0);
		access.setSource(1, source1);
		access.setSource(2, source2);
		access.setSource(3, source3);

		assertEquals(source1, access.getSource(1));
		assertEquals(source2, access.getSource(2));
		assertEquals(source3, access.getSource(3));
		assertNull(access.getSource(4));
		assertNull(access.getSource(null));
	}

	@Test
	public void testSetGetPassiveReadSource() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		Pusher<Integer> pusher1 = new Pusher<Integer>();
		Pusher<Integer> pusher2 = new Pusher<Integer>();
		Pusher<Integer> pusher3 = new Pusher<Integer>();
		PassiveReadAccess<?> source1 = (PassiveReadAccess<?>) pusher1;
		PassiveReadAccess<?> source2 = (PassiveReadAccess<?>) pusher2;
		PassiveReadAccess<?> source3 = (PassiveReadAccess<?>) pusher3;
		ActiveReadAccess<?> wrapper1 = access.setSource(1, source1);
		ActiveReadAccess<?> wrapper2 = access.setSource(2, source2);
		ActiveReadAccess<?> wrapper3 = access.setSource(3, source3);

		assertEquals(wrapper1, access.getSource(1));
		assertEquals(wrapper2, access.getSource(2));
		assertEquals(wrapper3, access.getSource(3));

		pusher1.set(3);
		assertEquals(3, (Object) wrapper1.get());
		pusher1.set(65);
		assertEquals(65, (Object) wrapper1.get());

		pusher2.set(null);
		assertEquals(null, (Object) wrapper2.get());
		pusher2.set(-12);
		assertEquals(-12, (Object) wrapper2.get());

		pusher3.set(46);
		assertEquals(46, (Object) wrapper3.get());
		pusher3.set(-3);
		assertEquals(-3, (Object) wrapper3.get());
	}

	@Test
	public void testSetGetPassiveWriteSource() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		PassiveWriteAccess<?> source1 = (PassiveWriteAccess<?>) new Counter(0);
		PassiveWriteAccess<?> source2 = (PassiveWriteAccess<?>) new Counter(5);
		PassiveWriteAccess<?> source3 = (PassiveWriteAccess<?>) new Counter(-10);
		ActiveReadAccess<?> wrapper1 = access.setSource(1, source1);
		ActiveReadAccess<?> wrapper2 = access.setSource(2, source2);
		ActiveReadAccess<?> wrapper3 = access.setSource(3, source3);

		assertEquals(wrapper1, access.getSource(1));
		assertEquals(wrapper2, access.getSource(2));
		assertEquals(wrapper3, access.getSource(3));

		assertEquals(0, (Object) wrapper1.get());
		assertEquals(1, (Object) wrapper1.get());
		assertEquals(2, (Object) wrapper1.get());

		assertEquals(5, (Object) wrapper2.get());
		assertEquals(6, (Object) wrapper2.get());
		assertEquals(7, (Object) wrapper2.get());

		assertEquals(-10, (Object) wrapper3.get());
		assertEquals(-9, (Object) wrapper3.get());
		assertEquals(-8, (Object) wrapper3.get());
	}

	@Test
	public void testSetAllSources() {
		Counter source1 = new Counter(0);
		Pusher<Integer> source2 = new Pusher<Integer>();
		final int[] source3Value = { 0 };
		SimplePassiveWriteAccess<Integer> source3 = new SimplePassiveWriteAccess<Integer>(
				new ValueGenerator<Integer>() {

					@Override
					public Integer generateValue() {
						return source3Value[0];
					}
				});

		Map<Object, PropertyAccess<?>> sources = new HashMap<Object, PropertyAccess<?>>();
		sources.put(1, source1);
		sources.put(2, source2);
		sources.put(3, source3);
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		access.setAllSources(sources.entrySet());

		assertEquals(0, access.getSource(1).get());
		assertEquals(1, access.getSource(1).get());
		assertEquals(2, access.getSource(1).get());

		source2.set(8);
		assertEquals(8, access.getSource(2).get());
		assertEquals(8, access.getSource(2).get());
		source2.set(-4);
		assertEquals(-4, access.getSource(2).get());

		source3Value[0] = 5;
		assertEquals(5, access.getSource(3).get());
		source3Value[0] = -2;
		assertEquals(-2, access.getSource(3).get());
		source3Value[0] = 44;
		assertEquals(44, access.getSource(3).get());
	}

	@Test
	public void testGetAllSources() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ActiveReadAccess<?> source1 = (ActiveReadAccess<?>) new Counter(0);
		PassiveReadAccess<?> source2 = (PassiveReadAccess<?>) new Pusher<Integer>();
		PassiveWriteAccess<?> source3 = (PassiveWriteAccess<?>) new Counter(-10);
		access.setSource(1, source1);
		access.setSource(2, source2);
		access.setSource(3, source3);

		assertEquals(3, access.getAllSources().size());
		assertEquals(access.getSource(1), access.getAllSources().get(1));
		assertEquals(access.getSource(2), access.getAllSources().get(2));
		assertEquals(access.getSource(3), access.getAllSources().get(3));
	}

	@Test
	public void testRemoveSource() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ActiveReadAccess<?> source1 = (ActiveReadAccess<?>) new Counter(0);
		PassiveReadAccess<?> source2 = (PassiveReadAccess<?>) new Pusher<Integer>();
		PassiveWriteAccess<?> source3 = (PassiveWriteAccess<?>) new Counter(-10);
		access.setSource(1, source1);
		ActiveReadAccess<?> wrapper2 = access.setSource(2, source2);
		ActiveReadAccess<?> wrapper3 = access.setSource(3, source3);

		assertEquals(source1, access.getSource(1));
		assertEquals(wrapper2, access.getSource(2));
		assertEquals(wrapper3, access.getSource(3));

		access.removeSource(2);
		assertEquals(source1, access.getSource(1));
		assertNull(access.getSource(2));
		assertEquals(wrapper3, access.getSource(3));

		access.removeSource(3);
		assertEquals(source1, access.getSource(1));
		assertNull(access.getSource(2));
		assertNull(access.getSource(3));

		access.removeSource(2);
		assertEquals(source1, access.getSource(1));
		assertNull(access.getSource(2));
		assertNull(access.getSource(3));

		access.removeSource(1);
		assertNull(access.getSource(1));
		assertNull(access.getSource(2));
		assertNull(access.getSource(3));
	}

	@Test
	public void testSetGetComputer() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		Computer<Integer> computer1 = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				return null;
			}
		};
		Computer<Integer> computer2 = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				return null;
			}
		};
		Computer<Integer> computer3 = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				return null;
			}
		};

		access.setComputer(computer1);
		assertEquals(computer1, access.getComputer());

		access.setComputer(computer2);
		assertEquals(computer2, access.getComputer());

		access.setComputer(computer3);
		assertEquals(computer3, access.getComputer());
	}

	@Test
	public void testOutputValueCorrespondsToComputer() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				Integer i1 = (Integer) inputs.get(1);
				Integer i2 = (Integer) inputs.get(2);
				return i1 + i2;
			}
		});

		assertEquals(3, (Object) access.get());
		assertEquals(3, (Object) access.get());
		source1.set(48);
		assertEquals(50, (Object) access.get());
		source2.set(-48);
		assertEquals(0, (Object) access.get());
		assertEquals(0, (Object) access.get());
	}

	@Test
	public void testComputationDoneOnFirstCall() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		access.setSource(1, new ControlledProperty<Integer>(1));
		access.setSource(2, new ControlledProperty<Integer>(2));
		final boolean[] isComputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isComputed[0] = true;
				return 0;
			}
		});

		isComputed[0] = false;
		access.get();
		assertTrue(isComputed[0]);
	}

	@Test
	public void testNoRecomputationWhenNoChange() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		source1.set(3);
		access.get();
		assertTrue(isRecomputed[0]);

		isRecomputed[0] = false;
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		source2.set(4);
		access.get();
		assertTrue(isRecomputed[0]);

		isRecomputed[0] = false;
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testNoRecomputationWhenInputChangedAndChangedBack() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		source1.set(3);
		source1.set(1);
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		source2.set(4);
		source2.set(2);
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testNoRecomputationWhenSourceChangedButSameValue() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		access.setSource(1, new ControlledProperty<Integer>(1));
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		access.setSource(2, new ControlledProperty<Integer>(2));
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testNoRecomputationWhenComputerChangedAndChangedBack() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		Computer<Integer> computer = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		};
		access.setComputer(computer);
		access.get();

		isRecomputed[0] = false;
		access.setComputer(null);
		access.setComputer(computer);
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testNoRecomputationWhenInputRemovedAndThenSetWithSameValue() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		access.removeSource(1);
		access.setSource(1, new ControlledProperty<Integer>(1));
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		access.removeSource(2);
		access.setSource(2, new ControlledProperty<Integer>(2));
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testRecomputationWhenInputChanged() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		source1.set(3);
		access.get();
		assertTrue(isRecomputed[0]);

		isRecomputed[0] = false;
		source2.set(4);
		access.get();
		assertTrue(isRecomputed[0]);
	}

	@Test
	public void testRecomputationWhenComputerChanged() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false, false };
		Computer<Integer> computer1 = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		};
		access.setComputer(computer1);
		access.get();

		Computer<Integer> computer2 = new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[1] = true;
				return 0;
			}
		};
		isRecomputed[0] = false;
		isRecomputed[1] = false;
		access.setComputer(computer2);
		access.get();
		assertFalse(isRecomputed[0]);
		assertTrue(isRecomputed[1]);

		isRecomputed[0] = false;
		isRecomputed[1] = false;
		access.setComputer(computer1);
		access.get();
		assertTrue(isRecomputed[0]);
		assertFalse(isRecomputed[1]);
	}

	@Test
	public void testRecomputationWhenNonNullInputRemoved() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		access.removeSource(1);
		access.get();
		assertTrue(isRecomputed[0]);

		isRecomputed[0] = false;
		access.removeSource(2);
		access.get();
		assertTrue(isRecomputed[0]);
	}

	@Test
	public void testNoRecomputationWhenNullInputRemoved() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(
				null);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(
				null);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final boolean[] isRecomputed = { false };
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				isRecomputed[0] = true;
				return 0;
			}
		});
		access.get();

		isRecomputed[0] = false;
		access.removeSource(1);
		access.get();
		assertFalse(isRecomputed[0]);

		isRecomputed[0] = false;
		access.removeSource(2);
		access.get();
		assertFalse(isRecomputed[0]);
	}

	@Test
	public void testInputValueRemovedWhenSourceRemoved() {
		LastInstantComputer<Integer> access = new LastInstantComputer<Integer>();
		ControlledProperty<Integer> source1 = new ControlledProperty<Integer>(1);
		ControlledProperty<Integer> source2 = new ControlledProperty<Integer>(2);
		access.setSource(1, source1);
		access.setSource(2, source2);
		final Collection<Object> keys = new LinkedList<Object>();
		access.setComputer(new Computer<Integer>() {

			@Override
			public Integer compute(Map<Object, Object> inputs) {
				keys.addAll(inputs.keySet());
				return 0;
			}
		});

		access.get();
		assertEquals(2, keys.size());
		assertTrue(keys.contains(1));
		assertTrue(keys.contains(2));

		keys.clear();
		access.removeSource(1);
		access.get();
		assertEquals(1, keys.size());
		assertTrue(keys.contains(2));

		keys.clear();
		access.removeSource(2);
		access.get();
		assertEquals(0, keys.size());
	}
}
