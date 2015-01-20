package fr.vergne.access.impl;

import fr.vergne.access.PullReadAccess;
import fr.vergne.access.WriteAccess;

/**
 * A {@link ReadWriteProperty} emulates a property which can be read and
 * written. Thus it only implements the {@link WriteAccess} and
 * {@link PullReadAccess} interfaces and uses a single variable to store the
 * value provided through {@link #set(Object)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class ReadWriteProperty<Value> implements WriteAccess<Value>,
		PullReadAccess<Value> {

	private Value value;

	/**
	 * Create the {@link ReadWriteProperty} with a <code>null</code> value.
	 */
	public ReadWriteProperty() {
		this(null);
	}

	/**
	 * Create the {@link ReadWriteProperty} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public ReadWriteProperty(Value initialValue) {
		set(initialValue);
	}

	@Override
	public Value get() {
		return value;
	}

	@Override
	public void set(Value value) {
		this.value = value;
	}

}
