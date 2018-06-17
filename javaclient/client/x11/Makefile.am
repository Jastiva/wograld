#
# Makefile for wograld client
#
#   Copyright (C) 2005 gnurpgs & wograld Development Team
#
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation; either version 2 of the License, or
#   (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
#
#   The author can be reached via e-mail to wograld.sourceforge.net


bin_PROGRAMS = wogclnt

wogclnt_SOURCES = \
	png.c  \
	sound.c  \
	x11.c  \
	xutil.c


noinst_HEADERS = \
	clientbmap.h\
	rcs-id.h \
	x11.h \
	x11proto.h

dist_man6_MANS = \
	wogclnt.man

AM_CPPFLAGS = -I$(top_srcdir) -I$(top_srcdir)/common -I$(top_srcdir)/pixmaps
wogclnt_LDADD = $(DMALLOC_LIB) $(X_LIBS) ../common/libwogclnt.a

../common/libwogclnt.a:
	( cd ../common ; make libwogclnt.a )


# Check if everything is installed as it should
installcheck-local:
	$(bindir)/wogclnt -help

proto:
	cproto -e -o x11proto.h $(AM_CPPFLAGS) $(wogclnt_SOURCES)
	chmod 644 x11proto.h

