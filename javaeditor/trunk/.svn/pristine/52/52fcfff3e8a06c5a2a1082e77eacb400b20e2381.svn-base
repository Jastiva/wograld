This document describes some hints and requirements for
general "development" on the CrossfireEditor.
If you plan to make changes to the editor code or setup please
read the following and keep it in mind:


1. The basic setup of the editor was done by Michael Toennies,
   derived from a basic editor-application called "Gridder" by
   Pasi Ker�nen. Then, I (Andreas Vogl) have added the countless features
   necessary to turn this application into a real useful CF map-editor.
   We have both spent a lot of time on the editor (take a glimpse at the
   CHANGES.txt file), so please communicate with us, best through the
   cf-devel-mailing list, before considering any fundamental changes.

2. About code formatting:
   Please, DO NOT USE TABS. No matter what Java development platform you
   are using, please configure "insert spaces", indent 4.
   Tabs are displayed totally different in every editor and there are millions
   of different editors out there. The insertion of tabs in the source code is
   messing up the syntax formatting in a way that is UNREPAIRABLE.

   Apart from that, please keep code indentation accurate. This is not
   just "good practice", it helps to keep code readable and in that way
   dramatically decreases the chance for overlooked bugs.
   Everyone is welcomed to correct indentation errors wherever they are
   spotted. Before you start to do this however, please double-check that
   your editor is really configured to "insert spaces".

   Line feeds may be checked in either in windows or in unix/linux style.
   All reasonable text- and java editors can deal with both linefeed formats.
   Converting line feeds is allowed, but in this case please make sure that only
   linefeed characters are changed and nothing else is affected.

2. Due to the platform-independent nature of Java, the editor has the
   potential to run on almost any given operating system.
   Unfortunately, the build process differs greatly between systems
   as well as java environments. In the past, several people have attempted
   to add build scripts along with structural changes to optimize the setup
   on one particular system/environment which has led to conflict.

   Please do *not* attempt to change the structure or any directories
   for the mere purpose of improving a build process or performance
   in a java environment.

   Build scripts may be placed in the root directory, it would be
   especially fine if it is just one or two files but the latter is
   not required.

   Please excuse me for placing such restriction. I and many users of
   the editor greatly appreciate build-scripts. We just had some real
   troubles over this issue in the past and I don't want to have them
   repeated.

3. Unfortunately, the editor has relatively high performance requirements.
   I've spent a lot of extra-work to keep everything as fast and
   memory-efficient as possible. So, when you add new data fields or
   calculations in the archetype area, please make sure they are as
   efficient as possible and worth both the time and space they consume.

   Now don't be afraid too much. No development would be possible
   without adding calculations and data at all. Just bear in mind
   that, unlike for many other open source projects, performance
   does make a difference for the CrossfireEditor.

4. The GUI (graphical user interface) of the editor is not as simple
   as it may seem on first glance. I have invested a lot of time and work
   to make the GUI user-friendly and especially to make it work at all,
   for as many systems as possible.

   In case you are unexperienced with java and swing, note that the
   graphics look different on every system, and with every font.
   They also have different sizes/proportions and behave different.
   A seemingly trivial and effectless change can wreck havoc for
   the same GUI run on another system.

   Again, please don't be totally afraid of it, just keep it in mind.
   Nobody is gonna eat you alive when your code causes a "GUI-bug".
   The best way to deal with it is to test on different systems.
   Another good thing is to design all GUIs with care and avoid
   fixed sizes like hell, wherever you can.

5. You might notice that the basic structure of the code obeys
   the "model-view-controller" scheme. Too bad it is halfway
   messed up and violated, but please try to stick with it as
   good as you can. Don't make it worse.
   For the record: "Model"-classes should contain data of an object.
   "View"-classes should only manage the graphical layout and
   appearance of an object. "Controller"-classes should manage
   events and data flow, concerning that object. In a perfect world,
   these three things would be totally separated and you could
   replace any of these parts without changing the others.
   I have yet never seen it realized perfectly, so feel proud when
   you just tried your best.

