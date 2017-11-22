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
    TextView resultText;
    int measure;
    Spinner spinner;


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
        Log.i("WriteFragment", "onCreateView:  set postion read "+MainActivity.spinnerPosition);
        //spinner.setSelection(MainActivity.spinnerPosition,false);
        spinner.post(new Runnable() {
            public void run() {
                spinner.setSelection(MainActivity.spinnerPosition);
            }
        });
        //---------------------------------------------------------------------------------------------------------------

        //Récupération des objets se trouvant dans le layout
        Button temperatureButton = myView.findViewById(R.id.temperatureButton);
        Button humidityButton = myView.findViewById(R.id.humidityButton);
        Button presenceButton = myView.findViewById(R.id.presenceButton);
        Button lightButton = myView.findViewById(R.id.lightButton);
        resultText = myView.findViewById(R.id.result);

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
                Log.i("WriteFragment", "ReadFragment:  selection vlaue: " + MainActivity.selection);
                if(MainActivity.selection)
                {
                    MainActivity.spinnerPosition = i;

                    if(MainActivity.exempleList.get(i)=="Room 1")
                    {
                        JSONObject jsonvalue = new JSONObject();
                        try {
                            jsonvalue.put("MajorID", "32753");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        processAmazon(view, jsonvalue);
                        Toast.makeText(getContext(), "Ask Value for Room1", Toast.LENGTH_SHORT).show();

                       /* MainActivity.storeID="1"; //ou 2
                        MainActivity.radiatorID="1"; // ou 2
                        MainActivity.dimmerID="4"; // ou 5
                        MainActivity.sensorID="3"; // ou 6
                        MainActivity.floorID="4";*/

                    }
                    else if(MainActivity.exempleList.get(i)=="Room 2")
                    {
                        JSONObject jsonvalue = new JSONObject();
                        try {
                            jsonvalue.put("MajorID", "57473");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        processAmazon(view, jsonvalue);

                        Toast.makeText(getContext(), "Ask Value for Room2", Toast.LENGTH_SHORT).show();

                       /* MainActivity.storeID="2"; //ou 2
                        MainActivity.radiatorID="2"; // ou 2
                        MainActivity.dimmerID="5"; // ou 5
                        MainActivity.sensorID="6"; // ou 6
                        MainActivity.floorID="4";*/
                    }

                    Log.i("WriteFragment", "onCreateView:  j'y suis read "+i);
                }


            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        //Contrôler la pression sur le bouton temperature
        temperatureButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "sensors/"+ MainActivity.sensorID +"/get_temperature");
                measure=0;
                Toast.makeText(getContext(), "Print the resultText of the temperature", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton humidity
        humidityButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "sensors/"+ MainActivity.sensorID +"/get_humidity");
                measure=1;
                Toast.makeText(getContext(), "Print the resultText of the humidity", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton presence
        presenceButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "sensors/"+ MainActivity.sensorID +"/get_motion");
                measure=2;
                Toast.makeText(getContext(), "Print the resultText of the presence", Toast.LENGTH_SHORT).show();
            }
        });

        //Contrôler la pression sur le bouton light
        lightButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                processGETRequest(v, "sensors/"+ MainActivity.sensorID +"/get_luminance");
                measure=3;
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
        String server = "http://192.168.2.1:"; //5000
        Utils.processRequest(v.getContext(),server, "5000", ressource, Request.Method.GET,  null,
                new Utils.VolleyCallback() {

                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            Log.i(TAG, "onSuccessResponse -> result: "  +result);
                            String response = result.getString("value");
                            if(measure==0)
                            {
                                resultText.setText(response+"°C");
                            }
                            else if (measure==1)
                            {
                                resultText.setText(response+" [humidity]");
                            }
                            else if(measure==2)
                            {
                                resultText.setText(response);
                            }
                            else
                            {
                                resultText.setText(response+" [luminance]");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void processAmazon(View v, JSONObject obj) {
        String server = "https://0mcll40zmf.execute-api.us-west-2.amazonaws.com/prod/beaconRanging"; //5000
        Utils.processRequest(v.getContext(),server, "", "", Request.Method.POST,  obj,
                new Utils.VolleyCallback() {

                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            Log.i(TAG, "onSuccessResponse -> result: "  +result);
                            MainActivity.storeID = result.getString("storeID");
                            MainActivity.radiatorID = result.getString("radiatorID");
                            MainActivity.dimmerID = result.getString("dimmerID");
                            MainActivity.sensorID = result.getString("sensorID");
                            MainActivity.floorID = result.getString("floorID");

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
        void onFragmentInteraction(int position, int fragmentCaller  );
    }
}
