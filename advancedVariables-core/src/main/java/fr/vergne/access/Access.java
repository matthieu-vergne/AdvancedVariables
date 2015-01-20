package fr.vergne.access;

import java.io.File;
import java.util.List;

import fr.vergne.access.impl.ReadWriteProperty;
import fr.vergne.access.impl.SimpleAccessProvider;

/**
 * An {@link Access} is a specialized access provided on a property. A usual
 * example is an instance, or {@link Object}, which has several properties that
 * we want to access to in different ways. For example a {@link List} has a
 * given size, it has a given element associated to each index, etc. While the
 * {@link List} itself provides methods to interact with it, it is possible to
 * adapt this interaction for advanced needs. For example, one can use a
 * {@link WriteAccess} on the size property to modify the size directly, without
 * having the need to know about how to do it. In this case, the
 * {@link WriteAccess} could add a default value several time at the end of the
 * {@link List} or remove the last elements to fit with the requested size. This
 * way, an {@link Access} provides a customized way to interact with the
 * {@link Object}.<br/>
 * <br/>
 * However, while an {@link Access} does provide access to a property in a given
 * way, this property does not have to be supported by an existing
 * {@link Object}. For instance, one could consider the remaining space in a
 * folder as an interesting property, and create a {@link PullReadAccess} to
 * provide the access to this property without having to maintain a {@link File}
 * instance. It is then an implementation choice of the {@link PullReadAccess}
 * to decide to instantiate a {@link File} instance, which can then be
 * maintained or discarded, or to use any other method to measure the remaining
 * space in the folder. The {@link Access} chosen only tells how the value can
 * be interacted with.<br/>
 * <br/>
 * If one want to manage different {@link Access}es on the same property, he can
 * create a class which implements the corresponding {@link Access} interfaces,
 * like the {@link ReadWriteProperty} example. However, when several properties
 * need to be managed, one cannot implement several time the same interface for
 * different properties. In such a case, the {@link AccessProvider} allows to
 * deal with such multiplicity. It can also be used to aggregate properties of
 * different objects from a single manager, without having to create a dedicated
 * class. The {@link SimpleAccessProvider} provides an implementation able to
 * deal with these needs.<br/>
 * <br/>
 * One could consider that such access management could be a burden to master
 * and could prefer to make it by hand by creating dedicated classes and
 * methods, which would have the added advantage to be further customizable. The
 * aim of this {@link Access} interface and its extensions is however to
 * centralize the experience gathered in data access to provide a simple basis
 * which can be combined easily to obtain advanced interactions that other
 * programmers would not necessarily think about nor put effort in designing.
 * For instance, managing listeners is easy but can become verbose and
 * repetitive, thus annoying to manage and leading to an arguable economy of
 * design effort and code writing. These interfaces and their implementations
 * come as supporting tools to facilitate the implementation of advanced
 * interactions, providing support for data flow optimization without the burden
 * of creating additional classes or methods.<br/>
 * <br/>
 * Notice that this {@link Access} interface is the common root for all types of
 * accesses. No one should extend nor implement {@link Access}. Please look at
 * its extensions and their Javadoc to know their details and choose among them
 * for further extension or implementation.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Value>
 *            the type of value to access
 */
public interface Access<Value> {

}
