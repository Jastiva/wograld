/*
 * static char *rcsid_x11_h =
 *   "$Id: x11.h,v 1.1.1.1 2012/09/14 03:30:02 serpentshard Exp $";
 */
/*
    Wograld client, a client program for the wograld program.

    Copyright (C) 2001 Mark Wedel & Wograld Development Team

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

    The original author can be reached via e-mail to wograld-devel@real-time.com 
    The modification editor for the wograld project at	jastiv@yahoo.com
*/

#ifndef GX11_H
#define GX11_H

#include "client-types.h"
#if defined(_MSC_VER)
#include "SDL.h"
#else
#include "SDL/SDL.h"
#endif


extern int map_size;
extern uint8 updatekeycodes;
extern int updatelock;
extern int ctrans; 
extern int floor_active[7];

/* extern int dark1num;*/
/*extern int dark2num;*/
/*extern int dark3num;*/
/*extern int blanknum;*/
/*extern int blockednum;*/

struct PixmapInfo {
  Pixmap pixmap,mask;
  uint8 width, height;		/* size of pixmap in tiles */
};

extern struct PixmapInfo *pixmapst[MAXPIXMAPNUM];
extern struct PixmapInfo *pixmaps[MAXPIXMAPNUM];

extern Display *display;
extern uint8   image_size;
extern Window win_root,win_game;
extern GC gc_game;
extern Colormap colormap;
extern Window win_stats,win_message;
extern SDL_Surface *screen;


 #endif 
