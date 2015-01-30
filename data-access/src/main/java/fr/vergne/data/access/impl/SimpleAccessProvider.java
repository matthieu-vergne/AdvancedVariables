package fr.vergne.data.access.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.ActiveWriteAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.PropertyAccess;
import fr.vergne.data.access.PropertyAccessProvider;

/**
 * This {@link SimpleAccessProvider} allows to manage a set of properties and
 * their {@link PropertyAccess}es in a simple way. It should be satisfying for
 * any basic use of {@link PropertyAccessProvider}.<br/>
 * <br/>
 * <h5>Single {@link PropertyAccess} for single property:</h5> <br/>
 * Additionally to the getX methods to request the corresponding
 * {@link PropertyAccess} on a given property, this
 * {@link PropertyAccessProvider} implements the corresponding setX and removeX
 * methods to add and remove the corresponding {@link PropertyAccess}es. It is
 * also possible to add {@link PropertyAccess}es for any new property, simply by
 * choosing a different key.<br/>
 * <br/>
 * <h5>Multiple {@link PropertyAccess}es for single property:</h5> <br/>
 * If a single instance implements several {@link PropertyAccess} interfaces,
 * like the {@link ControlledProperty} and {@link FlowController}
 * implementations end their extensions, the method
 * {@link #setMultiAccesses(Object, PropertyAccess)} can be used to add all of
 * them at once. Similarly, the method {@link #removeMultiAccesses(Object)}
 * allows to remove all the {@link PropertyAccess}es associated to a given key,
 * independently on how they have been added.<br/>
 * <br/>
 * <h5>Multiple {@link PropertyAccess}es on multiple properties:</h5> <br/>
 * Finally, for massive management, the methods {@link #setAllAccesses(Map)} and
 * {@link #removeAllAccesses(Collection)} can be used, with an additional
 * {@link #clear()} method to remove all the current {@link PropertyAccess}es on
 * all the managed properties.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class SimpleAccessProvider implements PropertyAccessProvider {

	private final Map<Object, ActiveReadAccess<?>> activeReaders = new HashMap<Object, ActiveReadAccess<?>>();
	private final Map<Object, ActiveWriteAccess<?>> activeWriters = new HashMap<Object, ActiveWriteAccess<?>>();
	private final Map<Object, PassiveReadAccess<?>> passiveReaders = new HashMap<Object, PassiveReadAccess<?>>();
	private final Map<Object, PassiveWriteAccess<?>> passiveWriters = new HashMap<Object, PassiveWriteAccess<?>>();
	private final Collection<Object> keys = new HashSet<Object>();

	/**
	 * Create an empty {@link SimpleAccessProvider}.
	 */
	public SimpleAccessProvider() {
		// do not store anything
	}

	/**
	 * Create a {@link SimpleAccessProvider} with an initial content.
	 * 
	 * @param accesses
	 *            the {@link PropertyAccess}es to use in this
	 *            {@link PropertyAccessProvider}
	 */
	public SimpleAccessProvider(Map<Object, PropertyAccess<?>> accesses) {
		setAllAccesses(accesses);
	}

	/**
	 * Create a {@link SimpleAccessProvider} which reuses all the current
	 * {@link PropertyAccess}es of an already existing
	 * {@link PropertyAccessProvider}. Notice that if the
	 * {@link PropertyAccessProvider} used in argument keep the same
	 * {@link PropertyAccess} instances, any modification done to these
	 * instances, e.g. listener registration, will apply to the
	 * {@link PropertyAccess} instances stored in this new
	 * {@link SimpleAccessProvider}, and vice-versa. However, if any of these
	 * two {@link PropertyAccessProvider} assign a new {@link PropertyAccess}
	 * instance, the corresponding synchronization will be lost.
	 * 
	 * @param provider
	 *            the {@link PropertyAccessProvider} to copy
	 */
	public SimpleAccessProvider(PropertyAccessProvider provider) {
		for (Object key : provider.getKeys()) {
			setActiveReadAccess(key, provider.getActiveReadAccess(key));
			setPassiveReadAccess(key, provider.getPassiveReadAccess(key));
			setActiveWriteAccess(key, provider.getActiveWriteAccess(key));
			setPassiveWriteAccess(key, provider.getPassiveWriteAccess(key));
		}
	}

	/**
	 * Return all the keys having at least one {@link PropertyAccess} instance
	 * set up. Please, use the corresponding getX methods to know which
	 * {@link PropertyAccess}es are available for a given key.
	 */
	@Override
	public Collection<Object> getKeys() {
		return keys;
	}

	/**
	 * @deprecated Prefer to use {@link #getKeys()}. Will be deleted in future
	 *             versions.
	 */
	@Deprecated
	@Override
	public Collection<Object> getIDs() {
		return getKeys();
	}

	/**
	 * This method assign a given {@link ActiveReadAccess} to a given property.
	 * If the property is unknown, it is added, otherwise only the
	 * {@link PropertyAccess} is changed to the new one.
	 * 
	 * @param key
	 *            the key of a property
	 * @param access
	 *            the {@link ActiveReadAccess} to this property
	 */
	public void setActiveReadAccess(Object key, ActiveReadAccess<?> access) {
		activeReaders.put(key, access);
		keys.add(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ActiveReadAccess<T> getActiveReadAccess(Object key) {
		return (ActiveReadAccess<T>) activeReaders.get(key);
	}

	/**
	 * This method removes any {@link ActiveReadAccess} assigned to a given
	 * property. If the key is unknown, it has no effect, otherwise the
	 * {@link PropertyAccess} is removed and, if the key has no other
	 * {@link PropertyAccess}, it is forgotten.
	 * 
	 * @param key
	 *            the key of the property to remove the {@link ActiveReadAccess}
	 *            from
	 */
	public void removeActiveReadAccess(Object key) {
		activeReaders.remove(key);
		if (passiveReaders.containsKey(key) || activeWriters.containsKey(key)
				|| passiveWriters.containsKey(key)) {
			// key still used
		} else {
			keys.remove(key);
		}
	}

	/**
	 * This method assign a given {@link PassiveReadAccess} to a given property.
	 * If the key is unknown, it is added, otherwise only the
	 * {@link PropertyAccess} is changed to the new one.
	 * 
	 * @param key
	 *            the key of a property
	 * @param access
	 *            the {@link PassiveReadAccess} to this property
	 */
	public void setPassiveReadAccess(Object key, PassiveReadAccess<?> access) {
		passiveReaders.put(key, access);
		keys.add(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PassiveReadAccess<T> getPassiveReadAccess(Object key) {
		return (PassiveReadAccess<T>) passiveReaders.get(key);
	}

	/**
	 * This method removes any {@link PassiveReadAccess} assigned to a given
	 * property. If the key is unknown, it has no effect, otherwise the
	 * {@link PropertyAccess} is removed and, if the key has no other
	 * {@link PropertyAccess}, it is forgotten.
	 * 
	 * @param key
	 *            the key of the property to remove the
	 *            {@link PassiveReadAccess} from
	 */
	public void removePassiveReadAccess(Object key) {
		passiveReaders.remove(key);
		if (activeReaders.containsKey(key) || activeWriters.containsKey(key)
				|| passiveWriters.containsKey(key)) {
			// key still used
		} else {
			keys.remove(key);
		}
	}

	/**
	 * This method assign a given {@link ActiveWriteAccess} to a given property.
	 * If the key is unknown, it is added, otherwise the only
	 * {@link PropertyAccess} is changed to the new one.
	 * 
	 * @param key
	 *            the key of a property
	 * @param access
	 *            the {@link ActiveWriteAccess} to this property
	 */
	public void setActiveWriteAccess(Object key, ActiveWriteAccess<?> access) {
		activeWriters.put(key, access);
		keys.add(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ActiveWriteAccess<T> getActiveWriteAccess(Object key) {
		return (ActiveWriteAccess<T>) activeWriters.get(key);
	}

	/**
	 * This method removes any {@link ActiveWriteAccess} assigned to a given
	 * property. If the key is unknown, it has no effect, otherwise the
	 * {@link PropertyAccess} is removed and, if the key has no other
	 * {@link PropertyAccess}, it is forgotten.
	 * 
	 * @param key
	 *            the key of the property to remove the
	 *            {@link ActiveWriteAccess} from
	 */
	public void removeActiveWriteAccess(Object key) {
		activeWriters.remove(key);
		if (activeReaders.containsKey(key) || passiveReaders.containsKey(key)
				|| passiveWriters.containsKey(key)) {
			// key still used
		} else {
			keys.remove(key);
		}
	}

	/**
	 * This method assign a given {@link PassiveWriteAccess} to a given
	 * property. If the key is unknown, it is added, otherwise the only
	 * {@link PropertyAccess} is changed to the new one.
	 * 
	 * @param key
	 *            the key of a property
	 * @param access
	 *            the {@link PassiveWriteAccess} to this property
	 */
	public void setPassiveWriteAccess(Object key, PassiveWriteAccess<?> access) {
		passiveWriters.put(key, access);
		keys.add(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PassiveWriteAccess<T> getPassiveWriteAccess(Object key) {
		return (PassiveWriteAccess<T>) passiveWriters.get(key);
	}

	/**
	 * This method removes any {@link PassiveWriteAccess} assigned to a given
	 * property. If the key is unknown, it has no effect, otherwise the
	 * {@link PropertyAccess} is removed and, if the key has no other
	 * {@link PropertyAccess}, it is forgotten.
	 * 
	 * @param key
	 *            the key of the property to remove the
	 *            {@link PassiveWriteAccess} from
	 */
	public void removePassiveWriteAccess(Object key) {
		passiveWriters.remove(key);
		if (activeReaders.containsKey(key) || passiveReaders.containsKey(key)
				|| activeWriters.containsKey(key)) {
			// key still used
		} else {
			keys.remove(key);
		}
	}

	/**
	 * This method assign all the available {@link PropertyAccess}es to a given
	 * property based on the implemented interfaces. If the key is unknown, it
	 * is added, otherwise only the {@link PropertyAccess}es are changed to the
	 * new ones.
	 * 
	 * @param key
	 *            the key of a property
	 * @param access
	 *            the instance implementing the {@link PropertyAccess}
	 *            interfaces
	 */
	public void setMultiAccesses(Object key, PropertyAccess<?> access) {
		if (access instanceof ActiveReadAccess<?>) {
			setActiveReadAccess(key, (ActiveReadAccess<?>) access);
		}
		if (access instanceof PassiveReadAccess<?>) {
			setPassiveReadAccess(key, (PassiveReadAccess<?>) access);
		}
		if (access instanceof ActiveWriteAccess<?>) {
			setActiveWriteAccess(key, (ActiveWriteAccess<?>) access);
		}
		if (access instanceof PassiveWriteAccess<?>) {
			setPassiveWriteAccess(key, (PassiveWriteAccess<?>) access);
		}
	}

	/**
	 * This method removes all the {@link PropertyAccess}es assigned to a given
	 * property. If the key is unknown, it has no effect, otherwise the
	 * {@link PropertyAccess}es are removed and the key is forgotten.
	 * 
	 * @param key
	 *            the key of the property to remove the {@link PropertyAccess}es
	 *            from
	 */
	public void removeMultiAccesses(Object key) {
		activeReaders.remove(key);
		passiveReaders.remove(key);
		activeWriters.remove(key);
		passiveWriters.remove(key);
		keys.remove(key);
	}

	/**
	 * This method is equivalent to using
	 * {@link #setMultiAccesses(Object, PropertyAccess)} for several properties.
	 * 
	 * @param accesses
	 *            the map telling which {@link PropertyAccess}es to assign to
	 *            which keys
	 */
	public void setAllAccesses(Map<Object, ? extends PropertyAccess<?>> accesses) {
		for (Entry<Object, ? extends PropertyAccess<?>> entry : accesses
				.entrySet()) {
			setMultiAccesses(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * This method is equivalent to using {@link #removeMultiAccesses(Object)}
	 * on several properties.
	 * 
	 * @param keys
	 *            the keys of the {@link PropertyAccess}es to remove
	 */
	public void removeAllAccesses(Collection<Object> keys) {
		for (Object key : keys) {
			removeMultiAccesses(key);
		}
	}

	/**
	 * This method is equivalent to using {@link #removeAllAccesses(Collection)}
	 * on all the keys currently managed. No more key should be returned by
	 * {@link #getKeys()} and only <code>null</code> values should be returned
	 * for {@link PropertyAccess}es after the execution of this method.
	 */
	public void clear() {
		activeReaders.clear();
		passiveReaders.clear();
		activeWriters.clear();
		passiveWriters.clear();
		keys.clear();
	}
}
