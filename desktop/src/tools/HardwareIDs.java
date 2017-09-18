package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class HardwareIDs {

    private static String sn = null;

    public static void main(String[] args) {
        System.out.println(getSerial());
    }

    public static String getSerial() {
        try {
            String OsName = System.getProperty("os.name");
            String OsVer = "";//System.getProperty("os.version");
            String OsArch = System.getProperty("os.arch");
            String UserName = System.getProperty("user.name");
            int cpus = Runtime.getRuntime().availableProcessors();
            // System.out.println(OsName + "|" + OsVer + "|" + OsArch + "|" +
            // UserName + cpus + getWinSerialNumber());
            if (OsName.toLowerCase().contains("windows"))
                return OsName + "|" + OsVer + "|" + OsArch + "|" + UserName + cpus + getWinSerialNumber();
            else if (OsName.toLowerCase().contains("mac"))
                return OsName + "|" + OsVer + "|" + OsArch + "|" + UserName + cpus + getMacSerialNumber();
            else if (OsName.toLowerCase().contains("linux") || OsName.toLowerCase().contains("free") || OsName.toLowerCase().contains("sun"))
                return OsName + "|" + OsVer + "|" + OsArch + "|" + UserName + cpus + getLixSerialNumber();
            else
                return OsName + "|" + OsVer + "|" + OsArch + "|" + UserName + cpus + "ghgsadjg33434###>>sd";
        }catch (Exception e){
            e.printStackTrace();
            return "ghgsadjg33434###>>sd";
        }
    }

    @SuppressWarnings("resource")
    public static final String getWinSerialNumber() throws Exception {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"wmic", "bios", "get", "serialnumber"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                if ("SerialNumber".toLowerCase().contains(next.toLowerCase())) {
                    sn = sc.next().trim();
                    break;
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            sn = "nulldiversity-it";
        }

        return sn;
    }

    public static final String getMacSerialNumber() throws Exception {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "serial number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(marker)) {
                    sn = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            sn = "nulldiversity-it";
        }

        return sn;
    }

    public static final String getLixSerialNumber() throws Exception {

        if (sn == null) {
            readDmidecode();
        }
        if (sn == null) {
            readLshal();
        }
        if(sn == null){
            sn = Arrays.toString(getMACAddress());
        }
        if (sn == null) {
            sn = "nulldiversity-it";
        }

        return sn;
    }

    private static BufferedReader read(String command) throws Exception {

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command.split(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    private static void readDmidecode() throws Exception {

        String line = null;
        String marker = "Serial Number:";
        BufferedReader br = null;

        try {
            br = read("dmidecode -t system");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readLshal() throws Exception{

        String line = null;
        String marker = "system.hardware.serial =";
        BufferedReader br = null;

        try {
            br = read("lshal");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static byte[] getMACAddress() throws SocketException, UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);

        return networkInterface.getHardwareAddress();
    }


}
