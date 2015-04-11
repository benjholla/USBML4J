# USBML4J
A USB Missile Launcher for Java library

This is a simple Java API for interacting with the original Dream Cheeky USB Missile Launcher (see picture below).  Works on Windows, Mac, and Linux.

![Supported Missile Launcher](missile_launcher.jpg)

## Example Code
    // discover all USB missile launchers
	DefenseGrid grid = DefenseGrid.getInstance();
		
	// perform initialization (zeroing) of each missile launcher in parallel
	grid.initializeMissileLaunchers();
		
	// get an array of missile launchers to control
	MissileLauncher[] launchers = grid.getMissileLaunchers();
		
	// test each missile launchers ability to aim up, down, left, right, and fire
	if(launchers.length == 0){
		System.out.println("No USB Missile Launchers could not be found.  Try disconnecting and reconnecting USB devices from port.");
	} else {
		for(int i=0; i<launchers.length; i++){
			MissileLauncher ml = launchers[i];
			int launcherID = i+1;
			System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(500));
			System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(-500));
			System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(0));
			System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(500));
			System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(-500));
			System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(0));
			System.out.println("[Launcher " + launcherID + "] Fired: " + ml.fire());
		}
	}
