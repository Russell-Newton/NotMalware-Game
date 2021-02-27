# A Dungeon Crawler by What.exe

#### For CS 2340

JDK should be at least version 11. JavaFX is automatically included through gradle for compile and run times :)

This project is modular! This means that a functioning distribution with a launcher can be created with `gradlew jlink`
and will be located at *build/image*, which can be run with either `"What.exe Dungeon Crawler"`
or `"What.exe Dungeon Crawler.bat"` in a terminal.

Alternatively, the project can be run with `gradlew run` or by creating an Application Run Configuration in IntelliJ.

A compressed distribution can be built with `gradlew dist`. Calling `gradlew dist run` will update the image and zipped
distribution and run the project.

Checkstyle can be run with `python run_checkstyle.py` or `gradlew checkstyleMain checkstyleTest`. **The configuration
file *cs2340_checks.xml* has been modified to ignore instances of *module-info.java*. The following module tag has been
added to the "Checker" module:**

```xml
<module name="BeforeExecutionExclusionFileFilter">
    <property name="fileNamePattern" value="module\-info\.java$"/>
</module>
```

This is the only modification made to *cs2340_checks.xml*.
