/*
 * char *rcsid_gtk2_main_h =
 *   "$Id: main.h,v 1.1.1.1 2012/09/14 03:30:02 serpentshard Exp $";
 */
/*
    Wograld client, a client program for the wograld program.

    Copyright (C) 2005 Mark Wedel & Wograld Development Team

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

    The author can be reached via e-mail to wograld@metalforge.org
*/

#define NUM_COLORS 13
extern GdkColor root_color[NUM_COLORS];
GtkWidget *window_root, *spinbutton_count;

#define DEFAULT_IMAGE_SIZE      32
extern int map_image_size, map_image_half_size, image_size;


/* Notebook page of the magic map */
#define MAGIC_MAP_PAGE	1
