package com.example.myappas3.ui.main;

import static com.example.myappas3.R.id.btFrontCenterDown;
import static com.example.myappas3.R.id.btFrontCenterUp;
import static com.example.myappas3.R.id.btFrontLeftDown;
import static com.example.myappas3.R.id.btFrontLeftUp;
import static com.example.myappas3.R.id.btFrontRightDown;
import static com.example.myappas3.R.id.btFrontRightUp;
import static com.example.myappas3.R.id.btRearCenterDown;
import static com.example.myappas3.R.id.btRearCenterUp;
import static com.example.myappas3.R.id.btRearLeftDown;
import static com.example.myappas3.R.id.btRearLeftUp;
import static com.example.myappas3.R.id.btRearRightDown;
import static com.example.myappas3.R.id.btRearRightUp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myappas3.R;
import com.example.myappas3.SerialService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualModeFragment extends Fragment implements View.OnTouchListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManualModeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManualModeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualModeFragment newInstance(String param1, String param2) {
        ManualModeFragment fragment = new ManualModeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual_mode, container, false);

        ImageButton btFLU = view.findViewById(btFrontLeftUp);
        ImageButton btFLD = view.findViewById(btFrontLeftDown);
        btFLU.setOnTouchListener(this);
        btFLD.setOnTouchListener(this);

        ImageButton btFCU = view.findViewById(btFrontCenterUp);
        ImageButton btFCD = view.findViewById(btFrontCenterDown);
        btFCU.setOnTouchListener(this);
        btFCD.setOnTouchListener(this);

        ImageButton btFRU = view.findViewById(btFrontRightUp);
        ImageButton btFRD = view.findViewById(btFrontRightDown);
        btFRU.setOnTouchListener(this);
        btFRD.setOnTouchListener(this);

        ////////

        ImageButton btRLU = view.findViewById(btRearLeftUp);
        ImageButton btRLD = view.findViewById(btRearLeftDown);
        btRLU.setOnTouchListener(this);
        btRLD.setOnTouchListener(this);

        ImageButton btRCU = view.findViewById(btRearCenterUp);
        ImageButton btRCD = view.findViewById(btRearCenterDown);
        btRCU.setOnTouchListener(this);
        btRCD.setOnTouchListener(this);

        ImageButton btRRU = view.findViewById(btRearRightUp);
        ImageButton btRRD = view.findViewById(btRearRightDown);
        btRRU.setOnTouchListener(this);
        btRRD.setOnTouchListener(this);


        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String str="";
            switch (v.getId()) {
                case btFrontLeftUp:
                    str = "1:1000";
                    break;
                case btFrontCenterUp:
                    str = "1:1100";
                    break;
                case btFrontRightUp:
                    str = "1:0100";
                    break;
                case btRearLeftUp:
                    str = "1:0010";
                    break;
                case btRearCenterUp:
                    str = "1:0011";
                    break;
                case btRearRightUp:
                    str = "1:0001";
                    break;
                case btFrontLeftDown:
                    str = "0:1000";
                    break;
                case btFrontCenterDown:
                    str = "0:1100";
                    break;
                case btFrontRightDown:
                    str = "0:0100";
                    break;
                case btRearLeftDown:
                    str = "0:0010";
                    break;
                case btRearCenterDown:
                    str = "0:0011";
                    break;
                case btRearRightDown:
                    str = "0:0001";
                    break;
            }
            if(str!="")Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        return false;
    }
}