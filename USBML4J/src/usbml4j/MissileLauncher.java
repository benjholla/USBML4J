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
	public static enum Command {
		AIM_UP,
		AIM_DOWN,
		AIM_LEFT,
		AIM_RIGHT,
		FIRE,
		RELOAD,
		HAULT
	}
	
	private UsbDevice device = null;
	private boolean isReloaded = true; // assume we are reloaded by default
	private int verticalPosition;
	private int horizontalPosition;

	public MissileLauncher() {
		device = findMissileLauncher(Constants.VENDOR_ID, Constants.PRODUCT_ID);
	}
	
	/**
	 * Returns true if the USB missile launcher is connected and ready for commands
	 * @return
	 */
	public boolean isConnected(){
		return device != null;
	}
	
	/**
	 * Whatever you are doing stop doing it right now!
	 * @throws UsbException 
	 */
	public void hault() throws UsbException {
		if(isConnected()){
			// HACK: only reliable way to stop firing is to do an aim action and then send hault command
			sendCommand(device, Command.AIM_LEFT); 
			sendCommand(device, Command.HAULT);
		}
	}
	
	public void aimUp(int period) throws UsbException {
		sendCommand(device, Command.AIM_UP);
		for(int i=0; i<period; i++){
			if(verticalPosition >= Constants.MAX_VERTICAL){
				break;
			} else {
				sleep(1);
				verticalPosition++;
			}
		}
		hault();
	}
	
	public void aimDown(int period) throws UsbException {
		sendCommand(device, Command.AIM_DOWN);
		for(int i=0; i<period; i++){
			if(verticalPosition <= Constants.MIN_VERTICAL){
				break;
			} else {
				sleep(1);
				verticalPosition--;
			}
		}
		hault();
	}

	/**
	 * Fires the USB missile launcher
	 * @throws UsbException
	 */
	public void fire() throws UsbException {
		if(isConnected()){
			if(!isReloaded){
				reload();
			}
			sendCommand(device, Command.FIRE);
			sleep(Constants.RELOAD_PERIOD);
			hault();
		}	
	}
	
	/**
	 * Reload the gun (primes the air pump to be fired) if not already reloaded
	 * @throws UsbException
	 */
	public void reload() throws UsbException {
		if(isConnected()){
			if (!isReloaded){
				sendCommand(device, Command.RELOAD);
				sleep(Constants.RELOAD_PERIOD);
				hault();
				isReloaded = true;
			}
		}
	}
	
	/**
	 * Sleeps for the number of milliseconds
	 * @param time
	 */
	private void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (Exception e){
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
