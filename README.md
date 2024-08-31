# SimpleLibrary [![Latest Version](https://jitpack.io/v/willatendo/Simple-Library.svg)](https://jitpack.io/#willatendo/Simple-Library) ![Minecraft Version](https://img.shields.io/badge/minecraft-1.20.4-blue)

## Overview:
SimpleLibrary is a small library to make modding a little easier and reduce the amount of step. Small little things found in SimpleUtils are ment to reduce hastle across multiple projects.

SimpleLibrary suports Neoforge, Fabric and Forge

## Installation:
First add the maven.
```gradle
repositories {
	...
	maven { url "https://raw.githubusercontent.com/willatendo/Willatendo-Mods/main/builds/" }
}
```

Then add the dependency in the dependencies.


```Common:
dependencies {
	...
	compileOnly "simplelibrary:simplelibrary-common:${simple_library_version}"
}
```

```Fabric:
dependencies {
	...
	modApi "simplelibrary:simplelibrary-fabric:${simple_library_version}"
}
```

```NeoForge:
dependencies {
	...
	implementation "simplelibrary:simplelibrary-neoforge:${simple_library_version}"
}
```

In your gradle.properties, define simple_library_version as any github release. (ex. v1.0.0)