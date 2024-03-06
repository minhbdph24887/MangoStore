package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.request.CreateProductRequest;
import com.example.mangostore.request.ProductDetailRequest;
import com.example.mangostore.service.ProductDetailService;
import com.example.mangostore.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final MaterialRepository materialRepository;
    private final OriginRepository originRepository;
    private final Gender gender;

    public ProductDetailServiceImpl(AccountRepository accountRepository,
                                    RoleRepository roleRepository,
                                    ProductDetailRepository productDetailRepository,
                                    ProductRepository productRepository,
                                    SizeRepository sizeRepository,
                                    ColorRepository colorRepository,
                                    MaterialRepository materialRepository,
                                    OriginRepository originRepository,
                                    Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productDetailRepository = productDetailRepository;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.materialRepository = materialRepository;
        this.originRepository = originRepository;
        this.gender = gender;
    }

    @Override
    public String indexProductDetail(Model model, HttpSession session, String keyword) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);

                LocalDateTime checkDate = LocalDateTime.now();
                int hour = checkDate.getHour();
                if (hour >= 5 && hour < 10) {
                    model.addAttribute("dates", "Morning");
                } else if (hour >= 10 && hour < 13) {
                    model.addAttribute("dates", "Noon");
                } else if (hour >= 13 && hour < 18) {
                    model.addAttribute("dates", "Afternoon");
                } else {
                    model.addAttribute("dates", "Evening");
                }

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<ProductDetail> itemsProductDetail = productDetailRepository.getAllProductDetailByStatus1();
//                if (keyword != null) {
//                    itemsProduct = productRepository.searchProduct(keyword);
//                    model.addAttribute("keyword", keyword);
//                }
                model.addAttribute("listProductDetail", itemsProductDetail);

                List<ProductDetail> itemsProductDetailInactive = productDetailRepository.getAllProductDetailByStatus0();
                model.addAttribute("listProductDetailInactive", itemsProductDetailInactive);
                return "admin/productDetail/IndexProductDetail";
            }
        }
    }

    @Override
    public String viewCreateProductDetail(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
                return "redirect:/mangostore/home";
            } else {
                model.addAttribute("profile", detailAccount);

                LocalDateTime checkDate = LocalDateTime.now();
                int hour = checkDate.getHour();
                if (hour >= 5 && hour < 10) {
                    model.addAttribute("dates", "Morning");
                } else if (hour >= 10 && hour < 13) {
                    model.addAttribute("dates", "Noon");
                } else if (hour >= 13 && hour < 18) {
                    model.addAttribute("dates", "Afternoon");
                } else {
                    model.addAttribute("dates", "Evening");
                }

                Role detailRole = roleRepository.getRoleByEmail(email);
                if (detailRole.getName().equals("ADMIN")) {
                    model.addAttribute("checkMenuAdmin", true);
                } else {
                    model.addAttribute("checkMenuAdmin", false);
                }

                List<Product> itemsProduct = productRepository.getAllProductByStatus1();
                model.addAttribute("listProduct", itemsProduct);

                List<Size> itemsSize = sizeRepository.getAllSizeByStatus1();
                model.addAttribute("listSize", itemsSize);

                List<Color> itemsColor = colorRepository.getAllColorByStatus1();
                model.addAttribute("listColor", itemsColor);

                List<Material> itemsMaterial = materialRepository.getAllMaterialByStatus1();
                model.addAttribute("listMaterial", itemsMaterial);

                List<Origin> itemsOrigin = originRepository.getAllOriginByStatus1();
                model.addAttribute("listOrigin", itemsOrigin);

                List<ProductDetail> itemsProductDetailInactive = productDetailRepository.getAllProductDetailByStatus0();
                model.addAttribute("listProductDetailInactive", itemsProductDetailInactive);

                model.addAttribute("productDetailForm", new ProductDetailRequest());
                return "admin/productDetail/CreateProductDetail";
            }
        }
    }

    @Override
    public String addProductDetail(ProductDetailRequest productDetailForm, BindingResult result, HttpSession session, Model model) {
        List<Product> products = productRepository.getAllProductByStatus1();
        model.addAttribute("listProduct", products);

        List<Size> sizes = sizeRepository.getAllSizeByStatus1();
        model.addAttribute("listSize", sizes);

        List<Color> colors = colorRepository.getAllColorByStatus1();
        model.addAttribute("listColor", colors);

        List<Material> materials = materialRepository.getAllMaterialByStatus1();
        model.addAttribute("listMaterial", materials);

        model.addAttribute("productDetailForm", new ProductDetailRequest());

        Product product = productRepository.findByMa(productDetailForm.getIdProduct());
        Material material = materialRepository.findByMa(productDetailForm.getMaterial());

        if (productDetailForm.getProductVariants() == null) {
            productDetailForm.setProductVariants(new ArrayList<>());
        }

        System.out.println("aaaaaaaaaaaaaaaa " + productDetailForm.getIdProduct());
        System.out.println("bbbbbbbbbbbbbbbb " + productDetailForm.getImagesProductDetail());
        System.out.println("cccccccccccccccc " + productDetailForm.getIdSizes());
        System.out.println("dddddddddddddddd " + productDetailForm.getIdColors());
        System.out.println("eeeeeeeeeeeeeeee " + productDetailForm.getMaterial());
        System.out.println("gggggggggggggggg " + productDetailForm.getOrigin());
        System.out.println("hhhhhhhhhhhhhhhh " + productDetailForm.getDescribe());

        for (Long sizeId : productDetailForm.getIdSizes()) {
            for (Long colorId : productDetailForm.getIdColors()) {
                Size size = sizeRepository.findById(sizeId).orElse(null);
                Color color = colorRepository.findById(colorId).orElse(null);

                if (size != null && color != null) {
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setSize(size);
                    productDetail.setColor(color);
                    productDetail.setProduct(product);
                    productDetail.setMaterial(material);
                    productDetail.setQuantity(productDetailForm.getQuantity());
                    productDetail.setImportPrice(productDetail.getImportPrice());
                    productDetail.setPrice(productDetail.getPrice());
                    productDetailRepository.save(productDetail);
                    productDetailForm.getProductVariants().add(productDetail);
                }
            }
        }
        model.addAttribute("variants", productDetailForm.getProductVariants());
        return "admin/productDetail/CreateProductDetail";
    }

    @Override
    public boolean saveProductDetailAPI(CreateProductRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            response.sendRedirect("/mangostore/home");
        } else {
            Account account = accountRepository.detailAccountByEmail(email);
            Optional<Product> product = productRepository.findById(request.getIdProduct());
            Optional<Material> material = materialRepository.findById(request.getIdMaterial());
            Optional<Origin> origin = originRepository.findById(request.getIdOrigin());
            request.getVariantRequests().forEach(items -> {
                Size size = sizeRepository.findByName(items.getSize());
                Color color = colorRepository.findByName(items.getColor());
                ProductDetail productDetail = new ProductDetail();
                productDetail.setImagesProductDetail(request.getImagesProduct());
                productDetail.setDescribe(request.getDescribe());
                productDetail.setNameUserCreate(account.getFullName());
                productDetail.setNameUserUpdate(account.getFullName());
                productDetail.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                productDetail.setDateUpdate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                productDetail.setImportPrice(items.getImportPrice());
                productDetail.setPrice(items.getPrice());
                productDetail.setStatus(1);
                productDetail.setQuantity(items.getQuantity());
                productDetail.setProduct(product.get());
                productDetail.setMaterial(material.get());
                productDetail.setOrigin(origin.get());
                productDetail.setSize(size);
                productDetail.setColor(color);
                productDetailRepository.save(productDetail);
            });
        }
        return true;
    }
}