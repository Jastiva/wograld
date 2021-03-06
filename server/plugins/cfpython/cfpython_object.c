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
/*                                                                           */ /*****************************************************************************/
#include <cfpython.h>
#include <cfpython_object_private.h>

static PyObject* Player_GetIP(Wograld_Player* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_player_get_ip(whoptr->obj));
}
static PyObject* Player_GetMarkedItem(Wograld_Player* whoptr, void* closure)
{
    return Wograld_Object_wrap(cf_player_get_marked_item(whoptr->obj));
}
static int Player_SetMarkedItem(Wograld_Player* whoptr, PyObject* value, void* closure)
{
	Wograld_Object* ob;
    if (!PyArg_Parse(value,"O!",&Wograld_ObjectType,&ob))
        return -1;
	cf_player_set_marked_item(whoptr->obj,ob->obj);
	return 0;
}
static PyObject* Wograld_Player_Message( Wograld_Player* who, PyObject* args )
{
    char* message;
    int   color  = NDI_UNIQUE | NDI_ORANGE;

    if (!PyArg_ParseTuple(args,"s|i",&message,&color))
        return NULL;

    cf_player_message(who->obj, message, color);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Player_GetParty(Wograld_Player* whoptr, void* closure)
{
    return Wograld_Party_wrap(cf_player_get_party(whoptr->obj));
}
static int Player_SetParty(Wograld_Player* whoptr, PyObject* value, void* closure)
{
	Wograld_Party* ob;
    if (!PyArg_Parse(value,"O!",&Wograld_PartyType,&ob))
        return -1;
	cf_player_set_party(whoptr->obj,ob->party);
	return 0;
}

static PyObject* Wograld_Player_CanPay( Wograld_Player* who, PyObject* args )
{
    return Py_BuildValue("i", cf_player_can_pay(who->obj));
}

/* Object properties. Get and maybe set. */
static PyObject* Object_GetName(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_query_name(whoptr->obj));
}

static PyObject* Object_GetNamePl(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_query_name_pl(whoptr->obj));
}

static PyObject* Object_GetTitle(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_TITLE ));
}

static PyObject* Object_GetMap(Wograld_Object* whoptr, void* closure)
{
    mapstruct* m;
    m = cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_MAP );
    return Wograld_Map_wrap(m);
}

static PyObject* Object_GetCha(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_CHA));
}

static PyObject* Object_GetCon(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_CON));
}

static PyObject* Object_GetDex(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_DEX));
}

static PyObject* Object_GetInt(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_INT));
}

static PyObject* Object_GetPow(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_POW));
}

static PyObject* Object_GetStr(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_STR));
}

static PyObject* Object_GetWis(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_WIS));
}

static PyObject* Object_GetHP(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_HP));
}

static PyObject* Object_GetMaxHP(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_MAXHP));
}

static PyObject* Object_GetSP(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_SP));
}

static PyObject* Object_GetMaxSP(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_MAXSP));
}

static PyObject* Object_GetGrace(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_GP));
}

static PyObject* Object_GetMaxGrace(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_MAXGP));
}

static PyObject* Object_GetFood(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_FP));
}

static PyObject* Object_GetAC(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_AC));
}

static PyObject* Object_GetWC(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_WC));
}

static PyObject* Object_GetDam(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_DAM));
}

static PyObject* Object_GetLuck(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*( int* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_LUCK));
}

static PyObject* Object_GetMessage(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_MESSAGE));
}

static PyObject* Object_GetExp(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("L",*( sint64* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_EXP));
}

