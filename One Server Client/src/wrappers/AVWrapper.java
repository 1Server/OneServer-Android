package wrappers;


public class AVWrapper {
	
	private static native void openFile(String file);

    private static native int[] naGetVideoResolution();
 
    private static native String naGetVideoCodecName();
 
    private static native String naGetVideoFormatName();
 
    private static native void naClose();
    
    private static native void decode(byte[] videoBuffer, byte[] audioBuffer);

}
