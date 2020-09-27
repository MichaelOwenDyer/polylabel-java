# polylabel-java
A Java port of <b>PolyLabel</b> from MapBox.

Official repo: [https://github.com/mapbox/polylabel](https://github.com/mapbox/polylabel)

Article: https://blog.mapbox.com/a-new-algorithm-for-finding-a-visual-center-of-a-polygon-7c77e6492fbc

## Requirements
<b> - Java 8</b> or later

<b> - Maven</b>

## Installation

Add polylabel-java to your local .m2 folder with the following commands:
```
git clone https://github.com/FreshLlamanade/polylabel-java
cd polylabel-java
mvn clean install
```

Then add the following dependency to your pom.xml:
```
<groupId>com.monst</groupId>
<artifactId>polylabel-java</artifactId>
<version>1.2</version>
```

## Usage

<i>Using default precision (1.0), no console messages:</i>
```java
PolyLabel.Result result = PolyLabel.polyLabel(new Integer[][][] {{{0, 0}, {10, 0}, {0, 10}}})
// result.getCoordinates() -> {3.125, 3.125}
```
<i>Using precision of 0.5, no console messages:</i>
```java
PolyLabel.Result result = PolyLabel.polyLabel(new Integer[][][] {{{0, 0}, {10, 0}, {0, 10}}}, 0.5)
// result.getCoordinates() -> {2.8125, 2.8125}
```
<i>Using precision of 0.5, with console messages:</i>
```java
PolyLabel.Result result = PolyLabel.polyLabel(new Integer[][][] {{{0, 0}, {10, 0}, {0, 10}}}, 0.5, true)
//    Found best 2.50 after 5 probes
//
//    Found best 2.65 after 21 probes
//
//    Found best 2.81 after 25 probes
//
//    Num probes: 25
//    Best distance: 2.812500
//
// result.getCoordinates() -> {2.8125, 2.8125}
```
