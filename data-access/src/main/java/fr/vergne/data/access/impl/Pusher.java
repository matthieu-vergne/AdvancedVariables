package fr.vergne.data.access.impl;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.ActiveWriteAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess.ValueListener;

/**
 * A {@link Pusher} is a common pattern where some sources of data aims at
 * providing values to some interested observers on real time. Thus, the sources
 * can provide their values at any time with {@link #set(Object)} and the
 * observers will be notified immediately, as long as they have registered a
 * {@link ValueListener} with {@link #addValueListener(ValueListener)}.<br/>
 * <br/>
 * While a {@link ReactiveControlledProperty} can do the same thing than a
 * {@link Pusher}, the {@link Pusher} is specialized for a one way flow
 * controlled by the writer. Thus, it does not provide any
 * {@link ActiveReadAccess#get()} method and do not store the value.
 * Consequently, it is recommended for performance purpose.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class Pusher<Value> extends SimplePassiveReadAccess<Value> implements
		ActiveWriteAccess<Value>, PassiveReadAccess<Value> {

	@Override
	public void set(Value value) {
		notifyValueListeners(value);
	}
}
