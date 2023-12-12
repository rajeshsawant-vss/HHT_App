package com.tatamotors.errorproofing.interfaces;

import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;

public interface PartData {
    void getPartData(ChildPartsAndFamily partModel);
}
