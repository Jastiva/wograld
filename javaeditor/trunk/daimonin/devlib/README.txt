daimonin/editor/devlib/README.txt

This directory contains some files and libraries for development tools,
currently:

* Checkstyle
  Tool to verify code conventions
  http://checkstyle.sf.net/
* Jalopy
  Tool to beautify Java code, similar to indent for C
  http://jalopy.sf.net/

They are referred from "optional targets" in make/ant/build.xml.
You don't need these libraries if you don't want to use.

To not bother developers that are not related to the editor, and because many
Java developers already have these libraries somewhere else on their
system, they are not included in the Daimonin repository.

If you are interested in using them, you could copy the checkstyle-3.5-all.jar
file and jalopy directory here or create a user.properties file in the editor
base directory which overrides the default paths from build.xml to point to the
location where these tools already are installed on your system.
