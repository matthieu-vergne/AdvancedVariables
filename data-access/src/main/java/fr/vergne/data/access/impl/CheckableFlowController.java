package fr.vergne.data.access.impl;

import fr.vergne.data.access.ActiveReadAccess;

/**
 * A {@link CheckableFlowController}, like a {@link FlowController}, aims at
 * allowing a value, provided by a source, to be transferred to some targets.
 * However, it also allows to know which value will be transferred through the
 * additional {@link #get()} method. The main difference with
 * {@link ReadableFlowController} is that the {@link #get()} method provide the
 * current value of the source instead of the last value transferred to the
 * targets.<br/>
 * <br/>
 * If the ability to register a {@link ValueListener} is not necessary and only
 * the {@link #get()} method is needed, one could prefer to use a {@link Puller}
 * , which is more optimized.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class CheckableFlowController<Value> extends FlowController<Value>
		implements ActiveReadAccess<Value> {

	/**
	 * Create a {@link CheckableFlowController} without any
	 * {@link ValueGenerator}. Like {@link FlowController}, be sure that you use
	 * {@link #setValueGenerator(ValueGenerator)} before to call
	 * {@link #transfer()}.
	 */
	public CheckableFlowController() {
		super();
	}

	/**
	 * Create a {@link CheckableFlowController} associated to the given
	 * {@link ValueGenerator}.
	 * 
	 * @param generator
	 *            the {@link ValueGenerator} which provides the values to
	 *            transfer to the {@link ValueListener}s
	 */
	public CheckableFlowController(ValueGenerator<Value> generator) {
		super(generator);
	}

	/**
	 * @return the current value provided by the {@link ValueGenerator}
	 */
	@Override
	public Value get() {
		return getValueGenerator().generateValue();
	}
}
