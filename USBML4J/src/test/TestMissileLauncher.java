package test;

import javax.usb.UsbException;

import usbml4j.MissileLauncher;

public class TestMissileLauncher {

	public static void main(String[] args) throws UsbException {
		MissileLauncher ml = new MissileLauncher();
		ml.aimDown(500);
		ml.aimUp(500);
		ml.fire();
		ml.reload();
	}

}
