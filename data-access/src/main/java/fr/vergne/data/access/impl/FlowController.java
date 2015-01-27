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
public class FlowController<Value> implements PassiveWriteAccess<Value>,
		PassiveReadAccess<Value> {

	private final Collection<ValueListener<Value>> listeners = new HashSet<ValueListener<Value>>();
	private ValueGenerator<Value> generator;

	/**
	 * Create a {@link FlowController} without any {@link ValueGenerator}. Such
	 * a state is inconsistent (a {@link FlowController} has no meaning if it is
	 * not associated to a source) so be sure that you use
	 * {@link #setValueGenerator(ValueGenerator)} before to call
	 * {@link #transfer()}.
	 */
	public FlowController() {
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
		setValueGenerator(generator);
	}

	@Override
	public void addValueListener(ValueListener<Value> listener) {
		listeners.add(listener);
	}
	
	@Override
	public Collection<fr.vergne.data.access.PassiveReadAccess.ValueListener<Value>> getValueListeners() {
		return listeners;
	}

	@Override
	public void removeValueListener(ValueListener<Value> listener) {
		listeners.remove(listener);
	}

	@Override
	public void setValueGenerator(ValueGenerator<Value> generator) {
		this.generator = generator;
	}

	@Override
	public ValueGenerator<Value> getValueGenerator() {
		return generator;
	}

	/**
	 * Retrieve the value from the {@link ValueGenerator} and transfer it to the
	 * registered {@link ValueListener}s. If no {@link ValueListener} is
	 * registered, nothing happen to preserve performance.
	 */
	public void transfer() {
		if (listeners.isEmpty()) {
			// no need to retrieve the value
		} else {
			Value value = generator.generateValue();
			for (ValueListener<Value> listener : listeners) {
				listener.valueGenerated(value);
			}
		}
	}
}
