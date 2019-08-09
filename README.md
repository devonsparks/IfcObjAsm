# IfcObjAsm

## Summary
IfcObjAsm is an experimental command-line utility for transforming [IFCXML](https://en.wikipedia.org/wiki/Industry_Foundation_Classes#IFC/ifcXML_specifications) files into networks of linked objects. It does this exploding monolithic IFCXML documents into its constituent entities, with one entity per file. Each entity file is named by its IFC [GloballyUniqueId](http://docs.buildingsmartalliance.org/IFC4x2_Bridge/schema/ifcutilityresource/lexical/ifcgloballyuniqueid.htm) and placed in a common repository (named "objects") on the local file system. IFCObjAsm rewrites all references between IFC entities as references into this common repository. This approach has a few consequences:

* Monolithic IFC(XML) files are transformed into networks of granular, hyperlinked objects.
* Subsets of data can be extracted on demand by simply traversing the object graph (see the _subgraph_ command below). This affords a modular decomposition of IFC authoring and reduces (de)serialization demand on authoring environments. You only ever transact the subgraph of data you need. 
* Because IFC entities are stored as globally unique files in a repository, they can be managed directly by revision control systems like git. This opens the possibility for patterns of open, distributed collaboration based on IFC. 
* By using standard XML linking technologies, stakeholders can create custom "views" into their object repositories based on use case. [Xincludes](https://www.w3.org/TR/xinclude/), for example, can be used as [symlinks](https://en.wikipedia.org/wiki/Symbolic_link) to entities in an objects repository. These symlinks can be placed in arbitrary folder structures, allowing for simultaneous organization of the project by, e.g., classification, work status, or object type. Because the XIncludes only ever reference object data, it's possible to build up as many "projections" onto the object repository as needed. 

For a domain practitioner, IfcObjAsm acts like an swiss army knife for IFCXML data, able to separate and reconstitute IFC entities based on their relationships. For a software engineer, IfcObjAsm approximates a [linker](https://en.wikipedia.org/wiki/Linker_(computing) whose object files are IFC entities. For all, IfcObjAsm explores the possibility of better control and coordination of IFC-based assets by breaking them up into their constitutent parts.

IfcObjAsm is a proof-of-concept only. It has not been heavily tested or even used. It is provided AS-IS with no warranty. Have fun!

## Commands

### _objectify_

### _subgraph_

### _expand_

## About
