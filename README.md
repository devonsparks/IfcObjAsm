# IfcObjAsm

## Summary
IfcObjAsm is an experimental command-line utility for transforming [IFCXML](https://en.wikipedia.org/wiki/Industry_Foundation_Classes#IFC/ifcXML_specifications) files into networks of small, hyperlinked documents. It does this exploding an IFCXML file into its constituent IFC entities, with one entity per file. Each entity file is named by its IFC [GloballyUniqueId](http://docs.buildingsmartalliance.org/IFC4x2_Bridge/schema/ifcutilityresource/lexical/ifcgloballyuniqueid.htm) and placed in a common folder repository (named "objects") on the local file system. IFCObjAsm then rewrites all references between IFC entities as references into this common repository. This approach has a few consequences:

* Monolithic IFC(XML) files are transformed into networks of small, linked objects, represented as files.

* Subsets of data can be extracted on demand by traversing the object graph. This affords a modular decomposition of IFC data and reduces demand on authoring environments. You only ever transact the subgraph of data you need. 

* Because IFC entities are stored as files, they can be managed directly by version control systems like git. This offers new patterns of open, distributed collaboration based on IFC. 

* By using standard XML linking technologies, stakeholders can create  "views" into their object repositories organized by use case. [Xincludes](https://www.w3.org/TR/xinclude/), for example, can be used to [symlink](https://en.wikipedia.org/wiki/Symbolic_link) IFC objects into arbitrary containers, classification structures, or folders.  Because the XIncludes only ever reference object data, it's possible to build up as many "views" onto the object repository as needed. 

* Composition of IFC data reduces to folder merging. Conflicts now occur at the level of individual IFC entities, not project files. 

For a domain practitioner, IfcObjAsm acts like an swiss army knife for IFCXML data, able to separate and reconstitute IFC entities based on their relationships. For a software engineer, IfcObjAsm approximates a [linker](https://en.wikipedia.org/wiki/Linker_(computing)) whose object files are IFC entities. For all, IfcObjAsm explores the possibility of better control and coordination of IFC-based assets by breaking them up into their constitutent parts, only generating 'complete' IFC files just-in-time.

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
_objectify_ takes an IFCXML and transforms it into an object repository. The resulting repository contains one file
for each IFCRoot-descended entity in the input file, named by its ifc:GlobalId. _objectify_  will emit on stdout an
IFCXML file containing XIncludes to the created objects. If this file is later _expand_ed, it will be semantically equivalent to the input file.

See [hellowall](https://github.com/devonsparks/IfcObjAsm/tree/master/samples/hellowall/objects) as an example. 

```
$ java -jar IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar objectify \
			 samples/hellowall/hellowall.ifc -b samples/hellowall
			 
<doc:iso_10303_28 xmlns:exp="urn:oid:1.0.10303.28.2.1.1"
                  xmlns:doc="urn:oid:1.0.10303.28.2.1.3"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xmlns:ifc="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL"
                  xsi:schemaLocation="urn:oid:1.0.10303.28.2.1.1 ex.xsd"
                  version="2.0">
   ...
   <ifc:uos xmlns="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL"
            description=""
            schema="exp_1"
            configuration="IFCXML_Official"
            edo=""
            xsi:schemaLocation="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL 
            http://www.iai-tech.org/ifcXML/IFC2x2/FINAL/ifc2x3g_alpha.xsd">
            
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1RQuwGrNPCLxmVr$e9D_aA"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2xd9Hy$kzEXOevAafeX$79"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0agga2sOr8cPHF3GuuPzoK"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0agga2sOr8cPHF3Gx7c285"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/34kf9kI6DCPP1peIk4MS_L"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0cRr2BICT1kPRTSulp4F7n"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/3PfHr$H7b2NfM9hd0vYzH$"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2RsG2fUqL428sywFLRnBg2"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0cRr2BICT1kPRTU7Rp4F7p"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2Hvy0tpsL40APhwoeStH$d"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2fZiJ1wKb9EArbCo$EYmWM"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/16P5ZsthHACPo8$zHG9Vpv"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2MayaW1Gv4GviuBtqfLNUo"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0UiXtFOUj5fPDWBOJWIDS0"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2MayaW1Gv4Gviu990fLNUo"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/3Xf10drQn1qxlF0ajUgFo7"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/2fb9oBlXfFexGU1p8oU$2Y"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1b7tIashzEUx2OKPg24_7R"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1b7tIashzEUx2OMdA24_7R"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1fj4I5j8T17At$G6FRmpKb"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1zrhqnYvDAgOpQf7y0sEYV"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/1vuq88Ttn9KB6u41nU0oWQ"/>
   </ifc:uos>
</doc:iso_10303_28>

```

Examining one of these object files ([an IFCWallType](https://github.com/devonsparks/IfcObjAsm/blob/master/samples/hellowall/objects/0cRr2BICT1kPRTSulp4F7n)) reveals the key transformations _objectify_ performs:
1. All IFC entities descending from IFCRoot receive updated @id attributes matching their ifc:GlobalId. 
All @refs to these entities are updated accordingly.
2. @refs to IFC entities not descending from IFCRoot (Resource Layer Entities) are copied inline. This ensures every object only links to other, globally addressable objects. Because Resource Layer Entity instances are semantically equivalent, the only cost of copy-by-value is storage space. 
3. The resulting objects are then written out to the object repository. All links are links into the object repository. These  links are then bundled and returned on stdout as an IFCXML file for further processing.

### _subgraph_
_subgraph_ takes an IFCXML input file containing @refs to an objects repository. It then performs a recursive depth-first search to discover all object files connected those those listed in the input file (including the input file itself). This allows complete extraction of subsets of IFC data without risking "dangling" references. This subset can then be edited and committed back to the repository using _objectify_.

For example, using an input file that contains one reference to an IfcWallType instance, we can discover all objects @ref'd to it.

```
$ java -jar target/IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar subgraph samples/hellowall/hellowall-subgraph.ifcxml -b samples/hellowall/ 

			 
<doc:iso_10303_28 xmlns:exp="urn:oid:1.0.10303.28.2.1.1"
                  xmlns:doc="urn:oid:1.0.10303.28.2.1.3"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xmlns:ifc="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL">
   ...
   <ifc:uos xmlns="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL"
            description=""
            schema="exp_1"
            configuration="IFCXML_Official"
            edo=""
            xsi:schemaLocation="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL           
                                http://www.iai-tech.org/ifcXML/IFC2x2/FINAL/ifc2x3g_alpha.xsd">
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/hellowall-subgraph.ifcxml"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0cRr2BICT1kPRTSulp4F7n"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/3ewKlnEfbD$OIToybZDVSW"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/27974KITb6q9mSCftpWcB8"/>
      <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
                  href="file:/home/devon/git/IfcObjAsm/samples/hellowall/objects/0cRr2BICT1kPRTU7Rp4F7n"/>
   </ifc:uos>
</doc:iso_10303_28>
```

From here, the result could be _expand_ed and fed to an authoring application. We imagine subgraph refs could serve as the
basic for a work packaging system, where a single file records all IFC entities required for a particular scope of work.


### _expand_
_expand_ is an identity transformation on an input IFCXML file, with the added benefit of resolving XIncludes. 
This is useful for "rehydrating" files generated by _objectify_ and _subgraph_. For example, running _expand_ on the output
of hellowall's _objectify_ command returns an IFCXML document semantically equivalent to helloworld.ifcxml itself.

```
java -jar target/IfcObjAsm-0.0.1-SNAPSHOT-jar-with-dependencies.jar expand samples/hellowall/hellowall-refs.ifcxml -b samples/hellowall/ > samples/hellowall/hellowall-refs-expand.ifcxml
```
Output: [hellowall-refs-expand.ifcxml](https://github.com/devonsparks/IfcObjAsm/blob/master/samples/hellowall/hellowall-refs-expand.ifcxml).


## Notes

There are a lot of directions to go from here. Some ideas:
* Add a 'library' command that turns all IfcProject instances into IfcProjectLibrary instances. This could serve as
  the basis of a portfolio management workflow while respecting the IfcSingleProjectInstance rule. 
* Explore distributed and collaborative workflows using git. Can branches be used to separate the object repository by security domain? 
* Elaborate on the work package idea by using _subgraph_ to extract discipline-focused models directly from the objects repository.
* Write XSL Transforms for organizing an objects repository into one or more classification systems (Omniclass, Uniclass).

More critical is working out the kinks, like:
* Writing a proper test suite
* Validating output (and input?) against the XSD spces
* Determining whether [canonical XML](https://en.wikipedia.org/wiki/Canonical_XML) is enough to prevent git from having a seizure every time we run _objectify_. If so, can we achieve this without Saxon-EE?
* Improving documentation and making demos

## About

IfcObjAsm is provided AS-IS, without warranty, and licensed under GPL v3. Pull requests and commentary are welcome :)
