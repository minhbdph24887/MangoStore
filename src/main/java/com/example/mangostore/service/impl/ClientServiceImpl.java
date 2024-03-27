package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.request.AddToCartRequest;
import com.example.mangostore.request.AddToFavouriteRequest;
import com.example.mangostore.request.QuantityRequest;
import com.example.mangostore.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final FavouriteRepository favouriteRepository;
    private final FavouriteDetailRepository favouriteDetailRepository;

    public ClientServiceImpl(AccountRepository accountRepository,
                             RoleRepository roleRepository,
                             ProductDetailRepository productDetailRepository,
                             SizeRepository sizeRepository,
                             ColorRepository colorRepository,
                             Gender gender,
                             ShoppingCartDetailRepository shoppingCartDetailRepository,
                             ShoppingCartRepository shoppingCartRepository,
                             FavouriteRepository favouriteRepository,
                             FavouriteDetailRepository favouriteDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.productDetailRepository = productDetailRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.gender = gender;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.favouriteRepository = favouriteRepository;
        this.favouriteDetailRepository = favouriteDetailRepository;
    }

    @Override
    public String indexClient(Model model,
                              HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }
        return "client/Home";
    }

    @Override
    public String viewProductClient(Model model,
                                    HttpSession session,
                                    String sortDirection,
                                    Integer pageNo) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
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

        model.addAttribute("listProductDetail", itemsAllProductDetail);
        model.addAttribute("totalPage", itemsAllProductDetail.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        Map<Long, PriceRange> priceRangeMap = gender.getPriceRangMap();
        model.addAttribute("priceRangeMap", priceRangeMap);
        return "client/List";
    }

    @Override
    public String detailProductClient(Long idProductDetail,
                                      Model model,
                                      HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email != null) {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }
        }

        ProductDetail detailProductClient = productDetailRepository.findById(idProductDetail).orElse(null);
        model.addAttribute("detailProductClient", detailProductClient);

        Map<Long, PriceRange> priceRangeMap = gender.getPriceRangMap();
        model.addAttribute("priceRangeMap", priceRangeMap);

        List<Size> itemsSize = sizeRepository.findAll();
        model.addAttribute("listSize", itemsSize);

        List<Color> itemsColor = colorRepository.findAll();
        model.addAttribute("listColor", itemsColor);
        return "client/DetailProductClient";
    }

    @Override
    public ResponseEntity<?> quantityProductDetail(QuantityRequest request) {
        ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
        if (detailProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại");
        } else if (detailProduct.getQuantity() == 0) {
            return ResponseEntity.ok("Sản phẩm đã hết");
        } else {
            return ResponseEntity.ok(detailProduct.getQuantity());
        }
    }

    @Override
    public boolean addToCart(AddToCartRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return false;
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
            ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            List<ShoppingCartDetail> allShoppingCartDetails = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());

            ShoppingCartDetail existingShoppingCartDetail = allShoppingCartDetails.stream()
                    .filter(shoppingCartDetail -> shoppingCartDetail.getProductDetail().getId().equals(detailProduct.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingShoppingCartDetail == null) {
                ShoppingCartDetail newShoppingCartDetail = new ShoppingCartDetail();
                newShoppingCartDetail.setShoppingCart(detailShoppingCart);
                newShoppingCartDetail.setProductDetail(detailProduct);
                newShoppingCartDetail.setQuantity(request.getQuantity());
                newShoppingCartDetail.setPrice(detailProduct.getPrice());
                newShoppingCartDetail.setCapitalSum(request.getQuantity() * detailProduct.getPrice());
                newShoppingCartDetail.setDateCreate(LocalDateTime.now());
                newShoppingCartDetail.setStatus(1);
                shoppingCartDetailRepository.save(newShoppingCartDetail);
            } else {
                Integer quantityNew = request.getQuantity() + existingShoppingCartDetail.getQuantity();
                existingShoppingCartDetail.setQuantity(quantityNew);
                existingShoppingCartDetail.setCapitalSum(quantityNew * existingShoppingCartDetail.getPrice());
                existingShoppingCartDetail.setDateCreate(LocalDateTime.now());
                shoppingCartDetailRepository.save(existingShoppingCartDetail);
            }
            gender.updateTotalShoppingCart(detailShoppingCart);
            return true;
        }
    }

    @Override
    public boolean addToFavourite(AddToFavouriteRequest request, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        ProductDetail detailProduct = productDetailRepository.getQuantityProductDetail(request.getIdProduct(), request.getIdSize(), request.getIdColor());
        Favourite detailFavourite = favouriteRepository.findByIdAccount(detailAccount.getId());
        List<FavouriteDetail> allFavouriteDetails = favouriteDetailRepository.getAllFavourite(detailFavourite.getId());

        FavouriteDetail existingFavouriteDetail = allFavouriteDetails.stream()
                .filter(favouriteDetail -> favouriteDetail.getProductDetail().getId().equals(detailProduct.getId()))
                .findFirst()
                .orElse(null);

        if (existingFavouriteDetail == null) {
            FavouriteDetail newFavouriteDetail = new FavouriteDetail();
            newFavouriteDetail.setFavourite(detailFavourite);
            newFavouriteDetail.setProductDetail(detailProduct);
            newFavouriteDetail.setDateCreate(LocalDateTime.now());
            newFavouriteDetail.setStatus(1);
            favouriteDetailRepository.save(newFavouriteDetail);
            return true;
        } else {
            return false;
        }
    }
}