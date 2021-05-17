package com.example.gatromanagerclient;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.gatromanagerclient.socket.Client;

import java.util.Arrays;

public class ChildFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView txtOne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_child, container, false);
        txtOne = (TextView) returnView.findViewById(R.id.orderDetailsText);
        System.out.println("second fragment call ");
        //new MyTask().execute("1"); //TODO enable after solving server issue on the socket connection when client closes

        return returnView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

    private class MyTask extends AsyncTask<String, Void, Void> {
        String response;
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... request) {
            Client client = new Client();//.getInstance();
            response = client.getResponse(Arrays.stream(request).findFirst().get());
            System.out.println("Requesting for "+Arrays.stream(request).findFirst().get());
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            txtOne.setText(response);
            super.onPostExecute(result);
        }
    }
}
