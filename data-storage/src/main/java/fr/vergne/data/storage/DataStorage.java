package fr.vergne.data.storage;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A {@link DataStorage} aims at providing an access to a set of values. Each
 * value is identified by a {@link Key}. If the {@link Key} is unknown or no
 * value is mapped to it, <code>null</code> is returned. Their is no difference
 * between an unknown {@link Key} and a known {@link Key} with a
 * <code>null</code> value: both relates to no value mapped to the specific
 * {@link Key}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Key>
 */
public interface DataStorage<Key> extends Iterable<Entry<Key, Object>> {

	/**
	 * This method should return all the {@link Key}s which are mapped to some
	 * values within this {@link DataStorage}. However, extra {@link Key}s
	 * (which are not mapped to any value) can also be returned. Don't rely on
	 * this method to know which keys are mapped to non-<code>null</code>
	 * values.
	 * 
	 * @return a set of {@link Key}s which can be requested to this
	 *         {@link DataStorage}
	 */
	public Set<Key> getKeys();

	/**
	 * 
	 * @param key
	 *            the {@link Key} to check
	 * @return the value assigned to this {@link Key}, <code>null</code> if
	 *         there is no value
	 */
	public Object get(Key key);

	/**
	 * This method allows to retrieve easily a set of values. The returned
	 * {@link List} follow the same order than the {@link List} provided in
	 * argument, such that the value at index <code>i</code> in the returned
	 * {@link List} is the value assigned to the {@link Key} at index
	 * <code>i</code> in the argument {@link List}. If no value is assigned to a
	 * given {@link Key} , <code>null</code> will be used at its index.
	 * 
	 * @param keys
	 *            the {@link Key}s to check
	 * @return the corresponding {@link List} of values
	 */
	public List<Object> getAll(List<? extends Key> keys);

}
