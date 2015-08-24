package fr.vergne.data.access.impl.advanced;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import fr.vergne.data.access.ActiveReadAccess;
import fr.vergne.data.access.PassiveReadAccess;
import fr.vergne.data.access.PassiveWriteAccess;
import fr.vergne.data.access.PropertyAccess;
import fr.vergne.data.access.util.AccessFactory;

/**
 * A {@link LastInstantComputer} aims at reducing computation time by computing
 * an output value at the last moment. In order to do so, the values required
 * for the inputs to be computed should have a source registered. When the
 * {@link #get()} method of this {@link LastInstantComputer} is called, a check
 * is made to know if any input has changed since the last call. If at least one
 * input has changed (or if no computation has been made yet), then the output
 * is recomputed, otherwise its last value is reused.<br/>
 * <br/>
 * The sources registered are {@link ActiveReadAccess} sources, meaning that we
 * need to manually check each input before to know whether or not the output
 * should be recomputed. However, it is also possible to register other types of
 * sources (cf. the different setSource() methods) but these sources will be
 * transformed into {@link ActiveReadAccess} sources before to be registered.
 * Thus, when you call {@link #getSource(Object)}, you always get the
 * {@link ActiveReadAccess} version of the source, not necessarily the instance
 * you registered. If you want to know which {@link ActiveReadAccess}
 * corresponds to the source you have registered, the setSource() method returns
 * it.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 */
public class LastInstantComputer<Value> implements ActiveReadAccess<Value> {

	private static final Logger logger = Logger
			.getLogger(LastInstantComputer.class.getName());

	/**
	 * The sources to use to get the inputs to compute.
	 */
	private Map<Object, ActiveReadAccess<?>> sources = new HashMap<Object, ActiveReadAccess<?>>();
	/**
	 * The inputs used in the last computation. If <code>null</code>, it means
	 * that the computation should be made.
	 */
	private Map<Object, Object> lastInputs = null;
	/**
	 * The {@link Computer} used to compute the returned value of {@link #get()}
	 * .
	 */
	private Computer<Value> lastComputer = null;
	/**
	 * The {@link Computer} to use for the next computation. .
	 */
	private Computer<Value> nextComputer = null;
	/**
	 * The last value computed based on {@link #lastInputs} and
	 * {@link #nextComputer}.
	 */
	private Value lastOutput;
	/**
	 * The {@link AccessFactory} used to transform the inadapted sources into
	 * {@link ActiveReadAccess} sources.
	 */
	private static final AccessFactory FACTORY = new AccessFactory();

	/**
	 * Create a {@link LastInstantComputer} without any {@link Computer} (
	 * <code>null</code>). Be sure to set a {@link Computer} before to call its
	 * {@link #get()} method.
	 */
	public LastInstantComputer() {
		this(null);
	}

	/**
	 * Create a {@link LastInstantComputer} with a given {@link Computer}.
	 * 
	 * @param computer
	 *            the {@link Computer} to use
	 */
	public LastInstantComputer(Computer<Value> computer) {
		this(computer, Collections.<Object, PropertyAccess<?>> emptyMap()
				.entrySet());
	}

	/**
	 * Create a {@link LastInstantComputer} with a given {@link Computer} and
	 * set of input sources.
	 * 
	 * @param computer
	 *            the {@link Computer} to use
	 * @param sources
	 *            the sources to use, each identified by a given key
	 * @throws IllegalArgumentException
	 *             if a source cannot be registered.
	 */
	public LastInstantComputer(Computer<Value> computer,
			Iterable<Entry<Object, PropertyAccess<?>>> sources) {
		setComputer(computer);
		setAllSources(sources);
	}

	/**
	 * Registers a source to provide the values of a given input. When
	 * {@link #get()} is called, if the value returned by this source has
	 * changed since the last computation, the computation will be done in order
	 * to update the output value before to return it. If no source has changed
	 * compared to the last computation, then the output value is not recomputed
	 * and {@link #get()} directly returns the same value than its last call.<br/>
	 * <br/>
	 * Notice that the check is made on the input value, not the source itself.
	 * So if a different source is registered for the same key, as long as it
	 * returns the same value than the previous source it will not lead to a
	 * re-computation (unless other changes justify it).
	 * 
	 * @param key
	 *            the key identifying the input
	 * @param source
	 *            the source of value for this input
	 * @throws NullPointerException
	 *             if the source is <code>null</code>
	 */
	public void setSource(Object key, ActiveReadAccess<?> source) {
		if (source == null) {
			throw new NullPointerException("No source provided.");
		} else {
			sources.put(key, source);
		}
	}

