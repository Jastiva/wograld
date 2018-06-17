static PyObject* Wograld_Party_GetName( Wograld_Party* whoptr, void* closure);
static PyObject* Wograld_Party_GetPassword( Wograld_Party* whoptr, void* closure);
static PyObject* Wograld_Party_GetNext( Wograld_Party* who, PyObject* args );
static PyObject* Wograld_Party_GetPlayers( Wograld_Party* who, PyObject* args );

static int Wograld_Party_InternalCompare(Wograld_Party* left, Wograld_Party* right);

static PyGetSetDef Party_getseters[] = {
    { "Name",       (getter)Wograld_Party_GetName,     NULL, NULL, NULL },
    { "Password",   (getter)Wograld_Party_GetPassword, NULL, NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef PartyMethods[] = {
    { "Next",		    (PyCFunction)Wograld_Party_GetNext,           METH_VARARGS},
	{ "GetPlayers",     (PyCFunction)Wograld_Party_GetPlayers,        METH_VARARGS},
    {NULL, NULL, 0}
};

/* Our actual Python ArchetypeType */
PyTypeObject Wograld_PartyType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Party",        /* tp_name*/
            sizeof(Wograld_Party),  /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            (cmpfunc)Wograld_Party_InternalCompare,                         /* tp_compare*/
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
            "Wograld parties",       /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            PartyMethods,              /* tp_methods */
            0,                         /* tp_members */
            Party_getseters,           /* tp_getset */
};
