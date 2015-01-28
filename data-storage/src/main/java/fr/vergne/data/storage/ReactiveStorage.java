package fr.vergne.data.storage;

import java.util.Collection;

/**
 * A {@link ReactiveStorage} is a {@link DataStorage} which notifies observers
 * about its modifications. Any observer which need to be notified has to
 * register an {@link OperationListener} through
 * {@link #addOperationListener(OperationListener)}. When the observer do not
 * want to receive any notification anymore, its {@link OperationListener} can
 * be removed through {@link #removeOperationListener(OperationListener)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Key>
 */
public interface ReactiveStorage<Key> extends DataStorage<Key> {

	/**
	 * Register an {@link OperationListener} to manage the notification of this
	 * {@link DataStorage}.
	 * 
	 * @param listener
	 *            the {@link OperationListener} to register
	 */
	public void addOperationListener(OperationListener<Key> listener);

	/**
	 * 
	 * @return all the {@link OperationListener} registered to this
	 *         {@link DataStorage}
	 */
	public Collection<OperationListener<Key>> getOperationListeners();

	/**
	 * Remove an {@link OperationListener} previously registered. If the
	 * provided {@link OperationListener} is not registered, nothing should
	 * happen.
	 * 
	 * @param listener
	 *            the {@link OperationListener} to unregister
	 */
	public void removeOperationListener(OperationListener<Key> listener);

	/**
	 * An {@link OperationListener} allows to specify what to do when a
	 * {@link ReactiveStorage} is modified (i.e. a value is assigned to a
	 * {@link Key}).
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <Key>
	 */
	public static interface OperationListener<Key> {
		/**
		 * This method is called when a value is assigned to a {@link Key}. It
		 * could be that the {@link Key} has been reassigned with the same
		 * value, leading to the same old and new value. If the old value (resp.
		 * new value) is <code>null</code>, it means that the {@link Key} has
		 * been added to (resp. removed from) the {@link ReactiveStorage}. It
		 * could also be that both the old and new values are <code>null</code>
		 * .
		 * 
		 * @param key
		 *            the {@link Key} which has been set
		 * @param oldValue
		 *            the value assigned to the {@link Key} before the
		 *            modification
		 * @param newValue
		 *            the value assigned to the {@link Key} from now on
		 */
		public void entrySet(Key key, Object oldValue, Object newValue);
	}
}
