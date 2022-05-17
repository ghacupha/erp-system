package io.github.erp.erp.reports;

import java.util.ArrayList;

public class DataBeanList {

    public ArrayList<DataBean> getDataBeanList() {
        ArrayList<DataBean> dataBeanList = new ArrayList<DataBean>();

        dataBeanList.add(produce(2, "India"));
        dataBeanList.add(produce(3, "USA"));
        dataBeanList.add(produce(4, "India"));
        dataBeanList.add(produce(5, "California"));

        return dataBeanList;
    }

    private DataBean produce(long id, String name) {
        DataBean dataBean = new DataBean();
        dataBean.setId(id);
        dataBean.setDealerName(name);

        return dataBean;
    }
}
