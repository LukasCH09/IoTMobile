package com.iot.iotsmartbuilding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadFragment extends Fragment {

    private static final String TAG = "ReadFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView roomNumber;
    TextView resultText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadFragment newInstance(String param1, String param2) {
        ReadFragment fragment = new ReadFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_read, container, false);

        //Récupération des objets se trouvant dans le layout
        Button temperatureButton = myView.findViewById(R.id.temperatureButton);
        Button humidityButton = myView.findViewById(R.id.humidityButton);
        Button presenceButton = myView.findViewById(R.id.presenceButton);
        Button lightButton = myView.findViewById(R.id.lightButton);
        roomNumber = myView.findViewById(R.id.roomNumber);
        resultText = myView.findViewById(R.id.result);


        //Contrôler la pression sur le bouton temperature
        temperatureButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "temperature");
                Toast.makeText(getContext(), "Print the resultText of the temperature", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton humidity
        humidityButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "humidity");
                Toast.makeText(getContext(), "Print the resultText of the humidity", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton presence
        presenceButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "presence");
                Toast.makeText(getContext(), "Print the resultText of the presence", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton light
        lightButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "light");
                Toast.makeText(getContext(), "Print the resultText of the light", Toast.LENGTH_SHORT).show();
            }
        });
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    private void processGETRequest(View v, String ressource) {
        Utils.processRequest(v.getContext(), ressource, Request.Method.GET,  null,
                new Utils.VolleyCallback() {

                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            String response = result.getString("args");
                            resultText.setText(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
        // TODO: Update argument type and name
        void onFragmentInteraction(int position, int fragmentCaller  );
    }
}
