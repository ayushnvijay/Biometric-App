package biometric;

public class KeyData {
	//
	public int timeBeforeKeyPressed;
	public int timeAfterKeyPressed;
	public int timeKeyDown;
	public int keyPressedCode;
	public int nextkeyPressedCode;
	private final static String newline = "\n";

	public KeyData() {
		timeBeforeKeyPressed = 0;
		timeAfterKeyPressed = 0;
		timeKeyDown = 0;
		keyPressedCode = 0;
		nextkeyPressedCode = 0;
	}
	@Override
	public String toString() {
		String res = "";
		res += "timeBeforeKeyPressed = " + this.timeBeforeKeyPressed + newline;
		res += "timeAfterKeyPressed = " + this.timeAfterKeyPressed + newline;
		res += "timeKeyDown = " + this.timeKeyDown + newline;
		res += "keyPressedCode " + this.keyPressedCode + newline;
		res += "nextkeyPressedCode " + this.nextkeyPressedCode + newline;
		return res;
	}
	
}
