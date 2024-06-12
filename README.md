# SimpleLibrary [![Minecraft Version](https://img.shields.io/badge/minecraft-1.20.4-blue)

## Overview:
SimpleLibrary is a small library to make modding a little easier and reduce the amount of step. Small little things found in SimpleUtils are ment to reduce hastle across multiple projects.

SimpleLibrary suports FML, NeoForge, and Fabric 1.20.6

## Installation:
First add the maven.
```gradle
repositories {
	...
	maven { "https://raw.githubusercontent.com/willatendo/Willatendo-Mods/main/builds/" }
}
```

Then add the dependency in the dependencies.

Common:
```
dependencies {
	...
	compileOnly "simplelibrary:simplelibrary-common:${simple_library_version}"
}
```

Fabric
```
dependencies {
	...
	compileOnly "simplelibrary:simplelibrary-fabric:${simple_library_version}"
}
```

FML
```
dependencies {
	...
	compileOnly "simplelibrary:simplelibrary-forge:${simple_library_version}"
}
```

NeoForge
```
dependencies {
	...
	compileOnly "simplelibrary:simplelibrary-neoforge:${simple_library_version}"
}
```

In your gradle.properties, define simple_library_version as any github release. (ex. v1.0.0)