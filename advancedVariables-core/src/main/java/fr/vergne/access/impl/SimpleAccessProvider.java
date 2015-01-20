package fr.vergne.access.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import fr.vergne.access.Access;
import fr.vergne.access.AccessProvider;
import fr.vergne.access.PullReadAccess;
import fr.vergne.access.PushReadAccess;
import fr.vergne.access.WriteAccess;

/**
 * This {@link SimpleAccessProvider} allows to manage a set of IDs and their
 * {@link Access}es in a simple way. It should be satisfying for any basic use
 * of {@link AccessProvider}.<br/>
 * <br/>
 * <h5>Single {@link Access} for single property:</h5> <br/>
 * Additionally to the getX methods to request the corresponding {@link Access}
 * on a given property, this {@link AccessProvider} implements the corresponding
 * setX and removeX methods to add and remove the corresponding {@link Access}
 * es.<br/>
 * <br/>
 * <h5>Multiple {@link Access}es for single property:</h5> <br/>
 * If a single instance implements several {@link Access} interfaces, the method
 * {@link #setMultiAccesses(Object, Access)} can be used to add all of them at
 * once. Similarly, the method {@link #removeMultiAccesses(Object)} allows to
 * remove all the {@link Access}es associated to a given ID, independently on
 * how they have been added.<br/>
 * <br/>
 * <h5>Multiple {@link Access}es on multiple properties:</h5> <br/>
 * Finally, for massive management, the methods {@link #setAllAccesses(Map)} and
 * {@link #removeAllAccesses(Collection)} can be used, with an additional
 * {@link #clear()} method to remove all the current {@link Access}es on all the
 * managed properties.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class SimpleAccessProvider implements AccessProvider {

	private final Map<Object, PullReadAccess<?>> pullers = new HashMap<Object, PullReadAccess<?>>();
	private final Map<Object, PushReadAccess<?>> pushers = new HashMap<Object, PushReadAccess<?>>();
	private final Map<Object, WriteAccess<?>> writers = new HashMap<Object, WriteAccess<?>>();
	private final Collection<Object> ids = new HashSet<Object>();

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
	 *            the {@link Access}es to use in this {@link AccessProvider}
	 */
	public SimpleAccessProvider(Map<Object, Access<?>> accesses) {
		setAllAccesses(accesses);
	}

	/**
	 * Create a {@link SimpleAccessProvider} which reuses all the current
	 * {@link Access}es of an already existing {@link AccessProvider}. Notice
	 * that if the {@link AccessProvider} used in argument keep the same
	 * {@link Access} instances, any modification done to these instances, e.g.
	 * listener registration, will apply to the {@link Access} instances stored
	 * in this new {@link SimpleAccessProvider}, and vice-versa. However, if any
	 * of these two {@link AccessProvider} assign a new {@link Access} instance,
	 * the corresponding synchronization will be lost.
	 * 
	 * @param provider
	 *            the {@link AccessProvider} to copy
	 */
	public SimpleAccessProvider(AccessProvider provider) {
		for (Object id : provider.getIDs()) {
			setPullAccess(id, provider.getPullAccess(id));
			setPushAccess(id, provider.getPushAccess(id));
			setWriteAccess(id, provider.getWriteAccess(id));
		}
	}

	/**
	 * Return all the IDs having at least one {@link Access} instance set up.
	 * Please, use the corresponding getX methods to know which {@link Access}es
	 * are available for a given ID.
	 */
	@Override
	public Collection<Object> getIDs() {
		return ids;
	}

	/**
	 * This method assign a given {@link PullReadAccess} to a given property. If
	 * the property is unknown, it is added, otherwise only the {@link Access}
	 * is changed to the new one.
	 * 
	 * @param id
	 *            the ID of a property
	 * @param puller
	 *            the {@link PullReadAccess} to this property
	 */
	public void setPullAccess(Object id, PullReadAccess<?> puller) {
		pullers.put(id, puller);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PullReadAccess<T> getPullAccess(Object id) {
		return (PullReadAccess<T>) pullers.get(id);
	}

	/**
	 * This method removes any {@link PullReadAccess} assigned to a given
	 * property. If the ID is unknown, it has no effect, otherwise the
	 * {@link Access} is removed and, if the ID has no other {@link Access}, it
	 * is forgotten.
	 * 
	 * @param id
	 *            the ID of the property to remove the {@link PullReadAccess}
	 *            from
	 */
	public void removePullAccess(Object id) {
		pullers.remove(id);
		if (pushers.containsKey(id) || writers.containsKey(id)) {
			// ID still used
		} else {
			ids.remove(id);
		}
	}

	/**
	 * This method assign a given {@link PushReadAccess} to a given property. If
	 * the ID is unknown, it is added, otherwise only the {@link Access} is
	 * changed to the new one.
	 * 
	 * @param id
	 *            the ID of a property
	 * @param pusher
	 *            the {@link PushReadAccess} to this property
	 */
	public void setPushAccess(Object id, PushReadAccess<?> pusher) {
		pushers.put(id, pusher);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PushReadAccess<T> getPushAccess(Object id) {
		return (PushReadAccess<T>) pushers.get(id);
	}

	/**
	 * This method removes any {@link PushReadAccess} assigned to a given
	 * property. If the ID is unknown, it has no effect, otherwise the
	 * {@link Access} is removed and, if the ID has no other {@link Access}, it
	 * is forgotten.
	 * 
	 * @param id
	 *            the ID of the property to remove the {@link PushReadAccess}
	 *            from
	 */
	public void removePushAccess(Object id) {
		pushers.remove(id);
		if (pullers.containsKey(id) || writers.containsKey(id)) {
			// ID still used
		} else {
			ids.remove(id);
		}
	}

	/**
	 * This method assign a given {@link WriteAccess} to a given property. If
	 * the ID is unknown, it is added, otherwise the only {@link Access} is
	 * changed to the new one.
	 * 
	 * @param id
	 *            the ID of a property
	 * @param writer
	 *            the {@link WriteAccess} to this property
	 */
	public void setWriteAccess(Object id, WriteAccess<?> writer) {
		writers.put(id, writer);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> WriteAccess<T> getWriteAccess(Object id) {
		return (WriteAccess<T>) writers.get(id);
	}

	/**
	 * This method removes any {@link WriteAccess} assigned to a given property.
	 * If the ID is unknown, it has no effect, otherwise the {@link Access} is
	 * removed and, if the ID has no other {@link Access}, it is forgotten.
	 * 
	 * @param id
	 *            the ID of the property to remove the {@link WriteAccess} from
	 */
	public void removeWriteAccess(Object id) {
		writers.remove(id);
		if (pullers.containsKey(id) || pushers.containsKey(id)) {
			// ID still used
		} else {
			ids.remove(id);
		}
	}

	/**
	 * This method assign all the available {@link Access}es to a given property
	 * based on the implemented interfaces. If the ID is unknown, it is added,
	 * otherwise only the {@link Access}es are changed to the new ones.
	 * 
	 * @param id
	 *            the ID of a property
	 * @param accesses
	 *            the instance implementing the {@link Access} interfaces
	 */
	public void setMultiAccesses(Object id, Access<?> accesses) {
		if (accesses instanceof PullReadAccess<?>) {
			setPullAccess(id, (PullReadAccess<?>) accesses);
		}
		if (accesses instanceof PushReadAccess<?>) {
			setPushAccess(id, (PushReadAccess<?>) accesses);
		}
		if (accesses instanceof PullReadAccess<?>) {
			setWriteAccess(id, (WriteAccess<?>) accesses);
		}
	}

	/**
	 * This method removes all the {@link Access}es assigned to a given
	 * property. If the ID is unknown, it has no effect, otherwise the
	 * {@link Access}es are removed and the ID is forgotten.
	 * 
	 * @param id
	 *            the ID of the property to remove the {@link Access}es from
	 */
	public void removeMultiAccesses(Object id) {
		pullers.remove(id);
		pushers.remove(id);
		writers.remove(id);
		ids.remove(id);
	}

	/**
	 * This method is equivalent to using
	 * {@link #setMultiAccesses(Object, Access)} for several properties.
	 * 
	 * @param accesses
	 *            the map telling which {@link Access}es to assign to which IDs
	 */
	public void setAllAccesses(Map<Object, ? extends Access<?>> accesses) {
		for (Entry<Object, ? extends Access<?>> entry : accesses.entrySet()) {
			setMultiAccesses(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * This method is equivalent to using {@link #removeMultiAccesses(Object)}
	 * on several properties.
	 * 
	 * @param ids
	 *            the IDs of the {@link Access}es to remove
	 */
	public void removeAllAccesses(Collection<Object> ids) {
		for (Object id : ids) {
			removeMultiAccesses(id);
		}
	}

	/**
	 * This method is equivalent to using {@link #removeAllAccesses(Collection)}
	 * on all the IDs currently managed. No more ID should be returned by
	 * {@link #getIDs()} and only <code>null</code> values should be returned
	 * for {@link Access}es after the execution of this method.
	 */
	public void clear() {
		pullers.clear();
		pushers.clear();
		writers.clear();
		ids.clear();
	}
}
