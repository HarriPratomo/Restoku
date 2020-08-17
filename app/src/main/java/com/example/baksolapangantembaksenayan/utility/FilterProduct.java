package com.example.baksolapangantembaksenayan.utility;

import android.widget.Filter;


import com.example.baksolapangantembaksenayan.adapter.AdapterMenuAdmin;
import com.example.baksolapangantembaksenayan.model.model_menu_admin;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private AdapterMenuAdmin adapterMenu;
    private ArrayList<model_menu_admin> filterList;

    public FilterProduct(AdapterMenuAdmin adapterMenu, ArrayList<model_menu_admin> filterList) {
        this.adapterMenu = adapterMenu;
        this.filterList = filterList;
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<model_menu_admin> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get( i ).getNamaMenu().toUpperCase().contains( constraint ) ||
                        filterList.get( i ).getKategoriMenu().toUpperCase().contains( constraint )) {
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
        adapterMenu.listMenu = (ArrayList<model_menu_admin>) results.values;
        adapterMenu.notifyDataSetChanged();
    }
}
