package com.sina.sinagame.credit;

import com.alibaba.fastjson.JSON;
import com.android.overlay.RunningEnvironment;
import com.android.overlay.utils.StringUtils;
import com.sina.engine.base.enums.HttpTypeEnum;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.request.AccountInfo;
import com.sina.request.AccountInfoRequestModel;
import com.sina.request.AdditionInfo;
import com.sina.request.CreditData;
import com.sina.request.FindDataIntegralGameModel;
import com.sina.request.GiftDataAllModel;
import com.sina.request.GiftDataModel;
import com.sina.request.HomeDataModel;
import com.sina.request.HomeDataRequestModel;
import com.sina.request.HomeTopNewsModel;
import com.sina.request.PlatformType;
import com.sina.request.ReuqestDataProcess;
import com.sina.request.Sina973RequestOptions;
import com.sina.request.SinaGameRequestOptions;
import com.sina.request.UserGiftListRequestModel;

import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchonghui on 16/4/3.
 */
public class CreditManager implements Serializable {

    protected static CreditManager instance;
    protected List<GiftDataModel> giftList;
    protected List<HomeTopNewsModel> newsList;

    static {
        instance = new CreditManager();
        RunningEnvironment.getInstance().addManager(instance);
    }

    public static CreditManager getInstance() {
        return instance;
    }

    protected CreditManager() {
        giftList = new ArrayList<GiftDataModel>();
        newsList = new ArrayList<HomeTopNewsModel>();
    }

    public void requestAccountList(OnAccountListReceivedListener l) {
        String requestDomainName = "file:///android_asset/account_info.txt";
        String requestPhpName = "";
        String requestAction = null;

        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);

        RequestOptions requestOptions = new SinaGameRequestOptions()
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

    public static final String SINAGAME_DOMAIN_NAME = "http://gameapi.g.sina.com.cn/game_api/";
    public static final String SINAGAME_NEW_DNAME = "http://gameapi.g.sina.com.cn/app/games/api/";

    public static final String SINA973_DOMAIN_NAME = "http://gameapi.g.sina.com.cn/97973_api/";


    public void requestAccountScore(String guid, String token, String deadline, String uid) {
        if (guid == null || guid.length() == 0) {
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
        String requestDomainName = SINAGAME_DOMAIN_NAME;
        String requestPhpName = "userApi.php";
        String requestAction = "userInfo";

        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);
        accountInfoRequestModel.setGuid(guid);
        accountInfoRequestModel.setGtoken(token);
        accountInfoRequestModel.setDeadline(deadline);
        accountInfoRequestModel.setUid(uid);
        accountInfoRequestModel.setFrom(nextID());

        RequestOptions requestOptions = new SinaGameRequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(AccountInfo.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AccountScoreRequestResult(guid), null);
    }

    class AccountScoreRequestResult implements RequestDataListener {
        String guid;

        public AccountScoreRequestResult(String guid) {
            this.guid = guid;
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
                        retModel.setGuid(guid);
                        success = true;
                    }
                }
            }
            notifyAccountScoreResult(success, message, guid, retModel.getScore());
        }
    }

    protected void notifyAccountScoreResult(final boolean success, final String message,
                                            final String guid, final String score) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnAccountScoreReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnAccountScoreReceivedListener.class)) {
                    if (success) {
                        listener.onAccountScoreReceivedSuccess(guid, score);
                    } else {
                        listener.onAccountScoreReceivedFailure(message);
                    }
                }
            }
        });
    }

    public static final String launchTaskId = "126"; // "开启应用"

    public static final String shareTaskId = "127"; // "分享"

    public static final String commentTaskId = "128"; // "评论"

