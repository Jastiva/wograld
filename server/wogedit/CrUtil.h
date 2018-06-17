#ifndef _CrUtil_h
#define _CrUtil_h

extern void DrawFacePart(Widget w, GC gc, New_Face *face, int x, int y, int x_offset, int y_offset);
extern void DrawFacePart3(Widget w, GC gc, New_Face *face, int x, int y, int x_offset, int y_offset);
extern void DrawPartObject(Widget w, GC gc, object * op, int x, int y);
extern void DrawPartObject3(Widget w, GC gc, object * op, int x, int y);
extern void DrawFacePart2(Widget w, GC gc, New_Face *face, int x, int y, int x_offset, int y_offset, int pixlx, int pixly, int ceil);
extern void DrawPartObject2(Widget w, GC gc, object * op, int x, int y, int pixlx, int pixly, int ceil);
extern GC GCCreate (Widget w);

#endif /* _CrUtil_h */
