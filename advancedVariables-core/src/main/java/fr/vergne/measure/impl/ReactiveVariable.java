package fr.vergne.measure.impl;

import java.util.Collection;
import java.util.HashSet;

import fr.vergne.measure.PullReadable;
import fr.vergne.measure.PushReadable;
import fr.vergne.measure.Writable;

/**
 * A {@link ReactiveVariable}, like {@link SimpleVariable}, emulates a variable
 * which can be read and written. However, it adds some reactivity by giving the
 * possibility to be notified when the {@link #set(Object)} method is called. To
 * receive these notifications, one has to register a listener through
 * {@link #addValueListener(ValueListener)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class ReactiveVariable<T> implements Writable<T>, PullReadable<T>,
		PushReadable<T> {

	private T value;
	private final Collection<ValueListener<T>> listeners = new HashSet<ValueListener<T>>();

	/**
	 * Create the {@link ReactiveVariable} with a <code>null</code> value.
	 */
	public ReactiveVariable() {
		this(null);
	}

	/**
	 * Create the {@link ReactiveVariable} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public ReactiveVariable(T initialValue) {
		set(initialValue);
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public void set(T value) {
		this.value = value;
		for (ValueListener<T> listener : listeners) {
			listener.valueGenerated(value);
		}
	}

	@Override
	public void addValueListener(ValueListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeValueListener(ValueListener<T> listener) {
		listeners.remove(listener);
	}
}
