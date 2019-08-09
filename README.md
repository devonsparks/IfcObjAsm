# IfcObjAsm

## Summary
IfcObjAsm is an experimental command-line utility for transforming [IFCXML](https://en.wikipedia.org/wiki/Industry_Foundation_Classes#IFC/ifcXML_specifications) files into networks of linked objects. It does this exploding monolithic IFCXML documents into its constituent entities, with one entity per file. Each entity file is named by its IFC [GloballyUniqueId](http://docs.buildingsmartalliance.org/IFC4x2_Bridge/schema/ifcutilityresource/lexical/ifcgloballyuniqueid.htm) and placed in a common repository (named "objects") on the local file system. IFCObjAsm rewrites all references between IFC entities as references into this common repository. This approach has a few consequences:

* Monolithic IFC(XML) files are transformed into networks of granular, hyperlinked objects.

* Subsets of data can be extracted on demand by simply traversing the object graph (see the _subgraph_ command below). This affords a modular decomposition of IFC data and reduces demand on authoring environments. You only ever transact the subgraph of data you need. 

* Because IFC entities are stored as files, they can be managed directly by revision control systems like git. This opens the possibility for patterns of open, distributed collaboration based on IFC. 

* By using standard XML linking technologies, stakeholders can create  "views" into their object repositories organized by use case. [Xincludes](https://www.w3.org/TR/xinclude/), for example, can be used to [symlink](https://en.wikipedia.org/wiki/Symbolic_link) IFC objects into arbitrary containers, classification structures, or folders.  Because the XIncludes only ever reference object data, it's possible to build up as many "views" onto the object repository as needed. 

For a domain practitioner, IfcObjAsm acts like an swiss army knife for IFCXML data, able to separate and reconstitute IFC entities based on their relationships. For a software engineer, IfcObjAsm approximates a [linker](https://en.wikipedia.org/wiki/Linker_(computing) whose object files are IFC entities. For all, IfcObjAsm explores the possibility of better control and coordination of IFC-based assets by breaking them up into their constitutent parts.

IfcObjAsm is a proof-of-concept only. It has not been heavily tested or even used. It is provided AS-IS with no warranty. Have fun!


## Usage

IfcObjAsm is written as a collection of XSL transformations bundled with a small Java 8-based driver program. You can trigger a list of available commands by:
```
$ java -jar IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
Command required. Options: expand | subgraph | objectify
```

Short description of each commands can be triggered with a `-h` flag:
```
$ java -jar IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar <command-name> [-h]
```

All command invocations follow the same pattern:
```
$ java -jar IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar <command-name> <input.ifcxml> [-b <basedir>]
```
With the `-b` flag, IfcObjAsm will look for the objects repository folder (called "objects") in `<basedir>`. 
Otherwise, `<basedir>` is set to current working directory.

If you need to build IfcObjAsm yourself: `mvn clean package`. 

## Commands

### _objectify_

### _subgraph_

### _expand_

## About
