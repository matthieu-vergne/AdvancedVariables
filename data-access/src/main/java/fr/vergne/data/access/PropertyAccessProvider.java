package fr.vergne.data.access;

import java.util.Collection;

import fr.vergne.data.access.impl.SimpleAccessProvider;

/**
 * A {@link PropertyAccessProvider} aims at managing {@link PropertyAccess}es
 * among one or several properties. The different properties are identified
 * through a unique key which is provided by {@link #getKeys()}. Depending on
 * the key, the {@link PropertyAccessProvider} can provide one or several
 * {@link PropertyAccess}es to this entity with the corresponding getX methods.
 * If a given type of {@link PropertyAccess} is managed for a given key, the
 * method returns the corresponding {@link PropertyAccess} instance, otherwise
 * it returns <code>null</code> (also if the key is simply unknown).<br/>
 * <br/>
 * If the {@link PropertyAccessProvider} contains a single key, one could prefer
 * to use a single instance which implements all the needed
 * {@link PropertyAccess} interfaces for simplicity, but another could prefer to
 * use a {@link PropertyAccessProvider} to exploit additional features. For
 * example, {@link SimpleAccessProvider} allows to change the
 * {@link PropertyAccess}es on demand, which could have some advantages even for
 * a single property.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface PropertyAccessProvider {

	/**
	 * This method is a facility to access the possible keys that this
	 * {@link PropertyAccessProvider} can manage. If at least one of the getX
	 * methods return an {@link PropertyAccess} instance (not <code>null</code>)
	 * for a given key, then this key should be returned by this method.
	 * However, it is not because a key is returned by this method that it
	 * necessarily has some {@link PropertyAccess} instances (all getX methods
	 * could return <code>null</code> for this key). Thus, this method offers a
	 * convenient way to retrieve all the available {@link PropertyAccess}es but
	 * it should be always checked which one return <code>null</code> for
	 * confirmation.
	 * 
	 * @return the IDs managed by this {@link PropertyAccessProvider}
	 */
	public Collection<Object> getKeys();

	/**
	 * @deprecated Prefer to use {@link #getKeys()}. Will be deleted in future
	 *             versions.
	 */
	// TODO remove in 2.0 version
	@Deprecated
	public Collection<Object> getIDs();

	/**
	 * 
	 * @param key
	 *            the key of the property to access to
	 * @return an {@link ActiveReadAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> ActiveReadAccess<T> getActiveReadAccess(Object key);

	/**
	 * 
	 * @param key
	 *            the key of the property to access to
	 * @return a {@link PassiveReadAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PassiveReadAccess<T> getPassiveReadAccess(Object key);

	/**
	 * 
	 * @param key
	 *            the key of the property to access to
	 * @return an {@link ActiveWriteAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> ActiveWriteAccess<T> getActiveWriteAccess(Object key);

	/**
	 * 
	 * @param key
	 *            the key of the property to access to
	 * @return a {@link PassiveWriteAccess} giving access to this property,
	 *         <code>null</code> if it is not managed
	 */
	public <T> PassiveWriteAccess<T> getPassiveWriteAccess(Object key);
}
