package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class CartServiceImpl implements CartService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final Gender gender;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public CartServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           ShoppingCartDetailRepository shoppingCartDetailRepository,
                           Gender gender,
                           InvoiceRepository invoiceRepository,
                           InvoiceDetailRepository invoiceDetailRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.gender = gender;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public String cartIndex(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }

            ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            List<ShoppingCartDetail> listShoppingCartByAccount = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
            model.addAttribute("listShoppingCartByAccount", listShoppingCartByAccount);

            Integer totalAmount = listShoppingCartByAccount.stream()
                    .mapToInt(ShoppingCartDetail::getCapitalSum)
                    .sum();
            String formattedTotalAmount = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(totalAmount) + " VND";
            model.addAttribute("totalShoppingCart", formattedTotalAmount);
            return "client/CartIndex";
        }
    }

    @Override
    public String reduceQuantity(Long idShoppingCartDetail) {
        ShoppingCartDetail detail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert detail != null;

        int quantityReduce = detail.getQuantity() - 1;
        detail.setQuantity(quantityReduce);

        detail.setCapitalSum(quantityReduce * detail.getPrice());
        shoppingCartDetailRepository.save(detail);

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(detail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.get(0);
        assert shoppingCart != null;

        int totalShoppingCartAmount = shoppingCart.getTotalShoppingCart() - detail.getPrice();
        shoppingCart.setTotalShoppingCart(totalShoppingCartAmount);
        shoppingCartRepository.save(shoppingCart);
        if (quantityReduce == 0) {
            shoppingCartDetailRepository.deleteById(idShoppingCartDetail);
        }
        return "redirect:/mangostore/cart";
    }

    @Override
    public String increaseQuantity(Long idShoppingCartDetail) {
        ShoppingCartDetail detail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert detail != null;

        int quantityIncrease = detail.getQuantity() + 1;
        detail.setQuantity(quantityIncrease);
        detail.setCapitalSum(quantityIncrease * detail.getPrice());
        shoppingCartDetailRepository.save(detail);

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(detail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.get(0);
        assert shoppingCart != null;

        int totalShoppingCartAmount = shoppingCart.getTotalShoppingCart() + detail.getPrice();
        shoppingCart.setTotalShoppingCart(totalShoppingCartAmount);
        shoppingCartRepository.save(shoppingCart);
        return "redirect:/mangostore/cart";
    }

    @Override
    public String deleteProductCart(Long idShoppingCartDetail) {
        ShoppingCartDetail shoppingCartDetail = shoppingCartDetailRepository.findById(idShoppingCartDetail).orElse(null);
        assert shoppingCartDetail != null;

        List<ShoppingCart> itemsShoppingCart = shoppingCartRepository.getAllShoppingCartById(shoppingCartDetail.getShoppingCart().getId());
        ShoppingCart shoppingCart = itemsShoppingCart.isEmpty() ? null : itemsShoppingCart.get(0);
        Integer totalSum = shoppingCartDetail.getQuantity() * shoppingCartDetail.getPrice();
        assert shoppingCart != null;
        Integer totalCustomer = shoppingCart.getTotalShoppingCart() - totalSum;
        shoppingCart.setTotalShoppingCart(totalCustomer);
        shoppingCartRepository.save(shoppingCart);
        shoppingCartDetailRepository.deleteById(idShoppingCartDetail);
        return "redirect:/mangostore/cart";
    }

    @Override
    public String viewCheckOut(Model model,
                               HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return "redirect:/mangostore/home";
        } else {
            Account detailAccount = accountRepository.detailAccountByEmail(email);
            model.addAttribute("profile", detailAccount);
            Role detailRoleByEmail = roleRepository.getRoleByEmail(email);
            if (detailRoleByEmail.getName().equals("ADMIN") || detailRoleByEmail.getName().equals("STAFF")) {
                model.addAttribute("checkAuthentication", detailRoleByEmail);
            }

            ShoppingCart shoppingCart = shoppingCartRepository.findByIdAccount(detailAccount.getId());
            Invoice newInvoice = new Invoice();
            newInvoice.setCodeInvoice(gender.generateCode());
            newInvoice.setNameInvoice(gender.generateNameInvoice());
            newInvoice.setInvoiceForm("paying");
            newInvoice.setIdCustomer(detailAccount.getId());
            newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newInvoice.setTotalInvoiceAmount(shoppingCart.getTotalShoppingCart());
            newInvoice.setTotalPayment(shoppingCart.getTotalShoppingCart());
            newInvoice.setInvoiceStatus(1);
            invoiceRepository.save(newInvoice);

            List<ShoppingCartDetail> itemsShoppingCartDetail = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
            for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCartDetail) {
                InvoiceDetail invoiceDetail = new InvoiceDetail();
                invoiceDetail.setProductDetail(shoppingCartDetail.getProductDetail());
                invoiceDetail.setInvoice(newInvoice);
                invoiceDetail.setQuantity(shoppingCartDetail.getQuantity());
                invoiceDetail.setPrice(shoppingCartDetail.getPrice());
                invoiceDetail.setCapitalSum(shoppingCartDetail.getCapitalSum());
                invoiceDetailRepository.save(invoiceDetail);
            }

            Invoice detailInvoiceOnline = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            model.addAttribute("shoppingCart", detailInvoiceOnline);
            return "client/CartCheckOut";
        }
    }

    @Override
    public String updateStatusInvoiceOnline(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccountClient = accountRepository.detailAccountByEmail(email);
        Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccountClient.getId());
        assert detailInvoice != null;
        detailInvoice.setInvoiceStatus(5);
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/home";
    }
}
