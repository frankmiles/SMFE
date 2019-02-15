package com.freeoda.franktirkey.smfe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * this is fragment 1 and will carry the details of the searching syllabus
 */
public class Select_Sem_SubjectFrag extends Fragment {


    public Select_Sem_SubjectFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select__sem__subjectfrag, container, false);
    }

}
