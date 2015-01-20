package fr.vergne.access;

import java.util.Collection;

import fr.vergne.access.impl.SimpleAccessProvider;

/**
 * An {@link AccessProvider} manages {@link Access}es among one or several
 * properties. The different properties are identified through a unique ID which
 * is provided in {@link #getIDs()}. Depending on the ID, the
 * {@link AccessProvider} can provide one or several {@link Access}es to this
 * entity with the corresponding getX methods. If a given type of {@link Access}
 * is managed for a given ID, the method returns the corresponding
 * {@link Access} isntance, otherwise it returns <code>null</code> (also if the
 * ID is simply unknown).<br/>
 * <br/>
 * If the {@link AccessProvider} contains a single ID, one could prefer to use a
 * single instance which implements all the needed {@link Access} interfaces for
 * simplicity, but another could prefer to use an {@link AccessProvider} to
 * exploit additional features. For example, {@link SimpleAccessProvider} allows
 * to change the {@link Access}es on demand, while others could offer other
 * facilities.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface AccessProvider {

	/**
	 * This method is a facility to access the possible IDs that this
	 * {@link AccessProvider} can manage. If at least one of the getX methods
	 * return an {@link Access} instance (not <code>null</code>) for a given ID,
	 * then this ID should be returned by this method. However, it is not
	 * because an ID is returned by this method that it necessarily has some
	 * {@link Access} instances (all getX methods could return <code>null</code>
	 * for this ID). Thus, this method offers a convenient way to retrieve all
	 * the available {@link Access}es but it should be always checked which one
	 * return <code>null</code> for confirmation.
	 * 
	 * @return the IDs managed by this {@link AccessProvider}
	 */
	public Collection<Object> getIDs();

	/**
	 * 
	 * @param id
	 *            the ID of the property to access to
	 * @return a {@link PullReadAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PullReadAccess<T> getPullAccess(Object id);

	/**
	 * 
	 * @param id
	 *            the ID of the property to access to
	 * @return a {@link PushReadAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PushReadAccess<T> getPushAccess(Object id);

	/**
	 * 
	 * @param id
	 *            the ID of the property to access to
	 * @return a {@link WriteAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> WriteAccess<T> getWriteAccess(Object id);
}
