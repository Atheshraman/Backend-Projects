package com.example.ecom.Controller;



import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ecom.DTO.Ecomdto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.Service.service;
import com.example.ecom.model.Ecom;


@RestController
@RequestMapping("/ecom")
@Validated
public class controller {
    
    @Autowired
    private service service;

    @PostMapping("/products")
    public ResponseEntity<Ecom> Addproduct(@RequestPart Ecom e, @RequestPart MultipartFile imageFile){
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println("AUTH USER: " + auth.getName());
        System.out.println("AUTHENTICATED: " + auth.isAuthenticated());
        System.out.println("AUTHORITIES: " + auth.getAuthorities());
        try
        {
            Ecom product=service.Addproduct(e,imageFile);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
        catch(Exception a)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Ecomdto>> getproduct(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10")int size){
        Page<Ecom> prod = service.getproducts(page,size);
        Page<Ecomdto> dtos=prod.map(this::convertToEcomDTO);
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    @GetMapping("/products/search")
    public ResponseEntity<Page<Ecom>> searchproduct(@RequestParam(required = false) String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam (defaultValue = "20") int size
                                                    ){
        System.out.println("Searching with"+keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(service.getproducts(page,size));
        }
        Pageable pageable= PageRequest.of(page,size);
        return ResponseEntity.ok(service.SearchProducts(keyword,pageable));
    }

    @GetMapping("/products/{ecomId}")
    public ResponseEntity<Ecomdto> getproductbyid(@PathVariable Long ecomId){
        Ecom product=service.getprodbyID(ecomId);
        Ecomdto dto=convertToEcomDTO(product);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/products/{ecomId}")
    public Ecom updateprod(@PathVariable Long ecomId,@RequestPart Ecom e,@RequestPart(required = false) MultipartFile imageFile) throws IOException{
        return service.updateproducts(ecomId,e,imageFile);
    }

    @DeleteMapping("/products/{ecomId}")
    public void Deleteproduct(@PathVariable Long ecomId){
        service.deleteproducts(ecomId);
    }

    @GetMapping("/products/{ecomId}/image")
    public ResponseEntity<byte[]> getproductimagebyid(@PathVariable Long ecomId)
    {
        Ecom e=service.getprodbyID(ecomId);
        byte[] imageFile=e.getImageData();
        return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(e.getImagetype()))
                    .body(imageFile);
    }



     private Ecomdto convertToEcomDTO(Ecom ecom) {
            Ecomdto dto = new Ecomdto();
            dto.setEcomId(ecom.getEcomId());
            dto.setName(ecom.getName());
            dto.setDescript(ecom.getDescript());
            dto.setPrice(ecom.getPrice());
            dto.setQuantity(ecom.getQuantity());
            dto.setAvailability(ecom.isAvailabity());
            dto.setReleaseDate(ecom.getReleasDate());
            dto.setImageName(ecom.getImagename());
            dto.setImageType(ecom.getImagetype());
            return dto;
    }


}
