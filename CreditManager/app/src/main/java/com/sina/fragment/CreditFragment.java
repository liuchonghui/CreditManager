package com.sina.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.sina.activity.CustomWaitDialog;
import com.sina.request.AccountInfo;
import com.sina.sinagame.credit.CreditManager;
import com.sina.sinagame.credit.OnAccountListReceivedListener;
import com.sina.sinagame.credit.OnAccountScoreReceivedListener;
import com.sina.sinagame.credit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_chonghui
 */
public class CreditFragment extends BaseFragment implements OnAccountScoreReceivedListener {

    protected int getPageLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTransaction();

        RunningEnvironment.getInstance().addUIListener(
                OnAccountScoreReceivedListener.class, this);

        initData();
    }

    @Override
    public void onDestroy() {
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

    protected void initRequestData() {
        CreditManager.getInstance().requestGiftListData();
        CreditManager.getInstance().requestAccountList(new OnAccountListReceivedListener() {
            @Override
            public void onAccountListReceivedSuccess(List<AccountInfo> accountInfos) {
                accountList.addAll(accountInfos);
                flushPage();

                requestAccountsScore();
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
    ListView mListView;
    MyAdapter myAdapter;

    @SuppressLint("InflateParams")
    protected void intView(View view) {
        mUpdateDialog = new CustomWaitDialog(getActivity());
        mUpdateDialog.setCanceledOnTouchOutside(false);

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

    @Override
    public void onResume() {
        super.onResume();
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
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.score = (TextView) convertView.findViewById(R.id.item_score);
                holder.btn = (Button) convertView.findViewById(R.id.item_btn);
                holder.listener = new ItemClickListener();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(model.getName());
            if (model.getScore() != null && model.getScore().length() > 0) {
                holder.score.setText(model.getScore());
            }
            holder.listener.setData(model);
            holder.btn.setOnClickListener(holder.listener);

            return convertView;
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
            TextView title;
            TextView score;
            Button btn;
            ItemClickListener listener;
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
            }
        }
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
}
