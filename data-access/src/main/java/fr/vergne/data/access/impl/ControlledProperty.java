package fr.vergne.data.access.impl;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.ActiveWriteAccess;

/**
 * A {@link ControlledProperty} emulates a property which can be read and
 * written on demand, like any primitive variable. Thus it implements the
 * {@link ActiveWriteAccess} and {@link ActiveReadAccess} interfaces and uses a
 * single variable to store the value provided through {@link #set(Object)},
 * allowing an immediate access with {@link #get()}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
// TODO put in a deeper package "advanced" (tests too)
public class ControlledProperty<Value> implements ActiveWriteAccess<Value>,
		ActiveReadAccess<Value> {

	private Value value;

	/**
	 * Create the {@link ControlledProperty} with a <code>null</code> value.
	 */
	public ControlledProperty() {
		this(null);
	}

	/**
	 * Create the {@link ControlledProperty} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public ControlledProperty(Value initialValue) {
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
