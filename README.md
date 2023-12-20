# SimpleLibrary [![Latest Version](https://jitpack.io/v/willatendo/Simple-Library.svg)](https://jitpack.io/#willatendo/Simple-Library) ![Minecraft Version](https://img.shields.io/badge/minecraft-1.20.4-blue)

## Overview:
SimpleLibrary is a library ment to be used jar-in-jar to make modding a little easier and reduce the amount of step. Small little things found in SimpleUtils and DataHelper are ment to reduce hastle across multiple projects.

SimpleLibrary suports Fabric

There are plans to make it easier to add fabric/quilt suport to a mod too.

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

Then enable jar-in-jar.

```gradle
jarJar.enable()
```

Add the following

```gradle
reobf {
    jarJar { }
}

tasks.jarJar.finalizedBy('reobfJarJar')
```

Then you're done!