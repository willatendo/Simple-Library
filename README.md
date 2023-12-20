# SimpleLibrary [![Latest Version](https://jitpack.io/v/willatendo/Simple-Library.svg)](https://jitpack.io/#willatendo/Simple-Library) ![Minecraft Version](https://img.shields.io/badge/minecraft-1.20.4-blue)

## Overview:
SimpleLibrary is a small library to make modding a little easier and reduce the amount of step. Small little things found in SimpleUtils are ment to reduce hastle across multiple projects.

SimpleLibrary suports Fabric

## Installation:
First add the maven. Just use JitPack with a gradle release.
```gradle
repositories {
	...
	maven { "https://jitpack.io/" }
}
```

Then add the dependency in the dependencies.

```gradle
dependencies {
	...
	implementation fg.deobf("com.github.willatendo:Simple-Library:${simple_library_version}")
}
```

In your gradle.properties, define simple_library_version as any github release. (ex. v1.0.0)