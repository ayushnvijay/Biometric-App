package biometric;

public class KeyData {
	public int timeBeforeKeyPressed;
	public int timeAfterKeyPressed;
	public int timeKeyDown;
	public int keyPressedCode;
	private final static String newline = "\n";
	//location code: 0->standard, 1->left, 2->right 3->numpad
	public int location;
	public KeyData() {
		timeBeforeKeyPressed = 0;
		timeAfterKeyPressed = 0;
		timeKeyDown = 0;
		keyPressedCode = 0;
		location = 0;
	}
	@Override
	public String toString() {
		String res = "";
		res += "timeBeforeKeyPressed = " + this.timeBeforeKeyPressed + newline;
		res += "timeAfterKeyPressed = " + this.timeAfterKeyPressed + newline;
		res += "timeKeyDown = " + this.timeKeyDown + newline;
		res += "keyPressedCode " + this.keyPressedCode + newline;
		res += "location = " + this.location + newline;
		return res;
	}
	
}
