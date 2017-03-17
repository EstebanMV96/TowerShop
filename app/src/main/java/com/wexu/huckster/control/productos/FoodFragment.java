package com.wexu.huckster.control.productos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wexu.huckster.R;
import com.wexu.huckster.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OthersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OthersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recycler;
    private FoodAdapter productsAdapter;
    private RecyclerView.LayoutManager lManager;
    private ArrayList<Producto> catalogo;
    private FoodFragment.OnFragmentInteractionListener mListener;
    private TextView emptyView;

    public FoodFragment() {
        // Required empty public constructor
    }

    public interface UpdateableInterface{
        public void update(Producto p);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StationeryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
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
            catalogo=(ArrayList<Producto>) getArguments().getSerializable("productos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_food, container, false);

        // Inicializar Productos
        List items =catalogo;

        // Obtener el FoodRecycler
        recycler = (RecyclerView) root.findViewById(R.id.reciclador1);
        emptyView = (TextView) root.findViewById(R.id.empty_view);
        recycler.setHasFixedSize(true);




        if (items.isEmpty()) {
            recycler.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recycler.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());

        // Crear un nuevo adaptador
        productsAdapter = new FoodAdapter(items);

        recycler.setAdapter(productsAdapter);
        recycler.setLayoutManager(lManager);
        // Inflate the layout for this fragment
        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FoodFragment.OnFragmentInteractionListener) {
            mListener = (FoodFragment.OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(Uri uri);
    }

    public void refresh()
    {

        if(productsAdapter!=null)
        productsAdapter.notifyDataSetChanged();
        //recycler.setAdapter(new FoodAdapter(catalogo));
        //recycler.invalidate();

    }




}
