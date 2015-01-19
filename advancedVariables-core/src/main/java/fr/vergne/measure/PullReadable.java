package fr.vergne.measure;

/**
 * A {@link PullReadable} entity is an entity for which we can get the value on
 * a "pull" basis: any external entity can request the value of this one at any
 * time. Implementing this interface implies that the entity should be able to
 * provide a value at any time.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 * @see PushReadable
 */
public interface PullReadable<T> {
	/**
	 * 
	 * @return the current value of the entity
	 */
	public T get();
}
