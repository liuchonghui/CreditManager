package com.sina.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sina.engine.base.enums.HttpTypeEnum;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.request.AccountInfo;
import com.sina.request.AccountInfoRequestModel;
import com.sina.request.ReuqestDataProcess;
import com.sina.sinagame.credit.CreditManager;
import com.sina.sinagame.credit.OnAccountListReceivedListener;
import com.sina.sinagame.credit.R;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_chonghui
 */
public class CreditFragment extends BaseFragment {

    protected int getPageLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTransaction();
        initData();
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
        CreditManager.getInstance().requestAccountList(new OnAccountListReceivedListener() {
            @Override
            public void onAccountListReceivedSuccess(List<AccountInfo> accountInfos) {
                accountList.addAll(accountInfos);
                flushPage();
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

    ListView mListView;
    MyAdapter myAdapter;

    @SuppressLint("InflateParams")
    protected void intView(View view) {
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
                holder.listener = new ItemClickListener();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(model.getName());
            holder.score.setText(model.getScore());
            holder.listener.setData(model);
            holder.layout.setOnClickListener(holder.listener);

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
            }
        }

        class ViewHolder {
            ViewGroup layout;
            TextView title;
            TextView score;
            ItemClickListener listener;
        }
    }
}
