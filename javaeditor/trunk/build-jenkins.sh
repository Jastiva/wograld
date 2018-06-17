#! /bin/sh

set -e
unset DISPLAY
cd trunk
debuild -uc -us
ant clean jar test checkstyle javadoc
