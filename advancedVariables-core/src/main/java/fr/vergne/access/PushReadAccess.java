package fr.vergne.access;

/**
 * {@link PushReadAccess} provides a read access to the value of a property on a
 * "push" basis: the property appears to be generated at some given instants,
 * and any {@link Object} needing it should be notified on the fly when the
 * value is generated. If you prefer to manually control the requests, you
 * should consider to use a {@link PullReadAccess}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public interface PushReadAccess<Value> extends Access<Value> {

	/**
	 * Because the value is generated on the fly, any {@link Object} needing to
	 * know about this value need to register a {@link PushListener} to be
	 * notified when the value is generated. All registered {@link PushListener}
	 * s should be executed every time the value is generated.
	 * 
	 * @param listener
	 *            the {@link PushListener} to register
	 */
	public void addPushListener(PushListener<Value> listener);

	/**
	 * When registered through {@link #addPushListener(PushListener)}, any
	 * notification should be sent to the registered {@link PushListener}s. When
	 * a {@link PushListener} should not be notified anymore, it can be removed
	 * through this method.
	 * 
	 * @param listener
	 *            the {@link PushListener} to unregister
	 */
	public void removePushListener(PushListener<Value> listener);

	/**
	 * A {@link PushListener} allows to describe what should be done when a
	 * value is generated and accessed through a {@link PushReadAccess}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <T>
	 */
	public static interface PushListener<T> {
		/**
		 * 
		 * @param value
		 *            the value generated and to be accessed through the
		 *            {@link PushReadAccess}
		 */
		public void valueGenerated(T value);
	}
}
