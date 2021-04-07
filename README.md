# ExternalStorageRW
杭州迈冲科技外置 SD/USB 读写实例

## 一、使用说明
### SD 卡

#### 申请读写权限
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

#### 获取挂载路径
- 高于/等于 Android 6.0
使用 TFCardUtil 类获取路径。
```
TFCardUtil.getMountPaths(this); 
```

- Android 5.1
使用固定路径。
```
/mnt/external_sd
```

- Android 4.4
使用固定路径。
```
/mnt/extsd
```

#### 使用 Java 接口读写文件。
```
FileIOUtils.writeFileFromString(filePath, etTfCard.getText().toString().trim());
```

### USB

#### 申请读写权限
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

#### 获取挂载路径
使用 UsbUtil 类获取路径。
```
UsbUtil.getMountPaths(this);
```

#### 使用 Java 接口读写文件。
```
FileIOUtils.writeFileFromString(filePath, etUsb.getText().toString().trim());
```

## 二、下载体验
[外置 SD/USB 实例 apk 下载](https://github.com/Hangzhou-Maichong-Technology/ExternalStorageRW/raw/master/apk/ExternalStorageRW.apk)