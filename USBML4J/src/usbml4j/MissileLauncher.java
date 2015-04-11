package usbml4j;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbException;

public class MissileLauncher {

	/**
	 * An enumeration of each Missile Launcher Command
	 */
	private static enum Command {
		AIM_UP, AIM_DOWN, AIM_LEFT, AIM_RIGHT, FIRE, RELOAD, HAULT
	}

	private UsbDevice device = null;
	private boolean isInitialized = false;
	private boolean isReloaded = true; // assume we are reloaded by default
	private int verticalPosition;
	private int horizontalPosition;

	public MissileLauncher(UsbDevice device) throws UsbException {
		if (device == null) {
			throw new UsbException("USB Missile Launcher could not be found.  Try disconnecting and reconnecting USB from port.");
		}
		this.device = device;

		System.out.println("Created Missile Launcher!");
	}

	/**
	 * Returns true if the USB missile launcher is connected and ready for
	 * commands
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return (device != null) && isInitialized;
	}

	public boolean initialize() throws UsbException {
		System.out.println("Started Homing Missile Launcher!");
		if (device != null) {
			// this missile launcher just dead reckons its position,
			// so its good to set the position to a known starting point
			zero();
			isInitialized = true;
		}
		System.out.println("Finished Homing Missile Launcher!");
		return isInitialized;
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
	 * Returns the vertical position Ranges from Constants.MAX_VERTICAL to
	 * Constants.MIN_VERTICAL
	 * 
	 * @return
	 */
	public int getVerticalPosition() {
		return verticalPosition;
	}

	/**
	 * Returns the vertical position Ranges from Constants.MAX_HORIZONTAL to
	 * Constants.MIN_HORIZONTAL
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
		if (isInitialized()) {
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
		if (isInitialized()) {
			zero();
		}
	}

	/**
	 * Moves the missile launcher to the vertical position coordinate Range is
	 * from Constants.MAX_VERTICAL to Constants.MIN_VERTICAL
	 * 
	 * @param position
	 * @return
	 * @throws UsbException
	 */
	public int adjustVerticalPosition(int position) throws UsbException {
		if (isInitialized()) {
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
	 * Moves the missile launcher to the vertical position coordinate Range is
	 * from Constants.MAX_VERTICAL to Constants.MIN_VERTICAL
	 * 
	 * @param position
	 * @return
	 * @throws UsbException
	 */
	public int adjustHorizontalPosition(int position) throws UsbException {
		if (isInitialized()) {
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
	 * Aims the gun upward for the given number of milliseconds Does not exceed
	 * the maximum range of Constants.MAX_VERTICAL
	 * 
	 * @param period
	 * @return Returns the current vertical position
	 * @throws UsbException
	 */
	public int aimUp(long period) throws UsbException {
		if (isInitialized()) {
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
	 * 
	 * @param period
	 * @return Returns the current vertical position
	 * @throws UsbException
	 */
	public int aimDown(int period) throws UsbException {
		if (isInitialized()) {
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
	 * 
	 * @param period
	 * @return Returns the current horizontal position
	 * @throws UsbException
	 */
	public int aimRight(int period) throws UsbException {
		if (isInitialized()) {
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
	 * 
	 * @param period
	 * @return Returns the current horizontal position
	 * @throws UsbException
	 */
	public int aimLeft(int period) throws UsbException {
		if (isInitialized()) {
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
		if (isInitialized()) {
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
		if (isInitialized()) {
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
	 * Performs first time setup to zero the device Does not check if device is
	 * initialized because this routine is called during initialization
	 * 
	 * @throws UsbException
	 */
	private void zero() throws UsbException {
		// go all the way down
		sendCommand(device, Command.AIM_DOWN);
		sleep(Constants.HOMING_DOWN_PERIOD);
		verticalPosition = Constants.MIN_VERTICAL;
		adjustVerticalPosition(0);

		// go all the way left
		sendCommand(device, Command.AIM_LEFT);
		sleep(Constants.HOMING_LEFT_PERIOD);
		horizontalPosition = Constants.MIN_HORIZONTAL;
		adjustHorizontalPosition(0);

		hault();
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

}
