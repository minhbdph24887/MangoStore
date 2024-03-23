package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ProductDetailRepository productDetailRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final Gender gender;

    public ClientServiceImpl(AccountRepository accountRepository,
                             RoleRepository roleRepository,
                             ProductDetailRepository productDetailRepository,
                             SizeRepository sizeRepository,
                             ColorRepository colorRepository,
                             Gender gender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productDetailRepository = productDetailRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.gender = gender;
    }

    @Override
    public String indexClient(Model model,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
            } else {
                model.addAttribute("profile", detailAccount);
                Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
                if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                    model.addAttribute("checkAuthentication", detailRoleByEmail);
                }
            }
        }
        return "client/Home";
    }

    @Override
    public String viewProductClient(Model model,
                                    HttpSession session,
                                    String sortDirection,
                                    Integer pageNo,
                                    List<Long> sizes,
                                    List<Long> colors) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            if (detailAccount.getStatus() == 0) {
                session.invalidate();
            } else {
                model.addAttribute("profile", detailAccount);
                Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
                if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                    model.addAttribute("checkAuthentication", detailRoleByEmail);
                }
            }
        }

        List<Size> itemsSize = sizeRepository.getAllSizeByStatus1();
        model.addAttribute("listSize", itemsSize);

        Map<String, Integer> productDetailCountBySize = gender.countProductsBySize();
        model.addAttribute("productDetailCountBySize", productDetailCountBySize);

        List<Color> itemsColor = colorRepository.getAllColorByStatus1();
        model.addAttribute("listColor", itemsColor);

        Map<String, Integer> productDetailCountByColor = gender.countProductsByColor();
        model.addAttribute("productDetailCountByColor", productDetailCountByColor);
        Page<ProductDetail> itemsAllProductDetail;
        if ("LowToHigh".equals(sortDirection)) {
            itemsAllProductDetail = productDetailRepository.sortProductDetailLowToHigh(PageRequest.of(pageNo - 1, 8));
            model.addAttribute("sortDirection", "LowToHigh");
        } else if ("HighToLow".equals(sortDirection)) {
            itemsAllProductDetail = productDetailRepository.sortProductDetailHighToLow(PageRequest.of(pageNo - 1, 8));
            model.addAttribute("sortDirection", "HighToLow");
        } else {
            itemsAllProductDetail = productDetailRepository.getAllProductDetailByIdProduct(PageRequest.of(pageNo - 1, 8));
        }

        sizes = (sizes != null) ? sizes : new ArrayList<>();
        colors = (colors != null) ? colors : new ArrayList<>();

        if (!sizes.isEmpty() && !colors.isEmpty()) {
            itemsAllProductDetail = productDetailRepository.filterProductBySizeAndColor(sizes, colors, PageRequest.of(pageNo - 1, 8));
        } else if (!sizes.isEmpty()) {
            itemsAllProductDetail = productDetailRepository.filterProductBySize(sizes, PageRequest.of(pageNo - 1, 8));
        } else if (!colors.isEmpty()) {
            itemsAllProductDetail = productDetailRepository.filterProductByColor(colors, PageRequest.of(pageNo - 1, 8));
        } else {
            // Nếu không có bộ lọc nào được áp dụng, hiển thị tất cả sản phẩm
            itemsAllProductDetail = productDetailRepository.getAllProductDetailByIdProduct(PageRequest.of(pageNo - 1, 8));
        }

        model.addAttribute("listProductDetail", itemsAllProductDetail);
        model.addAttribute("totalPage", itemsAllProductDetail.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        Map<Long, PriceRange> priceRangeMap = gender.getPriceRangMap();
        model.addAttribute("priceRangeMap", priceRangeMap);

        model.addAttribute("selectedSizes", sizes);
        model.addAttribute("selectedColors", colors);
        return "client/List";
    }
}