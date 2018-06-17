/*
 * static char *rcsid_rcsid_h =
 *   "$Id: rcs-id.h,v 1.1.1.1 2012/09/14 03:30:03 serpentshard Exp $";
 */
#define HAS_COMMON_RCSID
extern const char *rcsid_common_client_c;
extern const char *rcsid_common_commands_c;
extern const char *rcsid_common_image_c;
extern const char *rcsid_common_init_c;
extern const char *rcsid_common_item_c;
extern const char *rcsid_common_metaserver_c;
extern const char *rcsid_common_misc_c;
extern const char *rcsid_common_newsocket_c;
extern const char *rcsid_common_player_c;
extern const char *rcsid_common_script_c;
#define INIT_COMMON_RCSID \
    const char *common_rcsid[]={\
    "$Id: rcs-id.h,v 1.1.1.1 2012/09/14 03:30:03 serpentshard Exp $",\
    rcsid_common_client_c,\
    rcsid_common_commands_c,\
    rcsid_common_image_c,\
    rcsid_common_init_c,\
    rcsid_common_item_c,\
    rcsid_common_metaserver_c,\
    rcsid_common_misc_c,\
    rcsid_common_newsocket_c,\
    rcsid_common_player_c,\
    rcsid_common_script_c,\
    NULL}
