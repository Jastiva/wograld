static PyObject* Wograld_Archetype_GetName( Wograld_Archetype* whoptr, void* closure);

static PyObject* Wograld_Archetype_GetNext( Wograld_Archetype* who, PyObject* args );
static PyObject* Wograld_Archetype_GetMore( Wograld_Archetype* who, PyObject* args );
static PyObject* Wograld_Archetype_GetHead( Wograld_Archetype* who, PyObject* args );
static PyObject* Wograld_Archetype_GetClone( Wograld_Archetype* who, PyObject* args );
static PyObject* Wograld_Archetype_GetNewObject( Wograld_Archetype* who, PyObject* args );

static int Wograld_Archetype_InternalCompare(Wograld_Archetype* left, Wograld_Archetype* right);

static PyGetSetDef Archetype_getseters[] = {
    { "Name",       (getter)Wograld_Archetype_GetName,     NULL, NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef ArchetypeMethods[] = {
    { "Next",         (PyCFunction)Wograld_Archetype_GetNext,       METH_VARARGS},
    { "More",         (PyCFunction)Wograld_Archetype_GetMore,       METH_VARARGS},
    { "Head",         (PyCFunction)Wograld_Archetype_GetHead,       METH_VARARGS},
    { "Clone",         (PyCFunction)Wograld_Archetype_GetClone,       METH_VARARGS},
    { "NewObject",         (PyCFunction)Wograld_Archetype_GetNewObject,       METH_VARARGS},
    {NULL, NULL, 0}
};

/* Our actual Python ArchetypeType */
PyTypeObject Wograld_ArchetypeType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Archetype",        /* tp_name*/
            sizeof(Wograld_Archetype),  /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            (cmpfunc)Wograld_Archetype_InternalCompare,                         /* tp_compare*/
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
            Py_TPFLAGS_DEFAULT | Py_TPFLAGS_BASETYPE,        /* tp_flags*/
            "Wograld archetypes",       /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            ArchetypeMethods,          /* tp_methods */
            0,                         /* tp_members */
            Archetype_getseters,       /* tp_getset */
};
