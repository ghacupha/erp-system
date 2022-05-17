package io.github.erp.erp.reports;

import java.util.Objects;

public class DataBean {
    private long id;
    private String dealerName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBean dataBean = (DataBean) o;
        return id == dataBean.id && Objects.equals(dealerName, dataBean.dealerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dealerName);
    }
}
