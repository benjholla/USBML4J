package usbml4j;

public class Constants {

	// hardcoded values for the vendor and product ids
	// of this USB missle launcher model
	public static final short VENDOR_ID = 0x0a81;
	public static final short PRODUCT_ID = 0x0701;

	// hardcoded values for the bytes to send for each command to USB device
	public static final byte AIM_DOWN_COMMAND = (byte) 0x01;
	public static final byte AIM_UP_COMMAND = (byte) 0x02;
	public static final byte AIM_LEFT_COMMAND = (byte) 0x04;
	public static final byte AIM_RIGHT_COMMAND = (byte) 0x08;
	public static final byte FIRE_COMMAND = (byte) 0x10;
	public static final byte HAULT_COMMAND = (byte) 0x20;

	// time in milliseconds required to complete each action
	public final static long FIRE_PERIOD = 500;
	public final static long RELOAD_PERIOD = 4500;
	public final static long AIM_PERIOD = 1;

	// maximum bounds on aiming up, down, left, or right
	// by design these bounds correspond to one AIM_PERIOD which is 1
	// millisecond this could be read as 1 second of movement time is allowed
	// in up or down direction starting from home position and 4 seconds of
	// movement time in the left or right direction starting from the home
	// position
	public final static int MIN_VERTICAL = -1000; // most down you can aim
	public final static int MAX_VERTICAL = 1000; // most up you can aim
	public final static int MIN_HORIZONTAL = -4000; // most left you can aim
	public final static int MAX_HORIZONTAL = 4000; // most right you can aim

}