static PyObject* Object_GetSlaying(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",( char* )cf_object_get_property( whoptr->obj, CFAPI_OBJECT_PROP_SLAYING));
}
static PyObject* Object_GetCursed(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CURSED));
}
static PyObject* Object_GetDamned(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", cf_object_get_flag(whoptr->obj, FLAG_DAMNED));
}
static PyObject* Object_GetWeight(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_WEIGHT));
}
static PyObject* Object_GetWeightLimit(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_WEIGHT_LIMIT));
}
static PyObject* Object_GetAbove(Wograld_Object* whoptr, void* closure)
{
    object* op;
    op = cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_OB_ABOVE);
    return Wograld_Object_wrap(op);
}
static PyObject* Object_GetBelow(Wograld_Object* whoptr, void* closure)
{
    object* op;
    op = cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_OB_BELOW);
    return Wograld_Object_wrap(op);
}
static PyObject* Object_GetInventory(Wograld_Object* whoptr, void* closure)
{
    object* op;
    op = cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_INVENTORY);
    return Wograld_Object_wrap(op);
}
static PyObject* Object_GetX(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_X));
}
static PyObject* Object_GetY(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_Y));
}
static PyObject* Object_GetDirection(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(char*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_DIRECTION));
}
static PyObject* Object_GetFacing(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(char*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_FACING));
}
static PyObject* Object_GetUnaggressive(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_UNAGGRESSIVE));
}
static PyObject* Object_GetGod(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_GOD));
}
static PyObject* Object_GetPickable(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",!cf_object_get_flag(whoptr->obj, FLAG_NO_PICK));
}
static PyObject* Object_GetQuantity(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_NROF));
}
static PyObject* Object_GetInvisible(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj,
                         CFAPI_OBJECT_PROP_INVISIBLE));
}
static PyObject* Object_GetSpeed(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("d", cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_SPEED));
}
static PyObject* Object_GetLastSP(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(sint16*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_LAST_SP));
}
static PyObject* Object_GetLastGrace(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(sint16*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_LAST_GRACE));
}
static PyObject* Object_GetLevel(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_LEVEL));
}
static PyObject* Object_GetFace(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", *(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_FACE));
}
static PyObject* Object_GetAttackType(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i", cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_ATTACK_TYPE));
}
static PyObject* Object_GetBeenApplied(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_BEEN_APPLIED));
}
static PyObject* Object_GetIdentified(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_IDENTIFIED));
}
static PyObject* Object_GetAlive(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_ALIVE));
}
static PyObject* Object_GetDM(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_WIZ));
}
static PyObject* Object_GetWasDM(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_WAS_WIZ));
}
static PyObject* Object_GetApplied(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_APPLIED));
}
static PyObject* Object_GetUnpaid(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_UNPAID));
}
static PyObject* Object_GetFlying(Wograld_Object* whoptr, void* closure)
{
    /* FIXME */
    /*return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_FLYING));*/
}
static PyObject* Object_GetMonster(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_MONSTER));
}
static PyObject* Object_GetFriendly(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_FRIENDLY));
}
static PyObject* Object_GetGenerator(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_GENERATOR));
}
static PyObject* Object_GetThrown(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_IS_THROWN));
}
static PyObject* Object_GetCanSeeInvisible(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_SEE_INVISIBLE));
}
static PyObject* Object_GetRollable(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CAN_ROLL));
}
static PyObject* Object_GetTurnable(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_IS_TURNABLE));
}
static PyObject* Object_GetUsedUp(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_IS_USED_UP));
}
static PyObject* Object_GetSplitting(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_SPLITTING));
}
static PyObject* Object_GetBlind(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_BLIND));
}
static PyObject* Object_GetCanUseHorn(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_HORN));
}
static PyObject* Object_GetCanUseRod(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_ROD));
}
static PyObject* Object_GetCanUseSkill(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CAN_USE_SKILL));
}
static PyObject* Object_GetKnownCursed(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_KNOWN_CURSED));
}
static PyObject* Object_GetStealthy(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_STEALTH));
}
static PyObject* Object_GetConfused(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CONFUSED));
}
static PyObject* Object_GetSleeping(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_SLEEP));
}
static PyObject* Object_GetLifesaver(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_LIFESAVE));
}
static PyObject* Object_GetFloor(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_IS_FLOOR));
}
static PyObject* Object_GetHasXRays(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_XRAYS));
}
static PyObject* Object_GetCanUseRing(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_RING));
}
static PyObject* Object_GetCanUseBow(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_BOW));
}
static PyObject* Object_GetCanUseWand(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_RANGE));
}
static PyObject* Object_GetCanSeeInDark(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_SEE_IN_DARK));
}
static PyObject* Object_GetKnownMagical(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_KNOWN_MAGICAL));
}
static PyObject* Object_GetCanUseWeapon(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_WEAPON));
}
static PyObject* Object_GetCanUseArmour(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_ARMOUR));
}
static PyObject* Object_GetCanUseScroll(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_USE_SCROLL));
}
static PyObject* Object_GetCanCastSpell(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CAST_SPELL));
}
static PyObject* Object_GetReflectSpells(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_REFL_SPELL));
}
static PyObject* Object_GetReflectMissiles(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_REFL_MISSILE));
}
static PyObject* Object_GetUnique(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_UNIQUE));
}
static PyObject* Object_GetCanPickUp(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_PICK_UP));
}
static PyObject* Object_GetCanPassThru(Wograld_Object* whoptr, void* closure)
{
    /* FIXME */
    /*return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_CAN_PASS_THRU));*/
}
static PyObject* Object_GetRunAway(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_RUN_AWAY));
}
static PyObject* Object_GetScared(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_SCARED));
}
static PyObject* Object_GetUndead(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_UNDEAD));
}
static PyObject* Object_GetBlocksView(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_BLOCKSVIEW));
}
static PyObject* Object_GetHitBack(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_HITBACK));
}
static PyObject* Object_GetStandStill(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_STAND_STILL));
}
static PyObject* Object_GetOnlyAttack(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_ONLY_ATTACK));
}
static PyObject* Object_GetMakeInvisible(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_get_flag(whoptr->obj, FLAG_MAKE_INVIS));
}
static PyObject* Object_GetMoney(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",cf_object_query_money(whoptr->obj));
}
static PyObject* Object_GetType(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("i",*(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_TYPE));
}
static PyObject* Object_GetValue(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("l",*(int*)cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_VALUE));
}
static PyObject* Object_GetArchName(Wograld_Object* whoptr, void* closure)
{
    return Py_BuildValue("s",cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_ARCH_NAME));
}
static PyObject* Object_GetArchetype(Wograld_Object* whoptr, void* closure)
{
	return Wograld_Archetype_wrap(cf_object_get_property(whoptr->obj, CFAPI_OBJECT_PROP_ARCHETYPE));
}


