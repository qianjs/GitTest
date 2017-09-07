package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class DemandPricesEntity {
    private String name;
    private String docId;
    private String mName;
    private String offerPrice;

    public String getName() {
        return Utils.getMyString(name, "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocId() {
        return Utils.getMyString(docId, "");
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getmName() {
        return Utils.getMyString(mName, "");
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getOfferPrice() {
        return Utils.getMyString(offerPrice, "");
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }
}
