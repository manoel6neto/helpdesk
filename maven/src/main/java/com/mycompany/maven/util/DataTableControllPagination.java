package com.mycompany.maven.util;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 *
 * @author Manoel
 */
@ManagedBean
@SessionScoped
public class DataTableControllPagination implements Serializable {
    protected int firstProduct;
    
    
    public int getFirstProduct() {
        return firstProduct;
    }
    
    public void setFirstProduct(int firstProduct) {
        this.firstProduct = firstProduct;
    }
    
    
    public void onPageChangeProduct(PageEvent event) {
        this.setFirstProduct(((DataTable) event.getSource()).getFirst());
    }
}
