package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final Gender gender;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final AddressClientRepository addressClientRepository;
    private final VoucherClientRepository voucherClientRepository;
    private final ProductDetailRepository productDetailRepository;
    private final RankRepository rankRepository;

    public CartServiceImpl(AccountRepository accountRepository,
                           RoleRepository roleRepository,
                           ShoppingCartRepository shoppingCartRepository,
                           ShoppingCartDetailRepository shoppingCartDetailRepository,
                           Gender gender,
                           InvoiceRepository invoiceRepository,
                           InvoiceDetailRepository invoiceDetailRepository,
                           AddressClientRepository addressClientRepository,
                           VoucherClientRepository voucherClientRepository,
                           ProductDetailRepository productDetailRepository,
                           RankRepository rankRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartDetailRepository = shoppingCartDetailRepository;
        this.gender = gender;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.addressClientRepository = addressClientRepository;
        this.voucherClientRepository = voucherClientRepository;
        this.productDetailRepository = productDetailRepository;
        this.rankRepository = rankRepository;
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
            Invoice checkInvoiceByAccount = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            if (checkInvoiceByAccount != null) {
                if (!Objects.equals(checkInvoiceByAccount.getTotalInvoiceAmount(), shoppingCart.getTotalShoppingCart())) {
                    checkInvoiceByAccount.setInvoiceStatus(5);
                    invoiceRepository.save(checkInvoiceByAccount);

                    Invoice newInvoice = new Invoice();
                    newInvoice.setCodeInvoice(gender.generateCode());
                    newInvoice.setNameInvoice(gender.generateNameInvoice());
                    newInvoice.setInvoiceForm("paying");
                    newInvoice.setIdCustomer(detailAccount.getId());
                    newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                    newInvoice.setTotalInvoiceAmount(shoppingCart.getTotalShoppingCart());
                    newInvoice.setCustomerPoints(0);
                    Integer shippingMoney = 0;
                    if (shoppingCart.getTotalShoppingCart() >= 0 && shoppingCart.getTotalShoppingCart() < 500000) {
                        newInvoice.setShippingMoney(15000);
                        shippingMoney = 15000;
                    } else {
                        newInvoice.setShippingMoney(30000);
                        shippingMoney = 30000;
                    }
                    newInvoice.setTotalPayment(shoppingCart.getTotalShoppingCart() + shippingMoney);
                    newInvoice.setInvoiceStatus(0);
                    invoiceRepository.save(newInvoice);

                    List<ShoppingCartDetail> itemsShoppingCartDetail = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
                    for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCartDetail) {
                        InvoiceDetail invoiceDetail = new InvoiceDetail();
                        invoiceDetail.setProductDetail(shoppingCartDetail.getProductDetail());
                        invoiceDetail.setInvoice(newInvoice);
                        invoiceDetail.setQuantity(shoppingCartDetail.getQuantity());
                        invoiceDetail.setPrice(shoppingCartDetail.getPrice());
                        invoiceDetail.setForm("online");
                        invoiceDetail.setCapitalSum(shoppingCartDetail.getCapitalSum());
                        invoiceDetailRepository.save(invoiceDetail);
                    }
                }
            } else {
                Invoice newInvoice = new Invoice();
                newInvoice.setCodeInvoice(gender.generateCode());
                newInvoice.setNameInvoice(gender.generateNameInvoice());
                newInvoice.setInvoiceForm("paying");
                newInvoice.setIdCustomer(detailAccount.getId());
                AddressClient newAddressClient = addressClientRepository.addressClientDefault(detailAccount.getId());
                if (newAddressClient != null) {
                    newInvoice.setAddressClient(newAddressClient);
                }
                newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
                newInvoice.setTotalInvoiceAmount(shoppingCart.getTotalShoppingCart());
                newInvoice.setCustomerPoints(0);
                Integer shippingMoney = 0;
                if (shoppingCart.getTotalShoppingCart() >= 0 && shoppingCart.getTotalShoppingCart() < 500000) {
                    newInvoice.setShippingMoney(15000);
                    shippingMoney = 15000;
                } else {
                    newInvoice.setShippingMoney(30000);
                    shippingMoney = 30000;
                }
                newInvoice.setTotalPayment(shoppingCart.getTotalShoppingCart() + shippingMoney);
                newInvoice.setInvoiceStatus(0);
                invoiceRepository.save(newInvoice);

                List<ShoppingCartDetail> itemsShoppingCartDetail = shoppingCartDetailRepository.getAllShoppingCart(shoppingCart.getId());
                for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCartDetail) {
                    InvoiceDetail invoiceDetail = new InvoiceDetail();
                    invoiceDetail.setProductDetail(shoppingCartDetail.getProductDetail());
                    invoiceDetail.setInvoice(newInvoice);
                    invoiceDetail.setQuantity(shoppingCartDetail.getQuantity());
                    invoiceDetail.setPrice(shoppingCartDetail.getPrice());
                    invoiceDetail.setForm("online");
                    invoiceDetail.setCapitalSum(shoppingCartDetail.getCapitalSum());
                    invoiceDetailRepository.save(invoiceDetail);
                }
            }

            Invoice detailInvoiceOnline = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            model.addAttribute("shoppingCart", detailInvoiceOnline);
            model.addAttribute("pointClient", detailAccount.getAccumulatedPoints());
            model.addAttribute("listAddressClient", addressClientRepository.findAllByAccount(detailAccount.getId()));
            model.addAttribute("addressClientStatus", addressClientRepository.findAllByIdAccountAndStatus(detailAccount.getId()));
            model.addAttribute("newAddressClient", new AddressClient());

            List<VoucherClient> findAllVoucherClient = voucherClientRepository.findAllVoucherStatusVoucher1(detailAccount.getRank().getId());
            model.addAttribute("listVoucherClient", findAllVoucherClient);

            model.addAttribute("conversionPoint", detailInvoiceOnline.getCustomerPoints() * 1000);

            List<InvoiceDetail> findAllProductByIdInvoice = invoiceDetailRepository.findAllByIdInvoice(detailInvoiceOnline.getId());
            model.addAttribute("listProductByIdInvoice", findAllProductByIdInvoice);
            return "client/CartCheckOut";
        }
    }

    @Override
    public String addAddressClientForClient(AddressClient newAddressClient, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<AddressClient> getAllAddressClientByAccount = addressClientRepository.findAllByAccount(detailAccount.getId());

        if (newAddressClient.getStatus() == 1) {
            for (AddressClient addressClient : getAllAddressClientByAccount) {
                if (addressClient.getStatus() == 1) {
                    addressClient.setStatus(0);
                    addressClientRepository.save(addressClient);
                }
            }
        }

        AddressClient newAddressClientClient = new AddressClient();
        newAddressClientClient.setCodeAddress(gender.generateCode());
        newAddressClientClient.setNameClient(newAddressClient.getNameClient());
        newAddressClientClient.setPhoneNumber(newAddressClient.getPhoneNumber());
        newAddressClientClient.setSpecificAddress(newAddressClient.getSpecificAddress());
        newAddressClientClient.setCommune(newAddressClient.getCommune());
        newAddressClientClient.setDistrict(newAddressClient.getDistrict());
        newAddressClientClient.setProvince(newAddressClient.getProvince());
        newAddressClientClient.setDateCreate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        newAddressClientClient.setAccount(detailAccount);
        newAddressClientClient.setStatus(newAddressClient.getStatus());
        addressClientRepository.save(newAddressClientClient);

        AddressClient addressClientDefault = addressClientRepository.addressClientDefault(detailAccount.getId());
        if (addressClientDefault != null) {
            Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
            detailInvoice.setAddressClient(addressClientDefault);
            invoiceRepository.save(detailInvoice);
        }
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String updateStatusClientAddress(Long id,
                                            HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<AddressClient> findAllAddressByAccount = addressClientRepository.findAllByAccount(detailAccount.getId());
        for (AddressClient addressClient : findAllAddressByAccount) {
            if (addressClient.getStatus() == 1) {
                addressClient.setStatus(0);
                addressClientRepository.save(addressClient);
            }
        }

        AddressClient newAddressClientStatus = addressClientRepository.findById(id).orElse(null);
        assert newAddressClientStatus != null;
        newAddressClientStatus.setStatus(1);
        addressClientRepository.save(newAddressClientStatus);

        Invoice updateAddressClientInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
        updateAddressClientInvoice.setAddressClient(newAddressClientStatus);
        invoiceRepository.save(updateAddressClientInvoice);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String addVoucherToInvoice(Long id,
                                      HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        VoucherClient detailVoucherClient = voucherClientRepository.findById(id).orElse(null);
        Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());
        detailInvoice.setVoucherClient(detailVoucherClient);
        invoiceRepository.save(detailInvoice);

        int pointClient = 0;
        if (detailInvoice.getCustomerPoints() != null) {
            pointClient = detailInvoice.getCustomerPoints();
        }

        assert detailVoucherClient != null;
        Integer conversionPoint = pointClient * 1000;
        int newPaymentInvoice = detailInvoice.getTotalInvoiceAmount() + detailInvoice.getShippingMoney() - conversionPoint - detailVoucherClient.getReducedValue();
        detailInvoice.setTotalPayment(Math.max(newPaymentInvoice, 0));
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String addPointClientToInovice(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        assert email != null;
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        Invoice detailInvoice = invoiceRepository.findInvoiceByIdAccount(detailAccount.getId());

        detailInvoice.setCustomerPoints(detailAccount.getAccumulatedPoints());
        invoiceRepository.save(detailInvoice);

        int voucherAmount = 0;
        if (detailInvoice.getVoucherClient() != null) {
            voucherAmount = detailInvoice.getVoucherClient().getReducedValue();
        }

        int conversionPoint = detailInvoice.getCustomerPoints() * 1000;
        int newPaymentInvoice = detailInvoice.getTotalInvoiceAmount() + detailInvoice.getShippingMoney() - voucherAmount - conversionPoint;
        detailInvoice.setTotalPayment(Math.max(newPaymentInvoice, 0));
        invoiceRepository.save(detailInvoice);
        return "redirect:/mangostore/cart/checkout";
    }

    @Override
    public String updateCashInvoice(Long idInvoice) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        assert detailInvoice != null;
        detailInvoice.setInvoiceForm("online");
        detailInvoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        detailInvoice.setPayments("cod");
        detailInvoice.setInvoiceStatus(1);
        invoiceRepository.save(detailInvoice);

        List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoice(idInvoice);
        for (InvoiceDetail detail : getAllInvoiceDetail) {
            ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
            assert productDetail != null;
            Integer quantityNew = productDetail.getQuantity() - detail.getQuantity();
            productDetail.setQuantity(quantityNew);
            productDetailRepository.save(productDetail);
        }

        ShoppingCart detailShoppingCart = shoppingCartRepository.findByIdAccount(detailInvoice.getIdCustomer());
        detailShoppingCart.setTotalShoppingCart(0);
        shoppingCartRepository.save(detailShoppingCart);

        List<ShoppingCartDetail> itemsShoppingCart = shoppingCartDetailRepository.getAllShoppingCart(detailShoppingCart.getId());
        for (ShoppingCartDetail shoppingCartDetail : itemsShoppingCart) {
            shoppingCartDetail.setStatus(0);
            shoppingCartDetailRepository.save(shoppingCartDetail);
        }

        if (detailInvoice.getVoucherClient() != null) {
            VoucherClient voucherClient = voucherClientRepository.findById(detailInvoice.getVoucherClient().getId()).orElse(null);
            assert voucherClient != null;
            voucherClient.setStatus(0);
            voucherClientRepository.save(voucherClient);
        }

        if (detailInvoice.getCustomerPoints() != 0) {
            Account detailAccount = accountRepository.findById(detailInvoice.getIdCustomer()).orElse(null);
            assert detailAccount != null;
            detailAccount.setAccumulatedPoints(0);
            accountRepository.save(detailAccount);
        }

        if (detailInvoice.getCustomerPoints() != 0) {
            Double rewardPoints = detailInvoice.getTotalInvoiceAmount().doubleValue() / 12500;
            Integer addPoints = gender.roundingNumber(rewardPoints);
            Account detailAccount = accountRepository.findById(detailInvoice.getIdCustomer()).orElse(null);
            assert detailAccount != null;
            Integer points = detailAccount.getAccumulatedPoints() + addPoints;
            detailAccount.setAccumulatedPoints(points);
            accountRepository.save(detailAccount);

            List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
            itemsRank.sort((rank1, rank2) -> rank2.getMaximumScore().compareTo(rank1.getMaximumScore()));
            for (Rank rank : itemsRank) {
                if (detailAccount.getAccumulatedPoints() >= rank.getMinimumScore() && detailAccount.getAccumulatedPoints() < rank.getMaximumScore()) {
                    detailAccount.setRank(rank);
                    break;
                } else if (detailAccount.getAccumulatedPoints() >= rank.getMaximumScore()) {
                    detailAccount.setRank(itemsRank.get(0));
                    break;
                }
            }
            accountRepository.save(detailAccount);
        }
        return "redirect:/mangostore/home";
    }

    @Override
    public String bankingVnPay(Long idInvoice, HttpServletRequest request, HttpSession session) {
        Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
        session.setAttribute("idInvoiceClient", idInvoice);
        String vnPayUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        assert detailInvoice != null;
        return "redirect:" + gender.createPaymentVnPay(detailInvoice, vnPayUrl);
    }
}
