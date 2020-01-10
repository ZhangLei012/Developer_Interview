package com.github.keyboard3.developerinterview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.keyboard3.developerinterview.adapter.CommentExpandAdapter;
import com.github.keyboard3.developerinterview.base.BaseActivity;
import com.github.keyboard3.developerinterview.entity.CommentBean;
import com.github.keyboard3.developerinterview.entity.CommentDetailBean;
import com.github.keyboard3.developerinterview.entity.Problem;
import com.github.keyboard3.developerinterview.entity.ReplyDetailBean;
import com.github.keyboard3.developerinterview.model.ShareModel;
import com.github.keyboard3.developerinterview.model.WebViewModel;
import com.github.keyboard3.developerinterview.util.SystemUtil;
import com.github.keyboard3.developerinterview.view.CommentExpandableListView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 题目详情页
 * Author zhanglei 2019-12-16
 *
 */
public class ProblemDetailActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = "ProblemDetailActivity";
    private Problem problem;
    private String userName;
    @BindView(R.id.tv_title) TextView titleView;
    @BindView(R.id.tv_content) TextView contentView;
    @BindView(R.id.tv_source) TextView sourceView;
    @BindView(R.id.wb_answer) WebView answerHtmlView;
    @BindView(R.id.detail_page_lv_comment) CommentExpandableListView commentExpandableListView;

    //private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private String problemId;
    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://90sheji.com/yuansu/0-0-0-0-1.html?pid=15033923\",\n" +
            "\t\t\t\t\"content\": \"Java是世界上最好的语言！\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://90sheji.com/yuansu/0-0-0-0-1.html?pid=15033923\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"C++才是！\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"程序员2\",\n" +
            "\t\t\t\t\"userLogo\": \"http://90sheji.com/yuansu/0-0-0-0-1.html?pid=15033923\",\n" +
            "\t\t\t\t\"content\": \"PHP才是世界上最好的语言!\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://90sheji.com/yuansu/0-0-0-0-1.html?pid=15033923\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"Python才是！\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"程序员3\",\n" +
            "\t\t\t\t\"userLogo\": \"http://90sheji.com/yuansu/0-0-0-0-1.html?pid=15033923\",\n" +
            "\t\t\t\t\"content\": \"Javascript才是世界上最好的语言！\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取密码
        userName=sp.getString("loginUserName", "");//传入用户名获取密码
        setContentView(R.layout.activity_problem_detail);
        setTitle(R.string.title_problem_detail);

        ButterKnife.bind(this);

        initProblemFromIntentAndCheck();
        initViewsWithProblemAndComment();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.problem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share)
            SystemUtil.sendText(this, ConfigConst.getShareInnerLink(this, problem));
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_source)
    void sourceViewClick(){
        Intent intent = new Intent(ProblemDetailActivity.this, WebViewActivity.class);
        intent.putExtra(ConfigConst.INTENT_KEY, problem.source);
        intent.putExtra(ConfigConst.INTENT_SEARCH_KEY, problem.title);
        startActivity(intent);
    }

    void initProblemFromIntentAndCheck() {
        problem = (Problem) getIntent().getSerializableExtra(ConfigConst.INTENT_ENTITY);
        if (problem != null) return;

        String uri = getIntent().getStringExtra(ConfigConst.INTENT_KEY);
        if (TextUtils.isEmpty(uri))
            uri = getIntent().getData().toString();
        problem = ShareModel.problemOpenComingIntent(this, Uri.parse(uri));
        pullCommentFromServer();
    }

    void initViewsWithProblemAndComment() {
        titleView.setText(problem.title);
        contentView.setText(problem.content);

        toggleDialogAdvance(true);
        WebViewModel.initWebView(answerHtmlView, problem.answer, this);


        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        pullCommentFromServer();
        commentsList = generateTestData();
        initExpandableListView(commentsList);

    }


    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(ProblemDetailActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by zhanglei on 2019-12-16
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        //pullCommentFromServer();
        SharedPreferences sp=getSharedPreferences("testJson",MODE_PRIVATE);
        String tmp=sp.getString(problem.id+""+problem.content.length(),"");
        if(!tmp.equals("")){
            testJson=tmp;
        }
        Log.d(TAG,"testJson = " + testJson);
        commentBean = gson.fromJson(testJson, CommentBean.class);
        String commentContent=gson.toJson(commentBean);
        Log.d(TAG,commentContent);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){
            showCommentDialog();
        }
    }

    /**
     * by zhanglei 2019-12-16
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();


                    CommentDetailBean detailBean = new CommentDetailBean(userName, commentContent,"刚刚");
                    adapter.addTheCommentData(detailBean);
                    addCommentToServer();
                    //Log.d(TAG,newCommentContent);
                    Toast.makeText(ProblemDetailActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ProblemDetailActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    void addCommentToServer(){
        Gson gson =new Gson();
        String newCommentContent=gson.toJson(commentBean);
        SharedPreferences sp=getSharedPreferences("testJson",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(problem.id+""+problem.content.length(),newCommentContent);
        editor.apply();
        Log.d(TAG,"new commnet = "+newCommentContent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"start to add comment to server.\n");
                OkHttpClient client = new OkHttpClient();
                problemId=problem.id+""+(problem.content.length());
                Log.d(TAG,"add problemId="+problemId);
                RequestBody requestBody = new FormBody.Builder().add("problemId",problemId).add("commentData",newCommentContent).build();
                Request request = new Request.Builder().url("http://108.61.223.228:8888/comment").post(requestBody).build();
                try{
                    Response response  =client.newCall(request).execute();
                    String responseData = response.toString();
                    Log.d(TAG,responseData);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.d(TAG,"finish adding comment to server.\n");
            }
        }).start();

    }

    void pullCommentFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"start to pull comment from server.\n");
                OkHttpClient client = new OkHttpClient();
                problemId=problem.id+""+(problem.content.length());
                Log.d(TAG,"pull problemId = "+problemId);
                Request request = new Request.Builder().url("http://108.61.223.228:8888/comment?problemId="+problemId).get().build();
                try{
                    Log.d(TAG,"http://108.61.223.228:8888/comment?problemId="+problemId);
                    Response response = client.newCall(request).execute();

                    String commentStrList = response.body().string();
                    commentStrList=commentStrList.substring(13);
                    commentStrList=commentStrList.substring(0,commentStrList.length()-2);
                    Log.d(TAG,"pulled comment = "+commentStrList);

                    if(commentStrList!=null){
                        testJson=commentStrList;
                    }
                    Log.d(TAG,"new testJson = "+testJson);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.d(TAG,"finish pulling comment from server.\n");
            }
        }).start();


    }
    /**
     * by zhanglei 2019-12-16
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(userName,replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    addCommentToServer();
                    Toast.makeText(ProblemDetailActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProblemDetailActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
}
