# USBML4J
A USB Missile Launcher for Java library

This is a simple Java API for interacting with the original Dream Cheeky USB Missile Launcher (see picture below).

A zeroing/homing functionality is still to come...

![Supported Missile Launcher](missile_launcher.jpg)

## Example Code
    MissileLauncher ml = new MissileLauncher();
	ml.aimDown(500); // aim up for 500 milliseconds
	ml.aimUp(500); // aim down for 500 milliseconds
    ml.aimLeft(500); // aim left for 500 milliseconds
	ml.aimRight(500); // aim right for 500 milliseconds
	ml.fire(); // fire zee missiles!!!!
	ml.reload(); // reload!!!