	/**
	 * This method is a shortcut to {@link #setSource(Object, ActiveReadAccess)}
	 * . It builds an {@link ActiveReadAccess} based on the
	 * {@link PassiveReadAccess} given and register the former. If you already
	 * have an {@link ActiveReadAccess} available, it is recommended to use it
	 * unless you have reasons to not do so.
	 * 
	 * @param key
	 *            the key identifying the input
	 * @param source
	 *            the source of value for this input
	 * @return the {@link ActiveReadAccess} built on this source and registered
	 *         in this {@link LastInstantComputer}
	 * @throws NullPointerException
	 *             if the source is <code>null</code>
	 */
	public ActiveReadAccess<?> setSource(Object key, PassiveReadAccess<?> source) {
		if (source == null) {
			throw new NullPointerException("No source provided.");
		} else {
			ActiveReadAccess<?> wrapper = FACTORY
					.createActiveReadFromPassiveRead(source, null);
			setSource(key, wrapper);
			return wrapper;
		}
	}

	/**
	 * This method is a shortcut to {@link #setSource(Object, ActiveReadAccess)}
	 * . It builds an {@link ActiveReadAccess} based on the
	 * {@link PassiveWriteAccess} given and register the former. If you already
	 * have an {@link ActiveReadAccess} available, it is recommended to use it
	 * unless you have reasons to not do so.
	 * 
	 * @param key
	 *            the key identifying the input
	 * @param source
	 *            the source of value for this input
	 * @return the {@link ActiveReadAccess} built on this source and registered
	 *         in this {@link LastInstantComputer}
	 * @throws NullPointerException
	 *             if the source is <code>null</code>
	 */
	public ActiveReadAccess<?> setSource(Object key,
			PassiveWriteAccess<?> source) {
		if (source == null) {
			throw new NullPointerException("No source provided.");
		} else {
			ActiveReadAccess<?> wrapper = FACTORY
					.createActiveReadFromPassiveWrite(source);
			setSource(key, wrapper);
			return wrapper;
		}
	}

	/**
	 * Provide the source registered to this {@link LastInstantComputer} for the
	 * specified input. If you registered a source which was not an
	 * {@link ActiveReadAccess}, then this method returns the
	 * {@link ActiveReadAccess} built on it, not the original source. Please
	 * check the setSource() methods used to get the {@link ActiveReadAccess}
	 * built on your original source.
	 * 
	 * @param key
	 *            the key identifying the input
	 * @return the source of value for this input
	 */
	public ActiveReadAccess<?> getSource(Object key) {
		return sources.get(key);
	}

	/**
	 * Register a whole set of sources with the provided keys. If a source has
	 * an {@link ActiveReadAccess}, this is the one used in priority. Otherwise,
	 * it registers its {@link PassiveReadAccess} or, if not available, its
	 * {@link PassiveWriteAccess}. Any other {@link PropertyAccess} is not
	 * managed and will throw an {@link IllegalArgumentException}.<br/>
	 * <br/>
	 * Notice that if a source does not have an {@link ActiveReadAccess}, one
	 * will be built and registered in place of the original source. If you want
	 * to get the {@link ActiveReadAccess} corresponding to this source, call
	 * {@link #getSource(Object)} on the corresponding key.
	 * 
	 * @param sources
	 *            the sources to register as inputs
	 */
	public void setAllSources(Iterable<Entry<Object, PropertyAccess<?>>> sources) {
		for (Entry<Object, PropertyAccess<?>> entry : sources) {
			Object key = entry.getKey();
			PropertyAccess<?> source = entry.getValue();
			if (source instanceof ActiveReadAccess) {
				setSource(key, (ActiveReadAccess<?>) source);
			} else if (source instanceof PassiveReadAccess) {
				setSource(key, (PassiveReadAccess<?>) source);
			} else if (source instanceof PassiveWriteAccess) {
				setSource(key, (PassiveWriteAccess<?>) source);
			} else {
				throw new IllegalArgumentException(
						"The source identified with " + key
								+ " cannot be registered: " + source);
			}
		}
	}

	/**
	 * 
	 * @return all the registered sources
	 */
	public Map<Object, ActiveReadAccess<?>> getAllSources() {
		return Collections.unmodifiableMap(sources);
	}

	/**
	 * This method unregisters an input. It implies that its value won't be
	 * provided anymore to the {@link Computer}. If the value of this source was
	 * not <code>null</code> during the last computation of the output value, it
	 * will be considered as an input change, thus leading to a re-computation
	 * if {@link #get()} is called. If a source is set before to call
	 * {@link #get()} and its value corresponds to the last value computed, then
	 * the removal will have no effect and the computation will not be remade.
	 * 
	 * @param key
	 *            the key identifying the input to remove
	 */
	public void removeSource(Object key) {
		sources.remove(key);
	}

