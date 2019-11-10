package com.engingplan.womarketing.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public class DownloadUtil {


    private boolean isConnected(Context context, int type) {
        // 获得链接服务管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获得所有的网络信息
        Network[] networks = connectivityManager.getAllNetworks();

        for (Network network : networks) {
            // 获得网络信息
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

            if (networkInfo != null
                    && networkInfo.getType() == type // 判断链接的网络类型
                    && networkInfo.isConnected()) {  // 该网络是否链接
                return true;
            }
        }

        return false;
    }


    public static void download(String urlString, Map map, String localFileName) {
        OkHttpClientUtils.getInstance().doPostAsyn(urlString, map, new OkHttpClientUtils.NetWorkCallBack() {
            @Override
            public void onSuccess(String response) {


            }

            @Override
            public void onFail(String str) {

            }
        });
    }



}
