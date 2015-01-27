package fr.vergne.data.access.impl;

import java.util.Collection;
import java.util.HashSet;

import fr.vergne.data.access.PassiveReadAccess;

/**
 * This {@link SimplePassiveReadAccess} implements the minimal needs to manage
 * the {@link ValueListener}s of a {@link PassiveReadAccess}. It implements the
 * necessary controls and can be reused in other implementations to reduce the
 * effort.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class SimplePassiveReadAccess<Value> implements PassiveReadAccess<Value> {
	
	private final Collection<ValueListener<Value>> listeners = new HashSet<ValueListener<Value>>();

	@Override
	public void addValueListener(ValueListener<Value> listener) {
		if (listener == null) {
			throw new NullPointerException("No listener provided");
		} else {
			listeners.add(listener);
		}
	}

	@Override
	public Collection<ValueListener<Value>> getValueListeners() {
		return listeners;
	}

	@Override
	public void removeValueListener(ValueListener<Value> listener) {
		listeners.remove(listener);
	}

}
