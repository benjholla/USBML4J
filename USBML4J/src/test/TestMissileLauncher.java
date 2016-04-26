package test;

import javax.usb.UsbException;

import usbml4j.DefenseGrid;
import usbml4j.MissileLauncher;

public class TestMissileLauncher {

	public static void main(String[] args) throws UsbException {
		
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
			int launcherID = 0; // launcher's index
			for(MissileLauncher ml : launchers){
				launcherID++;
				System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(500));
				System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(-500));
				System.out.println("[Launcher " + launcherID + "] Vertical Position: " + ml.adjustVerticalPosition(0));
				System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(500));
				System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(-500));
				System.out.println("[Launcher " + launcherID + "] Horizontal Position: " + ml.adjustHorizontalPosition(0));
				System.out.println("[Launcher " + launcherID + "] Fired: " + ml.fire());
			}
		}
	}

}
