/*****************************************************************************/
/* CFPython - A Python module for Wograld RPG.                             */
/* Version: 2.0beta8 (also known as "Alexander")                             */
/* Contact: yann.chachkoff@myrealbox.com                                     */
/*****************************************************************************/
/* That code is placed under the GNU General Public Licence (GPL)            */
/* (C)2001-2005 by Chachkoff Yann (Feel free to deliver your complaints)     */
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

#include <cfpython.h>
#include <cfpython_archetype_private.h>

static PyObject* Wograld_Archetype_GetName( Wograld_Archetype* whoptr, void* closure)
{
	return Py_BuildValue("s",cf_archetype_get_name(whoptr->arch));
}

static PyObject* Wograld_Archetype_GetNext( Wograld_Archetype* who, PyObject* args )
{
	return Wograld_Archetype_wrap(cf_archetype_get_next(who->arch));
}

static PyObject* Wograld_Archetype_GetMore( Wograld_Archetype* who, PyObject* args )
{
	return Wograld_Archetype_wrap(cf_archetype_get_more(who->arch));
}

static PyObject* Wograld_Archetype_GetHead( Wograld_Archetype* who, PyObject* args )
{
	return Wograld_Archetype_wrap(cf_archetype_get_head(who->arch));
}

static PyObject* Wograld_Archetype_GetClone( Wograld_Archetype* who, PyObject* args )
{
	return Wograld_Object_wrap(cf_archetype_get_clone(who->arch));
}

static PyObject* Wograld_Archetype_GetNewObject( Wograld_Archetype* who, PyObject* args )
{
	return Wograld_Object_wrap(cf_create_object_by_name(cf_archetype_get_name(who->arch)));
}

PyObject *Wograld_Archetype_wrap(archetype *what)
{
    Wograld_Archetype *wrapper;

    /* return None if no object was to be wrapped */
    if(what == NULL) {
        Py_INCREF(Py_None);
        return Py_None;
    }

    wrapper = PyObject_NEW(Wograld_Archetype, &Wograld_ArchetypeType);
    if(wrapper != NULL)
        wrapper->arch = what;
    return (PyObject *)wrapper;
}

static int Wograld_Archetype_InternalCompare(Wograld_Archetype* left, Wograld_Archetype* right)
{
	return (left->arch < right->arch ? -1 : ( left->arch == right->arch ? 0 : 1 ) );
}
