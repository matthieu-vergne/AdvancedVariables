package fr.vergne.data.access.impl.advanced;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess.ValueGenerator;

/**
 * A {@link ReadableFlowController}, like a {@link FlowController}, aims at
 * allowing a value, provided by a source, to be transferred to some targets.
 * However, it also allows to know which value has been transferred through the
 * additional {@link #get()} method. As long as the {@link #transfer()} method
 * has not been called, the value returned by {@link #get()} is
 * <code>null</code>. The main difference with {@link CheckableFlowController}
 * is that the {@link #get()} method provides the last value transferred to the
 * targets instead of the current value of the source.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class ReadableFlowController<Value> extends FlowController<Value>
		implements ActiveReadAccess<Value> {

	private Value lastValue = null;

	/**
	 * Create a {@link ReadableFlowController} without any
	 * {@link ValueGenerator}. Like {@link FlowController}, be sure that you use
	 * {@link #setValueGenerator(ValueGenerator)} before to call
	 * {@link #transfer()}.
	 */
	public ReadableFlowController() {
		this(null);
	}

	/**
	 * Create a {@link ReadableFlowController} associated to the given
	 * {@link ValueGenerator}.
	 * 
	 * @param generator
	 *            the {@link ValueGenerator} which provides the values to
	 *            transfer to the {@link ValueListener}s
	 */
	public ReadableFlowController(ValueGenerator<Value> generator) {
		super(generator);
		addValueListener(new ValueListener<Value>() {

			@Override
			public void valueGenerated(Value value) {
				lastValue = value;
			}
		});
	}

	/**
	 * @return the value retrieved at the last {@link #transfer()} call
	 */
	@Override
	public Value get() {
		return lastValue;
	}
}
