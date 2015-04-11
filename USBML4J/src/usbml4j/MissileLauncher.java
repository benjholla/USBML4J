package usbml4j;

import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;

public class MissileLauncher {

	/**
	 * An enumeration of each Missile Launcher Command
	 */
	private static enum Command {
		AIM_UP, AIM_DOWN, AIM_LEFT, AIM_RIGHT, FIRE, RELOAD, HAULT
	}

	private UsbDevice device = null;
	private boolean isReloaded = true; // assume we are reloaded by default
	private int verticalPosition;
	private int horizontalPosition;

	public MissileLauncher() throws UsbException {
		device = findMissileLauncher(Constants.VENDOR_ID, Constants.PRODUCT_ID);
		if(device == null){
			throw new UsbException("USB Missile Launcher could not be found.  Try disconnecting and reconnecting USB from port.");
		}
		// this missile launcher just dead reckons its position, 
		// so its good to set the position to a known starting point
		home();
	}

	/**
	 * Returns true if the USB missile launcher is connected and ready for
	 * commands
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return device != null;
	}

	/**
	 * Returns true if the gun is cocked and ready to fire
	 * 
	 * @return
	 */
	public boolean isReloaded() {
		return isReloaded;
	}

	/**
	 * Returns the vertical position 
	 * Ranges from Constants.MAX_VERTICAL to Constants.MIN_VERTICAL
	 * 
	 * @return
	 */
	public int getVerticalPosition() {
		return verticalPosition;
	}

	/**
	 * Returns the vertical position 
	 * Ranges from Constants.MAX_HORIZONTAL to Constants.MIN_HORIZONTAL
	 * 
	 * @return
	 */
	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	/**
	 * Whatever you are doing stop doing it right now!
	 * 
	 * @throws UsbException
	 */
	public void hault() throws UsbException {
		if (isConnected()) {
			// HACK: only reliable way to stop firing is to do an aim action and
			// then send hault command
			sendCommand(device, Command.AIM_LEFT);
			sendCommand(device, Command.HAULT);
		}
	}

	/**
	 * Homes (zeros) the missile launcher by setting the vertical and horizontal
	 * positions to zero
	 * 
	 * @throws UsbException
	 */
	public void home() throws UsbException {
		if (isConnected()) {
			// go all the way down
			sendCommand(device, Command.AIM_DOWN);
			sleep(5000);
			verticalPosition = Constants.MIN_VERTICAL;
			adjustVerticalPosition(0);

			// go all the way left
			sendCommand(device, Command.AIM_LEFT);
			sleep(10000);
			horizontalPosition = Constants.MIN_HORIZONTAL;
			adjustHorizontalPosition(0);

			hault();
		}
	}

	/**
	 * Moves the missile launcher to the vertical position coordinate
	 * Range is from Constants.MAX_VERTICAL to Constants.MIN_VERTICAL
	 * @param position
	 * @return
	 * @throws UsbException
	 */
	public int adjustVerticalPosition(int position) throws UsbException {
		if (isConnected()) {
			if (verticalPosition < position) {
				sendCommand(device, Command.AIM_UP);
				while (verticalPosition < position) {
					if (verticalPosition >= Constants.MAX_VERTICAL) {
						break;
					} else {
						sleep(Constants.AIM_PERIOD);
						verticalPosition++;
					}
				}
			} else {
				sendCommand(device, Command.AIM_DOWN);
				while (verticalPosition > position) {
					if (verticalPosition <= Constants.MIN_VERTICAL) {
						break;
					} else {
						sleep(Constants.AIM_PERIOD);
						verticalPosition--;
					}
				}
			}
			hault();
		}
		return verticalPosition;
	}

	/**
	 * Moves the missile launcher to the vertical position coordinate
	 * Range is from Constants.MAX_VERTICAL to Constants.MIN_VERTICAL
	 * @param position
	 * @return
	 * @throws UsbException
	 */
	public int adjustHorizontalPosition(int position) throws UsbException {
		if (isConnected()) {
			if (horizontalPosition < position) {
				sendCommand(device, Command.AIM_RIGHT);
				while (horizontalPosition < position) {
					if (horizontalPosition >= Constants.MAX_HORIZONTAL) {
						break;
					} else {
						sleep(Constants.AIM_PERIOD);
						horizontalPosition++;
					}
				}
			} else {
				sendCommand(device, Command.AIM_LEFT);
				while (horizontalPosition > position) {
					if (horizontalPosition <= Constants.MIN_HORIZONTAL) {
						break;
					} else {
						sleep(Constants.AIM_PERIOD);
						horizontalPosition--;
					}
				}
			}
			hault();
		}
		return horizontalPosition;
	}

	/**
	 * Aims the gun upward for the given number of milliseconds
	 * Does not exceed the maximum range of Constants.MAX_VERTICAL
	 * @param period
	 * @return Returns the current vertical position
	 * @throws UsbException
	 */
	public int aimUp(long period) throws UsbException {
		if (isConnected()) {
			sendCommand(device, Command.AIM_UP);
			for (int i = 0; i < period; i++) {
				if (verticalPosition >= Constants.MAX_VERTICAL) {
					break;
				} else {
					sleep(Constants.AIM_PERIOD);
					verticalPosition++;
				}
			}
			hault();
		}
		return verticalPosition;
	}