	/**
	 * When you change the {@link Computer}, we assume that the value must be
	 * recomputed. The {@link Computer} is considered to be different based on
	 * {@link Computer#equals(Object)}, not just because it is a different
	 * instance. If you change back to the old {@link Computer} before the
	 * output value is recomputed, it will be as if you did not change the
	 * {@link Computer}.<br/>
	 * <br/>
	 * <b>ATTENTION:</b> the only valid {@link Computer}s are the deterministic
	 * ones. If we provide the same inputs, it should return the same output. If
	 * you want to use an undeterministic {@link Computer}, then you should
	 * trick the {@link LastInstantComputer} to force it to recompute the value
	 * each time you call the {@link #get()} method, for instance by registering
	 * a source which always provide a different value.
	 * 
	 * @param computer
	 *            the {@link Computer} to use in order to compute the output
	 *            value returned by {@link #get()}
	 */
	// TODO manage undeterministic computers
	public void setComputer(Computer<Value> computer) {
		this.nextComputer = computer;
	}

	/**
	 * 
	 * @return the {@link Computer} used in order to compute the output value
	 *         returned by {@link #get()}
	 */
	public Computer<Value> getComputer() {
		return nextComputer;
	}

	/**
	 * Return the output value computed by this {@link LastInstantComputer}. If
	 * the computation has already been made due to a previous call and nothing
	 * have changed since the last computation (i.e. we should get the same
	 * result), the value already returned during the last call is returned
	 * again, without recomputing it.
	 */
	@Override
	public Value get() {
		if (nextComputer == null) {
			throw new IllegalStateException("No computer set.");
		} else {
			// can compute
		}

		boolean requireComputation = false;

		logger.fine("Cheking first computation...");
		if (lastInputs == null) {
			// never computed before, need to compute it
			lastInputs = new HashMap<Object, Object>();
			requireComputation = true;
		} else {
			// do not lead to re-computation
		}
		logger.fine("Recompute? " + requireComputation);

		logger.fine("Cheking different computer...");
		if (!nextComputer.equals(lastComputer)) {
			// same inputs could imply a different output, need to compute it
			requireComputation = true;
			/*
			 * The assignment is done out of the condition because it should be
			 * done also if the equal() returns true but not == (equivalent but
			 * not the same).
			 */
		} else {
			// do not lead to re-computation
		}
		lastComputer = nextComputer;
		logger.fine("Recompute? " + requireComputation);

		logger.fine("Cheking input updates...");
		for (Entry<Object, ActiveReadAccess<?>> entry : sources.entrySet()) {
			Object key = entry.getKey();
			ActiveReadAccess<?> source = entry.getValue();
			Object lastValue = lastInputs.get(key);
			Object newValue = source.get();
			if (same(lastValue, newValue)) {
				// do not lead to re-computation
			} else {
				logger.finest("New value for " + key + ": " + lastValue
						+ " -> " + newValue);
				lastInputs.put(key, newValue);
				requireComputation = true;
			}
		}
		logger.fine("Recompute? " + requireComputation);

		logger.fine("Cheking inputs removal...");
		if (!requireComputation) {
			Collection<Object> removedKeys = new LinkedList<Object>(
					lastInputs.keySet());
			removedKeys.removeAll(sources.keySet());
			logger.fine("Removed keys: " + removedKeys);
			if (removedKeys.isEmpty()) {
				// do not lead to re-computation
			} else {
				requireComputation = !wereAllNullInputs(removedKeys);
			}
		} else {
			// computation already required, don't make further checks
		}
		logger.fine("Recompute? " + requireComputation);

		if (requireComputation) {
			lastInputs.keySet().retainAll(sources.keySet());
			logger.info("Computing with: " + lastInputs);
			lastOutput = lastComputer.compute(lastInputs);
			logger.info("New output: " + lastOutput);
		} else {
			logger.info("No need to recompute, return the old output: "
					+ lastOutput);
		}
		return lastOutput;
	}

	private boolean wereAllNullInputs(Collection<Object> keys) {
		for (Object key : keys) {
			Object value = lastInputs.get(key);
			logger.finest("Last input for " + key + ": " + value);
			if (value == null) {
				// still null
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean same(Object a, Object b) {
		return a == b || a != null && a.equals(b);
	}

	/**
	 * A {@link Computer} aims at computing the output value of a
	 * {@link LastInstantComputer} based on some input values provided by the
	 * sources registered on this {@link LastInstantComputer}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <Value>
	 */
	public static interface Computer<Value> {
		/**
		 * This method should return the output value which corresponds to the
		 * given input values. Each input value is identified by a key so that
		 * you can identify them. The key of an input is the same than the
		 * source which has provided this input.
		 * 
		 * @param inputs
		 *            the input values, identified by their keys
		 * @return the output value corresponding to these inputs
		 */
		public Value compute(Map<Object, Object> inputs);
	}
}
