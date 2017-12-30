package com.ferainc.kudla.interfaces;

import com.ferainc.kudla.itemclasses.ProductItem;

public interface SecondPageFragmentListener {
    void onSwitchToNextFragment(String category, String subCategory, ProductItem listItem, boolean backPressed);
}