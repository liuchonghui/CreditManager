package com.sina.sinagame.credit;

import com.android.overlay.BaseUIListener;
import com.sina.request.AccountInfo;

import java.util.List;

/**
 * Created by liuchonghui on 16/4/3.
 */
public interface OnAccountListReceivedListener extends BaseUIListener {
    void onAccountListReceivedSuccess(List<AccountInfo> accountInfos);
    void onAccountListReceivedFailure(String message);
}
