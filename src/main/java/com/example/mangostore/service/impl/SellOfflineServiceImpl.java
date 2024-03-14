package com.example.mangostore.service.impl;

import com.example.mangostore.config.Gender;
import com.example.mangostore.entity.*;
import com.example.mangostore.repository.*;
import com.example.mangostore.service.SellOfflineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SellOfflineServiceImpl implements SellOfflineService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final InvoiceRepository invoiceRepository;
    private final Gender gender;
    private final VoucherRepository voucherRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final RankRepository rankRepository;

    public SellOfflineServiceImpl(AccountRepository accountRepository,
                                  RoleRepository roleRepository,
                                  InvoiceRepository invoiceRepository,
                                  Gender gender,
                                  VoucherRepository voucherRepository,
                                  InvoiceDetailRepository invoiceDetailRepository,
                                  ProductDetailRepository productDetailRepository,
                                  RankRepository rankRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.invoiceRepository = invoiceRepository;
        this.gender = gender;
        this.voucherRepository = voucherRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public String indexSellOffline(Model model, HttpSession session) {
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

                List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
                model.addAttribute("listInvoiceByAccount", itemsInvoice);
                return "sellOffline/IndexSell";
            }
        }
    }

    @Override
    public ResponseEntity<String> createInvoiceAPI(HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        Account detailAccount = accountRepository.detailAccountByEmail(email);
        List<Invoice> checkInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
        if (checkInvoice.size() >= 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Invoice newInvoice = new Invoice();
            newInvoice.setCodeInvoice(gender.generateCode());
            newInvoice.setNameInvoice(gender.generateNameInvoice());
            newInvoice.setAccount(detailAccount);
            newInvoice.setInvoiceForm("offline");
            newInvoice.setInvoiceCreationDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
            newInvoice.setInvoiceStatus(0);
            invoiceRepository.save(newInvoice);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public String editInvoice(Long idInvoice, Model model, HttpSession session) {
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

                List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceByAccount(detailAccount.getId());
                model.addAttribute("listInvoiceByAccount", itemsInvoice);

                Invoice detailInvoice = invoiceRepository.findById(idInvoice).orElse(null);
                model.addAttribute("detailInvoice", detailInvoice);

                if (detailInvoice.getIdCustomer() != null) {
                    Account detailAccountByIdAccount = accountRepository.findById(detailInvoice.getIdCustomer()).orElse(null);
                    model.addAttribute("nameClient", detailAccountByIdAccount.getFullName());
                    model.addAttribute("pointClient", detailAccountByIdAccount.getAccumulatedPoints());

                    List<Voucher> itemsVoucherOffline = voucherRepository.findVoucherByVoucherFrom(detailAccount.getRank().getId());
                    model.addAttribute("listVoucherClient", itemsVoucherOffline);
                } else {
                    List<Voucher> itemsVoucherOffline = voucherRepository.getAllVoucherByStatus1();
                    model.addAttribute("listVoucherClient", itemsVoucherOffline);
                }

                if (detailInvoice.getVoucher() != null) {
                    Voucher getReducedValue = voucherRepository.findById(detailInvoice.getVoucher().getId()).orElse(null);
                    model.addAttribute("discountVouchers", getReducedValue.getReducedValue());
                }

                if (detailInvoice.getTotalInvoiceAmount() != null) {
                    model.addAttribute("totalInvoice", detailInvoice.getTotalInvoiceAmount());
                }

                if (detailInvoice.getTotalPayment() != null) {
                    model.addAttribute("totalPayment", detailInvoice.getTotalPayment());
                }

                if (detailInvoice.getCustomerPoints() != null) {
                    Integer customerPoints = detailInvoice.getCustomerPoints() * 1000;
                    model.addAttribute("customerPoints", customerPoints);
                }

                List<InvoiceDetail> itemsInvoiceDetailByIdInvoice = invoiceDetailRepository.getAllInvoiceDetailByIdInvoice(detailInvoice.getId());
                model.addAttribute("listInvoiceDetail", itemsInvoiceDetailByIdInvoice);

                List<ProductDetail> itemsProductDetail = productDetailRepository.getAllProductDetailByStatus1();
                model.addAttribute("listProductDetail", itemsProductDetail);
                return "sellOffline/DetailInvoiceSell";
            }
        }
    }

    @Override
    public String updateClient(Long idInvoice, String numberPhoneClient) {
        Account detailAccountCustom = accountRepository.findAccountByNumberPhone(numberPhoneClient);
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        if (invoice.getIdCustomer() == null) {
            invoice.setIdCustomer(detailAccountCustom.getId());
            invoiceRepository.save(invoice);
        }
        return "redirect:/mangostore/admin/sell/edit?id=" + invoice.getId();
    }

    @Override
    public String updatePoint(Long idInvoice, Integer pointClient) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        if (invoice.getTotalPayment() != null) {
            Account detailAccountCustom = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
            Integer customerPoints = detailAccountCustom.getAccumulatedPoints() * 1000;
            Integer totalPaymentPoint = invoice.getTotalPayment() - customerPoints;
            invoice.setTotalPayment(totalPaymentPoint);
            invoiceRepository.save(invoice);
        }

        if (invoice.getCustomerPoints() == null) {
            invoice.setCustomerPoints(pointClient);
            invoiceRepository.save(invoice);
            Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
            detailAccount.setAccumulatedPoints(0);
            accountRepository.save(detailAccount);
        }
        return "redirect:/mangostore/admin/sell/edit?id=" + invoice.getId();
    }

    @Override
    public String updateVoucher(Long idInvoice, Voucher voucher) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);

        if (invoice.getVoucher() == null) {
            invoice.setVoucher(voucher);
            invoiceRepository.save(invoice);
            Voucher detailVoucher = voucherRepository.findById(voucher.getId()).orElse(null);
            Integer quantityNew = detailVoucher.getQuantity() - 1;
            detailVoucher.setQuantity(quantityNew);
            if (quantityNew == 0) {
                detailVoucher.setStatus(0);
            }
            voucherRepository.save(detailVoucher);
        }

        if (invoice.getTotalPayment() != null) {
            Voucher detailVoucher = voucherRepository.findById(invoice.getVoucher().getId()).orElse(null);
            Integer totalPaymentVoucher = invoice.getTotalPayment() - detailVoucher.getReducedValue();
            invoice.setTotalPayment(totalPaymentVoucher);
            invoiceRepository.save(invoice);
        }
        return "redirect:/mangostore/admin/sell/edit?id=" + invoice.getId();
    }

    @Override
    public String cancelInvoice(Long idInvoice) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        invoice.setInvoiceStatus(6);
        invoiceRepository.save(invoice);

        if (invoice.getVoucher() != null) {
            Voucher detailVoucher = voucherRepository.findById(invoice.getVoucher().getId()).orElse(null);
            Integer quantityOld = detailVoucher.getQuantity() + 1;
            detailVoucher.setQuantity(quantityOld);
            voucherRepository.save(detailVoucher);
        }

        if (invoice.getIdCustomer() != null) {
            Integer customerClientOld = invoice.getCustomerPoints();
            Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
            detailAccount.setAccumulatedPoints(customerClientOld);
            accountRepository.save(detailAccount);
        }
        return "redirect:/mangostore/admin/sell";
    }

    @Override
    public String addProduct(Long idInvoice, Long idProductDetail, Integer newQuantity) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        List<ProductDetail> getAllProductDetailItems = productDetailRepository.findProductDetailById(idProductDetail);
        List<Integer> listTotalInvoice = invoiceDetailRepository.capitalSumDetailInvoice(idInvoice);
        Integer totalInvoice = listTotalInvoice.get(0);

        Integer nullTotal = 0;
        if (String.valueOf(totalInvoice).equalsIgnoreCase("null")) {
            nullTotal = 0;
        } else {
            nullTotal = totalInvoice;
        }

        Integer reducedValueVoucher = 0;
        if (invoice.getVoucher() == null) {
            reducedValueVoucher = 0;
        } else {
            reducedValueVoucher = invoice.getVoucher().getReducedValue();
        }

        Integer customPoint = 0;
        if (invoice.getCustomerPoints() == null) {
            customPoint = 0;
        } else {
            customPoint = invoice.getCustomerPoints();
        }

        for (ProductDetail detail : getAllProductDetailItems) {
            Integer sum = newQuantity * detail.getPrice();
            Integer total = sum + nullTotal;

            InvoiceDetail invoiceDetail = invoiceDetailRepository.findAllByIdInvoiceAndProductDetails(detail.getId(), invoice.getId());
            if (String.valueOf(invoiceDetail).equalsIgnoreCase("null")) {
                InvoiceDetail newInvoiceDetail = new InvoiceDetail();
                newInvoiceDetail.setInvoice(invoice);
                newInvoiceDetail.setProductDetail(detail);
                newInvoiceDetail.setQuantity(newQuantity);
                newInvoiceDetail.setPrice(detail.getPrice());
                newInvoiceDetail.setCapitalSum(sum);
                invoiceDetailRepository.save(newInvoiceDetail);

                Invoice detailInvoiceSet = invoiceRepository.findById(invoice.getId()).orElse(null);
                Integer newSum = 0;
                if (detailInvoiceSet.getTotalInvoiceAmount() == null) {
                    newSum = sum;
                    detailInvoiceSet.setTotalInvoiceAmount(newSum);
                } else {
                    newSum = detailInvoiceSet.getTotalInvoiceAmount() + sum;
                    detailInvoiceSet.setTotalInvoiceAmount(newSum);
                }

                Integer totalPayment = newSum - reducedValueVoucher - (customPoint * 1000);
                if (totalPayment <= 0) {
                    detailInvoiceSet.setTotalPayment(0);
                } else {
                    detailInvoiceSet.setTotalPayment(totalPayment);
                }
                invoiceRepository.save(detailInvoiceSet);
            }
        }
        return "redirect:/mangostore/admin/sell/edit?id=" + invoice.getId();
    }

    @Override
    public String deleteProduct(Long idInvoiceDetail) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(idInvoiceDetail).orElse(null);
        List<Invoice> itemsInvoice = invoiceRepository.getAllInvoiceById(invoiceDetail.getInvoice().getId());
        Invoice invoice = itemsInvoice.isEmpty() ? null : itemsInvoice.get(0);

        Integer totalSum = invoiceDetail.getQuantity() * invoiceDetail.getPrice();
        Integer totalCustomer = invoice.getTotalInvoiceAmount() - totalSum;

        Integer reducedValueVoucher = 0;
        if (invoice.getVoucher() == null) {
            reducedValueVoucher = 0;
        } else {
            reducedValueVoucher = invoice.getVoucher().getReducedValue();
        }

        Integer customPoint = 0;
        if (invoice.getCustomerPoints() == null) {
            customPoint = 0;
        } else {
            customPoint = invoice.getCustomerPoints();
        }

        Integer newTotalPayment = totalCustomer - reducedValueVoucher - customPoint;
        invoice.setTotalInvoiceAmount(totalCustomer);

        if (newTotalPayment < 0) {
            invoice.setTotalPayment(0);
        } else {
            invoice.setTotalPayment(newTotalPayment);
        }

        invoiceRepository.save(invoice);
        invoiceDetailRepository.deleteById(idInvoiceDetail);
        return "redirect:/mangostore/admin/sell/edit?id=" + invoiceDetail.getInvoice().getId();
    }

    @Override
    public String updateStatusInvoice(Long idInvoice, Integer returnClientMoney) {
        Invoice invoice = invoiceRepository.findById(idInvoice).orElse(null);
        invoice.setInvoicePaymentDate(LocalDateTime.parse(gender.getCurrentDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss")));
        invoice.setReturnClientMoney(returnClientMoney);
        invoice.setPayments("cash");
        Integer leftoverMoney = returnClientMoney - invoice.getTotalPayment();
        invoice.setLeftoverMoney(leftoverMoney);
        invoice.setInvoiceStatus(5);
        invoiceRepository.save(invoice);

        List<InvoiceDetail> getAllInvoiceDetail = invoiceDetailRepository.findAllByIdInvoice(idInvoice);
        for (InvoiceDetail detail : getAllInvoiceDetail) {
            ProductDetail productDetail = productDetailRepository.findById(detail.getProductDetail().getId()).orElse(null);
            Integer quantityNew = productDetail.getQuantity() - detail.getQuantity();
            productDetail.setQuantity(quantityNew);
            productDetailRepository.save(productDetail);
        }

        Double rewardPoints = invoice.getTotalInvoiceAmount().doubleValue() / 12500;
        Integer addPoints = gender.roundingNumber(rewardPoints);
        Account detailAccount = accountRepository.findById(invoice.getIdCustomer()).orElse(null);
        Integer points = detailAccount.getAccumulatedPoints() + addPoints;
        detailAccount.setAccumulatedPoints(points);
        accountRepository.save(detailAccount);

        List<Rank> itemsRank = rankRepository.getAllRankByStatus1();
        itemsRank.sort((rank1, rank2) -> rank2.getMaximumScore().compareTo(rank1.getMaximumScore()));
        for (Rank rank : itemsRank) {
            if (detailAccount.getAccumulatedPoints() > rank.getMinimumScore() && detailAccount.getAccumulatedPoints() < rank.getMaximumScore()) {
                detailAccount.setRank(rank);
            } else if (detailAccount.getAccumulatedPoints() > rank.getMaximumScore()) {
                detailAccount.setRank(itemsRank.get(0));
            }
        }
        accountRepository.save(detailAccount);
        return "redirect:/mangostore/admin/sell";
    }
}
