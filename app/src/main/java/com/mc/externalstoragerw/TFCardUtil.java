package com.mc.externalstoragerw;

import android.content.Context;
import android.os.storage.StorageManager;

import com.blankj.utilcode.util.ShellUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TFCardUtil {
    private static final String TAG = "TFCardUtil";

    public static List<String> getMountPaths(Context context) {
        List<String> resultList = new ArrayList<>();

        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        Class<?> volumeInfoClazz = null;
        Class<?> diskInfoClazz = null;

        try {
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            diskInfoClazz = Class.forName("android.os.storage.DiskInfo");

            Method getVolumes = storageManager.getClass().getMethod("getVolumes");
            Method getDisk = volumeInfoClazz.getMethod("getDisk");
            Method getPath = volumeInfoClazz.getMethod("getPath");
            Method isSd = diskInfoClazz.getMethod("isSd");

            List<Object> result = (List<Object>) getVolumes.invoke(storageManager);

            for (Object volume : result) {
                File file = (File) getPath.invoke(volume);
                if (file != null) {
                    Object diskInfo = getDisk.invoke(volume);

                    if (diskInfo != null) {
                        boolean isASd = (boolean) isSd.invoke(diskInfo);
                        if (isASd) {
                            resultList.add(file.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0, count = resultList.size(); i < count; i++) {
            resultList.set(i, resultList.get(i).replace("storage", "mnt/media_rw"));
        }

        return resultList;
    }

    public static boolean isTFCardExist(){
        return ShellUtils.execCmd("ls /dev/block/mmcblk1*", true).result == 0;
    }
}
