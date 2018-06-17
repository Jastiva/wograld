WOGEDIT = @WOGEDIT@

SUBDIRS = common random_maps socket server $(WOGEDIT)


CHECK_ROOT=../
CHECK_FOLDER=unit
CHECK_PARENT_NAME=Main
include $(top_srcdir)/check-fragment.am
check-local: check-html
clean-local: clean-unit