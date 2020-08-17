package com.example.baksolapangantembaksenayan.utility;

import android.widget.Filter;

import com.example.baksolapangantembaksenayan.adapter.AdapterMenuAdmin;
import com.example.baksolapangantembaksenayan.adapter.Adapter_Transaction_Admin;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;
import com.example.baksolapangantembaksenayan.model.model_transaction;

import java.util.ArrayList;

public class FilterOrderAdmin extends Filter {

    private Adapter_Transaction_Admin adapter;
    private ArrayList<model_transaction> filterList;

    public FilterOrderAdmin(Adapter_Transaction_Admin adapter, ArrayList<model_transaction> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<model_transaction> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get( i ).getStatusOrder().toUpperCase().contains( constraint ) ) {
                    filteredModels.add( filterList.get( i ) );
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<model_transaction>) results.values;
        adapter.notifyDataSetChanged();
    }
}
