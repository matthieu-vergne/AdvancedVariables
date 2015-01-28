package fr.vergne.data.storage;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link ModifiableStorage} is a {@link DataStorage} which provides some
 * features to modify its content.<br/>
 * <br/>
 * An implementation of this interface is close to an implementation of a
 * {@link Map}, but with some significant differences:
 * <ul>
 * <li>The terminology is different, especially the correspondence between
 * {@link Map#put(Object, Object)} and {@link #set(Object, Object)} (and similar
 * methods).</li>
 * <li>The old value is not returned when modified, so you should use
 * {@link #get(Object)} before or rely on an implementation of
 * {@link ReactiveStorage}.</li>
 * <li>No basic method like {@link Map#size()} or {@link Map#isEmpty()}, which
 * can already be computed based on the available methods. If such features are
 * of interest, they can be implemented in specific implementations though.</li>
 * </ul>
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Key>
 */
public interface ModifiableStorage<Key> extends DataStorage<Key> {

	/**
	 * This method assign a specific value to a given {@link Key}. If no
	 * value is provided (<code>null</code>) it is equivalent to
	 * {@link #remove(Object)}. In such a case, it is recommended to use
	 * {@link #remove(Object)} which can exploit the explicit reference to a
	 * specific case (set <code>null</code> to a {@link Key}) to optimize it.
	 * Please refer to the specific implementation you use to know about the
	 * difference between these two methods.
	 * 
	 * @param key
	 *            the {@link Key} to modify
	 * @param value
	 *            the new value to assign to this {@link Key}
	 */
	public void set(Key key, Object value);

	/**
	 * This method is theoretically equivalent to the use of
	 * {@link #set(Object, Object)} with a <code>null</code> value, because a
	 * <code>null</code> value represent both unknown keys and keys mapped to
	 * null. However, the specific implementation could react differently. This
	 * could have some effect on the performance and on other methods which are
	 * not much constrained, like {@link #getKeys()}. This method provides the
	 * advantage to request explicitly a removal/<code>null</code> value, thus it
	 * is recommended to use it in such cases.
	 * 
	 * @param key
	 *            the {@link Key} to remove
	 */
	public void remove(Key key);

	/**
	 * This method is the massive version of {@link #set(Object, Object)}. It is
	 * particularly suited for {@link Map}s (by providing their
	 * {@link Map#entrySet()}) and {@link DataStorage}s.
	 * 
	 * @param entries
	 *            the set of {@link Key}s to change and the values to assign to
	 *            each of them
	 */
	public void setAll(Iterable<? extends Entry<? extends Key, ? extends Object>> entries);

	/**
	 * This method is the massive version of {@link #remove(Object)}.
	 * 
	 * @param keys
	 *            the {@link Key}s to remove
	 */
	public void removeAll(Collection<? extends Key> keys);

	/**
	 * This method remove all the values currently contained in this
	 * {@link DataStorage}. It is equivalent to {@link #removeAll(Collection)}
	 * to which we provide the result of {@link #getKeys()} as argument. In the
	 * case where you plan to remove all the {@link Key}s, it is recommended to
	 * use this method because it can exploit some optimization due to the
	 * specific case it deals with. Please refer to the specific implementation
	 * you use to know about the differences.
	 */
	public void clear();

}
