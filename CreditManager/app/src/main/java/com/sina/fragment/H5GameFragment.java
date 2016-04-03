package com.sina.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.overlay.RunningEnvironment;
import com.android.overlay.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.activity.CustomWaitDialog;
import com.sina.activity.WebBrowserActivity;
import com.sina.activity.WebViewActivity;
import com.sina.request.AccountInfo;
import com.sina.request.FindDataIntegralGameModel;
import com.sina.request.UserCookieManager;
import com.sina.sinagame.credit.CreditManager;
import com.sina.sinagame.credit.OnCookieSetCompleteListener;
import com.sina.sinagame.credit.OnH5GamesReceivedListener;
import com.sina.sinagame.credit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liu_chonghui
 */
public class H5GameFragment extends BaseFragment implements OnH5GamesReceivedListener,
        OnCookieSetCompleteListener {

    protected int getPageLayout() {
        return R.layout.h5_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTransaction();

        RunningEnvironment.getInstance().addUIListener(
                OnH5GamesReceivedListener.class, this);
        RunningEnvironment.getInstance().addUIListener(
                OnCookieSetCompleteListener.class, this);

        initData();
    }

    @Override
    public void onDestroy() {
        RunningEnvironment.getInstance().removeUIListener(
                OnCookieSetCompleteListener.class, this);
        RunningEnvironment.getInstance().removeUIListener(
                OnH5GamesReceivedListener.class, this);
        super.onDestroy();
    }

    protected void startTransaction() {
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_still);
    }

    AccountInfo mAccountInfo = new AccountInfo();

    protected void initData() {
        if (getActivity().getIntent() != null) {
            mAccountInfo.setName(getActivity().getIntent().getStringExtra("name"));
            mAccountInfo.setGuid(getActivity().getIntent().getStringExtra("guid"));
            mAccountInfo.setGtoken(getActivity().getIntent().getStringExtra("gtoken"));
            mAccountInfo.setDeadline(getActivity().getIntent().getStringExtra("deadline"));
            mAccountInfo.setAccount(getActivity().getIntent().getStringExtra("account"));
            mAccountInfo.setAccessToken(getActivity().getIntent().getStringExtra("accessToken"));
            mAccountInfo.setExpiresin(getActivity().getIntent().getLongExtra("expiresin", 1460311190961L));
        }
        if (h5GameList.size() == 0) {
            initRequestData();
        }
    }

    List<FindDataIntegralGameModel> h5GameList = new ArrayList<FindDataIntegralGameModel>();

    protected void initRequestData() {
        UserCookieManager.getInstance().setUserCookie(mAccountInfo);
        CreditManager.getInstance().requestH5Games(mAccountInfo);
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
    Button topBtn;
    ListView mListView;
    MyAdapter myAdapter;

    @SuppressLint("InflateParams")
    protected void intView(View view) {
        mUpdateDialog = new CustomWaitDialog(getActivity());
        mUpdateDialog.setCanceledOnTouchOutside(false);


        TextView title = (TextView) view.findViewById(R.id.top_text);
        title.setText(mAccountInfo.getName());
        topBtn = (Button) view.findViewById(R.id.top_btn);
        topBtn.setEnabled(false);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
                intent.putExtra("title", "积分商城");
                String recordUrl = "http://jifen.sina.com.cn/h5/app_inner?back_url=http://jifen.sina.com.cn";
                intent.putExtra("url", recordUrl);
                getActivity().startActivity(intent);
            }
        });
        Button btn1 = (Button) view.findViewById(R.id.top_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
                intent.putExtra("title", "广告任务");
                String recordUrl = "http://jifen.sina.com.cn/task?back_url=http://jifen.sina.com.cn";
                intent.putExtra("url",recordUrl);
                getActivity().startActivity(intent);
            }
        });
        Button btn2 = (Button) view.findViewById(R.id.top_btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
                intent.putExtra("title", "转盘抽奖");
                String recordUrl = "http://jifen.sina.com.cn/hd/draw?qq-pf-to=pcqq.group?back_url=http://jifen.sina.com.cn";
                intent.putExtra("url",recordUrl);
                getActivity().startActivity(intent);
            }
        });
        mListView = (ListView) view.findViewById(R.id.list_layout);
        myAdapter = new MyAdapter();
        myAdapter.setData(h5GameList);
        mListView.setAdapter(myAdapter);
    }

    protected void flushPage() {
        myAdapter.setData(h5GameList);
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
        List<FindDataIntegralGameModel> listData = new ArrayList<FindDataIntegralGameModel>();

        public MyAdapter() {
        }

        public void setData(List<FindDataIntegralGameModel> mDatas) {
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
            return R.layout.h5_game_item;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FindDataIntegralGameModel model = listData.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(getItemLayout(), parent, false);
                holder.layout = (ViewGroup) convertView.findViewById(R.id.item_layout);
                holder.header = (SimpleDraweeView) convertView.findViewById(R.id.user_header);
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.score = (TextView) convertView.findViewById(R.id.item_score);
                holder.btn = (Button) convertView.findViewById(R.id.item_btn);
                holder.listener = new ItemClickListener();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (model.getPic() != null && model.getPic().length() > 0) {
                holder.header.setImageURI(Uri.parse(model.getPic()));
            }
            holder.title.setText(model.getName());
            if (model.getScore() != null && model.getScore().length() > 0) {
                holder.score.setText(model.getScore());
            }
            holder.listener.setData(model);
            holder.btn.setText("点击开始");
            holder.btn.setOnClickListener(holder.listener);

            return convertView;
        }

        class ItemClickListener implements View.OnClickListener {
            FindDataIntegralGameModel item;

            public void setData(FindDataIntegralGameModel item) {
                this.item = item;
            }

            @Override
            public void onClick(View view) {
                if (item == null) {
                    return;
                }
                String url = item.getId();
                if (url == null || url.length() == 0) {
                    url = item.getUrl();
                }
                if (StringUtils.isWebUrl(url)) {
                    Intent launchIntent = new Intent(getActivity(), WebBrowserActivity.class);
                    launchIntent.putExtra("title", item.getName());
                    launchIntent.putExtra("url", url);
                    getActivity().startActivity(launchIntent);
                }
            }
        }

        class ViewHolder {
            ViewGroup layout;
            SimpleDraweeView header;
            TextView title;
            TextView score;
            Button btn;
            ItemClickListener listener;
        }
    }

    @Override
    public void onH5GamesReceivedSuccess(String userId, List<FindDataIntegralGameModel> games) {
        h5GameList.addAll(games);
        flushPage();
    }

    @Override
    public void onH5GamesReceivedFailure(String message) {

    }

    @Override
    public void onCookieSetComplete(String guid) {
        if (topBtn != null) {
            if (guid.equalsIgnoreCase(this.mAccountInfo.getGuid())) {
                topBtn.setEnabled(true);
            } else {
                topBtn.setEnabled(false);
            }
        }
    }
}