static int Object_SetMessage(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;
    if (value==NULL)
    {
        PyErr_SetString(PyExc_TypeError, "Cannot delete the Message attribute");
        return -1;
    }
    if (!PyString_Check(value))
    {
        PyErr_SetString(PyExc_TypeError, "The Message attribute must be a string");
        return -1;
    }
    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_MESSAGE, val);
    return 0;
}
static int Object_SetName(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;
    if (value==NULL)
    {
        PyErr_SetString(PyExc_TypeError, "Cannot delete the Name attribute");
        return -1;
    }
    if (!PyString_Check(value))
    {
        PyErr_SetString(PyExc_TypeError, "The Name attribute must be a string");
        return -1;
    }
    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_NAME, val);
    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_NAME_PLURAL, val);
    return 0;
}
static int Object_SetNamePl(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;
    if (value==NULL)
    {
        PyErr_SetString(PyExc_TypeError, "Cannot delete the NamePl attribute");
        return -1;
    }
    if (!PyString_Check(value))
    {
        PyErr_SetString(PyExc_TypeError, "The NamePl attribute must be a string");
        return -1;
    }
    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_NAME_PLURAL, val);
    return 0;
}
static int Object_SetTitle(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;
    if (value==NULL)
    {
        PyErr_SetString(PyExc_TypeError, "Cannot delete the Title attribute");
        return -1;
    }
    if (!PyString_Check(value))
    {
        PyErr_SetString(PyExc_TypeError, "The Title attribute must be a string");
        return -1;
    }
    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_TITLE, val);
    return 0;
}
static int Object_SetMap(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    Wograld_Map* val;

    if (!PyArg_Parse(value,"O",&val))
        return -1;

    cf_object_change_map(whoptr->obj, -1, -1, val->map);
    return 0;
}
static int Object_SetSlaying(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;
    if (value==NULL)
    {
        PyErr_SetString(PyExc_TypeError, "Cannot delete the Slaying attribute");
        return -1;
    }
    if (!PyString_Check(value))
    {
        PyErr_SetString(PyExc_TypeError, "The Slaying attribute must be a string");
        return -1;
    }
    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_SLAYING, val);
    return 0;
}
static int Object_SetCursed(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_CURSED, val);
    return 0;
}
static int Object_SetDamned(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_DAMNED, val);
    return 0;
}
static int Object_SetStr(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_STR, val);
/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetDex(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_DEX, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetCon(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_CON, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetInt(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_INT, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetPow(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_POW, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetWis(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_WIS, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetCha(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_CHA, val);
    /*cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetHP(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_HP, val);
    return 0;
}
static int Object_SetMaxHP(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_MAXHP, val);
    return 0;
}
static int Object_SetSP(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_SP, val);
    return 0;
}
static int Object_SetMaxSP(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_MAXSP, val);
    return 0;
}
static int Object_SetGrace(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_GP, val);
    return 0;
}
static int Object_SetMaxGrace(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_MAXGP, val);
    return 0;
}
static int Object_SetAC(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_AC, val);
    return 0;
}
static int Object_SetWC(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_WC, val);
    return 0;
}
static int Object_SetDam(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_DAM, val);
    return 0;
}
static int Object_SetFood(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_FP, val);
    return 0;
}
static int Object_SetWeight(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_WEIGHT, val);
    return 0;
}
static int Object_SetWeightLimit(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_WEIGHT_LIMIT, val);
    return 0;
}
static int Object_SetDirection(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_DIRECTION, val);
    return 0;
}
static int Object_SetFacing(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_FACING, val);
    return 0;
}
static int Object_SetGod(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* val;

    if (!PyArg_Parse(value,"s",&val))
        return -1;

    cf_object_set_string_property(whoptr->obj, CFAPI_OBJECT_PROP_GOD, val);
    return 0;
}
static int Object_SetSpeed(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"d",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_SPEED, val);
/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetQuantity(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    if (cf_object_set_nrof(whoptr->obj, val) != 0) {
        PyErr_SetString(PyExc_TypeError, "Invalid quantity");
        return -1;
    }

/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetLastSP(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_LAST_SP, val);
/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetLastGrace(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_LAST_GRACE, val);
/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetFace(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    char* txt;

    if (!PyArg_ParseTuple(value,"s",&txt))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_FACE, cf_find_animation(txt));
    return 0;
}
static int Object_SetAttackType(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_ATTACK_TYPE, val);
/*    cf_fix_object(whoptr->obj);*/
    return 0;
}
static int Object_SetUnaggressive(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_UNAGGRESSIVE, val);
    return 0;
}
static int Object_SetPickable(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_NO_PICK, !val);
    return 0;
}
static int Object_SetInvisible(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_ParseTuple(value,"i",&val))
        return -1;

    cf_object_set_int_property(whoptr->obj, CFAPI_OBJECT_PROP_INVISIBLE, val);
    return 0;
}
static int Object_SetFlying(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;
    /* FIXME */
    /*cf_object_set_flag(whoptr->obj, FLAG_FLYING, val);*/
    return 0;
}
static int Object_SetUnpaid(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_UNPAID, val);
    return 0;
}
static int Object_SetFriendly(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_FRIENDLY, val);
    return 0;
}
static int Object_SetCanSeeInvisible(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_SEE_INVISIBLE, val);
    return 0;
}
static int Object_SetRollable(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_CAN_ROLL, val);
    return 0;
}
static int Object_SetTurnable(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_IS_TURNABLE, val);
    return 0;
}
static int Object_SetUsedUp(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_IS_USED_UP, val);
    return 0;
}
static int Object_SetBlind(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_BLIND, val);
    return 0;
}
static int Object_SetKnownCursed(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_KNOWN_CURSED, val);
    return 0;
}
static int Object_SetStealthy(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_STEALTH, val);
    return 0;
}
static int Object_SetConfused(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_CONFUSED, val);
    return 0;
}
static int Object_SetSleeping(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_SLEEP, val);
    return 0;
}
static int Object_SetLifesaver(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_LIFESAVE, val);
    return 0;
}
static int Object_SetHasXRays(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_XRAYS, val);
    return 0;
}
static int Object_SetCanSeeInDark(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_SEE_IN_DARK, val);
    return 0;
}
static int Object_SetKnownMagical(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_KNOWN_MAGICAL, val);
    return 0;
}
static int Object_SetReflectSpells(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_REFL_SPELL, val);
    return 0;
}
static int Object_SetReflectMissiles(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_REFL_MISSILE, val);
    return 0;
}
static int Object_SetUnique(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_UNIQUE, val);
    return 0;
}
static int Object_SetCanPassThru(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;
    /* FIXME */
    /*cf_object_set_flag(whoptr->obj, FLAG_CAN_PASS_THRU, val);*/
    return 0;
}
static int Object_SetRunAway(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_RUN_AWAY, val);
    return 0;
}
static int Object_SetScared(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_SCARED, val);
    return 0;
}
static int Object_SetUndead(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_UNDEAD, val);
    return 0;
}
static int Object_SetBlocksView(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_BLOCKSVIEW, val);
    return 0;
}
static int Object_SetHitBack(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_HITBACK, val);
    return 0;
}
static int Object_SetStandStill(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_STAND_STILL, val);
    return 0;
}
static int Object_SetOnlyAttack(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_ONLY_ATTACK, val);
    return 0;
}
static int Object_SetMakeInvisible(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    int val;

    if (!PyArg_Parse(value,"i",&val))
        return -1;

    cf_object_set_flag(whoptr->obj, FLAG_MAKE_INVIS, val);
    return 0;
}
static int Object_SetValue(Wograld_Object* whoptr, PyObject* value, void* closure)
{
    long val;

    if (!PyArg_Parse(value,"l",&val))
        return -1;

    cf_object_set_long_property(whoptr->obj, CFAPI_OBJECT_PROP_VALUE, val);
    return 0;
}

