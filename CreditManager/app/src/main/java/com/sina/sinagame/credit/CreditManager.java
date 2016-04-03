package com.sina.sinagame.credit;

import com.android.overlay.RunningEnvironment;
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
}
