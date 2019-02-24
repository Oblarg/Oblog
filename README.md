# Oblog
Java Annotation-driven Shuffleboard-based logging library for FRC teams

Oblog offers Java-using FRC teams a clean, powerful, low-overhead way to interact with the WPILib Shuffleboard API for robot telemetry logging.


Why use Oblog?
------------------

Beginning in 2019, WPILib added support for detailed programmatic control of Shuffleboard configuration from robot code through a new
[fluent builder API](https://github.com/wpilibsuite/allwpilib/blob/master/wpilibj/src/main/java/edu/wpi/first/wpilibj/shuffleboard/Shuffleboard.java).  While the supported features are powerful, they are also verbose - placing widgets on shuffleboard
takes a great number of duplicated method calls, in the worst case requiring the user to keep track of the entire shuffleboard configuration from
multiple places in their code.  Additionally, widget parameter lists lack any widget-specific method calls, requiring users to correctly remember a
significant number of property names and their default values, and to correctly type them without any compile-time checking.

Oblog offers a better, less error-prone way to use this functionality.


How Does It Work?
------------------

Oblog's API is very simple.  Using it takes just three easy steps:

1. Designating classes (e.g. subsystems) as loggable by implementing the Loggable interface.
2. Designating which fields and getters in Loggable classes will be logged with the @Log annotation, or one of its widget-specific sub-annotations.
3. Adding calls to Logger.configureLogging and Logger.updateEntries in robotInit and robotPeriodic, respectively.

Once this is done, Oblog's logger will recursively generate Shuffleboard tabs, layouts, and sub-layouts based on your own code's graph
of Loggable objects, and populate them with your desired widgets.  No lengthy builder calls, no confusing property lists, no hassle - 
just annotate, and log!

Oblog removes from the user the need to explicitly think about the structure of Shuffleboard's configuration - the 
logger automatically infers this from the structure of their code.  Additionally, the migration of widget-specific properties to annotation parameters 
removes the need for users to memorize the property namesfor each Shuffleboard widget type.

A (very) basic example robot can be found in the "examples" folder, demonstrating a few of the available annotations and the correct way
to call the necessary Logger functions.


Caveats
------------------

Oblog's API is mostly self-explanatory.  However, a few important caveats should be minded:

* As Oblog infers the structure of the desired Shuffleboard configuration from the nesting of Loggable objects in the user's code, the user
  must be mindful of the structure of their Loggable object graph.  For example, Loggable objects that are fields of multiple other Loggable objects will
  be sent to Shuffleboard multiple times unless explicitly instructed otherwise with the @Log.Exclude annotation.  Additionally, Loggable objects
  that are not reachable from the "root container" (generally, this is Robot.java) through a sequence of Loggable ancestors will not be logged, as
  the logger has no way of knowing that they are there.
* If a Loggable object (or the root container) contains multiple instances of a Loggable class as fields, that Loggable class *must* override the
  "configureLogName" method to provide a unique, programmatically-generated name for the object's tab or layout.  Failure to do so will result
  in a namespace collision and a crash.  An example of this is provided in the "examples" folder, in which two instances of the same LoggableCommand
  are present in Robot.java.
* Oblog does *not* currently provide for specifying the sizes or positions of layouts or widgets - they will have to be 
  manually resized and located.  This is due to current limitations of the Shuffleboard API - if some widgets are given specified sizes/positions
  and others are not, there is no way of preventing widgets from overlapping/covering each other up on the Shuffleboard.  Support for this 
  thus seems more likely to cause more problems than it will solve, and will not be included until/unless the Shuffleboard API changes.
  

Contributing to Oblog
------------------

Contributions to Oblog, as well as feature requests and bug reports, are welcome.  If you wish to contribute your own code, please include unit tests
in the appropriate test folder.


Author
------------------

Eli Barnett (emichaelbarnett@gmail.com)
