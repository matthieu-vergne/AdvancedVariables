package fr.vergne.access.impl;

import fr.vergne.access.PullReadable;
import fr.vergne.access.Writable;

/**
 * A {@link SimpleVariable} is an entity which emulates a variable which can be
 * read and written. Thus it only implements the {@link Writable} and
 * {@link PullReadable} interfaces and uses a single variable to store the value
 * provided through {@link #set(Object)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class SimpleVariable<T> implements Writable<T>, PullReadable<T> {

	private T value;

	/**
	 * Create the {@link SimpleVariable} with a <code>null</code> value.
	 */
	public SimpleVariable() {
		this(null);
	}

	/**
	 * Create the {@link SimpleVariable} with a given value.
	 * 
	 * @param initialValue
	 *            the value to set on creation
	 */
	public SimpleVariable(T initialValue) {
		set(initialValue);
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public void set(T value) {
		this.value = value;
	}

}
