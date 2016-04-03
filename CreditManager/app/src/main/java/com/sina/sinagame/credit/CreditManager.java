package com.sina.sinagame.credit;

import com.android.overlay.RunningEnvironment;
import com.android.overlay.utils.LogUtils;
import com.sina.engine.base.enums.HttpTypeEnum;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.request.AccountInfo;
import com.sina.request.AccountInfoRequestModel;
import com.sina.request.ReuqestDataProcess;

import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class CreditManager implements Serializable {

    protected static CreditManager instance;

    static {
        instance = new CreditManager();
        RunningEnvironment.getInstance().addManager(instance);
    }

    public static CreditManager getInstance() {
        return instance;
    }

    protected CreditManager() {
    }

    public void requestAccountList(OnAccountListReceivedListener l) {
        String requestDomainName = "file:///android_asset/account_info.txt";
        String requestPhpName = "";
        String requestAction = null;

        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);

        RequestOptions requestOptions = new RequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.list)
                .setReturnModelClass(AccountInfo.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AccountInfoRequestResult(l), null);
    }

    class AccountInfoRequestResult implements RequestDataListener {
        OnAccountListReceivedListener l;
        public AccountInfoRequestResult(OnAccountListReceivedListener l) {
            this.l = l;
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            ArrayList<AccountInfo> list = null;
            boolean success = false;
            String message = taskModel.getMessage();
            if (taskModel.getReturnModel() != null) {
                list = (ArrayList<AccountInfo>) taskModel.getReturnModel();
                if (list != null) {
                    if (String.valueOf(HttpStatus.SC_OK).equalsIgnoreCase(
                            taskModel.getResult())) {
                        success = true;
                    }
                }
            }
            notifyAccountListResult(success, message, list, l);
        }
    }

    protected void notifyAccountListResult(final boolean success, final String message,
                                          final List<AccountInfo> accountInfos,
                                          final OnAccountListReceivedListener listener) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    if (success) {
                        listener.onAccountListReceivedSuccess(accountInfos);
                    } else {
                        listener.onAccountListReceivedFailure(message);
                    }
                }
                for (OnAccountListReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnAccountListReceivedListener.class)) {
                    if (success) {
                        listener.onAccountListReceivedSuccess(accountInfos);
                    } else {
                        listener.onAccountListReceivedFailure(message);
                    }
                }
            }
        });
    }

    public void requestAccountScore(String id, String token, String deadline, String uid) {
        if (id == null || id.length() == 0) {
            return;
        }
        if (token == null || token.length() == 0) {
            return;
        }
        if (deadline == null || deadline.length() == 0) {
            return;
        }
        if (uid == null || uid.length() == 0) {
            return;
        }
        String requestDomainName = "http://gameapi.g.sina.com.cn/game_api/";
        String requestPhpName = "userApi.php";
        String requestAction = "userInfo";

        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);
        accountInfoRequestModel.setGuid(id);
        accountInfoRequestModel.setGtoken(token);
        accountInfoRequestModel.setDeadline(deadline);
        accountInfoRequestModel.setUid(uid);

        RequestOptions requestOptions = new RequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(AccountInfo.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AccountScoreRequestResult(id), null);
    }

    class AccountScoreRequestResult implements RequestDataListener {
        String id;

        public AccountScoreRequestResult(String id) {
            this.id = id;
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            AccountInfo retModel = null;
            boolean success = false;
            String message = taskModel.getMessage();
            if (taskModel.getReturnModel() != null) {
                retModel = (AccountInfo) taskModel.getReturnModel();
                if (retModel != null) {
                    if (String.valueOf(HttpStatus.SC_OK).equalsIgnoreCase(
                            taskModel.getResult())) {
                        retModel.setGuid(id);
                        success = true;
                    }
                }
            }
            notifyAccountScoreResult(success, message, id, retModel.getScore());
        }
    }

    protected void notifyAccountScoreResult(final boolean success, final String message,
                                           final String uid, final String score) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnAccountScoreReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnAccountScoreReceivedListener.class)) {
                    if (success) {
                        listener.onAccountScoreReceivedSuccess(uid, score);
                    } else {
                        listener.onAccountScoreReceivedFailure(message);
                    }
                }
            }
        });
    }
}
