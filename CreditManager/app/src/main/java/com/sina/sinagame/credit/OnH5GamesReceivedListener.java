package com.sina.sinagame.credit;

import com.android.overlay.BaseUIListener;
import com.sina.request.FindDataIntegralGameModel;

import java.util.List;

/**
 * Created by liuchonghui on 16/4/3.
 */
public interface OnH5GamesReceivedListener extends BaseUIListener {
    void onH5GamesReceivedSuccess(String userId, List<FindDataIntegralGameModel> games);

    void onH5GamesReceivedFailure(String message);
}
