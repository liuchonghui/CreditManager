package com.sina.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.overlay.RunningEnvironment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.activity.CustomWaitDialog;
import com.sina.activity.H5GameActivity;
import com.sina.request.AccountInfo;
import com.sina.sinagame.credit.CreditManager;
import com.sina.sinagame.credit.OnAccountIntegralReceivedListener;
import com.sina.sinagame.credit.OnAccountListReceivedListener;
import com.sina.sinagame.credit.OnAccountScoreReceivedListener;
import com.sina.sinagame.credit.OnGiftListReceivedListener;
import com.sina.sinagame.credit.OnNewsListReceivedListener;
import com.sina.sinagame.credit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_chonghui
 */
public class CreditFragment extends BaseFragment implements
        OnAccountScoreReceivedListener, OnAccountIntegralReceivedListener,
        OnGiftListReceivedListener, OnNewsListReceivedListener {

    protected int getPageLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTransaction();

        RunningEnvironment.getInstance().addUIListener(
                OnAccountScoreReceivedListener.class, this);
        RunningEnvironment.getInstance().addUIListener(
                OnAccountIntegralReceivedListener.class, this);
        RunningEnvironment.getInstance().addUIListener(
                OnGiftListReceivedListener.class, this);
        RunningEnvironment.getInstance().addUIListener(
                OnNewsListReceivedListener.class, this);

        initData();
    }

    @Override
    public void onDestroy() {
        RunningEnvironment.getInstance().removeUIListener(
                OnNewsListReceivedListener.class, this);
        RunningEnvironment.getInstance().removeUIListener(
                OnGiftListReceivedListener.class, this);
        RunningEnvironment.getInstance().removeUIListener(
                OnAccountIntegralReceivedListener.class, this);
        RunningEnvironment.getInstance().removeUIListener(
                OnAccountScoreReceivedListener.class, this);
        super.onDestroy();
    }

    protected void startTransaction() {
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_still);
    }

    protected void initData() {
        if (getActivity().getIntent() != null) {
        }
        if (accountList.size() == 0) {
            initRequestData();
        }
    }

    List<AccountInfo> accountList = new ArrayList<AccountInfo>();
    boolean accountListReceived = false;

    protected void initRequestData() {
        CreditManager.getInstance().requestGiftListData(); // 获取新浪游戏礼包列表
        CreditManager.getInstance().requestNewsListData(); // 获取973咨询列表

        CreditManager.getInstance().requestAccountList(new OnAccountListReceivedListener() {
            @Override
            public void onAccountListReceivedSuccess(List<AccountInfo> accountInfos) {
                accountListReceived = true;
                accountList.addAll(accountInfos);
                flushPage();

                requestAccountsScore();

                flushButton();
            }

            @Override
            public void onAccountListReceivedFailure(String message) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (!isViewNull()) {
            return mView;
        }
        mView = inflater.inflate(getPageLayout(), container, false);
        intView(mView);
        return mView;
    }

    CustomWaitDialog mUpdateDialog;
    Button mButton;
    ListView mListView;
    MyAdapter myAdapter;

    @SuppressLint("InflateParams")
    protected void intView(View view) {
        mUpdateDialog = new CustomWaitDialog(getActivity());
        mUpdateDialog.setCanceledOnTouchOutside(false);

        mButton = (Button) view.findViewById(R.id.top_btn);
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AccountInfo info : accountList) {
                    if (info == null || info.getGuid() == null) {
                        continue;
                    }
                    CreditManager.getInstance().getCredits(info);
                }
            }
        });
        mListView = (ListView) view.findViewById(R.id.list_layout);
        myAdapter = new MyAdapter();
        myAdapter.setData(accountList);
        mListView.setAdapter(myAdapter);
    }

    protected void flushPage() {
        myAdapter.setData(accountList);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    boolean firstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (firstResume) {
            firstResume = false;
        }
        if (!firstResume) {
            requestAccountsScore();
        }
    }

    public boolean holdGoBack() {
        // if (myOneKeyShare != null && myOneKeyShare.isShow()) {
        // return true;
        // }
        // if (popupAttacher != null && popupAttacher.isShowing()) {
        // return true;
        // }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (holdGoBack()) {
                // if (myOneKeyShare != null && myOneKeyShare.isShow()) {
                // myOneKeyShare.close();
                // }
                // if (popupAttacher != null && popupAttacher.isShowing()) {
                // popupAttacher.closePop();
                // }
                flag = true;
            }
        }
        return flag;
    }

    public void leaveCurrentPage() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.push_still,
                R.anim.push_right_out);
    }

    private class MyAdapter extends BaseAdapter {
        List<AccountInfo> listData = new ArrayList<AccountInfo>();

        public MyAdapter() {
        }

        public void setData(List<AccountInfo> mDatas) {
            this.listData = mDatas;
        }

        @Override
        public int getCount() {
            if (listData != null) {
                return listData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        protected int getItemLayout() {
            return R.layout.selectjubao_grid_item;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AccountInfo model = listData.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(getItemLayout(), parent, false);
                holder.layout = (ViewGroup) convertView.findViewById(R.id.item_layout);
                holder.header = (SimpleDraweeView) convertView.findViewById(R.id.user_header);
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.score_sinagame = (TextView) convertView.findViewById(R.id.score_sinagame);
                holder.score_973 = (TextView) convertView.findViewById(R.id.score_973);
                holder.btn = (Button) convertView.findViewById(R.id.item_btn);
                holder.btn1 = (Button) convertView.findViewById(R.id.item_btn1);
                holder.btn2 = (Button) convertView.findViewById(R.id.item_btn2);
                holder.listener = new ItemClickListener();
                holder.jclistener = new JiuCaiHuaClickListener();
                holder.h5listener = new H5ClickListener();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (model.getHeadUrl() != null && model.getHeadUrl().length() > 0) {
                holder.header.setImageURI(Uri.parse(model.getHeadUrl()));
            }
            holder.title.setText(model.getName());
            if (model.getScore() != null && model.getScore().length() > 0) {
                holder.score_sinagame.setText(model.getScore());
            }
            if (model.getIntegral() != null && model.getIntegral().length() > 0) {
                holder.score_973.setText(model.getIntegral());
            }
            holder.listener.setData(model);
            holder.btn.setOnClickListener(holder.listener);
            holder.jclistener.setData(model);
            holder.btn1.setOnClickListener(holder.jclistener);
            holder.h5listener.setData(model);
            holder.btn2.setOnClickListener(holder.h5listener);

            return convertView;
        }

        class H5ClickListener extends ItemClickListener {
            @Override
            public void onClick(View view) {
                if (item == null) {
                    return;
                }
                gotoH5GamePage(item);
            }
        }

        class JiuCaiHuaClickListener extends ItemClickListener {
            @Override
            public void onClick(View view) {
                if (item == null) {
                    return;
                }
            }
        }

        class ItemClickListener implements View.OnClickListener {
            AccountInfo item;

            public void setData(AccountInfo item) {
                this.item = item;
            }

            @Override
            public void onClick(View view) {
                if (item == null) {
                    return;
                }
                CreditManager.getInstance().getCredits(item);
            }
        }

        class ViewHolder {
            ViewGroup layout;
            SimpleDraweeView header;
            TextView title;
            TextView score_sinagame;
            TextView score_973;
            Button btn;
            Button btn1;
            Button btn2;
            ItemClickListener listener;
            ItemClickListener jclistener;
            ItemClickListener h5listener;
        }
    }

    protected void requestAccountsScore() {
        for (AccountInfo info : accountList) {
            if (info == null || info.getGuid() == null) {
                continue;
            }
            requestAccountScore(info.getGuid());
        }
    }

    protected void requestAccountScore(String id) {
        for (AccountInfo info : accountList) {
            if (id.equalsIgnoreCase(info.getGuid())) {
                CreditManager.getInstance().requestAccountScore(info.getGuid(),
                        info.getGtoken(), info.getDeadline(), info.getAccount());
                CreditManager.getInstance().requestAccountIntegral(info.getAccount());
            }
        }
    }

    protected void gotoH5GamePage(AccountInfo item) {
        if (item == null || item.getGuid() == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), H5GameActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("avatar", item.getHeadUrl());
        intent.putExtra("guid", item.getGuid());
        intent.putExtra("gtoken", item.getGtoken());
        intent.putExtra("deadline", item.getDeadline());
        intent.putExtra("account", item.getAccount());
        intent.putExtra("accessToken", item.getAccessToken());
        intent.putExtra("expiresin", item.getExpiresin());
        getActivity().startActivity(intent);
    }

    @Override
    public void onAccountScoreReceivedSuccess(String userId, String score) {
        boolean modified = false;
        for (AccountInfo info : accountList) {
            if (info == null || info.getGuid() == null) {
                continue;
            }
            if (info.getGuid().equalsIgnoreCase(userId)) {
                modified = true;
                info.setScore(score);
            }
        }
        if (modified) {
            flushPage();
        }
    }

    @Override
    public void onAccountScoreReceivedFailure(String message) {

    }

    @Override
    public void onAccountIntegralReceivedSuccess(String account, String integral) {
        boolean modified = false;
        for (AccountInfo info : accountList) {
            if (info == null || info.getAccount() == null) {
                continue;
            }
            if (info.getAccount().equalsIgnoreCase(account)) {
                modified = true;
                info.setIntegral(integral);
            }
        }
        if (modified) {
            flushPage();
        }
    }

    @Override
    public void onAccountIntegralReceivedFailure(String message) {

    }

    boolean giftListReceived = false;

    @Override
    public void onGiftListReceivedSuccess() {
        giftListReceived = true;
        flushButton();
    }

    boolean newsListReceived = false;

    @Override
    public void onNewsListReceivedSuccess() {
        newsListReceived = true;
        flushButton();
    }

    protected void flushButton() {
        if (mButton != null) {
            if (mButton.isEnabled()) {
                return;
            }
            if (accountListReceived && giftListReceived && newsListReceived) {
                mButton.setEnabled(true);
            } else {
                mButton.setEnabled(false);
            }
        }
    }

}
