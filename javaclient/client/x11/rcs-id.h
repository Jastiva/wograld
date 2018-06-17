/*
 * static char *rcsid_rcsid_h =
 *   "$Id: rcs-id.h,v 1.1.1.1 2012/09/14 03:30:02 serpentshard Exp $";
 */
#define HAS_X11_RCSID
extern const char *rcsid_x11_xutil_c;
extern const char *rcsid_x11_x11_c;
extern const char *rcsid_x11_sound_c;
extern const char *rcsid_x11_png_c;
#define INIT_X11_RCSID \
    const char *x11_rcsid[]={\
    "$Id: rcs-id.h,v 1.1.1.1 2012/09/14 03:30:02 serpentshard Exp $",\
    rcsid_x11_xutil_c,\
    rcsid_x11_x11_c,\
    rcsid_x11_sound_c,\
    rcsid_x11_png_c,\
    NULL};
