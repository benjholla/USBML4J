package usbml4j;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;

public class DefenseGrid {

	// the single instance for this singleton class
	private static DefenseGrid instance = null;
	
	// the instance variable to hold the defense grid missile launchers
	private MissileLauncher[] launchers;
	
	// private constructor so nobody can make a defense grid directly
	private DefenseGrid() throws SecurityException, UsbException {
		ArrayList<UsbDevice> devices = findMissileLaunchers(Constants.VENDOR_ID, Constants.PRODUCT_ID);
		if(devices.isEmpty()){
			launchers = new MissileLauncher[]{};
		} else {
			launchers = new MissileLauncher[devices.size()];
			for(int i=0; i<devices.size(); i++){
				launchers[i] = new MissileLauncher(devices.get(i));
			}
		}
	}
	
	/**
	 * Returns the singleton instance of the DefenseGrid
	 * @return
	 * @throws SecurityException
	 * @throws UsbException
	 */
	public static DefenseGrid getInstance() throws SecurityException, UsbException {
		if(instance == null){
			instance = new DefenseGrid();
		}
		return instance;
	}
	
	/**
	 * Returns an array of MissleLaunchers
	 * @return
	 */
	public MissileLauncher[] getMissileLaunchers(){
		return launchers;
	}
	
	/**
	 * Initializes all MissileLaunchers (performs homing operations) in parallel
	 */
	public void initializeMissileLaunchers(){
		if(launchers.length > 0){
			for(final MissileLauncher launcher : launchers){
				// run the homing operation in a background thread 
				// so we can move on to the next launcher
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							launcher.initialize();
						} catch (UsbException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			// wait for all launchers to complete homing operations
			try {
				Thread.sleep(Constants.HOMING_PERIOD + 5000);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Recursively searches for USB missile launchers
	 * @throws UsbException 
	 * @throws SecurityException 
	 */
	private ArrayList<UsbDevice> findMissileLaunchers(short vendor, short product) throws SecurityException, UsbException {
		ArrayList<UsbDevice> devices = new ArrayList<UsbDevice>();
		UsbHub hub = UsbHostManager.getUsbServices().getRootUsbHub();
		findMissileLaunchers(hub, vendor, product, devices);
		return devices;
	}

	@SuppressWarnings("unchecked")
	private void findMissileLaunchers(UsbHub hub, short vendor, short product, ArrayList<UsbDevice> devices) {
		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub()) {
				// device is a hub, so expand it and search its children
				findMissileLaunchers((UsbHub) device, vendor, product, devices);
			} else {
				// device is not a hub, check to see if its a missile launcher
				UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
				if (desc.idVendor() == vendor && desc.idProduct() == product) {
					// device is a missile launcher, claim the device if we have to
					if(OSUtils.isMac()){
						// on OSX we need to claim the USB interface
						try {
							UsbConfiguration configuration = device.getUsbConfiguration((byte) 1);
					        UsbInterface usbInterface = configuration.getUsbInterface((byte) 1);
					        usbInterface.claim(new UsbInterfacePolicy() {            
					            @Override
					            public boolean forceClaim(UsbInterface usbInterface) {
					                return true;
					            }
					        });
						} catch (Exception e){
							e.printStackTrace();
						}
					}
					// add the claimed missile launcher device to the collection
					devices.add(device);
				}
			}
		}
	}
	
}
