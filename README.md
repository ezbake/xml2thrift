XMLParser
======
**XML to Apache Thrift Object Mapper**

Requirements
- JDK 1.7
- Apache Thrift 0.9.1

Handler Types
- String: 	String based on start element
- TBase: 	Thrift objects populated with XML data
- XSD: 		XSD to Thrift file (currently in development)
- Thrift: 	Deprecated: Thrift objects populated with XML data

Versions:
0.1-SNAPSHOT

XML Capabilities
- Elements
- Attributes
- ComplexTypes
- Includes
- Imports
- Namespaces

Thrift Capabilities
- Supports Thrift Lists

XSD Capabilities (in development)
- Elements
- Attributes
- ComplexTypes
- Includes

Usage Notes 
- Thrift classes and type names MUST match XML tags
- Add attributes map to structs to load all attributes
```
	Map<String, String> attributes
	handler.loadAttributesToMap(true);
```
- Set 'locateChildren' flag on to have the XMLParser nest populated objects 
```
	handler.locateChildren(true);
```
- Utilize the 'comparables' functionality to map class names
```
	comparables.put("list", "listtype");
	comparables.put("class", "classtype");
	handler.setComparables(comparables);
```
- Utilize the 'namespaces' functionality to map XML namepsaces to Java/Thrift namespaces
```
	ListMultimap<String, String> namespaces = ArrayListMultimap.create();
	namespaces.put("urn:us:org:mynsapp:ns1", "ns1");
    namespaces.put("urn:us:org:mynsapp:ns2", "ns2");
    namespaces.put("urn:us:org:mynsapp:ns3", "ns3");
    handler.setNameSpaces(namespaces);
```




