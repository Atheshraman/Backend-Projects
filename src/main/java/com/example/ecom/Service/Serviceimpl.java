
package com.example.ecom.Service;
import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.ResourceNotfoundException;
import com.example.ecom.Repo.Repository;
import com.example.ecom.model.Ecom;

@Service
public class Serviceimpl implements service{
    
    @Autowired
    private Repository repository;

    public Page<Ecom> getproducts(int page,int size){
        Pageable pageable= PageRequest.of(page,size);
        return repository.findAll(pageable);
    }

    public Ecom Addproduct(Ecom e,MultipartFile imageFile) throws IOException {
        e.setImagename(imageFile.getOriginalFilename());
        e.setImagetype(imageFile.getContentType());
        e.setImageData(imageFile.getBytes());
        return repository.save(e);
    }

    @Override
    public List<Ecom> getproducts() {
        return repository.findAll();
    }

    @Override
    public Ecom getprodbyID(Long ecomId) {
       return repository.findById(ecomId)
                        .orElseThrow(()->new ResourceNotfoundException("Enter a Valid Id"));
    }

    @Override
    public Ecom updateproducts(Long ecomId,Ecom e,MultipartFile imagFile) throws IOException{
        Ecom existing=repository.findById(ecomId)
                                .orElseThrow(()->new ResourceNotfoundException("Enter a Valid Id"));
        if(imagFile!=null && !imagFile.isEmpty()) {
            existing.setImageData(imagFile.getBytes());
            existing.setImagename(imagFile.getOriginalFilename());
            existing.setImagetype(imagFile.getContentType());
        }
        existing.setName(e.getName());
        existing.setDescript(e.getDescript());
        existing.setAvailabity(e.isAvailabity());
        existing.setPrice(e.getPrice());
        existing.setQuantity(e.getQuantity());
        existing.setReleasDate(e.getReleasDate());
        return repository.save(existing);
    }

    @Override
    public void deleteproducts(Long ecomId) {
        Ecom prod=repository.findById(ecomId)
                            .orElseThrow(()-> new ResourceNotfoundException("Enter a Valid Id"));
        repository.delete(prod);
    }

    @Override
    public Page<Ecom> SearchProducts(String keyword,Pageable pageable) {

        return repository.Search(keyword,pageable);
    }

}
