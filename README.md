# Resources and classes scanner

## 1. Description

Scans the given locations for resources, and classes

### 2. Maven

```xml
<dependency>
	<groupId>com.fixiu</groupId>
	<artifactId>resources-scanner</artifactId>
	<version>1.0.0-RELEASE</version>
</dependency>
```

## 3. Usage

```java
// 1.Define locations to scan
List<Location> locations = new Locations("to/**/?/./your").getLocations();
// 2.Initialization the Scanner
Scanner<YourClass> scanner = new Scanner<>(YourClass.class, locations, Thread.currentThread().getContextClassLoader(), StandardCharsets.UTF_8, resourceNameCache, locationScannerCache);
/*** or using default parameter to initialization the Scanner ***/
Scanner<YourClass> scanner = new Scanner<>(Object.class, locations);
// 3.Get resources or classes
Collection<LoadableResource> resources = scanner.getResources();
Collection<Class<? extends YourClass>> classes = scanner.getClasses();
```

