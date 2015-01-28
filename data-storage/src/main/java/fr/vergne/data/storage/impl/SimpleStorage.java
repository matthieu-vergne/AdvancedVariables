package fr.vergne.data.storage.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.vergne.data.storage.ModifiableStorage;
import fr.vergne.data.storage.ReactiveStorage;

/**
 * A {@link SimpleStorage} provide a basic implementation of
 * {@link ModifiableStorage}. In order to allow a maximal customization, it also
 * implements {@link ReactiveStorage}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Key>
 */
public class SimpleStorage<Key> implements ModifiableStorage<Key>,
		ReactiveStorage<Key> {

	private final Map<Key, Object> map = new HashMap<Key, Object>();
	private final Set<OperationListener<Key>> listeners = new HashSet<OperationListener<Key>>();

	@Override
	public Set<Key> getKeys() {
		return map.keySet();
	}

	@Override
	public Object get(Key key) {
		return map.get(key);
	}

	@Override
	public void set(Key key, Object value) {
		Object oldValue;
		if (value == null) {
			oldValue = map.remove(key);
		} else {
			oldValue = map.put(key, value);
		}
		for (OperationListener<Key> listener : listeners) {
			listener.entrySet(key, oldValue, value);
		}
	}

	@Override
	public void remove(Key key) {
		set(key, null);
	}

	@Override
	public List<Object> getAll(List<? extends Key> keys) {
		List<Object> values = new LinkedList<Object>();
		for (Key key : keys) {
			values.add(get(key));
		}
		return values;
	}

	@Override
	public void setAll(
			Iterable<? extends Entry<? extends Key, ? extends Object>> entries) {
		for (Entry<? extends Key, ? extends Object> entry : entries) {
			set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void removeAll(Collection<? extends Key> keys) {
		for (Key key : keys) {
			remove(key);
		}
	}

	@Override
	public Iterator<Entry<Key, Object>> iterator() {
		return map.entrySet().iterator();
	}

	@Override
	public void clear() {
		removeAll(new LinkedList<Key>(getKeys()));
	}

	@Override
	public Collection<OperationListener<Key>> getOperationListeners() {
		return listeners;
	}

	@Override
	public void addOperationListener(OperationListener<Key> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeOperationListener(OperationListener<Key> listener) {
		listeners.remove(listener);
	}
}
