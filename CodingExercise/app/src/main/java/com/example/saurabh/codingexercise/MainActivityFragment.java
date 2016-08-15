package com.example.saurabh.codingexercise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements TaskListener {

    private static final String JSON_URL = "https://gist.githubusercontent.com/maclir/f715d78b49c3b4b3b77f/raw/8854ab2fe4cbe2a5919cea97d71b714ae5a4838d/items.json";

    RecyclerView entryRecyclerView = null;
    private ProgressDialog progressDialog;
    private boolean isTaskRunning = false;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isTaskRunning) {
            showProgressDialog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!isTaskRunning) {
            GetJsonDataTask dataTask = new GetJsonDataTask(this);
            dataTask.execute(JSON_URL);
        }
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        entryRecyclerView = (RecyclerView) rootView.findViewById(R.id.entry_listView);
        return rootView;
    }

    @Override
    public void onTaskStarted() {
        isTaskRunning = true;
        showProgressDialog();
    }

    private void showProgressDialog() {
        if (!(progressDialog != null && progressDialog.isShowing())) {
            progressDialog = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.fetching_data), getActivity().getResources().getString(R.string.please_wait));
        }
    }

    @Override
    public void onTaskFinished() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        // All dialogs should be closed before leaving the activity
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDetach();
    }

    class GetJsonDataTask extends AsyncTask<String, Void, String> {

        TaskListener mTaskListener;

        GetJsonDataTask(TaskListener mTaskListener) {
            this.mTaskListener = mTaskListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaskListener.onTaskStarted();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String jsonData = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                jsonData = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                jsonData = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            mTaskListener.onTaskFinished();
            if (jsonData == null) {
                Toast.makeText(getActivity(), R.string.check_internet_connection, Toast.LENGTH_LONG).show();
            } else {
                parseJsonData(jsonData);
            }
        }
    }

    private void parseJsonData(String jsonData) {
        try {
            JSONArray json = new JSONArray(jsonData);
            List<Entry> entryList = new ArrayList<>();
            for (int i = 0; i < json.length(); i++) {
                entryList.add(new Entry(json.getJSONObject(i)));
            }
            populateListView(entryList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateListView(final List<Entry> entryList) {
        EntryListAdapter listAdapter = new EntryListAdapter(entryList, new EntryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entry entry) {
                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                intent.putExtra(Constants.SELECTED_ENTRY, entry);
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        entryRecyclerView.setLayoutManager(mLayoutManager);
        entryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        entryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        entryRecyclerView.setAdapter(listAdapter);
    }
}

interface TaskListener {
    void onTaskStarted();
    void onTaskFinished();
}

