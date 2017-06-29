package asia.health.bitcare.utils;

/**
 * Created by HP on 12-Jan-17.
 */

public class StringUtils {
    public static String formatDataSimple(String date) {
        return date.replace("-", "").replace(":", "").replace(" ", "");
    }

    /**
     * Check String is empty or not
     * @param data
     * @return
     */
    public static boolean checkEmpty(String data){
        return data!=null && !data.equals("");
    }

    public static String BytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        int length = bytes.length;
        for(int idx=0; idx<length; idx++) {
            int intVal = bytes[idx] & 0xff;
            sbuf.append("0x");
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase()).append(" ");
        }
        return sbuf.toString();
    }
}
