Building the Daimonin Editor with Ant
=====================================

To build with Ant, there are 3 possible ways:
* Stay in the main directory (daimonin/editor), use
  ant -f make/ant/build.xml
  for building
* Change to the ant directory, use ant for building
  ( cd make/ant ; ant )
* Create a new build.xml with the following content:
<?xml version="1.0" encoding="UTF-8"?>
<project name="DaimoninEditor Private" default="jar">
    <import file="make/ant/build.xml" />
</project>

The third way probably is the preferred way by vi / vim users, because the
build.xml is searched in current file upwards path, not sub-directories.


developer.properties
--------------------

You can modify the build process to fit your personal needs. For this, create
a file named "developer.properties" in this directory.
The following can be changed (properties' names):
* debug mode compilation (debug)
* ctags tags creation (user.ctags)
* ftp upload of DaimoninEditor.jar (user.ftp.host, user.ftp.user,
    user.ftp.pass, user.ftp.dir, user.ftp.depends, user.ftp.passive,
    user.ftp.ignoreNoncriticalErrors)

The following properties are supported (place them as name-value-pairs in
daimonin/editor/build.properties, like user.ctags=yes)
* debug
    Developers sometimes want debug builds. A debug build contains all symbols
    needed for debugging a Java application (private symbol names, line
    numbers). But also, a debug build is 25% larger.
    Value: yes|no
    Default: no
* user.ctags
    ctags is a program that scans source codes for symbols and generates symbol
    tables. It is used by users of editors like vim or emacs for tag table
    initialization. The tags can then be used to navigate from a symbol usage
    to its declaration.
    Value: yes|no
    Default: no
* user.ftp.host
    DaimoninEditor.jar FTP Upload Host.
    Value: valid host name or ip address
    Default: no default value
* user.ftp.user
    DaimoninEditor.jar FTP Upload Login username.
    Value: valid ftp username
    Default: no default value
* user.ftp.pass
    DaimoninEditor.jar FTP Upload Login password
    Value: valid ftp password
    Default: no default value
* user.ftp.dir
    DaimoninEditor.jar FTP Upload Remote Directory
    Value: valid remote directory
    Default: no default value
* user.ftp.depends
    DaimoninEditor.jar FTP Upload Only if file has changed
    Value: yes|no
    Default: yes
* user.ftp.passive
    DaimoninEditor.jar FTP Upload use passive FTP
    Value: yes|no
    Default: yes
* user.ftp.ignoreNoncriticalErrors
    DaimoninEditor.jar FTP Upload ignore non-critical errors
    Value: yes|no
    Default: yes
* build.number
    Build number, will be automatically created and increased by Ant
* build.developer
    If you plan to release your DaimoninEditor.jar, you definitely should set this.
    The build number will only be shown in the about dialog if you also set
    the property build.developer.
    Theoretically, creating a unique build.number would be possible via CVS, but
    the delay in CVS usage is too long. To keep build numbers unique, they are
    only published if they also denote the build developer.

Example user.properties file:
user.ctags=yes
debug=yes
user.ftp.host=www.example.com
user.ftp.user=cheristheus1234
user.ftp.pass=12345678
user.ftp.dir=daimonin
