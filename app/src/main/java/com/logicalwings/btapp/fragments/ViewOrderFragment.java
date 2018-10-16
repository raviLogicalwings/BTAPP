package com.logicalwings.btapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.ViewOrderAdapter;
import com.logicalwings.btapp.utils.AppUtils;

public class ViewOrderFragment extends Fragment {

    RecyclerView viewOrderRecycler;
    ViewOrderAdapter viewOrderAdapter;
    ImageView toolSearch, toolCart, toolSave;

    public ViewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        viewOrderRecycler = view.findViewById(R.id.view_order_recycler);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        TextView textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        textToolbar.setText("Orders");
        loadState();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        viewOrderAdapter = new ViewOrderAdapter(getContext());
        viewOrderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        viewOrderRecycler.setAdapter(viewOrderAdapter);
    }

    private void loadState() {
        toolSearch.setVisibility(View.VISIBLE);
        toolCart.setVisibility(View.VISIBLE);
        toolSave.setVisibility(View.GONE);
        AppUtils.hasInternet(getActivity());
    }
}