//    public static final String inviteTaskId = "132"; // "邀请"

    public static final String shareAppId = "133"; // "分享App"

    @Deprecated
    public static final String integralTaskId973 = "1001"; // "签到"Ver1.0.0

    public static final String shareTaskId973 = "1002"; // "分享""Ver1.0.0

    public static final String commentTaskId973 = "1003"; // "评论""Ver1.0.0

    @Deprecated
    public static final String attenGameTaskId973 = "1004"; // "关注游戏""Ver1.0.0

    @Deprecated
    public static final String fetchGiftTaskId973 = "1005"; // "领取礼包""Ver1.0.0

    public static final String forumTaskId973 = "1006"; // "查看论坛""Ver1.1.0

    public static final String evaluateTaskId973 = "1007"; // "查看新闻评测""Ver1.1.0

    public static final String launchTaskId973 = "1008"; // "开启应用""Ver1.1.0

    public synchronized void getSinaGameCredits(final AccountInfo info) {
        if (info == null || info.getGuid() == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开启应用
                submitUserOperation(info, launchTaskId, launchTaskId, null); // ok

                // 分享app
                for (PlatformType type : PlatformType.values()) {
                    try {
                        Thread.sleep(300L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    submitUserOperation(info, shareAppId, shareAppId, parseShareAddInfo(type)); //ok
                    try {
                        Thread.sleep(300L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    submitUserOperation(info, shareAppId, shareAppId, parseShareAddInfo(type)); //ok
                }

                // 分享详情页面
                // 评论详情页面
                if (giftList != null) {
                    for (GiftDataModel gift : giftList) {
                        if (gift == null || gift.getGiftId() == null) {
                            continue;
                        }
                        try {
                            Thread.sleep(300L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        submitUserOperation(info, shareTaskId, gift.getGiftId(), parseShareAddInfo(PlatformType.QQ));
                        try {
                            Thread.sleep(300L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        submitUserOperation(info, commentTaskId, gift.getGiftId(), parseShareAddInfo(PlatformType.QQ)); //ok
                    }
                }
            }
        }).start();
    }

    public synchronized void get973Integrals(final AccountInfo info) {
        if (info == null || info.getAccount() == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开启应用
                submitAccountOperation(info, launchTaskId973, launchTaskId973);

                // 分享详情973页面
                // 评论详情973页面
                if (newsList != null) {
                    for (HomeTopNewsModel news : newsList) {
                        if (news == null || news.getAbsId() == null) {
                            continue;
                        }
                        try {
                            Thread.sleep(300L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        submitAccountOperation(info, shareTaskId973, news.getAbsId());
                        try {
                            Thread.sleep(300L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        submitAccountOperation(info, commentTaskId973, news.getAbsId()); //ok
                    }
                }

                // 973查看论坛
                try {
                    Thread.sleep(300L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                submitAccountOperation(info, forumTaskId973, forumTaskId973);

                // 973查看评测
                try {
                    Thread.sleep(300L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                submitAccountOperation(info, evaluateTaskId973, evaluateTaskId973);
            }
        }).start();
    }

    protected String parseShareAddInfo(PlatformType type) {
        if (PlatformType.SinaWeibo == type) {
            return "weibo";
        } else if (PlatformType.Wechat == type) {
            return "weixin";
        } else if (PlatformType.WechatMoment == type) {
            return "weixinzone";
        } else if (PlatformType.QQ == type) {
            return "qq";
        } else if (PlatformType.QZone == type) {
            return "qqzone";
        }
        return null;
    }

    private static String prefix = StringUtils.randomString(5) + "-";

    private static long id = 0;

    protected static synchronized String nextID() {
        return prefix + Long.toString(id++);
    }

    protected String parseTaskName(String taskId, String shareAddInfo) {
        String taskName = null;
        if (launchTaskId.equalsIgnoreCase(taskId)) {
            taskName = "开启应用";
        } else if (shareAppId.equalsIgnoreCase(taskId)) {
            taskName = "分享App";
            if (shareAddInfo != null && shareAddInfo.length() > 0) {
                taskName = shareAddInfo + taskName;
            }
        } else if (shareTaskId.equalsIgnoreCase(taskId)) {
            taskName = "分享详情";
        } else if (commentTaskId.equalsIgnoreCase(taskId)) {
            taskName = "评论详情";
        } else if (integralTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973签到";
        } else if (shareTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973分享";
        } else if (commentTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973评论";
        } else if (attenGameTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973关注游戏";
        } else if (fetchGiftTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973领取礼包";
        } else if (forumTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973查看论坛";
        } else if (evaluateTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973查看评测";
        } else if (launchTaskId973.equalsIgnoreCase(taskId)) {
            taskName = "973开启应用";
        }
        return taskName;
    }

    protected synchronized void submitUserOperation(AccountInfo info, String taskId, String newsId,
                                                    String shareAddInfo) {
        if (info == null || info.getGuid() == null || taskId == null
                || taskId.length() == 0 || newsId == null) {
            return;
        }
        String requestDomainName = SINAGAME_DOMAIN_NAME;
        String requestPhpName = "recodeApi.php";
        String requestAction = "add";

        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);
        accountInfoRequestModel.setUid(info.getAccount());
        accountInfoRequestModel.setGuid(info.getGuid());
        accountInfoRequestModel.setGtoken(info.getGtoken());
        accountInfoRequestModel.setDeadline(info.getDeadline());
        accountInfoRequestModel.setTask_id(taskId);
        if (newsId != null && newsId.length() > 0) {
            accountInfoRequestModel.setNews_id(newsId);
        }

        AdditionInfo ainfo = new AdditionInfo();
        if (shareAddInfo != null && shareAddInfo.length() > 0) {
            ainfo.setShare(shareAddInfo);
        }
        String additionInfo = JSON.toJSONString(ainfo);
        accountInfoRequestModel.setAdditionInfo(additionInfo);
        accountInfoRequestModel.setFrom(nextID());

        RequestOptions requestOptions = new SinaGameRequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(CreditData.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AddCreditRequestResult(info, taskId, newsId, shareAddInfo), null);
    }

    class AddCreditRequestResult implements RequestDataListener {
        AccountInfo info;
        String taskId;
        String newsId;
        String shareAddInfo;

        public AddCreditRequestResult(AccountInfo info, String taskId, String newsId, String shareAddInfo) {
            this.info = info;
            this.taskId = taskId;
            this.newsId = newsId;
            this.shareAddInfo = shareAddInfo;
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            boolean iscatched = false;
            boolean iswarnning = false;
            if (String.valueOf(HttpStatus.SC_OK).equalsIgnoreCase(
                    taskModel.getResult())) {
                iscatched = true;
            } else if (String.valueOf(-100).equalsIgnoreCase(
                    taskModel.getResult())) {
                if (taskModel.getMessage() != null
                        && taskModel.getMessage().contains("超出每天上限")) {
                    iscatched = true;
                    iswarnning = true;
                }
            }
            String result = taskModel == null ? null : taskModel.getResult();
            String message = taskModel == null ? null : taskModel.getMessage();
            if (message == null || message.length() == 0) {
                message = "未知错误";
            }
            String head = parseTaskName(taskId, shareAddInfo);
            message = head + message;
            if (!iscatched) {
                onAddCreditFailure(info, message);
            } else {
                if (iswarnning) {
                    onAddCreditEmpty(info, message);
                } else {
                    onAddCreditSuccess(info, taskId, newsId);
                }
            }
        }
    }

    protected void onAddCreditFailure(final AccountInfo info, final String message) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                new CustomToastDialog(RunningEnvironment.getInstance()
//                        .getApplicationContext()).setWaitTitle(message)
//                        .showMe();
            }
        });
    }

    protected void onAddCreditEmpty(final AccountInfo info, final String message) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                new CustomToastDialog(RunningEnvironment.getInstance()
//                        .getApplicationContext()).setWaitTitle(message)
//                        .showMe();
            }
        });
    }

    protected void onAddCreditSuccess(AccountInfo info, String taskId, String newsId) {
        requestAccountScore(info.getGuid(), info.getGtoken(), info.getDeadline(), info.getAccount());
    }

    public void requestGiftListData() {
        String requestDomainName = SINAGAME_DOMAIN_NAME;
        String requestPhpName = "giftApi.php";
        String requestAction = "recommendList";
        UserGiftListRequestModel userGiftListRequestModel = new UserGiftListRequestModel(
                requestDomainName, requestPhpName);
        userGiftListRequestModel.setAction(requestAction);
        userGiftListRequestModel.setCount(10);
        userGiftListRequestModel.setPage(1);
        userGiftListRequestModel.setType("0");
        RequestOptions requestOptions = new SinaGameRequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.generic)
                .setReturnModelClass(GiftDataAllModel.class);
        ReuqestDataProcess
                .requestData(true, Integer.MAX_VALUE, userGiftListRequestModel,
                        requestOptions, new GiftListRequestListener(), null);
    }

    class GiftListRequestListener implements RequestDataListener {

        public GiftListRequestListener() {
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            if (taskModel.getReturnModel() != null) {
                GiftDataAllModel rmodel = (GiftDataAllModel) taskModel
                        .getReturnModel();
                if (rmodel != null) {
                    if (rmodel.getPrivilege_list() != null) {
                        giftList.addAll(rmodel.getPrivilege_list());
                        notifyGiftListReceived();
                    }
                }
            }
        }
    }

    protected void notifyGiftListReceived() {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnGiftListReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnGiftListReceivedListener.class)) {
                    listener.onGiftListReceivedSuccess();
                }
            }
        });
    }

    public void requestH5Games(AccountInfo info) {
        if (info == null || info.getGuid() == null) {

        }
        String requestDomainName = SINAGAME_NEW_DNAME;
        String requestPhpName = "cf/game_list";

        AccountInfoRequestModel requestModel = new AccountInfoRequestModel(requestDomainName,
                requestPhpName);
        requestModel.setGuid(info.getGuid());
        requestModel.setGtoken(info.getGtoken());
        requestModel.setDeadline(info.getDeadline());

        RequestOptions requestOptions = new SinaGameRequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.list)
                .setReturnModelClass(FindDataIntegralGameModel.class);

        ReuqestDataProcess.requestData(true, requestModel,
                requestOptions, new H5GamesRequestResult(info.getGuid()), null);
    }

    class H5GamesRequestResult implements RequestDataListener {
        String guid;

        public H5GamesRequestResult(String guid) {
            this.guid = guid;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void resultCallBack(TaskModel taskModel) {
            ArrayList<FindDataIntegralGameModel> list = null;
            boolean success = false;
            String message = taskModel.getMessage();
            if (taskModel.getReturnModel() != null) {
                list = (ArrayList<FindDataIntegralGameModel>) taskModel.getReturnModel();
                if (list != null) {
                    if (String.valueOf(HttpStatus.SC_OK).equalsIgnoreCase(
                            taskModel.getResult())) {
                        success = true;
                    }
                }
            }
            notifyH5GamesResult(success, message, guid, list);
        }
    }

    protected void notifyH5GamesResult(final boolean success, final String message,
                                       final String guid, final List<FindDataIntegralGameModel> games) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnH5GamesReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnH5GamesReceivedListener.class)) {
                    if (success) {
                        listener.onH5GamesReceivedSuccess(guid, games);
                    } else {
                        listener.onH5GamesReceivedFailure(message);
                    }
                }
            }
        });
    }

    public void requestNewsListData() {
        String requestDomainName = SINA973_DOMAIN_NAME;
        String requestPhpName = "newsApi.php";
        String requestAction = "newMainList";

        RequestOptions requestOptions = new Sina973RequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(HomeDataModel.class);

        HomeDataRequestModel requestModel = new HomeDataRequestModel(
                requestDomainName, requestPhpName);
        requestModel.setAction(requestAction);
        requestModel.setCount(10);
        requestModel.setPage(1);
        ReuqestDataProcess.requestData(true, 1, requestModel,
                requestOptions, new NewsListRequestListener(), null);
    }

    class NewsListRequestListener implements RequestDataListener {

        public NewsListRequestListener() {
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            if (taskModel.getReturnModel() != null) {
                HomeDataModel rmodel = (HomeDataModel) taskModel.getReturnModel();
                if (rmodel != null) {
                    if (rmodel.getTopNews() != null) {
                        newsList.addAll(rmodel.getTopNews());
                        notifyNewsListReceived();
                    }
                }
            }
        }
    }

    protected void notifyNewsListReceived() {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnNewsListReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnNewsListReceivedListener.class)) {
                    listener.onNewsListReceivedSuccess();
                }
            }
        });
    }

    public void requestAccountIntegral(String uid) {
        if (uid == null || uid.length() == 0) {
            return;
        }
        String requestDomainName = SINA973_DOMAIN_NAME;
        String requestPhpName = "userApi.php";
        String requestAction = "userInfo";
        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);
        accountInfoRequestModel.setUid(uid);

        RequestOptions requestOptions = new Sina973RequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setMemoryLifeTime(120)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(AccountInfo.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AccountIntegralRequestResult(uid), null);
    }

    class AccountIntegralRequestResult implements RequestDataListener {
        String uid;

        public AccountIntegralRequestResult(String uid) {
            this.uid = uid;
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
                        retModel.setAccount(uid);
                        success = true;
                    }
                }
            }
            notifyAccountIntegralResult(success, message, uid, retModel.getIntegral());
        }
    }

    protected void notifyAccountIntegralResult(final boolean success, final String message,
                                               final String account, final String integral) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnAccountIntegralReceivedListener listener : RunningEnvironment
                        .getInstance().getUIListeners(
                                OnAccountIntegralReceivedListener.class)) {
                    if (success) {
                        listener.onAccountIntegralReceivedSuccess(account, integral);
                    } else {
                        listener.onAccountIntegralReceivedFailure(message);
                    }
                }
            }
        });
    }

    public void submitAccountOperation(AccountInfo info, String taskId, String newsId) {
        if (info == null || info.getAccount() == null || taskId == null
                || taskId.length() == 0) {
            return;
        }
        String requestDomainName = SINA973_DOMAIN_NAME;
        String requestPhpName = "userApi.php";
        String requestAction = "addRecode";
        AccountInfoRequestModel accountInfoRequestModel = new AccountInfoRequestModel(
                requestDomainName, requestPhpName);
        accountInfoRequestModel.setAction(requestAction);
        accountInfoRequestModel.setUid(info.getAccount());
        accountInfoRequestModel.setTaskId(taskId);

        RequestOptions requestOptions = new Sina973RequestOptions()
                .setHttpRequestType(HttpTypeEnum.get).setIsMainThread(false)
                .setIsSaveMemory(false).setIsSaveDb(false)
                .setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
                .setReturnModelClass(CreditData.class);

        ReuqestDataProcess.requestData(true, accountInfoRequestModel,
                requestOptions, new AddIntegralRequestResult(info, taskId,
                        newsId), null);
    }

    class AddIntegralRequestResult implements RequestDataListener {
        AccountInfo info;
        String taskId;
        String newsId;

        public AddIntegralRequestResult(AccountInfo info, String taskId, String newsId) {
            this.info = info;
            this.taskId = taskId;
            this.newsId = newsId;
        }

        @Override
        public void resultCallBack(TaskModel taskModel) {
            boolean iscatched = false;
            boolean iswarnning = false;
            if (String.valueOf(HttpStatus.SC_OK).equalsIgnoreCase(
                    taskModel.getResult())) {
                iscatched = true;
            } else if (String.valueOf(-100).equalsIgnoreCase(
                    taskModel.getResult())) {
                if (taskModel.getMessage() != null
                        && taskModel.getMessage().contains("超出每天上限")) {
                    iscatched = true;
                    iswarnning = true;
                }
            }
            String result = taskModel == null ? null : taskModel.getResult();
            String message = taskModel == null ? null : taskModel.getMessage();
            if (message == null || message.length() == 0) {
                message = "未知错误";
            }
            String head = parseTaskName(taskId, null);
            message = head + message;
            if (!iscatched) {
                onAddIntegralFailure(info, message);
            } else {
                if (iswarnning) {
                    onAddIntegralEmpty(info, message);
                } else {
                    onAddIntegralSuccess(info, taskId, newsId);
                }
            }
        }
    }

    protected void onAddIntegralFailure(final AccountInfo info, final String message) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                new CustomToastDialog(RunningEnvironment.getInstance()
//                        .getApplicationContext()).setWaitTitle(message)
//                        .showMe();
            }
        });
    }

    protected void onAddIntegralEmpty(final AccountInfo info, final String message) {
        RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                new CustomToastDialog(RunningEnvironment.getInstance()
//                        .getApplicationContext()).setWaitTitle(message)
//                        .showMe();
            }
        });
    }

    protected void onAddIntegralSuccess(AccountInfo info, String taskId, String newsId) {
        requestAccountIntegral(info.getAccount());
    }
}
