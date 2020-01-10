package com.github.keyboard3.developerinterview.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.keyboard3.developerinterview.ConfigConst;
import com.github.keyboard3.developerinterview.ProblemDetailActivity;
import com.github.keyboard3.developerinterview.R;
import com.github.keyboard3.developerinterview.SettingActivity;
import com.github.keyboard3.developerinterview.adapter.ProblemAdapter;
import com.github.keyboard3.developerinterview.base.BaseFragment;
import com.github.keyboard3.developerinterview.callback.Callback;
import com.github.keyboard3.developerinterview.data.ProblemsDrive;
import com.github.keyboard3.developerinterview.entity.Problem;
import com.github.keyboard3.developerinterview.model.ProblemListModel;
import com.github.keyboard3.developerinterview.pattern.BaseProblemState;
import com.github.keyboard3.developerinterview.util.ListUtil;
import com.github.keyboard3.developerinterview.util.SharePreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * 内容页面 展示不同类型的问题列表
 * A ProblemsFragment {@link Fragment} subclass.
 *
 * @author
 */
public class ProblemsFragment extends BaseFragment {
    private static String TAG = "ProblemsFragment";

    @BindView(R.id.rl_content) RecyclerView problemsView;
    @BindView(R.id.iv_nodata) View noDataView;
    @BindView(R.id.tv_input) View inputView;

    private List<Problem> problems = new ArrayList<>();
    private ProblemAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private BaseProblemState problemState;
    private SharePreferencesHelper spHelper;
    private ProblemsDrive problemsDrive;
    private String problemString;

    private List<Problem> problemList;
    private List<Problem> searchProblemList;

    public static ProblemsFragment newInstance(BaseProblemState state) {

        Bundle args = new Bundle();
        args.putSerializable(ConfigConst.INTENT_KEY, state);
        ProblemsFragment fragment = new ProblemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_problems, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initProblemDataFromIntent();
    }

    void getProblemFromServer(){
        int subjectPostion = getActivity().getIntent().getIntExtra("subjectPosition",0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"position = "+subjectPostion);
                //String problemId=problem.id+" "+(problem.content.length());
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().add("TtypeId",""+subjectPostion).build();
                Request request = new Request.Builder().url("http://108.61.223.228:8888/getquestion").post(requestBody).build();
                try{
                    Response response  =client.newCall(request).execute();
                    problemString = response.body().toString();
                    Log.d(TAG,"problemString = "+problemString);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.d(TAG,"finish adding comment to server.\n");
            }
        }).start();

    }

    private void readTextFromSDcard() {
        InputStreamReader inputStreamReader;
        try {
            //FileInputStream fileInputStream=new FileInputStream(new File("..//..//..//..//..//..//assets//Problem0.json"));
            int subjectPostion = getActivity().getIntent().getIntExtra("subjectPosition",0);
            String problemJsonName = "Problem"+subjectPostion+".json";
            inputStreamReader = new InputStreamReader(getContext().getAssets().open(problemJsonName), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            problemString = stringBuilder.toString();
            Log.d(TAG, stringBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJSONWithGSON(String problemString){
        Gson gson = new Gson();
        problemList = gson.fromJson(problemString,new TypeToken<List<Problem>>(){}.getType());
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeLastPositonOnProblemsView();
        initProblemsFromNet();
        initProblemsViewWithDaa();
    }

    private void initProblemsViewWithDaa() {
        problemsView.setAdapter(adapter);
        adapter.setOnItemClickListener((itemView, position) -> {
            Problem entity = problems.get(position);
            Intent intent = new Intent(getActivity(), ProblemDetailActivity.class);
            intent.putExtra(ConfigConst.INTENT_ENTITY, entity);
            startActivity(intent);
        });
        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(problemsView);
    }

    @Override
    public void onPause() {
        super.onPause();
        ProblemListModel.saveListPosition(problemsView, linearLayoutManager, spHelper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initProblemDataFromIntent() {
        problemState = (BaseProblemState) getArguments().getSerializable(ConfigConst.INTENT_KEY);
        problemsDrive = new ProblemsDrive(getActivity().getApplicationContext(), problemState);
    }

    private  void getAllProblems(){

    }
    void initProblemsFromNet() {
        //getProblemFromServer();
        readTextFromSDcard();
        parseJSONWithGSON(problemString);

        String searchKeyWord = getActivity().getIntent().getStringExtra("searchKeyWord");
        //Log.d(TAG,"searchKeyWord = "+searchKeyWord);
        if(searchKeyWord!=null&&!searchKeyWord.equals("")){
            //getAllProblems();
            searchProblemList = new ArrayList<>();
            for(int i=0;i<problemList.size();i++){
                //Log.d(TAG,problemList.get(i).title);
                if(problemList.get(i).title.contains(searchKeyWord)||problemList.get(i).answer.contains(searchKeyWord)){
                    //Log.d(TAG,"Contains "+searchKeyWord+" "+problemList.get(i).title);
                    searchProblemList.add(problemList.get(i));
                }
            }
            problemList=searchProblemList;
        }
        toggleDialogAdvance(true);
        showDialog();
        problemsDrive.asyncFetchProblems(new Callback<List<Problem>>() {
            @Override
            public void success(List<Problem> item) {
                try {
                    problems.addAll(problemList);
                    Log.d(TAG,problemList.toString());
                    adapter.notifyDataSetChanged();
                    showEmptyView(ListUtil.isEmpty(problems));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hideDialog();
                }
            }

            @Override
            public void fail(Throwable error) {
                hideDialog();
            }
        });
    }

    private void showEmptyView(boolean ishide) {
        if (!ishide) {
            problemsView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
            inputView.setVisibility(View.GONE);
        } else {
            problemsView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);
            inputView.setVisibility(View.VISIBLE);
        }
    }

    void resumeLastPositonOnProblemsView() {
        spHelper = problemState.createSpHelper(getActivity());
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        problemsView.setLayoutManager(linearLayoutManager);
        ProblemListModel.restoreListPosition(linearLayoutManager, spHelper);
        adapter = new ProblemAdapter(problems, getActivity());
    }

    @OnClick(R.id.tv_input) void inputViewClick(){
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                viewHolder) {
            int flag = ItemTouchHelper.LEFT;
            return makeMovementFlags(0, flag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder
                viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.com_tint)
                    .setMessage(R.string.home_delete_problem)
                    .setPositiveButton(R.string.com_ok, (dialogInterface, i) -> {
                        ProblemListModel.removeProblem(getActivity(), adapter, problems, viewHolder.getAdapterPosition(), problemsDrive.problemJsonPath);
                        dialogInterface.dismiss();
                    }).setNegativeButton(R.string.com_cancel, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                adapter.notifyDataSetChanged();
            }).create().show();
        }
    };

    public void goTop() {
        if(problemsView == null) return;
        problemsView.scrollToPosition(0);
    }
}