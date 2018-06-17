#include <Defines.h>
#include <global.h>
#include <debug.h>
#include <X11.h>
#include <CrUtil.h>
#include <App.h>

extern struct PixmapInfo pixmaps[10000];



/* Draw one of the tiles in a (potentially big) face.
 *
 * w, gc - Where you want to paint the face, um.
 * x, y - The pixel co-ordinates of w where you want the face to appear.
 *
 * x_offset, y_offset:
 *
 *   Which part of the big face you want to draw; how many tiles down and
 *   right from the head you want to offset into the face.
 *
 *   If you want to draw the bottom-right corner of a 2 wide, 3 tall face,
 *   provide x_offset y_offset 1, 2.
 *
 *   If you want the top-left corner or are drawing a single-tile face,
 *   give 0, 0.
 */
void DrawFacePart (
    Widget w, GC gc, New_Face * face, int x, int y,
    int x_offset, int y_offset 
) {
    int num = face->number;

    if (displaymode == Dm_Png) {
XSetClipMask(XtDisplay(w), gc, pixmaps[num].mask);
        /* On the other hand, x and y will be bigger, so the mask will be in
           the right place. */
        XSetClipOrigin(XtDisplay(w), gc,
            x - FontSize * x_offset,
            y - FontSize * y_offset);
int twidth, theight;
if(pixmaps[num].width > 64){
twidth = 64;
} else {
twidth = pixmaps[num].width;
}
if(pixmaps[num].height > 64){
theight = 64;
} else {
theight = pixmaps[num].height;
}
XCopyArea(XtDisplay(w), pixmaps[num].pixmap, XtWindow(w), gc,
            FontSize * x_offset, FontSize * y_offset,twidth, theight,
 x, y);
    }
}
void DrawFacePart2(
     Widget w, GC gc, New_Face * face, int x, int y,
	int x_offset, int y_offset, int pixlx, int pixly, int ceil)
     {
	int num = face->number;

	if (displaymode = Dm_Png) {
XSetClipMask(XtDisplay(w),gc,pixmaps[num].mask2);
		XSetClipOrigin(XtDisplay(w),gc,x-FontSize*x_offset,y-FontSize*y_offset);

int twidth, theight;
if(pixmaps[num].width2 > 64){
twidth = 64;
} else {
twidth = pixmaps[num].width2;
}
if(pixmaps[num].height2 > 64){
theight = 64;
} else {
theight = pixmaps[num].height2;
}

XCopyArea(XtDisplay(w), pixmaps[num].pixmap2, XtWindow(w),gc,
                FontSize*x_offset, FontSize*y_offset,twidth, theight,x,y);
	}
    }

void DrawFacePart3 (
 Widget w, GC gc, New_Face * face, int x, int y,
    int x_offset, int y_offset
) {
    int num = face->number;

    if (displaymode == Dm_Png) {
	XSetClipMask(XtDisplay(w),gc,pixmaps[num].mask2);
        /* On the other hand, x and y will be bigger, so the mask will be in
           the right place. */
        XSetClipOrigin(XtDisplay(w), gc,
            x - FontSize * x_offset,
            y - FontSize * y_offset);
int twidth, theight;
if(pixmaps[num].width2 > 64){
twidth = 64;
} else {        
twidth = pixmaps[num].width2;
}   
if(pixmaps[num].height2 > 64){
theight = 64;
} else {
theight = pixmaps[num].height2;
}
	XCopyArea(XtDisplay(w), pixmaps[num].pixmap2, XtWindow(w),gc,
                FontSize*x_offset, FontSize*y_offset,twidth,theight,x,y);
    }
}


/* void DrawFacePart2 (  */
/*   Widget w, GC gc, New_Face * face, int x, int y, int x_offset, int y_offset, int pixlx, int pixly, int ceil)  */
 /*  {  */
    /* int num = face->number; */

  /*   if(ceil == 0) {  */
    /* num += 10000; */
/*
     XSetClipMask(XtDisplay(w), gc, masks[num]);
     XSetClipOrigin(XtDisplay(w), gc, 2+0.5*FontSize*(x+y-10) - FontSize*x_offset, 2+0.5*FontSize*(y-x+7) - FontSize*y_offset);
XCopyArea(XtDisplay(w), pixmaps[num], XtWindow(w), gc, FontSize*x_offset, FontSize*y_offset, FontSize, FontSize, 2+0.5*FontSize*(x+y-10)+pixlx, 2+0.5*FontSize*(y-x+7)+pixly);*/ 
/* }  */
/* else */
/* {
     XSetClipMask(XtDisplay(w),gc,masks2[num]);
     XSetClipOrigin(XtDisplay(w),gc, 2+0.5*FontSize*(x+y-10) - FontSize*x_offset, 2+0.5*FontSize*(y-x+7) - FontSize*y_offset);
XCopyArea(XtDisplay(w), pixmaps2[num], XtWindow(w), gc, FontSize*x_offset, FontSize*y_offset, FontSize, FontSize, 2+0.5*FontSize*(x+y-10)+pixlx, 2+0.5*FontSize*(y-x+7)+pixly); 
}
}  */
 
/* Draws one tile of op on w, gc at pixel (x, y).
 *
 * This does the Right Thing with multi-tile objects: It paints the
 * particular part of the face when op is not the head, even for a big face.
 *
 * When op has a big face, DrawPartObject() calculates the offset to the
 * head to draw the right part of the big face for op.
 */
void DrawPartObject(Widget w, GC gc, object * op, int x, int y) {

    int x_offset = 0, y_offset = 0;

    if (op->head != NULL) {
        if (op->face == op->head->face) {
            /* We're not the head, but we've got the same face. Therefore,
               we've got a big face; get the offset in tiles. */
            x_offset = op->x - op->head->x;
            y_offset = op->y - op->head->y;
        }
    }

    DrawFacePart(w, gc,
        op->face,
        x, y,
        x_offset, y_offset);
}

void DrawPartObject3(Widget w, GC gc, object * op, int x, int y) {

int x_offset = 0, y_offset = 0;

    if (op->head != NULL) {
        if (op->face == op->head->face) {
            /* We're not the head, but we've got the same face. Therefore,
               we've got a big face; get the offset in tiles. */
            x_offset = op->x - op->head->x;
            y_offset = op->y - op->head->y;
        }
    }

    DrawFacePart3(w, gc,
        op->face,
        x, y,
        x_offset, y_offset);

}

void DrawPartObject2(Widget w, GC gc, object * op, int x, int y, int pixlx, int pixly, int ceil) {
    int x_offset = 0, y_offset = 0;

   if(op->head != NULL) {
    if(op-> face != NULL){
      if (op->face == op->head->face) {
         x_offset = op->x - op->head->x;
         y_offset = op->y - op->head->y;
      }
     }
   }
  DrawFacePart2(w, gc, op->face, x, y, x_offset, y_offset, pixlx, pixly, ceil);

}

/*
 *Function: GCCreate
 *Member-Of: Cr
 *Description:
 * Create suitable GC for Cr widgets.
 *Commentary:
 * There are inplicit use of use_pixmaps and color_pix, that should
 * be removed by better structuring of program.
 */
GC GCCreate (Widget w) {
    GC gc;
    gc= XtAllocateGC (w, 0, 0L, NULL, GCBackground | GCForeground, 0);
    XSetGraphicsExposures(XtDisplay(w), gc, False);
   return gc;
}


