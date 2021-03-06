/*
 * static char *rcsid_check_c_new_c =
 *   "$Id: check_c_new.c,v 1.2 2010/07/26 22:00:31 jastiv Exp $";
 */

/*
    CrossFire, A Multiplayer game for X-windows

    Copyright (C) 2002 Mark Wedel & Wograld Development Team
    Copyright (C) 1992 Frank Tore Johansen

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

    The authors can be reached via e-mail at wograld-devel@real-time.com
*/

/* 
 * This is the unit tests file for server/c_new.c
 */

#include <stdlib.h>
#include <check.h>


void setup(void) {
    /* put any initialisation steps here, they will be run before each testcase */
}

void teardown(void)
{
    /* put any cleanup steps here, they will be run after each testcase */
}

START_TEST (test_empty)
{
    /*TESTME test not yet developped*/
}
END_TEST

Suite *c_new_suite(void)
{
  Suite *s = suite_create("c_new");
  TCase *tc_core = tcase_create("Core");
    /*setup and teardown will be called before each test in testcase 'tc_core' */
  tcase_add_checked_fixture(tc_core,setup,teardown); 

  suite_add_tcase (s, tc_core);
  tcase_add_test(tc_core, test_empty);

  return s;
}

int main(void)
{
  int nf;
  Suite *s = c_new_suite();
  SRunner *sr = srunner_create(s);
  srunner_set_xml(sr,LOGDIR "/unit/server/c_new.xml");
  srunner_set_log(sr,LOGDIR "/unit/server/c_new.out");
  srunner_run_all(sr, CK_ENV); /*verbosity from env variable*/
  nf = srunner_ntests_failed(sr);
  srunner_free(sr);
  return (nf == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}
