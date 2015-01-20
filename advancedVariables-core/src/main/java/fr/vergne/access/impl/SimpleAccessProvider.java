package fr.vergne.access.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import fr.vergne.access.AccessProvider;
import fr.vergne.access.Accessable;
import fr.vergne.access.PullReadable;
import fr.vergne.access.PushReadable;
import fr.vergne.access.Writable;

/**
 * This {@link SimpleAccessProvider} allows to manage a set of IDs and their
 * accesses in a simple way. It should be satisfying for any basic use of
 * {@link AccessProvider}.<br/>
 * <br/>
 * <h5>Single access for single instance:</h5> <br/>
 * Additionally to the getX methods to request the corresponding access on a
 * given entity, this {@link AccessProvider} implements the corresponding setX
 * and removeX methods to add and remove the corresponding accesses.<br/>
 * <br/>
 * <h5>Multiple accesses for single instance:</h5> <br/>
 * If a single instance implements several {@link Accessable} interfaces, the
 * method {@link #setMultiAccesses(Object, Accessable)} can be used to add all
 * of them at once. Similarly, the method {@link #removeMultiAccesses(Object)}
 * allows to remove all the accesses associated to a given ID, independently on
 * how they have been added.<br/>
 * <br/>
 * <h5>Multiple accesses on multiple instances:</h5> <br/>
 * Finally, for massive management, the methods {@link #setAllAccesses(Map)} and
 * {@link #removeAllAccesses(Collection)} can be used, with an additional
 * {@link #clear()} method to remove all the current accesses on all the current
 * IDs.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class SimpleAccessProvider implements AccessProvider {

	private final Map<Object, PullReadable<?>> pullers = new HashMap<Object, PullReadable<?>>();
	private final Map<Object, PushReadable<?>> pushers = new HashMap<Object, PushReadable<?>>();
	private final Map<Object, Writable<?>> writers = new HashMap<Object, Writable<?>>();
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
	 * @param accessables
	 */
	public SimpleAccessProvider(Map<Object, Accessable> accessables) {
		setAllAccesses(accessables);
	}

	/**
	 * Return all the IDs having at least one access set. Please, use the
	 * corresponding getX methods to know which accesses are available for the
	 * given ID.
	 */
	@Override
	public Collection<Object> getIDs() {
		return ids;
	}

	/**
	 * This method assign a given {@link PullReadable} access to a given entity.
	 * If the ID is unknown, it is added, otherwise the access is changed to the
	 * new one.
	 * 
	 * @param id
	 *            the ID of an entity
	 * @param puller
	 *            the {@link PullReadable} access to this entity
	 */
	public void setPullAccess(Object id, PullReadable<?> puller) {
		pullers.put(id, puller);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PullReadable<T> getPullAccess(Object id) {
		return (PullReadable<T>) pullers.get(id);
	}

	/**
	 * This method removes any {@link PullReadable} access assigned to a given
	 * entity. If the ID is unknown, it has no effect, otherwise the access is
	 * removed and, if the ID has no other access, it is forgotten.
	 * 
	 * @param id
	 *            the ID of the entity to remove the {@link PullReadable} access
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
	 * This method assign a given {@link PushReadable} access to a given entity.
	 * If the ID is unknown, it is added, otherwise the access is changed to the
	 * new one.
	 * 
	 * @param id
	 *            the ID of an entity
	 * @param pusher
	 *            the {@link PushReadable} access to this entity
	 */
	public void setPushAccess(Object id, PushReadable<?> pusher) {
		pushers.put(id, pusher);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PushReadable<T> getPushAccess(Object id) {
		return (PushReadable<T>) pushers.get(id);
	}

	/**
	 * This method removes any {@link PushReadable} access assigned to a given
	 * entity. If the ID is unknown, it has no effect, otherwise the access is
	 * removed and, if the ID has no other access, it is forgotten.
	 * 
	 * @param id
	 *            the ID of the entity to remove the {@link PushReadable} access
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
	 * This method assign a given {@link Writable} access to a given entity. If
	 * the ID is unknown, it is added, otherwise the access is changed to the
	 * new one.
	 * 
	 * @param id
	 *            the ID of an entity
	 * @param writer
	 *            the {@link Writable} access to this entity
	 */
	public void setWriteAccess(Object id, Writable<?> writer) {
		writers.put(id, writer);
		ids.add(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Writable<T> getWriteAccess(Object id) {
		return (Writable<T>) writers.get(id);
	}

	/**
	 * This method removes any {@link Writable} access assigned to a given
	 * entity. If the ID is unknown, it has no effect, otherwise the access is
	 * removed and, if the ID has no other access, it is forgotten.
	 * 
	 * @param id
	 *            the ID of the entity to remove the {@link Writable} access
	 *            from
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
	 * This method assign all the available accesses to a given entity based on
	 * the implemented interfaces. If the ID is unknown, it is added, otherwise
	 * the access is changed to the new one.
	 * 
	 * @param id
	 *            the ID of an entity
	 * @param accessable
	 *            the instance implementing the {@link Accessable} interfaces
	 */
	public void setMultiAccesses(Object id, Accessable accessable) {
		if (accessable instanceof PullReadable<?>) {
			setPullAccess(id, (PullReadable<?>) accessable);
		}
		if (accessable instanceof PushReadable<?>) {
			setPushAccess(id, (PushReadable<?>) accessable);
		}
		if (accessable instanceof PullReadable<?>) {
			setWriteAccess(id, (Writable<?>) accessable);
		}
	}

	/**
	 * This method removes all the access assigned to a given entity. If the ID
	 * is unknown, it has no effect, otherwise the accesses are removed and the
	 * ID is forgotten.
	 * 
	 * @param id
	 *            the ID of the entity to remove the accesses from
	 */
	public void removeMultiAccesses(Object id) {
		pullers.remove(id);
		pushers.remove(id);
		writers.remove(id);
		ids.remove(id);
	}

	/**
	 * This method is equivalent to using
	 * {@link #setMultiAccesses(Object, Accessable)} for several entities.
	 * 
	 * @param accesses
	 *            the map telling which {@link Accessable}s to assign to which
	 *            ids
	 */
	public void setAllAccesses(Map<Object, Accessable> accesses) {
		for (Entry<Object, Accessable> entry : accesses.entrySet()) {
			setMultiAccesses(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * This method is equivalent to using {@link #removeMultiAccesses(Object)}
	 * on several entities.
	 * 
	 * @param ids
	 *            the IDs of the accesses to remove
	 */
	public void removeAllAccesses(Collection<Object> ids) {
		for (Object id : ids) {
			removeMultiAccesses(id);
		}
	}

	/**
	 * This method is equivalent to using {@link #removeAllAccesses(Collection)}
	 * on all the IDs currently stored.
	 */
	public void clear() {
		pullers.clear();
		pushers.clear();
		writers.clear();
		ids.clear();
	}
}
