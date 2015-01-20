package fr.vergne.access;

import java.util.Collection;

import fr.vergne.access.impl.SimpleAccessProvider;

/**
 * An {@link AccessProvider} manages accesses among one or several entities,
 * each entity being typically associated to a specific property of a given
 * object (but not always, some could be associated to data generated on the
 * fly). The different entities are identified through a unique ID which is
 * provided in {@link #getIDs()}. Depending on the ID, the
 * {@link AccessProvider} can provide one or several accesses to this entity
 * with the corresponding methods. If a given type of access is managed for a
 * given ID, the method returns the corresponding instance, otherwise it returns
 * <code>null</code> (also if the ID is simply unknown).<br/>
 * <br/>
 * If the {@link AccessProvider} contains a single ID, one could prefer to use a
 * single instance which implements all the needed accesses for simplicity, but
 * another could prefer to use an {@link AccessProvider} to exploit additional
 * features. For example, {@link SimpleAccessProvider} allows to change the
 * accesses on demand, while others could offer other facilities.<br/>
 * <br/>
 * Notice that different IDs do not correspond necessarily to accesses on
 * different {@link Object}s. For simple {@link Object}s, it could be than no
 * more than one ID is necessary, but for others, several properties on the same
 * {@link Object} could be represented each with its own ID. For a collection,
 * one could have a different ID for each index, another for the size of the
 * collection and other IDs for other properties. It is also possible to have
 * IDs associated to data generated on the fly without having a specific
 * {@link Object} supporting it, for instance having a {@link Writable} which
 * allows to instantiate a logger and write a custom message on it. The logger
 * would then be finalized by the garbage collector, thus having no specific
 * {@link Object} alive, but still having a working {@link Writable}.<br/>
 * <br/>
 * Requesting the same access for the same ID should preferably return the same
 * access instance for performance purpose, but it is not mandatory and it could
 * evolve depending on the purpose of the {@link AccessProvider}. Similarly,
 * requesting different accesses for the same ID could be managed by having a
 * single instance implementing all the needed interfaces or by having one
 * instance per interface, but the recommendation go towards efficient
 * performances. However, it has to be noticed that none of these
 * recommendations are constraints. Please refer to the documentation of the
 * specific implementation you use for further details.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface AccessProvider {

	/**
	 * If at least one of the getX methods return an access instance (not
	 * <code>null</code>) for a given ID, then this ID should be returned by
	 * this method. However, it is not because an ID is returned by this method
	 * that it necessarily has some access instances (all getX methods could
	 * return <code>null</code> for this ID). Thus, this method offers a
	 * convenient way to retrieve all the available accesses but it should be
	 * always checked which one return <code>null</code> for confirmation.
	 * 
	 * @return the IDs managed by this {@link AccessProvider}
	 */
	public Collection<Object> getIDs();

	/**
	 * 
	 * @param id
	 *            the ID of the entity to access to
	 * @return a {@link PullReadable} giving access to this entity,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PullReadable<T> getPullAccess(Object id);

	/**
	 * 
	 * @param id
	 *            the ID of the entity to access to
	 * @return a {@link PushReadable} giving access to this entity,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PushReadable<T> getPushAccess(Object id);

	/**
	 * 
	 * @param id
	 *            the ID of the entity to access to
	 * @return a {@link Writable} giving access to this entity,
	 *         <code>null</code> if it is not managed
	 */
	public <T> Writable<T> getWriteAccess(Object id);
}
