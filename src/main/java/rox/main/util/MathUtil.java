package rox.main.util;

import org.apache.commons.codec.digest.DigestUtils;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;

public class MathUtil {

    private char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789").toCharArray();

    public String computeSHAHex(String string) {
        return DigestUtils.shaHex(string);
    }

    public String computeMD5Hex(String string) {
        return DigestUtils.md5Hex(string);
    }

    public String computeSHA256(String string) {
        try {
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : MessageDigest.getInstance("SHA-256").digest(string.getBytes("UTF-8"))) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public char[] getPossibleCharacters() {
        return possibleCharacters;
    }

    public void setPossibleCharacters(char[] possibleCharacters) {
        this.possibleCharacters = possibleCharacters;
    }

    public double getCpuLoad() {

        try{
            AttributeList list = ManagementFactory.getPlatformMBeanServer().getAttributes(ObjectName.getInstance("java.lang:type=OperatingSystem"),
                    new String[]{"ProcessCpuLoad"});

            if (list.isEmpty()) return Double.NaN;
            Double value = (Double) ((Attribute) list.get(0)).getValue();
            if (value == -1.0) return Double.NaN;
            return ((int) (value * 1000) / 10.0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public long getTotalRAMSize(){
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    }
    public double getUsedRAMSize(){
        return Runtime.getRuntime().totalMemory() / (1024.0 * 1024.0 * 1024.0);
    }
}