	/**
	 * Aims the gun downward for the given number of milliseconds
	 * @param period
	 * @return Returns the current vertical position
	 * @throws UsbException
	 */
	public int aimDown(int period) throws UsbException {
		if (isConnected()) {
			sendCommand(device, Command.AIM_DOWN);
			for (int i = 0; i < period; i++) {
				if (verticalPosition <= Constants.MIN_VERTICAL) {
					break;
				} else {
					sleep(Constants.AIM_PERIOD);
					verticalPosition--;
				}
			}
			hault();
		}
		return verticalPosition;
	}

	/**
	 * Aims the gun right for the given number of milliseconds
	 * @param period
	 * @return Returns the current horizontal position
	 * @throws UsbException
	 */
	public int aimRight(int period) throws UsbException {
		if (isConnected()) {
			sendCommand(device, Command.AIM_RIGHT);
			for (int i = 0; i < period; i++) {
				if (horizontalPosition >= Constants.MAX_HORIZONTAL) {
					break;
				} else {
					sleep(Constants.AIM_PERIOD);
					horizontalPosition++;
				}
			}
			hault();
		}
		return horizontalPosition;
	}

	/**
	 * Aims the gun left for the given number of milliseconds
	 * @param period
	 * @return Returns the current horizontal position
	 * @throws UsbException
	 */
	public int aimLeft(int period) throws UsbException {
		if (isConnected()) {
			sendCommand(device, Command.AIM_LEFT);
			for (int i = 0; i < period; i++) {
				if (horizontalPosition <= Constants.MIN_HORIZONTAL) {
					break;
				} else {
					sleep(Constants.AIM_PERIOD);
					horizontalPosition--;
				}
			}
			hault();
		}
		return horizontalPosition;
	}

	/**
	 * Fires and reloads the USB missile launcher
	 * 
	 * @return Returns true if the gun was successfully fired
	 * @throws UsbException
	 */
	public boolean fire() throws UsbException {
		if (isConnected()) {
			if (!isReloaded) {
				reload();
			}
			sendCommand(device, Command.FIRE);
			sleep(Constants.RELOAD_PERIOD);
			hault();
			return true;
		}
		return false;
	}

	/**
	 * Reload the gun (primes the air pump to be fired) if not already reloaded
	 * 
	 * @return Returns true if the gun is reloaded
	 * @throws UsbException
	 */
	public boolean reload() throws UsbException {
		if (isConnected()) {
			if (!isReloaded) {
				sendCommand(device, Command.RELOAD);
				sleep(Constants.RELOAD_PERIOD);
				hault();
				isReloaded = true;
			}
		}
		return isReloaded;
	}

	/**
	 * Sleeps for the number of milliseconds
	 * 
	 * @param sleepDelay
	 */
	private void sleep(long sleepDelay) {
		try {
			Thread.sleep(sleepDelay);
		} catch (Exception e) {
			// do nothing if sleeping fails
		}
	}

	/**
	 * Sends a command to the missile launcher
	 */
	private void sendCommand(UsbDevice device, Command command) throws UsbException {
		switch (command) {
		case AIM_UP:
			sendMessage(device, new Integer(Constants.AIM_UP_COMMAND).byteValue());
			break;
		case AIM_DOWN:
			sendMessage(device, new Integer(Constants.AIM_DOWN_COMMAND).byteValue());
			break;
		case AIM_LEFT:
			sendMessage(device, new Integer(Constants.AIM_LEFT_COMMAND).byteValue());
			break;
		case AIM_RIGHT:
			sendMessage(device, new Integer(Constants.AIM_RIGHT_COMMAND).byteValue());
			break;
		case FIRE:
			sendMessage(device, new Integer(Constants.FIRE_COMMAND).byteValue());
			break;
		case RELOAD:
			// reloading is just a fire command for a shorter amount of time
			sendMessage(device, new Integer(Constants.FIRE_COMMAND).byteValue());
			break;
		case HAULT:
			sendMessage(device, new Integer(Constants.HAULT_COMMAND).byteValue());
			break;
		default:
			// do nothing
			break;
		}
	}

	/**
	 * Sends a raw command byte to the usb device
	 */
	private void sendMessage(UsbDevice device, byte command) throws UsbException {
		byte[] message = { command };
		UsbControlIrp irp = device.createUsbControlIrp((byte) (UsbConst.REQUESTTYPE_TYPE_CLASS | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE), (byte) 0x09, (short) 0x0200, (short) 0);
		irp.setData(message);
		device.syncSubmit(irp);
	}

	/**
	 * Recursively searches for the USB missile launcher
	 */
	private UsbDevice findMissileLauncher(short vendor, short product) {
		try {
			UsbHub hub = UsbHostManager.getUsbServices().getRootUsbHub();
			return findMissileLauncher(hub, vendor, product);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private UsbDevice findMissileLauncher(UsbHub hub, short vendor, short product) {
		UsbDevice launcher = null;
		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub()) {
				launcher = findMissileLauncher((UsbHub) device, vendor, product);
				if (launcher != null) {
					return launcher;
				}
			} else {
				UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
				if (desc.idVendor() == vendor && desc.idProduct() == product) {
					return device;
				}
			}
		}
		return null;
	}

}
