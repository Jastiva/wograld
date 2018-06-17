/* cfpython.c */
void initContextStack(void);
void pushContext(CFPContext *context);
CFPContext *popContext(void);
void freeContext(CFPContext *context);
CF_PLUGIN int initPlugin(const char *iversion, f_plug_api gethooksptr);
CF_PLUGIN void *getPluginProperty(int *type, ...);
CF_PLUGIN int runPluginCommand(object *op, char *params);
CF_PLUGIN int postInitPlugin(void);
CF_PLUGIN void *globalEventListener(int *type, ...);
CF_PLUGIN void *eventListener(int *type, ...);
CF_PLUGIN int closePlugin(void);
/* cfpython_archetype.c */
PyObject *Wograld_Archetype_wrap(archetype *what);
/* cfpython_object.c */
PyObject *Wograld_Object_wrap(object *what);
/* cfpython_party.c */
PyObject *Wograld_Party_wrap(partylist *what);
/* cfpython_region.c */
PyObject *Wograld_Region_wrap(region *what);
/* cfpython_map.c */
PyObject *Wograld_Map_wrap(mapstruct *what);
