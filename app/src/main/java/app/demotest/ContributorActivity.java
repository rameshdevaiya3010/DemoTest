package app.demotest;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.demotest.Adapters.ContributorsAdapter;
import app.demotest.ModelClass.Contributors;
import app.demotest.Utils.GlobalClass;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContributorActivity extends AppCompatActivity {
    String tag_json_array = "json_array_req";

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.btnFilter)
    Button btnFilter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView.LayoutManager mLayoutManager;
    ContributorsAdapter mAdapter;
    List<Contributors> list_contributors = new ArrayList<>();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setData();
        clickEvents();

    }

    public void setData() {
        if (GlobalClass.isConnected(ContributorActivity.this)) {
            getContributorsData();
        } else {
            GlobalClass.showSnackbar(findViewById(android.R.id.content), getResources().getString(R.string.nointernet), ContributorActivity.this);
        }
    }

    public void clickEvents() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.sort(list_contributors, new Comparator<Contributors>() {
                    @Override
                    public int compare(Contributors con1, Contributors con2) {
                        return con1.contributions - con2.contributions;
                    }
                });

                mAdapter.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalClass.isConnected(ContributorActivity.this)) {
                    getContributorsData();
                } else {
                    GlobalClass.showSnackbar(findViewById(android.R.id.content), getResources().getString(R.string.nointernet), ContributorActivity.this);

                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getContributorsData() {


        progressDialog = new ProgressDialog(ContributorActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String url = "https://api.github.com/repos/square/retrofit/contributors";

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, url
                , null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("response", "response " + response.toString());

                        try {
                            list_contributors = new ArrayList<>();
                            list_contributors.clear();


                            JSONArray array = new JSONArray(response.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                Contributors contributors = new Contributors();
                                contributors.login = object.getString("login");
                                contributors.avatar_url = object.getString("avatar_url");
                                contributors.repos_url = object.getString("repos_url");
                                contributors.contributions = object.getInt("contributions");
                                list_contributors.add(contributors);
                            }
                            SetContributorData(list_contributors);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error: " + error.getMessage());
                progressDialog.dismiss();


            }
        });

        GlobalClass.getInstance().addToRequestQueue(jsonObjReq, tag_json_array);
    }


    public void SetContributorData(List<Contributors> list_contributor) {

        mLayoutManager = new LinearLayoutManager(ContributorActivity.this);
        recycler_view.setLayoutManager(mLayoutManager);
        mAdapter = new ContributorsAdapter(ContributorActivity.this, list_contributor);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);
    }
}
