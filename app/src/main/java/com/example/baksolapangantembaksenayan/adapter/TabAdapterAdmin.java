package com.example.baksolapangantembaksenayan.adapter;



import com.example.baksolapangantembaksenayan.fragment.FragmentAdmin.MenuFragment;
import com.example.baksolapangantembaksenayan.fragment.FragmentAdmin.OrderFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Harri Pratomo
 * harrypratomo135@gmail.com
 */
public class TabAdapterAdmin extends FragmentStatePagerAdapter {
    int numbOfTabs;


    public TabAdapterAdmin(@NonNull FragmentManager fm, int numbOfTabs) {
        super(fm);
        this.numbOfTabs = numbOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MenuFragment newMenu =  new MenuFragment();
                return newMenu;
            case 1:
                OrderFragment order = new OrderFragment();
                return order;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}

