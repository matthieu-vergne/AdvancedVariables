package fr.vergne.data.access;

import java.util.Collection;

import fr.vergne.data.access.util.AccessFactory;

/**
 * {@link PassiveReadAccess} provides a read access to the value of a property
 * in a passive way, such that the user wait for the value to be delivered. Any
 * user needing the value should be notified on the fly when the value is
 * generated.<br/>
 * <br/>
 * If you prefer to manually control the reading, you should consider to use an
 * {@link ActiveReadAccess}. You can create one above a
 * {@link PassiveReadAccess} through
 * {@link AccessFactory#createActiveReadFromPassiveRead(PassiveReadAccess, Object)}
 * .
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface PassiveReadAccess<Value> extends PropertyAccess<Value> {

	/**
	 * Because the value is generated on the fly, any user needing the value has
	 * to register a {@link ValueListener} to be notified when the value is
	 * generated. All registered {@link ValueListener}s should be executed every
	 * time the value is generated.
	 * 
	 * @param listener
	 *            the {@link ValueListener} to register
	 */
	public void addValueListener(ValueListener<Value> listener);

	/**
	 * 
	 * @return the {@link ValueListener}s registered to this
	 *         {@link PassiveReadAccess}
	 */
	public Collection<ValueListener<Value>> getValueListeners();

	/**
	 * When registered through {@link #addValueListener(ValueListener)}, any
	 * notification should be sent to the registered {@link ValueListener}s.
	 * When a {@link ValueListener} should not be notified anymore, it can be
	 * removed through this method.
	 * 
	 * @param listener
	 *            the {@link ValueListener} to unregister
	 */
	public void removeValueListener(ValueListener<Value> listener);

	/**
	 * A {@link ValueListener} allows to describe what should be done when a
	 * value is generated and accessed through a {@link PassiveReadAccess}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <T>
	 */
	public static interface ValueListener<T> {
		/**
		 * 
		 * @param value
		 *            the value generated within the {@link PassiveReadAccess}
		 */
		public void valueGenerated(T value);
	}
}
