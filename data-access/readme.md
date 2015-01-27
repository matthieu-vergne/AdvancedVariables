# Advanced Data Access

## What is the aim of this library

This library aims at providing an easy way to deal with advanced data accesses. For instance, the fact that one use an immediate affectation or relies on a listener (e.g. to be notified when a value is changed) is an example of decision management process: the affectation is under the control of the user of the data while the listener call is under the control of the element to which the listener is registered. However, such advanced accesses generally need some effort to be setup. The aim of this library is, by considering the specific concepts related to these advanced accesses, to support their implementation by providing explicit interfaces and useful, reusable implementations.

## How to use it?

It is managed through Maven and is available on the [central repository](http://search.maven.org/). You can download the corresponding JAR and its dependncies or add it to your POM:
```
<dependency>
	<groupId>fr.matthieu-vergne</groupId>
	<artifactId>data-access</artifactId>
	<version>1.0</version>
</dependency>
```

## Who should be interested in this library?

All the programmers who want to implement advanced structures in which the data flow is optimized should care about the different types of accesses they can use. In particular, applications managing several types of data in different ways, such as optimized algorithms, are prone to find this library useful.

As an indicator, if you did not understand the section about the aim of this library, then you probably don't need it. At least for now {^_°}.

## Which problem this library tries to solve?

A basic interaction with a variable would be summarized by having the possibility to read/write its value on the fly. Said another way, their would have a user who interacts with a property by requesting its current value (read) or providing it its new value (write). But if we go further in the analysis, two decisions are made:

- *which* data should flow?
- *when* this data should flow?

The first question corresponds to choosing the type of operation to do: read or write. When reading, this is the data of the property which flows to the user, while writing is the opposite. The second question corresponds to choosing when this operation should occur.

With a simple variable interaction, the decision is fully made by the user: the data which flows is the one from the user (write) or the variable (read), and the flow should occur immediately, so the next instruction can exploit the fact that the variable already has its new value (write) or its value has been stored or used in another computation (read). However, producing an immediate flow is not always wanted. For instance, when dealing with a GUI, one could click a button which requires some computation to be done, such as changing the size of the window. In such a case, all other components should be resized correspondingly to fit in the window. These components (or the layout manager) would be the *users* of the *property* "window size", because they need it to know which size to apply to themselves. However, while they should consider any update of this property, it would be a burden to setup an infinite loop in order to recompute it everytime or to know when it is updated. This is where the decision between *what* should flow and *when* it should flow becomes separated: what should flow is the window size, because the components need it, but when it should flow is when the window is resized, because until then the property does not change. Delegating both controls to the components would lead them to run constantly, while delegating all to the window would lead to implement all the components' logics into the window.

To avoid these issues, one should be able to tell separately *which* data should flow and *when* it should do it. In Swing, this is managed by using getters and the [listeners](http://en.wikipedia.org/wiki/Observer_pattern): the window can provide several properties through `getX()` methods, like its size, and it can notify some components when it is resized, as long as they register a listener which will take care of this notification. In this case, the components would register a listener on the window such that, when the window is resized, the components are notified and can react correspondingly, in other words read the window size at this moment and adapt their own size to fit well. Regarding the control, *which* data flows is decided by the components (the window notifies the components without knowing what they will do once notified) but *when* it flows is decided by the window (the components have no idea of when they will receive a notification).

## What is the solution provided?

In this project, we deal with these two dimensions (which + when) by specifying specific accesses from the perspective of the *user* of the data. For the *when* dimension, if the user should decide when to access the property, the access provided to the user is said to be **active**. At the opposite, if the user is waiting for the value to be considered (provided or requested) then the access is said to be **passive**. For the *which* dimension, we reuse the notions of **write** operation, if the user is the source of the data, and **read** operation, if the user is the target of the data. The four configurations are described further in their own interfaces:

- ActiveReadAccess to read a value on demand
- ActiveWriteAccess to write a value on demand
- PassiveReadAccess to read a value when generated
- PassiveWriteAccess to write a value when requested

If one want to manage different accesses on the same property, he can create a class which implements the corresponding interfaces, like the ControlledProperty and FlowController implementations. However, when several properties need to be managed, one cannot implement several time the same interface for different properties. In such a case, one implementation needs to be made for each property separately, or you can use a PropertyAccessProvider to deal with such multiplicity in a centralized manner. The SimpleAccessProvider provides an implementation able to deal with the basic needs.

One could consider that such access management could be a burden to master and could prefer to make it by hand by creating dedicated classes and methods, which would have the added advantage to be further customizable. The aim of thes interfaces and their implementations is however to centralize the experience gathered in advanced data access to provide a simple basis which can be combined easily to obtain advanced accesses, what other programmers would not necessarily think about nor put effort in designing. For instance, managing listeners is easy but can become verbose and repetitive, thus annoying to manage and leading to an arguable economy of design effort and code writing. These interfaces and their implementations come as supporting tools to facilitate the implementation of advanced accesses, providing support for data flow optimization without the burden of creating additional classes or methods.