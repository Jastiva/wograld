/*****************************************************************************/
/* Wograld Animator v2.0a                                                  */
/* Contacts: yann.chachkoff@myrealbox.com, tchize@myrealbox.com              */
/*****************************************************************************/
/* That code is placed under the GNU General Public Licence (GPL)            */
/*                                                                           */
/* (C) 2001 David Delbecq for the original code version.                     */
/*****************************************************************************/
/*  CrossFire, A Multiplayer game for X-windows                              */
/*                                                                           */
/*  Copyright (C) 2000 Mark Wedel                                            */
/*  Copyright (C) 1992 Frank Tore Johansen                                   */
/*                                                                           */
/*  This program is free software; you can redistribute it and/or modify     */
/*  it under the terms of the GNU General Public License as published by     */
/*  the Free Software Foundation; either version 2 of the License, or        */
/*  (at your option) any later version.                                      */
/*                                                                           */
/*  This program is distributed in the hope that it will be useful,          */
/*  but WITHOUT ANY WARRANTY; without even the implied warranty of           */
/*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            */
/*  GNU General Public License for more details.                             */
/*                                                                           */
/*  You should have received a copy of the GNU General Public License        */
/*  along with this program; if not, write to the Free Software              */
/*  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.                */
/*                                                                           */
/*****************************************************************************/
#ifndef PLUGIN_ANIM_H
#define PLUGIN_ANIM_H

#define PLUGIN_NAME    "Animator"
#define PLUGIN_VERSION "CFAnim Plugin 2.0"

#ifndef __CEXTRACT__
#include <plugin.h>
#include <plugin_common.h>
#endif

#include <plugin_common.h>
enum time_enum {time_second, time_tick};
struct CFanimation_struct;
struct CFmovement_struct;
typedef int (*CFAnimRunFunc) (struct CFanimation_struct* animation, long int id, void* parameters);
typedef long int (*CFAnimInitFunc) (char* name,char* parameters,struct CFmovement_struct*);
typedef struct CFmovement_struct
{
    struct CFanimation_struct* parent;
    CFAnimRunFunc func;
    void* parameters;
    long int id;
    int tick;
    struct CFmovement_struct* next;
} CFmovement;
typedef struct CFanimation_struct
{
    char* name;
    object* victim;
    int paralyze;
    int invisible;
    int wizard;
    int unique;
    int verbose;
    int ghosted;
    int errors_allowed;
    object* corpse;
    long int tick_left;
    enum time_enum time_representation;
    struct CFmovement_struct* nextmovement;
    struct CFanimation_struct* nextanimation;
} CFanimation;
typedef struct
{
    const char *name;
    CFAnimInitFunc funcinit;
    CFAnimRunFunc funcrun;
} CFanimationHook;
extern CFanimationHook animationbox[];
extern int animationcount;
int get_boolean (char* string,int* bool);

typedef struct _cfpcontext
{
    struct _cfpcontext* down;
    object*     who;
    object*     activator;
    object*     third;
    char        message[1024];
    int         fix;
    int         event_code;
    char        options[1024];
    char        script[1024];
    int         returnvalue;
    int         parms[5];
} CFPContext;

extern f_plug_api  gethook;
extern CFPContext* context_stack;
extern CFPContext* current_context;
#include <cfanim_proto.h>

#endif /* PLUGIN_ANIM_H */
