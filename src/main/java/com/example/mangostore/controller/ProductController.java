package com.example.mangostore.controller;

import com.example.mangostore.entity.Material;
import com.example.mangostore.entity.Origin;
import com.example.mangostore.entity.Product;
import com.example.mangostore.entity.Role;
import com.example.mangostore.service.MaterialService;
import com.example.mangostore.service.OriginService;
import com.example.mangostore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/mangostore/admin/")
public class ProductController {
    private final ProductService productService;
    private final MaterialService materialService;
    private final OriginService originService;

    public ProductController(ProductService productService,
                             MaterialService materialService,
                             OriginService originService) {
        this.productService = productService;
        this.materialService = materialService;
        this.originService = originService;
    }

    @GetMapping(value = "product")
    public String indexProduct(Model model,
                               HttpSession session,
                               @RequestParam(defaultValue = "0") int page,
                               @Param("keyword") String keyword) {
        return productService.indexProduct(model, session, page, keyword);
    }

    @PostMapping(value = "product/add")
    public String addProduct(@Valid Product addProduct,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        return productService.addProduct(addProduct, result, session, redirectAttributes);
    }

    @GetMapping(value = "product/detail/{id}")
    public String detailProduct(Model model,
                                HttpSession session,
                                @PathVariable("id") Long idProduct,
                                @RequestParam(defaultValue = "0") int page) {
        return productService.detailProduct(model, session, idProduct, page);
    }

    @PostMapping(value = "product/update")
    public String updateProduct(@Valid Product product,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        return productService.updateProduct(redirectAttributes, result, session, product);
    }

    @GetMapping(value = "product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long idProduct,
                                RedirectAttributes redirectAttributes) {
        return productService.deleteProduct(redirectAttributes, idProduct);
    }

    @GetMapping(value = "product/restore/{id}")
    public String restoreProduct(RedirectAttributes redirectAttributes,
                                 @PathVariable("id") Long idProduct) {
        return productService.restoreProduct(redirectAttributes, idProduct);
    }


    @GetMapping(value = "material")
    public String indexMaterial(Model model,
                                HttpSession session,
                                @RequestParam(defaultValue = "0") int page,
                                @Param("keyword") String keyword) {
        return materialService.indexMaterial(model, session, page, keyword);
    }

    @PostMapping(value = "material/add")
    public String addMaterial(@Valid Material addMaterial,
                              BindingResult result,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        return materialService.addMaterial(addMaterial, result, session, redirectAttributes);
    }

    @GetMapping(value = "material/detail/{id}")
    public String detailMaterial(Model model,
                                 HttpSession session,
                                 @PathVariable("id") Long idMaterial,
                                 @RequestParam(defaultValue = "0") int page) {
        return materialService.detailMaterial(model, session, idMaterial, page);
    }

    @PostMapping(value = "material/update")
    public String updateMaterial(@Valid Material material,
                                 BindingResult result,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        return materialService.updateMaterial(redirectAttributes, result, session, material);
    }

    @GetMapping(value = "material/delete/{id}")
    public String deleteMaterial(@PathVariable("id") Long idMaterial,
                                 RedirectAttributes redirectAttributes) {
        return materialService.deleteMaterial(redirectAttributes, idMaterial);
    }

    @GetMapping(value = "material/restore/{id}")
    public String restoreMaterial(RedirectAttributes redirectAttributes,
                                  @PathVariable("id") Long idMaterial) {
        return materialService.restoreMaterial(redirectAttributes, idMaterial);
    }


    @GetMapping(value = "origin")
    public String indexOrigin(Model model,
                              HttpSession session,
                              @RequestParam(defaultValue = "0") int page,
                              @Param("keyword") String keyword) {
        return originService.indexOrigin(model, session, page, keyword);
    }

    @PostMapping(value = "origin/add")
    public String addOrigin(@Valid Origin addOrigin,
                            BindingResult result,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        return originService.addOrigin(addOrigin, result, session, redirectAttributes);
    }

    @GetMapping(value = "origin/detail/{id}")
    public String detailOrigin(Model model,
                               HttpSession session,
                               @PathVariable("id") Long idOrigin,
                               @RequestParam(defaultValue = "0") int page) {
        return originService.detailOrigin(model, session, idOrigin, page);
    }

    @PostMapping(value = "origin/update")
    public String updateOrigin(@Valid Origin origin,
                               BindingResult result,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        return originService.updateOrigin(redirectAttributes, result, session, origin);
    }

    @GetMapping(value = "origin/delete/{id}")
    public String deleteOrigin(@PathVariable("id") Long idOrigin,
                               RedirectAttributes redirectAttributes) {
        return originService.deleteOrigin(redirectAttributes, idOrigin);
    }

    @GetMapping(value = "origin/restore/{id}")
    public String restoreOrigin(RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long idOrigin) {
        return originService.restoreOrigin(redirectAttributes, idOrigin);
    }
}
