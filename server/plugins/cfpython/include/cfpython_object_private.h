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
static PyObject* Object_GetName(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetNamePl(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetTitle(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMap(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCha(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCon(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetDex(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetInt(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetPow(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetStr(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetWis(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetHP(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMaxHP(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetSP(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMaxSP(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetGrace(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMaxGrace(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFood(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetAC(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetWC(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetDam(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetLuck(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMessage(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetExp(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetSlaying(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCursed(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetDamned(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetWeight(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetWeightLimit(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetAbove(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetBelow(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetInventory(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetX(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetY(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetDirection(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFacing(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetUnaggressive(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetGod(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetPickable(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetQuantity(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetInvisible(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetSpeed(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetLastSP(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetLastGrace(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetLevel(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFace(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetAttackType(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetBeenApplied(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetIdentified(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetAlive(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetDM(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetWasDM(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetApplied(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetUnpaid(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFlying(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMonster(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFriendly(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetGenerator(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetThrown(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanSeeInvisible(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetRollable(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetTurnable(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetUsedUp(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetSplitting(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetBlind(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseHorn(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseRod(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseSkill(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetKnownCursed(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetStealthy(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetConfused(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetSleeping(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetLifesaver(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetFloor(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetHasXRays(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseRing(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseBow(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseWand(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanSeeInDark(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetKnownMagical(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseWeapon(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseArmour(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanUseScroll(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanCastSpell(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetReflectSpells(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetReflectMissiles(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetUnique(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanPickUp(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetCanPassThru(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetRunAway(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetScared(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetUndead(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetBlocksView(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetHitBack(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetStandStill(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetOnlyAttack(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMakeInvisible(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetMoney(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetType(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetValue(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetArchName(Wograld_Object* whoptr, void* closure);
static PyObject* Object_GetArchetype(Wograld_Object* whoptr, void* closure);

static int Object_SetMessage(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetName(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetNamePl(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetTitle(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetMap(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetSlaying(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCursed(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetDamned(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetStr(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetDex(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCon(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetInt(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetPow(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetWis(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCha(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetHP(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetMaxHP(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetSP(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetMaxSP(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetGrace(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetMaxGrace(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetDam(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetFood(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetAC(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetWC(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetWeight(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetWeightLimit(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetDirection(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetFacing(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetGod(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetSpeed(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetQuantity(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetLastSP(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetLastGrace(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetAttackType(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetUnaggressive(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetPickable(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetInvisible(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetFlying(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetUnpaid(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetFriendly(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCanSeeInvisible(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetRollable(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetTurnable(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetUsedUp(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetBlind(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetKnownCursed(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetStealthy(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetConfused(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetSleeping(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetLifesaver(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetHasXRays(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCanSeeInDark(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetKnownMagical(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetReflectSpells(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetReflectMissiles(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetUnique(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetCanPassThru(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetRunAway(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetScared(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetUndead(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetBlocksView(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetHitBack(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetStandStill(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetOnlyAttack(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetMakeInvisible(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetValue(Wograld_Object* whoptr, PyObject* value, void* closure);
static int Object_SetFace(Wograld_Object* whoptr, PyObject* value, void* closure);

static PyObject* Wograld_Object_Remove( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Apply( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Drop( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Fix( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Say( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Speak( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Pickup( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Take( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Teleport( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Reposition( Wograld_Object* who, PyObject* args );

static PyObject* Wograld_Object_QueryName( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_GetResist( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_ActivateRune( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_CheckTrigger( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_QueryCost( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Cast( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_LearnSpell( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_ForgetSpell( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_KnowSpell( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_CastAbility( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_PayAmount( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_Pay( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_CheckInventory( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_CheckArchInventory( Wograld_Object* who, PyObject* args );
static PyObject* Wograld_Object_GetOutOfMap(Wograld_Object* whoptr, PyObject* args);
static PyObject* Wograld_Object_CreateInside(Wograld_Object* who, PyObject* args);
static PyObject* Wograld_Object_InsertInto(Wograld_Object* who, PyObject* args);
static PyObject* Wograld_Object_ReadKey(Wograld_Object* who, PyObject* args);
static PyObject* Wograld_Object_WriteKey(Wograld_Object* who, PyObject* args);

static int Wograld_Object_InternalCompare(Wograld_Object* left, Wograld_Object* right);

static PyObject* Wograld_Object_Long( PyObject* obj );
static PyObject* Wograld_Object_Int( PyObject* obj );
static PyObject *Wograld_Object_new(PyTypeObject *type, PyObject *args, PyObject *kwds);

/* Python binding */
static PyGetSetDef Object_getseters[] = {
    { "Name",       (getter)Object_GetName,     (setter)Object_SetName, NULL, NULL },
    { "NamePl",     (getter)Object_GetNamePl,   (setter)Object_SetNamePl, NULL, NULL },
    { "Title",      (getter)Object_GetTitle,    (setter)Object_SetTitle, NULL, NULL },
    { "Map",        (getter)Object_GetMap,      (setter)Object_SetMap, NULL, NULL },
    { "Cha",        (getter)Object_GetCha,      (setter)Object_SetCha, NULL, NULL },
    { "Con",        (getter)Object_GetCon,      (setter)Object_SetCon, NULL, NULL },
    { "Dex",        (getter)Object_GetDex,      (setter)Object_SetDex, NULL, NULL },
    { "Int",        (getter)Object_GetInt,      (setter)Object_SetInt, NULL, NULL },
    { "Pow",        (getter)Object_GetPow,      (setter)Object_SetPow, NULL, NULL },
    { "Str",        (getter)Object_GetStr,      (setter)Object_SetStr, NULL, NULL },
    { "Wis",        (getter)Object_GetWis,      (setter)Object_SetWis, NULL, NULL },
    { "HP",         (getter)Object_GetHP,       (setter)Object_SetHP, NULL, NULL },
    { "MaxHP",      (getter)Object_GetMaxHP,    (setter)Object_SetMaxHP, NULL, NULL },
    { "SP",         (getter)Object_GetSP,       (setter)Object_SetSP, NULL, NULL },
    { "MaxSP",      (getter)Object_GetMaxSP,    (setter)Object_SetMaxSP, NULL, NULL },
    { "Grace",      (getter)Object_GetGrace,    (setter)Object_SetGrace, NULL, NULL },
    { "MaxGrace",   (getter)Object_GetMaxGrace, (setter)Object_SetMaxGrace, NULL, NULL },
    { "Food",       (getter)Object_GetFood,     (setter)Object_SetFood, NULL, NULL },
    { "AC",         (getter)Object_GetAC,       (setter)Object_SetAC, NULL, NULL },
    { "WC",         (getter)Object_GetWC,       (setter)Object_SetWC, NULL, NULL },
    { "Dam",        (getter)Object_GetDam,      (setter)Object_SetDam, NULL, NULL },
    { "Luck",       (getter)Object_GetLuck,     NULL, NULL, NULL },
    { "Exp",        (getter)Object_GetExp,      NULL, NULL, NULL },
    { "Message",    (getter)Object_GetMessage,  (setter)Object_SetMessage, NULL, NULL },
    { "Slaying",    (getter)Object_GetSlaying,  (setter)Object_SetSlaying, NULL, NULL },
    { "Cursed",     (getter)Object_GetCursed,   (setter)Object_SetCursed, NULL, NULL },
    { "Damned",     (getter)Object_GetDamned,   (setter)Object_SetDamned, NULL, NULL },
    { "Weight",     (getter)Object_GetWeight,   (setter)Object_SetWeight, NULL, NULL },
    { "WeightLimit",(getter)Object_GetWeightLimit,(setter)Object_SetWeightLimit, NULL, NULL },
    { "Above",      (getter)Object_GetAbove,    NULL, NULL, NULL },
    { "Below",      (getter)Object_GetBelow,    NULL, NULL, NULL },
    { "Inventory",  (getter)Object_GetInventory,NULL, NULL, NULL },
    { "X",          (getter)Object_GetX,        NULL, NULL, NULL },
    { "Y",          (getter)Object_GetY,        NULL, NULL, NULL },
    { "Direction",  (getter)Object_GetDirection,(setter)Object_SetDirection, NULL, NULL },
    { "Facing",     (getter)Object_GetFacing,   (setter)Object_SetFacing, NULL, NULL },
    { "Unaggressive",(getter)Object_GetUnaggressive, (setter)Object_SetUnaggressive, NULL, NULL },
    { "God",        (getter)Object_GetGod,      (setter)Object_SetGod, NULL, NULL },
    { "Pickable",   (getter)Object_GetPickable, (setter)Object_SetPickable, NULL, NULL },
    { "Quantity",   (getter)Object_GetQuantity, (setter)Object_SetQuantity, NULL, NULL },
    { "Invisible",  (getter)Object_GetInvisible,(setter)Object_SetInvisible, NULL, NULL },
    { "Speed",      (getter)Object_GetSpeed,    (setter)Object_SetSpeed, NULL, NULL },
    { "LastSP",     (getter)Object_GetLastSP,   (setter)Object_SetLastSP, NULL, NULL },
    { "LastGrace",  (getter)Object_GetLastGrace,(setter)Object_SetLastGrace, NULL, NULL },
    { "Level",      (getter)Object_GetLevel,    NULL, NULL, NULL },
    { "Face",       (getter)Object_GetFace,     (setter)Object_SetFace, NULL, NULL },
    { "AttackType", (getter)Object_GetAttackType, (setter)Object_SetAttackType, NULL, NULL },
    { "BeenApplied",(getter)Object_GetBeenApplied,NULL, NULL, NULL },
    { "Identified", (getter)Object_GetIdentified, NULL, NULL, NULL },
    { "Alive",      (getter)Object_GetAlive,    NULL, NULL, NULL },
    { "DungeonMaster",(getter)Object_GetDM,     NULL, NULL, NULL },
    { "WasDungeonMaster",(getter)Object_GetWasDM, NULL, NULL, NULL },
    { "Applied",    (getter)Object_GetApplied,  NULL, NULL, NULL },
    { "Unpaid",     (getter)Object_GetUnpaid,   (setter)Object_SetUnpaid, NULL, NULL },
    { "Flying",     (getter)Object_GetFlying,   (setter)Object_SetFlying, NULL, NULL },
    { "Monster",    (getter)Object_GetMonster,  NULL, NULL, NULL },
    { "Friendly",   (getter)Object_GetFriendly, (setter)Object_SetFriendly, NULL, NULL },
    { "Generator",  (getter)Object_GetGenerator,NULL, NULL, NULL },
    { "Thrown",     (getter)Object_GetThrown,   NULL, NULL, NULL },
    { "CanSeeInvisible", (getter)Object_GetCanSeeInvisible, (setter)Object_SetCanSeeInvisible, NULL, NULL },
    { "Rollable",   (getter)Object_GetRollable, (setter)Object_SetRollable, NULL, NULL },
    { "Turnable",   (getter)Object_GetTurnable, (setter)Object_SetTurnable, NULL, NULL },
    { "UsedUp",     (getter)Object_GetUsedUp,   (setter)Object_SetUsedUp, NULL, NULL },
    { "Splitting",  (getter)Object_GetSplitting, NULL, NULL, NULL },
    { "Blind",          (getter)Object_GetBlind,        (setter)Object_SetBlind, NULL, NULL },
    { "CanUseHorn",     (getter)Object_GetCanUseHorn,   NULL, NULL, NULL },
    { "CanUseRod",      (getter)Object_GetCanUseRod,    NULL, NULL, NULL },
    { "CanUseSkill",    (getter)Object_GetCanUseSkill,  NULL, NULL, NULL },
    { "KnownCursed",    (getter)Object_GetKnownCursed,  (setter)Object_SetKnownCursed, NULL, NULL },
    { "Stealthy",       (getter)Object_GetStealthy,     (setter)Object_SetStealthy, NULL, NULL },
    { "Confused",       (getter)Object_GetConfused,     (setter)Object_SetConfused, NULL, NULL },
    { "Sleeping",       (getter)Object_GetSleeping,     (setter)Object_SetSleeping, NULL, NULL },
    { "Lifesaver",      (getter)Object_GetLifesaver,    (setter)Object_SetLifesaver, NULL, NULL },
    { "Floor",          (getter)Object_GetFloor,        NULL, NULL, NULL },
    { "HasXRays",       (getter)Object_GetHasXRays,     (setter)Object_SetHasXRays, NULL, NULL },
    { "CanUseRing",     (getter)Object_GetCanUseRing,   NULL, NULL, NULL },
    { "CanUseBow",      (getter)Object_GetCanUseBow,    NULL, NULL, NULL },
    { "CanUseWand",     (getter)Object_GetCanUseWand,   NULL, NULL, NULL },
    { "CanSeeInDark",   (getter)Object_GetCanSeeInDark, (setter)Object_SetCanSeeInDark, NULL, NULL },
    { "KnownMagical",   (getter)Object_GetKnownMagical, (setter)Object_SetKnownMagical, NULL, NULL },
    { "CanUseWeapon",   (getter)Object_GetCanUseWeapon, NULL, NULL, NULL },
    { "CanUseArmour",   (getter)Object_GetCanUseArmour, NULL, NULL, NULL },
    { "CanUseScroll",   (getter)Object_GetCanUseScroll, NULL, NULL, NULL },
    { "CanCastSpell",   (getter)Object_GetCanCastSpell, NULL, NULL, NULL },
    { "ReflectSpells",  (getter)Object_GetReflectSpells,(setter)Object_SetReflectSpells, NULL, NULL },
    { "ReflectMissiles",(getter)Object_GetReflectMissiles,(setter)Object_SetReflectMissiles, NULL, NULL },
    { "Unique",         (getter)Object_GetUnique,       (setter)Object_SetUnique, NULL, NULL },
    { "CanPickUp",      (getter)Object_GetCanPickUp,    NULL, NULL, NULL },
    { "CanPassThru",    (getter)Object_GetCanPassThru,  (setter)Object_SetCanPassThru, NULL, NULL },
    { "RunAway",        (getter)Object_GetRunAway,      (setter)Object_SetRunAway, NULL, NULL },
    { "Scared",         (getter)Object_GetScared,       (setter)Object_SetScared, NULL, NULL },
    { "Undead",         (getter)Object_GetUndead,       (setter)Object_SetUndead, NULL, NULL },
    { "BlocksView",     (getter)Object_GetBlocksView,   (setter)Object_SetBlocksView, NULL, NULL },
    { "HitBack",        (getter)Object_GetHitBack,      (setter)Object_SetHitBack, NULL, NULL },
    { "StandStill",     (getter)Object_GetStandStill,   (setter)Object_SetStandStill, NULL, NULL },
    { "OnlyAttack",     (getter)Object_GetOnlyAttack,   (setter)Object_SetOnlyAttack, NULL, NULL },
    { "MakeInvisible",  (getter)Object_GetMakeInvisible,(setter)Object_SetMakeInvisible,NULL, NULL },
    { "Money",          (getter)Object_GetMoney,        NULL ,NULL, NULL },
    { "Type",           (getter)Object_GetType,         NULL ,NULL, NULL },
    { "Value",          (getter)Object_GetValue,        (setter)Object_SetValue ,NULL, NULL },
    { "ArchName",       (getter)Object_GetArchName,     NULL ,NULL, NULL },
    { "Archetype",      (getter)Object_GetArchetype,    NULL ,NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef ObjectMethods[] = {
    { "Remove",         (PyCFunction)Wograld_Object_Remove,       METH_VARARGS},
    { "Apply",          (PyCFunction)Wograld_Object_Apply,        METH_VARARGS},
    { "Drop",           (PyCFunction)Wograld_Object_Drop,         METH_VARARGS},
    { "Fix",            (PyCFunction)Wograld_Object_Fix,          METH_VARARGS},
    { "Say",            (PyCFunction)Wograld_Object_Say,          METH_VARARGS},
    { "Speak",          (PyCFunction)Wograld_Object_Say,          METH_VARARGS},
    { "Take",           (PyCFunction)Wograld_Object_Take,         METH_VARARGS},
    { "Teleport",       (PyCFunction)Wograld_Object_Teleport,     METH_VARARGS},
    { "Reposition",     (PyCFunction)Wograld_Object_Reposition,   METH_VARARGS},
    { "QueryName",      (PyCFunction)Wograld_Object_QueryName,    METH_VARARGS},
    { "GetResist",      (PyCFunction)Wograld_Object_GetResist,    METH_VARARGS},
    { "ActivateRune",   (PyCFunction)Wograld_Object_ActivateRune, METH_VARARGS},
    { "CheckTrigger",   (PyCFunction)Wograld_Object_CheckTrigger, METH_VARARGS},
    { "QueryCost",      (PyCFunction)Wograld_Object_QueryCost,    METH_VARARGS},
    { "Cast",           (PyCFunction)Wograld_Object_Cast,         METH_VARARGS},
    { "LearnSpell",     (PyCFunction)Wograld_Object_LearnSpell,   METH_VARARGS},
    { "ForgetSpell",    (PyCFunction)Wograld_Object_ForgetSpell,  METH_VARARGS},
    { "KnowSpell",      (PyCFunction)Wograld_Object_KnowSpell,    METH_VARARGS},
    { "CastAbility",    (PyCFunction)Wograld_Object_CastAbility,  METH_VARARGS},
    { "PayAmount",      (PyCFunction)Wograld_Object_PayAmount,    METH_VARARGS},
    { "Pay",            (PyCFunction)Wograld_Object_Pay,          METH_VARARGS},
    { "CheckInventory", (PyCFunction)Wograld_Object_CheckInventory,METH_VARARGS},
    { "CheckArchInventory", (PyCFunction)Wograld_Object_CheckArchInventory,METH_VARARGS},
    { "OutOfMap",       (PyCFunction)Wograld_Object_GetOutOfMap,  METH_VARARGS},
    { "CreateObject",   (PyCFunction)Wograld_Object_CreateInside, METH_VARARGS},
    { "InsertInto",     (PyCFunction)Wograld_Object_InsertInto,   METH_VARARGS},
    { "ReadKey",        (PyCFunction)Wograld_Object_ReadKey,      METH_VARARGS},
    { "WriteKey",       (PyCFunction)Wograld_Object_WriteKey,     METH_VARARGS},
    {NULL, NULL, 0}
};

static PyNumberMethods ObjectConvert[ ] = {
        0,               /* binaryfunc nb_add; */        /* __add__ */
        0,               /* binaryfunc nb_subtract; */   /* __sub__ */
        0,               /* binaryfunc nb_multiply; */   /* __mul__ */
        0,               /* binaryfunc nb_divide; */     /* __div__ */
        0,               /* binaryfunc nb_remainder; */  /* __mod__ */
        0,               /* binaryfunc nb_divmod; */     /* __divmod__ */
        0,               /* ternaryfunc nb_power; */     /* __pow__ */
        0,               /* unaryfunc nb_negative; */    /* __neg__ */
        0,               /* unaryfunc nb_positive; */    /* __pos__ */
        0,               /* unaryfunc nb_absolute; */    /* __abs__ */
        0,               /* inquiry nb_nonzero; */       /* __nonzero__ */
        0,               /* unaryfunc nb_invert; */      /* __invert__ */
        0,               /* binaryfunc nb_lshift; */     /* __lshift__ */
        0,               /* binaryfunc nb_rshift; */     /* __rshift__ */
        0,               /* binaryfunc nb_and; */        /* __and__ */
        0,               /* binaryfunc nb_xor; */        /* __xor__ */
        0,               /* binaryfunc nb_or; */         /* __or__ */
        0,               /* coercion nb_coerce; */       /* __coerce__ */
        Wograld_Object_Int, /* unaryfunc nb_int; */    /* __int__ */
        Wograld_Object_Long, /* unaryfunc nb_long; */  /* __long__ */
        0
};

static PyObject *Wograld_Object_new(PyTypeObject *type, PyObject *args, PyObject *kwds);

/* Our actual Python ObjectType */
PyTypeObject Wograld_ObjectType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Object",        /* tp_name*/
            sizeof(Wograld_Object),  /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            (cmpfunc)Wograld_Object_InternalCompare,                         /* tp_compare*/
            0,                         /* tp_repr*/
            ObjectConvert,             /* tp_as_number*/
            0,                         /* tp_as_sequence*/
            0,                         /* tp_as_mapping*/
            0,                         /* tp_hash */
            0,                         /* tp_call*/
            0,                         /* tp_str*/
            PyObject_GenericGetAttr,   /* tp_getattro*/
            PyObject_GenericSetAttr,   /* tp_setattro*/
            0,                         /* tp_as_buffer*/
            Py_TPFLAGS_DEFAULT | Py_TPFLAGS_BASETYPE,        /* tp_flags*/
            "Wograld objects",       /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            ObjectMethods,             /* tp_methods */
            0,                         /* tp_members */
            Object_getseters,          /* tp_getset */
            0,                         /* tp_base */
            0,                         /* tp_dict */
            0,                         /* tp_descr_get */
            0,                         /* tp_descr_set */
            0,                         /* tp_dictoffset */
            0,                         /* tp_init */
            0,                         /* tp_alloc */
            Wograld_Object_new,      /* tp_new */
};





static PyObject* Player_GetIP(Wograld_Player* whoptr, void* closure);
static PyObject* Player_GetMarkedItem(Wograld_Player* whoptr, void* closure);
static int Player_SetMarkedItem(Wograld_Player* whoptr, PyObject* value, void* closure);
static PyObject* Wograld_Player_Message( Wograld_Player* who, PyObject* args );
static PyObject* Player_GetParty(Wograld_Player* whoptr, void* closure);
static int Player_SetParty(Wograld_Player* whoptr, PyObject* value, void* closure);
static PyObject* Wograld_Player_CanPay( Wograld_Player* who, PyObject* args );

static PyGetSetDef Player_getseters[] = {
    { "IP",            (getter)Player_GetIP,            NULL, NULL, NULL },
	{ "MarkedItem",    (getter)Player_GetMarkedItem,    (setter)Player_SetMarkedItem, NULL, NULL },
	{ "Party",         (getter)Player_GetParty,         (setter)Player_SetParty,      NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef PlayerMethods[] = {
    { "Message",          (PyCFunction)Wograld_Player_Message,        METH_VARARGS},
    { "Write",          (PyCFunction)Wograld_Player_Message, METH_VARARGS},
    { "CanPay",         (PyCFunction)Wograld_Player_CanPay, METH_VARARGS},
    {NULL, NULL }
};

static PyObject *Wograld_Player_new(PyTypeObject *type, PyObject *args, PyObject *kwds);

/* Our actual Python ObjectPlayerType */
PyTypeObject Wograld_PlayerType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Player",        /* tp_name*/
            sizeof(Wograld_Player),  /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            0,                         /* tp_compare*/
            0,                         /* tp_repr*/
            0,                         /* tp_as_number*/
            0,                         /* tp_as_sequence*/
            0,                         /* tp_as_mapping*/
            0,                         /* tp_hash */
            0,                         /* tp_call*/
            0,                         /* tp_str*/
            PyObject_GenericGetAttr,   /* tp_getattro*/
            PyObject_GenericSetAttr,   /* tp_setattro*/
            0,                         /* tp_as_buffer*/
            Py_TPFLAGS_DEFAULT,        /* tp_flags*/
            "Wograld player",        /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            PlayerMethods,             /* tp_methods */
            0,                         /* tp_members */
            Player_getseters,          /* tp_getset */
            &Wograld_ObjectType,     /* tp_base */
            0,                         /* tp_dict */
            0,                         /* tp_descr_get */
            0,                         /* tp_descr_set */
            0,                         /* tp_dictoffset */
            0,                         /* tp_init */
            0,                         /* tp_alloc */
            Wograld_Player_new,      /* tp_new */
};
