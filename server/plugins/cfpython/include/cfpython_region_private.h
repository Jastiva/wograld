static PyObject* Wograld_Region_GetName( Wograld_Region* whoptr, void* closure);
static PyObject* Wograld_Region_GetLongname( Wograld_Region* whoptr, void* closure);
static PyObject* Wograld_Region_GetMessage( Wograld_Region* whoptr, void* closure);
static PyObject* Wograld_Region_GetNext( Wograld_Region* who, PyObject* args );
static PyObject* Wograld_Region_GetParent( Wograld_Region* who, PyObject* args );

static int Wograld_Region_InternalCompare(Wograld_Region* left, Wograld_Region* right);

static PyGetSetDef Region_getseters[] = {
    { "Name",       (getter)Wograld_Region_GetName,     NULL, NULL, NULL },
    { "Longname",   (getter)Wograld_Region_GetLongname, NULL, NULL, NULL },
    { "Message",    (getter)Wograld_Region_GetMessage,  NULL, NULL, NULL },
    { NULL, NULL, NULL, NULL, NULL }
};

static PyMethodDef RegionMethods[] = {
    { "Next",		    (PyCFunction)Wograld_Region_GetNext,           METH_VARARGS},
	{ "GetParent",      (PyCFunction)Wograld_Region_GetParent,         METH_VARARGS},
    {NULL, NULL, 0}
};

/* Our actual Python ArchetypeType */
PyTypeObject Wograld_RegionType = {
            PyObject_HEAD_INIT(NULL)
                    0,                         /* ob_size*/
            "Wograld.Party",        /* tp_name*/
            sizeof(Wograld_Region),  /* tp_basicsize*/
            0,                         /* tp_itemsize*/
            0,                         /* tp_dealloc*/
            0,                         /* tp_print*/
            0,                         /* tp_getattr*/
            0,                         /* tp_setattr*/
            (cmpfunc)Wograld_Region_InternalCompare,                         /* tp_compare*/
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
            "Wograld regions",       /* tp_doc */
            0,                         /* tp_traverse */
            0,                         /* tp_clear */
            0,                         /* tp_richcompare */
            0,                         /* tp_weaklistoffset */
            0,                         /* tp_iter */
            0,                         /* tp_iternext */
            RegionMethods,             /* tp_methods */
            0,                         /* tp_members */
            Region_getseters,          /* tp_getset */
};
