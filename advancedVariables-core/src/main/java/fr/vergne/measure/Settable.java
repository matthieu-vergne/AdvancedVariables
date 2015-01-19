package fr.vergne.measure;

/**
 * A {@link Settable} entity is an entity which can be re-affected directly.
 * This is the typical equivalent of "a = b", but here written "a.set(b)".
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public interface Settable<T> {

	/**
	 * 
	 * @param value
	 *            the new value of the entity
	 */
	public void set(T value);
}
