package com.example.com.contactslistproject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragement extends Fragment {


    //create inner class for recycler adapter

    Button button;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    MyAdapter myadapter;
    MyTask myTask;
    ArrayList<Contacts> al;




    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.row,viewGroup,false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Contacts c = al.get(i);
            viewHolder.name.setText(c.getName());
            viewHolder.phone.setText(c.getMobile());


        }

        @Override
        public int getItemCount() {
            return al.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name,phone;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                phone = itemView.findViewById(R.id.phone);



            }
        }
    }




    //create inner class for async task

    public class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("B42","1");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("B42", "2");

            try {
                URL url = null;//prepare url
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();//open input stream for reading
                InputStreamReader ir = new InputStreamReader(is);//start reading
                BufferedReader br = new BufferedReader(ir); //buffer it

                /*################*/

                String str = br.readLine();
                StringBuilder sb = new StringBuilder();
                while (str!=null){
                    sb.append(str);
                    str = br.readLine();
                }
                return sb.toString();

                /*888*/
            } catch (MalformedURLException m){
                m.printStackTrace();
                Log.d("B42", "Malfunction url connection"+m);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("B42","io exception"+e);
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d("B42", "3");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("B42","4");
            Log.d("B42", "SERVER RESPONSE= "+s);

            //****JSON PARSING --READ NAME,AND MOBILE NUMBER---DISPLAY TOAST

            try {
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("contacts");
                for (int i=0; i<arr.length();i++){
                    JSONObject temp = arr.getJSONObject(i);
                    String name = temp.getString("name");
                    JSONObject phone = temp.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    Toast.makeText(getActivity(), "details="+name+"-"+mobile, Toast.LENGTH_SHORT).show();
                    //INSERT name,mobile into array list of objects
                    Contacts c = new Contacts(name,mobile);
                    al.add(c);
                    //notify the adapter
                    myadapter.notifyDataSetChanged();

                    //UR TASK --ROW.XML - With 2 Text Views



                    //UR TASK -FINISH ADAPTER CODE




                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }




    public MyFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_fragement, container, false);

        button = v.findViewById(R.id.button);
        recyclerView = v.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        myadapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myadapter);
        al = new ArrayList<Contacts>();//IMPORTANT

        //button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTask = new MyTask();
                //start the async task and pass live web services url
                myTask.execute("https://api.androidhive.info/contacts/");

            }
        });
        return v;
    }

}
