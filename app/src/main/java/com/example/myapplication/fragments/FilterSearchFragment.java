package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterSearchFragment extends Fragment {

    TextInputEditText ettext1;
    TextInputEditText ettext2;
    TextInputEditText ettext3;
    ImageView ivBack;
    MaterialButton btnfilter;
    List<JobDTO>mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filter_search, container, false);
        ettext1 = root.findViewById(R.id.et_searchbyName);
        ettext2 = root.findViewById(R.id.et_jobstarRating);
        ettext3 = root.findViewById(R.id.et_searchbyAutisumLvl);
        btnfilter = root.findViewById(R.id.btn_filter);
        ivBack = root.findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListJobFragment fragment = new ListJobFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fl_container, fragment);
                trans.commit();

            }
        });

        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchbyName = ettext1.getText().toString();
                float searchByRating = Float.parseFloat(ettext2.getText().toString());
                int searchByAutismLvl = Integer.parseInt(ettext3.getText().toString()) ;
                if(!searchbyName.isEmpty() && searchByRating != 0 && searchByAutismLvl != 00){
                    bindDataAndResult(searchbyName,searchByRating,searchByAutismLvl,true);
                }
                else {
                    Toast.makeText(getActivity(), "Please enter a valid search keyword", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return root;
    }




    private void bindDataAndResult(String name, float rating, int lvl,boolean flag){
        ListJobFragment fragment = new ListJobFragment();
        fragment.sendData(name,rating,lvl,flag);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fl_container, fragment);
        trans.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}