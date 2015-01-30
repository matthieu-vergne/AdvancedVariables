package fr.vergne.data.access.impl;

import java.util.Collection;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess;

/**
 * A {@link ReactiveControlledProperty}, like a {@link ControlledProperty},
 * emulates a property which can be read and written. However, it adds some
 * reactivity by giving the possibility to be notified when the
 * {@link #set(Object)} method is called. To receive these notifications, one
 * has to register itself through {@link #addValueListener(ValueListener)}.<br/>
 * <br/>
 * If the ability to read the stored value at any time through the
 * {@link ActiveReadAccess#get()} method is not necessary, one could prefer to
 * use a {@link Pusher}, which is more optimized.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
// TODO put in a deeper package "advanced" (tests too)
public class ReactiveControlledProperty<Value> extends
		ControlledProperty<Value> implements PassiveReadAccess<Value> {

	private final PassiveReadAccess<Value> reader = new SimplePassiveReadAccess<Value>();

	/**
	 * Create the {@link ReactiveControlledProperty} with a <code>null</code>
	 * value.
	 */
	public ReactiveControlledProperty() {
		super();
	}

	/**
	 * Create the {@link ReactiveControlledProperty} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public ReactiveControlledProperty(Value initialValue) {
		super(initialValue);
	}

	@Override
	public void set(Value value) {
		super.set(value);
		if (reader == null) {
			/*
			 * This case happen when the instance is initializing due to the
			 * call of this method on initialization in the super class. In such
			 * a case, the members of the current class are not yet initialized,
			 * so executing the code below would lead to a NullPointerException.
			 * At this state, no listener has to be to notified anyway.
			 */
		} else {
			for (ValueListener<Value> listener : reader.getValueListeners()) {
				listener.valueGenerated(value);
			}
		}
	}

	@Override
	public void addValueListener(ValueListener<Value> listener) {
		reader.addValueListener(listener);
	}

	@Override
	public Collection<ValueListener<Value>> getValueListeners() {
		return reader.getValueListeners();
	}

	@Override
	public void removeValueListener(ValueListener<Value> listener) {
		reader.removeValueListener(listener);
	}
}
