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
#include <cfpython_party_private.h>

static PyObject* Wograld_Party_GetName( Wograld_Party* partyptr, void* closure)
{
    return Py_BuildValue("s",cf_party_get_name(partyptr->party));
}

static PyObject* Wograld_Party_GetPassword( Wograld_Party* partyptr, void* closure)
{
    return Py_BuildValue("s",cf_party_get_password(partyptr->party));
}

static PyObject* Wograld_Party_GetNext( Wograld_Party* party, PyObject* args )
{
    return Wograld_Party_wrap(cf_party_get_next(party->party));
}

static PyObject* Wograld_Party_GetPlayers( Wograld_Party* party, PyObject* args )
{
    PyObject* list;
    player* pl;

    list = PyList_New(0);
    pl = cf_party_get_first_player(party->party);
    while (pl)
    {
        PyList_Append(list,Wograld_Object_wrap(pl->ob));
        pl = cf_party_get_next_player(party->party,pl);
    }
    return list;
}

PyObject *Wograld_Party_wrap(partylist *what)
{
    Wograld_Party *wrapper;

    /* return None if no object was to be wrapped */
    if(what == NULL) {
        Py_INCREF(Py_None);
        return Py_None;
    }

    wrapper = PyObject_NEW(Wograld_Party, &Wograld_PartyType);
    if(wrapper != NULL)
        wrapper->party = what;
    return (PyObject *)wrapper;
}

static int Wograld_Party_InternalCompare(Wograld_Party* left, Wograld_Party* right)
{
    return (left->party < right->party ? -1 : ( left->party == right->party ? 0 : 1 ) );
}
