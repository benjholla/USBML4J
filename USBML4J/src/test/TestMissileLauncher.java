package test;

import javax.usb.UsbException;

import usbml4j.DefenseGrid;
import usbml4j.MissileLauncher;

public class TestMissileLauncher {

	public static void main(String[] args) throws UsbException {
		DefenseGrid grid = DefenseGrid.getInstance();
		grid.initializeMissileLaunchers();
		MissileLauncher[] launchers = grid.getMissileLaunchers();
		
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

}
