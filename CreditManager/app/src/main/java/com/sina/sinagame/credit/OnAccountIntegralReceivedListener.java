package com.sina.sinagame.credit;

import com.android.overlay.BaseUIListener;

/**
 * Created by liuchonghui on 16/4/3.
 */
public interface OnAccountIntegralReceivedListener extends BaseUIListener {
    void onAccountIntegralReceivedSuccess(String account, String integral);
    void onAccountIntegralReceivedFailure(String message);
}
