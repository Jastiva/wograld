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
static PyObject* Map_GetDifficulty(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetPath(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetTempName(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetName(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetResetTime(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetResetTimeout(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetPlayers(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetLight(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetDarkness(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetWidth(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetHeight(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetEnterX(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetEnterY(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetTemperature(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetPressure(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetHumidity(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetWindSpeed(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetWindDir(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetSky(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetWPartX(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetWPartY(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetMessage(Wograld_Map* whoptr, void* closure);
static PyObject* Map_GetRegion(Wograld_Map* whoptr, void* closure);

static PyObject* Map_Message(Wograld_Map* map, PyObject* args);
static PyObject* Map_GetFirstObjectAt(Wograld_Map* map, PyObject* args);
static PyObject* Map_CreateObject(Wograld_Map* map, PyObject* args);
static PyObject* Map_Check(Wograld_Map* map, PyObject* args);
static PyObject* Map_Next(Wograld_Map* map, PyObject* args);
static PyObject* Map_Insert(Wograld_Map* map, PyObject* args);

static int Map_InternalCompare(Wograld_Map* left, Wograld_Map* right);

static PyObject* Wograld_Map_Long( PyObject* obj );
static PyObject* Wograld_Map_Int( PyObject* obj );
static PyObject *Wograld_Map_new(PyTypeObject *type, PyObject *args, PyObject *kwds);

/* Python binding */
static PyGetSetDef Map_getseters[] = {
    {"Difficulty",      (getter)Map_GetDifficulty,  NULL, NULL, NULL },
    {"Path",            (getter)Map_GetPath,        NULL, NULL, NULL },
    {"TempName",        (getter)Map_GetTempName,    NULL, NULL, NULL },
    {"Name",            (getter)Map_GetName,        NULL, NULL, NULL },
    {"ResetTime",       (getter)Map_GetResetTime,   NULL, NULL, NULL },
    {"ResetTimeout",    (getter)Map_GetResetTimeout,NULL, NULL, NULL },
    {"Players",         (getter)Map_GetPlayers,     NULL, NULL, NULL },
    {"Light",           (getter)Map_GetLight,       NULL, NULL, NULL },
    {"Darkness",        (getter)Map_GetDarkness,    NULL, NULL, NULL },
    {"Width",           (getter)Map_GetWidth,       NULL, NULL, NULL },
    {"Height",          (getter)Map_GetHeight,      NULL, NULL, NULL },
    {"EnterX",          (getter)Map_GetEnterX,      NULL, NULL, NULL },
    {"EnterY",          (getter)Map_GetEnterY,      NULL, NULL, NULL },
    {"Temperature",     (getter)Map_GetTemperature, NULL, NULL, NULL },
    {"Pressure",        (getter)Map_GetPressure,    NULL, NULL, NULL },
    {"Humidity",        (getter)Map_GetHumidity,    NULL, NULL, NULL },
    {"WindSpeed",       (getter)Map_GetWindSpeed,   NULL, NULL, NULL },
    {"WindDirection",   (getter)Map_GetWindDir,     NULL, NULL, NULL },
    {"Sky",             (getter)Map_GetSky,         NULL, NULL, NULL },
    {"WPartX",          (getter)Map_GetWPartX,      NULL, NULL, NULL },
    {"WPartY",          (getter)Map_GetWPartY,      NULL, NULL, NULL },
    {"Message",         (getter)Map_GetMessage,     NULL, NULL, NULL },
    {"Region",          (getter)Map_GetRegion,     NULL, NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef MapMethods[] = {
    { "Print",    (PyCFunction)Map_Message, METH_VARARGS},
    { "ObjectAt", (PyCFunction)Map_GetFirstObjectAt, METH_VARARGS},
    { "CreateObject", (PyCFunction)Map_CreateObject, METH_VARARGS},
    { "Check",    (PyCFunction)Map_Check, METH_VARARGS},
    { "Next",    (PyCFunction)Map_Next, METH_VARARGS},
    { "Insert",  (PyCFunction)Map_Insert, METH_VARARGS},
    {NULL, NULL, 0}
};

static PyNumberMethods MapConvert[ ] = {
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
        Wograld_Map_Int, /* unaryfunc nb_int; */       /* __int__ */
        Wograld_Map_Long, /* unaryfunc nb_long; */     /* __long__ */
        0
};

/* Our actual Python MapType */
PyTypeObject Wograld_MapType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Map",           /* tp_name*/
            sizeof(Wograld_Map),     /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            (cmpfunc)Map_InternalCompare,                         /* tp_compare*/
            0,                         /* tp_repr*/
            MapConvert,                /* tp_as_number*/
            0,                         /* tp_as_sequence*/
            0,                         /* tp_as_mapping*/
            0,                         /* tp_hash */
            0,                         /* tp_call*/
            0,                         /* tp_str*/
            PyObject_GenericGetAttr,   /* tp_getattro*/
            PyObject_GenericSetAttr,   /* tp_setattro*/
            0,                         /* tp_as_buffer*/
            Py_TPFLAGS_DEFAULT | Py_TPFLAGS_BASETYPE,        /* tp_flags*/
            "Wograld maps",          /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            MapMethods,                /* tp_methods */
            0,                         /* tp_members */
            Map_getseters,             /* tp_getset */
            0,                         /* tp_base */
            0,                         /* tp_dict */
            0,                         /* tp_descr_get */
            0,                         /* tp_descr_set */
            0,                         /* tp_dictoffset */
            0,                         /* tp_init */
            0,                         /* tp_alloc */
            Wograld_Map_new,         /* tp_new */
};
