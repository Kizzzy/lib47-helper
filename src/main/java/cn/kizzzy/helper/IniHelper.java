package cn.kizzzy.helper;

import java.io.*;
import java.util.regex.Pattern;

/**
 * not used
 */
public class IniHelper {
    private File configFile;
    private String filePath;

    private static final IniHelper INSTANCE;

    private static final String REGEX_SEC = "^\\[.+\\]$";
    private static final String REGEX_KEY = "^.+=";
    private static final Pattern patternSec;
    private static final Pattern patternKey;

    static {
        INSTANCE = new IniHelper();
        patternSec = Pattern.compile(REGEX_SEC);
        patternKey = Pattern.compile(REGEX_KEY);
    }

    private IniHelper() {

    }

    public static IniHelper getInstance() {
        return INSTANCE;
    }

    public void setFilePath(String filePath) {
        try {
            this.filePath = filePath;
            configFile = new File(filePath);
            if (!configFile.exists())
                configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String get(String section, String key, String defaultString) {
        boolean isFound = false;
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configFile));
            while ((line = reader.readLine()) != null) {
                if (patternSec.matcher(line).matches()) {
                    if (line.contains(section))
                        isFound = true;
                    else {
                        isFound = false;
                    }
                }
                if (isFound && patternKey.matcher(line).find()) {
                    String[] keyValue = line.split("=");
                    if (keyValue[0].equals(key))
                        return keyValue.length == 1 ? "" : keyValue[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultString;
    }

    public void set(String section, String key, String value) {
        String tempPath = filePath + System.currentTimeMillis();
        File tempFile = new File(tempPath);

        boolean isChange = false;
        boolean isInSection = false;
        String line = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            if (!tempFile.exists())
                tempFile.createNewFile();
            reader = new BufferedReader(new FileReader(configFile));
            writer = new BufferedWriter(new FileWriter(tempFile));
            while ((line = reader.readLine()) != null) {
                if (patternSec.matcher(line).matches()) {
                    if (line.contains(section))
                        isInSection = true;
                    else {
                        if (isInSection && !isChange) {
                            writer.write(key + "=" + value + System.getProperty("line.separator"));
                        }
                        isInSection = false;
                    }
                }

                if (isInSection && patternKey.matcher(line).find()) {
                    if (line.split("=")[0].equals(key)) {
                        line = key + "=" + value;
                        isChange = true;
                    }
                }
                writer.write(line + System.getProperty("line.separator"));
            }
            if (!isChange) {
                if (!isInSection)
                    writer.write('[' + section + ']' + System.getProperty("line.separator"));
                writer.write(key + "=" + value + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
                if (reader != null)
                    reader.close();
                configFile.delete();
                tempFile.renameTo(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        IniHelper config = IniHelper.getInstance();
        config.setFilePath("src/config.ini");
        System.out.println(config.get("zzy", "vv", "empty"));
    }
}
