package fr.vergne.access.impl;

import java.util.Collection;
import java.util.HashSet;

import fr.vergne.access.PullReadAccess;
import fr.vergne.access.PushReadAccess;
import fr.vergne.access.WriteAccess;

/**
 * A {@link ReactiveReadWriteProperty}, like {@link ReadWriteProperty}, emulates
 * a property which can be read and written. However, it adds some reactivity by
 * giving the possibility to be notified when the {@link #set(Object)} method is
 * called. To receive these notifications, one has to register itself through
 * {@link #addPushListener(PushListener)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class ReactiveReadWriteProperty<Value> implements WriteAccess<Value>,
		PullReadAccess<Value>, PushReadAccess<Value> {

	private Value value;
	private final Collection<PushListener<Value>> listeners = new HashSet<PushListener<Value>>();

	/**
	 * Create the {@link ReactiveReadWriteProperty} with a <code>null</code>
	 * value.
	 */
	public ReactiveReadWriteProperty() {
		this(null);
	}

	/**
	 * Create the {@link ReactiveReadWriteProperty} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public ReactiveReadWriteProperty(Value initialValue) {
		set(initialValue);
	}

	@Override
	public Value get() {
		return value;
	}

	@Override
	public void set(Value value) {
		this.value = value;
		for (PushListener<Value> listener : listeners) {
			listener.valueGenerated(value);
		}
	}

	@Override
	public void addPushListener(PushListener<Value> listener) {
		listeners.add(listener);
	}

	@Override
	public void removePushListener(PushListener<Value> listener) {
		listeners.remove(listener);
	}
}
