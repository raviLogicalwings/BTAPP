package com.logicalwings.btapp.callbacks;

import com.logicalwings.btapp.model.ProductItem;

public interface FavouriteUnfavouriteOpration {

    void makeFavourite(ProductItem productItem);
    void makeUnfavourite(ProductItem productItem);
}
