package com.iot.iotsmartbuilding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "WriteFragment";

    public int aValue;

    Button radiatorButton;
    Button lightButtonWR;
    Button storeButton;
    Button plusButton;
    Button minButton;
    TextView roomText;
    TextView valueText;
    Spinner spinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
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
        View myView = inflater.inflate(R.layout.fragment_write, container, false);
        //-----------------------------------------------------------------------------------
        //SPINNER
        //-----------------------------------------------------------------------------------
        //Récupération du Spinner déclaré dans le fichier main.xml de res/layout
        spinner = myView.findViewById(R.id.spinner);
        //Le Spinner a besoin d'un adapter pour sa presentation alors on lui passe le context(this) et
        // un fichier de presentation par défaut( android.R.layout.simple_spinner_item)
        //Avec la liste des elements (exemple)
        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                MainActivity.exempleList
        );

        // On definit une présentation du spinner quand il est déroulé (android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Enfin on passe l'adapter au Spinner et c'est tout
        spinner.setAdapter(adapter);
        Log.i(TAG, "onCreateView:  set postion  "+MainActivity.spinnerPosition);
        //spinner.setSelection(MainActivity.spinnerPosition,false);
        spinner.post(new Runnable() {
            public void run() {
                spinner.setSelection(MainActivity.spinnerPosition);
            }
        });

        //---------------------------------------------------------------------------------------------------------------
        //Récupération des objets se trouvant dans le layout
        radiatorButton = (Button) myView.findViewById(R.id.radiatorButton);
        lightButtonWR = (Button) myView.findViewById(R.id.lightButtonWR);
        storeButton = (Button) myView.findViewById(R.id.storeButton);
        plusButton = (Button) myView.findViewById(R.id.plusButton);
        minButton = (Button) myView.findViewById(R.id.minButton);
        //roomText = (TextView)myView.findViewById(R.id.roomText);
        valueText = (TextView) myView.findViewById(R.id.valueText);

        DecimalFormat df = new DecimalFormat("##");
        valueText.setText(df.format(aValue) + "%");

        spinner.setOnTouchListener(new Spinner.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.selection=true;
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("WriteFragment", "WriteFragment:  selection vlaue: " + MainActivity.selection);
                if(MainActivity.selection)
                {
                    MainActivity.spinnerPosition = i;
                    Log.i(TAG, "onCreateView:  j'y suis "+i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        //Contrôler la pression sur le bouton radiator
        radiatorButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Send the value to the radiator", Toast.LENGTH_SHORT).show();
                JSONObject jsonvalue = new JSONObject();
                try {
                    jsonvalue.put("value", aValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                processPOSTRequest(v,"5500","radiator/" + MainActivity.floorID + MainActivity.radiatorID,jsonvalue);
            }
        });

        //Contrôler la pression sur le bouton light
        lightButtonWR.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Send the value to the light", Toast.LENGTH_SHORT).show();
                JSONObject jsonvalue = new JSONObject();
                try {

                    jsonvalue.put("node_id", MainActivity.dimmerID);
                    jsonvalue.put("value", aValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                processPOSTRequest(v,"5000","dimmers/set_level",jsonvalue);
            }

        });

        //Contrôler la pression sur le bouton store
        storeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Send the value to the store", Toast.LENGTH_SHORT).show();
                JSONObject jsonvalue = new JSONObject();
                try {
                    jsonvalue.put("value", aValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                processPOSTRequest(v,"5500","store/" + MainActivity.floorID + MainActivity.storeID,jsonvalue);
            }
        });

        //Contrôler la pression sur le bouton "+"
        plusButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add 5% to the value", Toast.LENGTH_SHORT).show();
                if(aValue<100)
                {
                    aValue+=5;
                }

                DecimalFormat df = new DecimalFormat("##");
                valueText.setText(df.format(aValue) + "%");
            }
        });

        //Contrôler la pression sur le bouton "-"
        minButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sub 5% to the value", Toast.LENGTH_SHORT).show();
                if(aValue>0)
                {
                    aValue-=5;
                }

                DecimalFormat df = new DecimalFormat("##");
                valueText.setText(df.format(aValue) + "%");

            }
        });


        return myView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(7, MainActivity.WRITE_FRAGMENT);
        }
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

    private void processPOSTRequest(View v, String port ,String ressource,JSONObject obj) {
        Utils.processRequest(v.getContext(), port, ressource, Request.Method.POST,  obj,
                new Utils.VolleyCallback() {

                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            Log.i(TAG, "onSuccessResponse -> result: "  +result);
                            String response = result.getString("value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
