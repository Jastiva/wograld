


CHECK_ROOT=../../
CHECK_FOLDER=unit/wogedit
CHECK_PARENT_NAME=Unit
include $(top_srcdir)/check-fragment.am
LDADD = ../../../common/libcross.a  ../../../wogedit/Cnv/libCnv.a ../../../wogedit/libwogedit.a $(LIBDL) @CHECK_LIBS@

../../logs/unit/wogedit:
	(mkdir ../../logs; mkdir ../../logs/unit; mkdir ../../logs/unit/wogedit)
../../../common/libcross.a:
	( cd ../../../common ; make libcross.a )
../../../wogedit/libwogedit.a:
	( cd ../../../wogedit ; make libwogedit.a )
../../../wogedit/Cnv/libCnv.a:
	( cd ../../../wogedit/Cnv ; make libCnv.a )

TESTS= check_App check_Attr check_Bitmaps check_CrEdit check_CrFace check_CrList check_wogedit check_CrUtil check_Edit check_MapAttr check_png check_Str check_stubs check_xutil


check-local: check-html
clean-local: clean-unit