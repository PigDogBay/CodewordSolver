package com.pigdogbay.codewordsolver.controllers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.lib.utils.ActivityUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HelpFragment extends Fragment {

    public static final String TAG = "help";
    private OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        view.findViewById(R.id.help_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback(getActivity());
            }
        });
        view.findViewById(R.id.help_rate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebPage(getActivity(),R.string.market_app_url);
            }
        });
        view.findViewById(R.id.help_reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onReset();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void sendFeedback(Activity activity)
    {
        ActivityUtils.SendEmail(
                activity,
                new String[]{activity.getString(R.string.email)},
                activity.getString(R.string.feedback_subject),
                activity.getString(R.string.feedback_body));
    }

    public static void showWebPage(Activity activity, int urlId)
    {
        try
        {
            ActivityUtils.ShowWebPage(activity, activity.getString(urlId));
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(activity, activity.getString(R.string.web_error), Toast.LENGTH_LONG)
                    .show();
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onReset();
    }
}
