package test;

import javax.usb.UsbException;

import usbml4j.MissileLauncher;

public class TestMissileLauncher {

	public static void main(String[] args) throws UsbException {
		MissileLauncher ml = new MissileLauncher();
		System.out.println("Vertical Position: " + ml.adjustVerticalPosition(500));
		System.out.println("Vertical Position: " + ml.adjustVerticalPosition(-500));
		System.out.println("Vertical Position: " + ml.adjustVerticalPosition(0));
		System.out.println("Horizontal Position: " + ml.adjustHorizontalPosition(500));
		System.out.println("Horizontal Position: " + ml.adjustHorizontalPosition(-500));
		System.out.println("Horizontal Position: " + ml.adjustHorizontalPosition(0));
		System.out.println("Fired: " + ml.fire());
		System.out.println("Reloaded: " + ml.isReloaded());
		System.out.println("Reloaded: " + ml.reload());
	}

}
