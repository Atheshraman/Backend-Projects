package com.example.ecom.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.model.Ecom;

public interface service {
    
    Ecom getprodbyID(Long ecomId);
    List<Ecom> getproducts();
    Ecom Addproduct(Ecom e,MultipartFile imageFile) throws IOException;
    Ecom updateproducts(Long ecomId,Ecom e,MultipartFile imagFile) throws IOException;
    void deleteproducts(Long ecomId);
    Page<Ecom> SearchProducts(String keyword, Pageable pageable);
    Page<Ecom> getproducts(int page,int size);
}
