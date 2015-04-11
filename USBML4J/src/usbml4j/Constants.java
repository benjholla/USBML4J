package usbml4j;

public class Constants {

	// hardcoded values for the vendor and product ids 
	// of this USB missle launcher model
	public static final short VENDOR_ID = 0x0a81;
	public static final short PRODUCT_ID = 0x0701;
	
	// hardcoded values for the bytes to send for each command to USB device
	public static final byte AIM_UP_COMMAND = (byte) 0x01;
	public static final byte AIM_DOWN_COMMAND = (byte) 0x02;
	public static final byte AIM_LEFT_COMMAND = (byte) 0x04;
	public static final byte AIM_RIGHT_COMMAND = (byte) 0x08;
	public static final byte FIRE_COMMAND = (byte) 0x10;
	public static final byte HAULT_COMMAND = (byte) 0x20;
	
	// time required to complete each action
	public final static int FIRE_PERIOD = 500;
	public final static int RELOAD_PERIOD = 4500;
	
	// maximum bounds on aiming up, down, left, or right
	public final static int MIN_HORIZONTAL = -500;
	public final static int MAX_HORIZONTAL = 500;
	public final static int MIN_VERTICAL = -500;
	public final static int MAX_VERTICAL = 500;

}
