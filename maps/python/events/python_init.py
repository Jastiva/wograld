import Wograld
import os.path
import sys

print "Running python initialize script."
sys.path.insert(0, os.path.join(Wograld.DataDirectory(), Wograld.MapDirectory(), 'python'))

path = os.path.join(Wograld.DataDirectory(), Wograld.MapDirectory(), 'python/events/init')

if os.path.exists(path):
	scripts = os.listdir(path)

	for script in scripts:
		if (script.endswith('.py')):
			execfile(os.path.join(path, script))
