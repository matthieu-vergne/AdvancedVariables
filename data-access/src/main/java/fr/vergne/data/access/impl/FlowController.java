package fr.vergne.data.access.impl;

import java.util.Collection;
import java.util.HashSet;

import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess;

/**
 * A {@link FlowController} aims at allowing a value, provided by a source, to
 * be provided to some targets. Thus, the {@link FlowController} needs to know
 * which value is provided by the source, through the
 * {@link #setValueGenerator(ValueGenerator)}, and which targets need to be
 * notified, through {@link #addValueListener(ValueListener)}. An additional
 * {@link #transfer()} method allows to execute the transfer.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class FlowController<Value> extends SimplePassiveWriteAccess<Value>
		implements PassiveWriteAccess<Value>, PassiveReadAccess<Value> {

	private final Collection<ValueListener<Value>> readers = new HashSet<ValueListener<Value>>();

	/**
	 * Create a {@link FlowController} without any {@link ValueGenerator}. Such
	 * a state is inconsistent (a {@link FlowController} has no meaning if it is
	 * not associated to a source) so be sure that you use
	 * {@link #setValueGenerator(ValueGenerator)} before to call
	 * {@link #transfer()}.
	 */
	public FlowController() {
		super(null);
	}

	/**
	 * Create a {@link FlowController} associated to the given
	 * {@link ValueGenerator}.
	 * 
	 * @param generator
	 *            the {@link ValueGenerator} which provides the values to
	 *            transfer to the {@link ValueListener}s
	 */
	public FlowController(ValueGenerator<Value> generator) {
		super(generator);
	}

	@Override
	public void addValueListener(ValueListener<Value> listener) {
		readers.add(listener);
	}

	@Override
	public Collection<ValueListener<Value>> getValueListeners() {
		return readers;
	}

	@Override
	public void removeValueListener(ValueListener<Value> listener) {
		readers.remove(listener);
	}

	/**
	 * Retrieve the value from the {@link ValueGenerator} and transfer it to the
	 * registered {@link ValueListener}s. If no {@link ValueListener} is
	 * registered, nothing happen to preserve performance.
	 */
	public void transfer() {
		if (readers.isEmpty()) {
			// no need to retrieve the value
		} else {
			Value value = getValueGenerator().generateValue();
			for (ValueListener<Value> reader : readers) {
				reader.valueGenerated(value);
			}
		}
	}
}
