package com.mc.externalstoragerw;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String USB_FILE = "usbFile.txt";
    private static final String TFCARD_FILE = "tfCardFile.txt";

    private Button btnUsbWrite;
    private Button btnUsbRead;
    private Button btnTfCardWrite;
    private Button btnTfCardRead;
    private EditText etUsb;
    private EditText etTfCard;
    private TextView tvUsb;
    private TextView tvTfCard;

    private PermissionUtil permissionUtil = new PermissionUtil();
    private ArrayList<String> usbPaths = new ArrayList<>();
    private ArrayList<String> tfCardPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !permissionUtil.hasPermission(this)) {
            permissionUtil.requestPermission(this);
        }

        btnUsbWrite = findViewById(R.id.btn_usb_write);
        btnUsbRead = findViewById(R.id.btn_usb_read);
        btnTfCardWrite = findViewById(R.id.btn_tfcard_write);
        btnTfCardRead = findViewById(R.id.btn_tfcard_read);
        etUsb = findViewById(R.id.et_usb);
        etTfCard = findViewById(R.id.et_tfcard);
        tvUsb = findViewById(R.id.tv_usb);
        tvTfCard = findViewById(R.id.tv_tfcard);

        mountUsbPaths();
        mountTfCardPaths();

        btnUsbWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (usbPaths.size() <= 0) {
                            ToastUtils.showShort("未找到 U 盘");
                            return;
                        }

                        String filePath = usbPaths.get(0) + "/" + USB_FILE;

                        if (!FileUtils.isFileExists(filePath)) {
                            FileUtils.createOrExistsFile(filePath);
                        }

                        FileIOUtils.writeFileFromString(filePath, etUsb.getText().toString().trim());
                    }
                });
            }
        });

        btnUsbRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (usbPaths.size() <= 0) {
                            ToastUtils.showShort("未找到 U 盘");
                            return;
                        }

                        String filePath = usbPaths.get(0) + "/" + USB_FILE;

                        if (!FileUtils.isFileExists(filePath)) {
                            return;
                        }

                        final String text = FileIOUtils.readFile2String(filePath);

                        if (!TextUtils.isEmpty(text)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvUsb.setText(text);
                                }
                            });
                        }
                    }
                });
            }
        });

        btnTfCardWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tfCardPaths.size() <= 0) {
                            ToastUtils.showShort("未找到 TF 卡");
                            return;
                        }

                        String filePath = tfCardPaths.get(0) + "/" + TFCARD_FILE;

                        if (!FileUtils.isFileExists(filePath)) {
                            FileUtils.createOrExistsFile(filePath);
                        }

                        FileIOUtils.writeFileFromString(filePath, etTfCard.getText().toString().trim());
                    }
                });
            }
        });

        btnTfCardRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tfCardPaths.size() <= 0) {
                            ToastUtils.showShort("未找到 TF 卡");
                            return;
                        }

                        String filePath = tfCardPaths.get(0) + "/" + TFCARD_FILE;

                        if (!FileUtils.isFileExists(filePath)) {
                            return;
                        }

                        final String text = FileIOUtils.readFile2String(filePath);

                        if (!TextUtils.isEmpty(text)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTfCard.setText(text);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void mountTfCardPaths() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tfCardPaths = (ArrayList<String>) TFCardUtil.getMountPaths(this);

            if (tfCardPaths.size() == 0) {
                ToastUtils.showShort("未找到 TF 卡");
                return;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tfCardPaths.add("/mnt/external_sd");

            if (!TFCardUtil.isTFCardExist()) {
                ToastUtils.showShort("未找到 TF 卡");
                return;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tfCardPaths.add("/mnt/extsd");

            if (!TFCardUtil.isTFCardExist()) {
                ToastUtils.showShort("未找到 TF 卡");
                return;
            }
        }

        Log.i(TAG, "tfCardPaths == " + tfCardPaths.toString());
    }

    private void mountUsbPaths() {
        usbPaths = (ArrayList<String>) UsbUtil.getMountPaths(this);

        if (usbPaths.size() == 0) {
            ToastUtils.showShort("未找到 U 盘");
            return;
        }

        Log.i(TAG, "usbPaths == " + usbPaths.toString());
    }
}