/* Methods. */

static PyObject* Wograld_Object_Remove( Wograld_Object* who, PyObject* args )
{
    if (!PyArg_ParseTuple(args,"",NULL))
        return NULL;

    if (((Wograld_Object*)current_context->who)->obj == who->obj)
        current_context->who = NULL;

    cf_object_remove(who->obj);

    if (current_context->activator != NULL &&
        ((Wograld_Object*)current_context->activator)->obj->type == PLAYER)
        cf_player_send_inventory(((Wograld_Object*)current_context->activator)->obj);

    cf_object_free(who->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Apply( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* whoptr;
    int flags;

    if (!PyArg_ParseTuple(args,"Oi",&whoptr,&flags))
        return NULL;

    cf_object_apply(whoptr->obj, who->obj, flags);

    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Drop( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* whoptr;

    if (!PyArg_ParseTuple(args,"O",&whoptr))
        return NULL;

    cf_object_drop(whoptr->obj, who->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Fix( Wograld_Object* who, PyObject* args )
{
    cf_fix_object(who->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Pickup( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* what;

    if (!PyArg_ParseTuple(args,"O",&what))
        return NULL;

    cf_object_pickup(who->obj, what->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Take( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* whoptr;

    if (!PyArg_ParseTuple(args,"O",&whoptr))
        return NULL;

    cf_object_take(whoptr->obj, who->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_Teleport( Wograld_Object* who, PyObject* args )
{
    Wograld_Map* where;
    int x, y;
    int val;

    if (!PyArg_ParseTuple(args,"Oii",&where,&x,&y))
        return NULL;

    val = cf_object_teleport(who->obj, where->map, x, y);

    return Py_BuildValue("i",val);
}
static PyObject* Wograld_Object_ActivateRune( Wograld_Object* who, PyObject* args )
{
    object* trap;
    object* victim;
    Wograld_Object* pcause;

    if (!PyArg_ParseTuple(args,"O",&pcause))
        return NULL;
    trap = who->obj;
    victim = pcause->obj;
    cf_object_activate_rune(trap, victim);
    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_CheckTrigger( Wograld_Object* who, PyObject* args )
{
    object* trigger;
    object* cause;
    int result;
    Wograld_Object* pcause;

    if (!PyArg_ParseTuple(args,"O",&pcause))
        return NULL;
    trigger = who->obj;
    cause = pcause->obj;
    result = cf_object_check_trigger(trigger, cause);

    return Py_BuildValue("i", result);
}

static PyObject* Wograld_Object_Say( Wograld_Object* who, PyObject* args )
{
    char* message;
    if ( !PyArg_ParseTuple( args, "s", &message ) )
        return NULL;
    cf_object_say(who->obj, message);
    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_Speak( Wograld_Object* who, PyObject* args )
{
    char* message;
    if ( !PyArg_ParseTuple( args, "s", &message ) )
        return NULL;
    cf_object_speak(who->obj, message);
    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_Reposition( Wograld_Object* who, PyObject* args )
{
    int x, y;

    if (!PyArg_ParseTuple(args,"ii",&x,&y))
        return NULL;

    cf_object_transfer(who->obj,x,y,0,NULL);
    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_QueryName( Wograld_Object* who, PyObject* args )
{
    return Py_BuildValue("s",cf_query_name(who->obj));
}

static PyObject* Wograld_Object_GetResist( Wograld_Object* who, PyObject* args )
{
    int resist;
    if ( !PyArg_ParseTuple( args, "l", &resist ) )
        return NULL;
    if ( ( resist < 0 ) || ( resist >= NROFATTACKS ) )
    {
        return Py_BuildValue("l",0);
    }
    return Py_BuildValue("i",cf_object_get_resistance( who->obj, resist));
}
static PyObject* Wograld_Object_QueryCost( Wograld_Object* who, PyObject* args )
{
    int flags;
    Wograld_Object* pcause;

    if (!PyArg_ParseTuple(args,"Oi",&pcause,&flags))
        return NULL;
    return Py_BuildValue("i",cf_object_query_cost(who->obj, pcause->obj, flags));
}
static PyObject* Wograld_Object_Cast( Wograld_Object* who, PyObject* args )
{
    int dir;
    char* op;
    Wograld_Object* pspell;

    if (!PyArg_ParseTuple(args,"Ois",&pspell, &dir,&op))
        return NULL;

    cf_object_cast_spell(who->obj, who->obj, dir, pspell->obj, op);

    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_LearnSpell( Wograld_Object* who, PyObject* args )
{

    Wograld_Object* pspell;

    if (!PyArg_ParseTuple(args,"O",&pspell))
        return NULL;

    cf_object_learn_spell(who->obj, pspell->obj);

    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_ForgetSpell( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* pspell;

    if (!PyArg_ParseTuple(args,"O",&pspell))
        return NULL;

    cf_object_forget_spell(who->obj, pspell->obj);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_KnowSpell( Wograld_Object* who, PyObject* args )
{
    char *spellname;
    object *op;

    if (!PyArg_ParseTuple(args,"s",&spellname))
        return NULL;

    op = cf_object_check_for_spell(who->obj, spellname);

    if (op != NULL)
        return Wograld_Object_wrap(op);
    else
    {
        Py_INCREF(Py_None);
        return Py_None;
    }
}

static PyObject* Wograld_Object_CastAbility( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* pspell;
    int dir;
    char* str;

    if (!PyArg_ParseTuple(args,"Ois",&pspell,&dir,&str))
        return NULL;

    cf_object_cast_ability(who->obj,who->obj,dir,pspell->obj,str);

    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_PayAmount( Wograld_Object* who, PyObject* args )
{
    uint64 to_pay;
    int val;

    if (!PyArg_ParseTuple(args,"L",&to_pay))
        return NULL;

    val = cf_object_pay_amount(who->obj, to_pay);

    return Py_BuildValue("i",val);
}
static PyObject* Wograld_Object_Pay( Wograld_Object* who, PyObject* args )
{
    Wograld_Object* op;
    int val;

    if (!PyArg_ParseTuple(args,"O",&op))
        return NULL;

    val = cf_object_pay_item(who->obj, op->obj);

    return Py_BuildValue("i",val);
}
static PyObject* Wograld_Object_ReadKey( Wograld_Object* who, PyObject* args )
{
    char* val;
    char* keyname;

    if (!PyArg_ParseTuple(args,"s",&keyname))
        return NULL;

    val = cf_object_get_key(who->obj, keyname);

    return Py_BuildValue("s",val ? val : "");
}
static PyObject* Wograld_Object_WriteKey( Wograld_Object* who, PyObject* args )
{
    char* keyname;
    char* value;
    int add_key = 0;

    if (!PyArg_ParseTuple(args,"ss|i",&keyname,&value,&add_key))
        return NULL;

    cf_object_set_key(who->obj, keyname, value, add_key);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_CheckInventory( Wograld_Object* who, PyObject* args )
{
    char* whatstr;
    object* foundob;

    if (!PyArg_ParseTuple(args,"s",&whatstr))
        return NULL;

    foundob = cf_object_present_archname_inside(who->obj, whatstr);

    if (foundob != NULL)
        return Wograld_Object_wrap(foundob);
/*    for(tmp = WHO->inv; tmp; tmp = tmp->below)
    {
        if (!strncmp(PyQueryName(tmp),whatstr,strlen(whatstr)))
        {
            return Py_BuildValue("l",(long)(tmp));
        };
        if (!strncmp(tmp->name,whatstr,strlen(whatstr)))
        {
            return Py_BuildValue("l",(long)(tmp));
        };
    };

    return Py_BuildValue("l",(long)0);*/

    Py_INCREF(Py_None);
    return Py_None;
}

static PyObject* Wograld_Object_CheckArchInventory( Wograld_Object* who, PyObject* args )
{
    char* whatstr;
    object* tmp;

    if (!PyArg_ParseTuple(args,"s",&whatstr))
        return NULL;

    for(tmp = who->obj->inv; tmp != NULL; tmp = tmp->below)
    {
        if (!strcmp(tmp->arch->name,whatstr))
            break;
    }
    if (tmp != NULL)
        return Wograld_Object_wrap(tmp);
    Py_INCREF(Py_None);
    return Py_None;
}
static PyObject* Wograld_Object_GetOutOfMap(Wograld_Object* who, PyObject* args)
{
    int x, y;

    if (!PyArg_ParseTuple(args,"ii",&x,&y))
        return NULL;

    return Py_BuildValue("i", cf_object_out_of_map(who->obj,x,y));
}
static PyObject* Wograld_Object_CreateInside(Wograld_Object* who, PyObject* args)
{
    char* txt;
    object* myob;

    if (!PyArg_ParseTuple(args,"s",&txt))
        return NULL;

    myob = cf_create_object_by_name(txt);
    myob = cf_object_insert_object(myob, who->obj);

    return Wograld_Object_wrap(myob);

}
static PyObject* Wograld_Object_InsertInto(Wograld_Object* who, PyObject* args)
{
    Wograld_Object* op;
    object* myob;

    if (!PyArg_ParseTuple(args,"O",&op))
        return NULL;

    myob = cf_object_insert_in_ob(who->obj, op->obj);

    return Wograld_Object_wrap(myob);
}

static int Wograld_Object_InternalCompare(Wograld_Object* left, Wograld_Object* right)
{
	return (left->obj < right->obj ? -1 : ( left->obj == right->obj ? 0 : 1 ) );
}

/* Legacy code: convert to long so that non-object functions work correctly */
static PyObject* Wograld_Object_Long( PyObject* obj )
{
    return Py_BuildValue("l", ((Wograld_Object*)obj)->obj);
}

static PyObject* Wograld_Object_Int( PyObject* obj )
{
    return Py_BuildValue("i", ((Wograld_Object*)obj)->obj);
}

/**
 * Python initialized.
 **/
static PyObject *
        Wograld_Object_new(PyTypeObject *type, PyObject *args, PyObject *kwds)
{
    Wograld_Object *self;

    self = (Wograld_Object *)type->tp_alloc(type, 0);
    if(self)
        self->obj = NULL;

    return (PyObject *)self;
}
static PyObject *
        Wograld_Player_new(PyTypeObject *type, PyObject *args, PyObject *kwds)
{
    Wograld_Player *self;

    self = (Wograld_Player *)type->tp_alloc(type, 0);
    if(self)
        self->obj = NULL;

    return (PyObject *)self;
}

PyObject *Wograld_Object_wrap(object *what)
{
    Wograld_Object *wrapper;
    Wograld_Player *plwrap;

    /* return None if no object was to be wrapped */
    if(what == NULL) {
        Py_INCREF(Py_None);
        return Py_None;
    }

    if (what->type == PLAYER)
    {
        plwrap = PyObject_NEW(Wograld_Player, &Wograld_PlayerType);
        if(plwrap != NULL)
            plwrap->obj = what;
        return (PyObject *)plwrap;
    }
    else
    {
        wrapper = PyObject_NEW(Wograld_Object, &Wograld_ObjectType);
        if(wrapper != NULL)
            wrapper->obj = what;
        return (PyObject *)wrapper;
    }
}
